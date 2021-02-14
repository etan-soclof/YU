package edu.yu.cs.com1320.project.stage5.impl;

import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.impl.*;
import edu.yu.cs.com1320.project.stage5.impl.DocumentImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore {

    private BTreeImpl<URI, Document> table;
    private StackImpl<Undoable> stack = new StackImpl();
    private TrieImpl<DocRef> trie = new TrieImpl();
    private MinHeapImpl<DocRef> heap = new MinHeapImpl();
    private long maxDocCount = -1;
    private long maxByteCount = -1;
    private long memoryCount = 0;
    private int count = 0;
    private File file;
    private DocumentPersistenceManager pm = new DocumentPersistenceManager();
/*
    public static void main (String[] args){
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        docStore.textToPDF("o");
    }*/

    public DocumentStoreImpl (){
        this.table = new BTreeImpl();
        this.table.setPersistenceManager(this.pm);
    }

    public DocumentStoreImpl (File file){
        this.table = new BTreeImpl();
        this.pm = new DocumentPersistenceManager(file);
        this.table.setPersistenceManager(this.pm);
    }

    protected class DocRef implements Comparable<DocRef> {

        private URI uri;

        public DocRef (URI uri){
            this.uri = uri;
        }

        @Override
        public boolean equals(Object o){
            if (this == o){
                return true;
            }
            if (o == null){
                return false;
            }
            if (getClass()!=o.getClass()) {
                return false;
            }
            return this.uri.equals(((DocRef) o).getURI());
        }

        @Override
        public int compareTo(DocRef o) {
            if (getDoc() != null && o != null && o.getDoc() != null){
                return getDoc().compareTo(o.getDoc());
            }
            return 1;
        }

        public URI getURI(){
            return this.uri;
        }

        public Document getDoc() {
            boolean onDisk = isOnDisk(uri);
            DocumentImpl doc = (DocumentImpl) table.get(uri);
            if (onDisk){
                try {
                    table.moveToDisk(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return doc;
        }
    }

    @Override
    public int putDocument(InputStream input, URI uri, DocumentFormat format) {
        if (uri == null || format == null){ throw new IllegalArgumentException("Must enter valid uri and format"); }
        //If the input is null, delete the document at the designated URI
        if (input == null){
            if (getDocumentAsTxt(uri)!= null){
                int code = getDocumentAsTxt(uri).hashCode();
                DocumentImpl doc = (DocumentImpl) table.put(uri, null);
                this.addDeleteCommand(uri, doc);
                return code;
            }
            return 0;
        }
        String text = "";
        byte[] bytes = new byte[0];
        bytes = inputToBytes(input);
        if (format == DocumentFormat.PDF){ text = PDFtoString(bytes); }
        else { text = new String(bytes); }
        int stringHC = text.hashCode();

        if (format == DocumentFormat.PDF){
            DocumentImpl doc = new DocumentImpl(uri, text, stringHC, bytes);
            return putDocument(uri, doc, stringHC);
        }
        else{
            DocumentImpl doc = null;
            doc = new DocumentImpl(uri, text, stringHC);
            return putDocument(uri, doc, stringHC);
        }
    }

    private int putDocument(URI uri, DocumentImpl doc, int stringHC){
        //If the document already exists at the same uri, do nothing
        if (table.get(uri) != null && table.get(uri).equals(doc)){
            table.get(uri).setLastUseTime(System.nanoTime());
            if (isOnDisk(uri)){
                this.heap.insert(new DocRef(uri));
                this.count++;
                this.memoryCount += doc.getSize();
                this.table.put(uri, doc);
                overCapacity();
            }
            heap.reHeapify(new DocRef(uri));
            return stringHC;
        }
        DocumentImpl replaced = (DocumentImpl) table.put(uri, doc);
        //Add the put command to the stack
        this.addPutCommand(uri, replaced);
        //Add doc to the trie
        this.addToTrie(doc);
        doc.setLastUseTime(System.nanoTime());
        this.heap.insert(new DocRef(uri));
        this.count++;
        this.memoryCount += doc.getSize();
        overCapacity();
        return doc.getDocumentTextHashCode();
    }

    @Override
    public byte[] getDocumentAsPdf(URI uri) {
        DocumentImpl doc = (DocumentImpl) table.get(uri);
        PDDocument pdf = new PDDocument();
        if (doc == null){
            return null;
        }
        if (doc != null){
            pdf = textToPDF(doc.getDocumentAsTxt());
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            pdf.save(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pdf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        doc.setLastUseTime(System.nanoTime());
        if (isOnDisk(uri)){
            this.heap.insert(new DocRef(uri));
            this.count++;
            this.memoryCount += doc.getSize();
            this.table.put(uri, doc);
            overCapacity();
        }
        return output.toByteArray();
    }

    @Override
    public String getDocumentAsTxt(URI uri) {
        if (table.get(uri) == null){
            return null;
        }
        DocumentImpl doc = (DocumentImpl) table.get(uri);
        doc.setLastUseTime(System.nanoTime());
        if (isOnDisk(uri)){
            this.heap.insert(new DocRef(uri));
            this.count++;
            this.memoryCount += doc.getSize();
            this.table.put(uri, doc);
            overCapacity();
        }
        return table.get(uri).getDocumentAsTxt();
    }

    @Override
    public boolean deleteDocument(URI uri) {
        DocumentImpl doc = (DocumentImpl) table.get(uri);
        if (doc == null){
            return false;
        }
        this.memoryCount -= doc.getSize();
        Set<String> set = setOfWords(doc);
        for (String string: set){
            string = string.toUpperCase();
            trie.delete(string, new DocRef(doc.getKey()));
        }
        table.put(uri, null);
        deleteFromHeap(doc);
        this.addDeleteCommand(uri, doc);
        this.count--;
        return true;
    }

    private boolean deleteManyDocument(URI uri) {
        DocumentImpl doc = (DocumentImpl) table.get(uri);
        if (doc == null){
            return false;
        }
        this.memoryCount -= doc.getSize();
        Set<String> set = setOfWords(doc);
        for (String string: set){
            trie.delete(string, new DocRef(doc.getKey()));
        }
        table.put(uri, null);
        deleteFromHeap(doc);
        this.count--;
        return true;
    }

    private PDDocument textToPDF (String text) {
        text = text.trim();
        text = text.replace("\n", "");
        text = text.replace("\t", "");
        text = text.replace("\r", "");
        PDDocument pdf = new PDDocument();
        PDPage page = new PDPage();
        pdf.addPage(page);
        PDFont font = PDType1Font.HELVETICA;
        try (PDPageContentStream contents = new PDPageContentStream(pdf, page)){
            contents.beginText();
            contents.setFont(font, 11);
            contents.newLineAtOffset(100, 700);
            contents.showText(text);
            contents.endText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pdf;
    }

    private byte[] inputToBytes (InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int num;
        try {
            while ((num = is.read(buffer)) != -1) {
                os.write(buffer, 0, num);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return os.toByteArray();
    }

    private String PDFtoString (byte[] bytes){
        String text = "";
        PDFTextStripper stripper = null;
        try {
            stripper = new PDFTextStripper();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PDDocument doc = new PDDocument();
        try {
            doc = PDDocument.load(bytes);
            text = stripper.getText(doc);
            text = text.trim();
            text = text.replace("\n", "");
            text = text.replace("\t", "");
            text = text.replace("\r", "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;

    }

    @Override
    public void undo() throws IllegalStateException {
        if (this.stack.peek() == null){
            throw new IllegalStateException();
        }
        Undoable undoable = this.stack.peek();
        if (undoable instanceof CommandSet){
            CommandSet<GenericCommand> undoable1 = (CommandSet) this.stack.peek();
            Set<GenericCommand<GenericCommand>> set = undoable1.undoAll();
            this.stack.pop();
            long time = System.nanoTime();
            DocumentImpl doc;
            for (GenericCommand gc: set){
                doc = (DocumentImpl) this.table.get((URI) gc.getTarget());
                doc.setLastUseTime(time);
                if (isOnDisk(doc.getKey())){
                    this.heap.insert(new DocRef(doc.getKey()));
                    this.count++;
                    this.memoryCount += doc.getSize();
                    this.table.put(doc.getKey(), doc);
                    overCapacity();
                }
                heap.insert(new DocRef(doc.getKey()));
                heap.reHeapify(new DocRef(doc.getKey()));
            }
        }
        else{
            GenericCommand command = (GenericCommand) this.stack.peek();
            command.undo();
        }
    }

    @Override
    public void undo(URI uri) throws IllegalStateException {
        if (this.stack.peek() == null){
            throw new IllegalStateException();
        }
        StackImpl<Undoable> holder = new StackImpl();
        for (Undoable command =  this.stack.peek(); command != null; command = this.stack.peek()){
            if (command instanceof CommandSet){
                if (((CommandSet) command).containsTarget(uri)){
                    ((CommandSet) command).undo(uri);
                    break;
                }
            }
            else if (((GenericCommand) command).getTarget().equals(uri)){
                command.undo();
                break;
            }
            holder.push( this.stack.pop());
        }
        for (Undoable e = holder.pop(); e != null; e = holder.pop()) { this.stack.push(e); }
    }

    private void addPutCommand(URI put, DocumentImpl replaced){
        Function<URI, Boolean> function = (URI uri1) -> {
            //If the put you want to undo is on the top of the stack, just undo it
            if (this.stack.peek() == null){
                return false;
            }
            GenericCommand command = (GenericCommand) this.stack.peek();
            if (command.getTarget().equals(uri1)){
                this.deleteDocument(uri1);
                //Note that a delete command has just been added to the stack. This must be removed before exiting
                // the lamda
                if (replaced != null){
                    table.put(replaced.getKey(), replaced);
                }
                this.stack.pop();
                //Remove extra delete command
                if (this.stack.peek() != null){
                    this.stack.pop();
                }
                return true;
            }
            return true; };
        this.stack.push(new GenericCommand<URI> (put, function));
    }

    private void addDeleteCommand(URI uri, DocumentImpl doc){
        Function<URI, Boolean> function = (URI uri1) -> {
            //If the put you want to undo is on the top of the stack, just undo it
            if (this.stack.peek() == null){
                return false;
            }
            GenericCommand command = (GenericCommand) this.stack.peek();
            if (command.getTarget().equals(uri1)){
                table.put(uri, doc);
                addToTrie(doc);
                this.stack.pop();
                return true;
            }
            return true; };
        this.stack.push(new GenericCommand (uri, function));
    }

    private GenericCommand<URI> addManyDeleteCommand(URI uri, DocumentImpl doc){
        Function<URI, Boolean> function = (URI uri1) -> {
            //If the put you want to undo is on the top of the stack, just undo it
            if (this.stack.peek() == null){
                return false;
            }
            CommandSet commandSet = (CommandSet) this.stack.peek();
            if (commandSet.containsTarget(uri1)){
                table.put(uri, doc);
                addToTrie(doc);
                return true;
            }
            return true; };
        return new GenericCommand<URI> (uri, function);
    }

    private void addToTrie (DocumentImpl doc){
        Set<String> set = setOfWords(doc);
        Iterator<String> itr = set.iterator();
        while(itr.hasNext()){
            trie.put(itr.next(), new DocRef(doc.getKey()));
        }
    }

    private Set<String> setOfWords (DocumentImpl doc){
        String string = doc.getDocumentAsTxt();
        string = string.replaceAll("[^a-zA-Z0-9\\s]", "");
        String [] split = string.split(" ");
        //Convert to set to remove all duplicates
        Set<String> set = new HashSet<>();
        for (int i = 0; i < split.length; i++){
            set.add(split[i]);
        }
        return set;
    }

    /**
     * @return the Document object stored at that URI, or null if there is no such
    Document
     Make sure to change to protected before handing IN*/
    protected Document getDocument(URI uri){
        if (isOnDisk(uri)){
            return null;
        }
        return this.table.get(uri);
    }

    @Override
    public List<String> search(String keyword) {
        keyword = keyword.toUpperCase();
        String finalKeyword = keyword;
        Comparator<DocRef> comparator = (DocRef doc1, DocRef doc2) -> {
            if (doc1.getDoc().wordCount(finalKeyword) > doc2.getDoc().wordCount(finalKeyword)){
                return -1;
            }
            if (doc1.getDoc().wordCount(finalKeyword) < doc2.getDoc().wordCount(finalKeyword)){
                return 1;
            }
            return 0;
        };

        ArrayList<DocRef> list = (ArrayList<DocRef>) trie.getAllSorted(finalKeyword, comparator);
        if (list == null){
            List<String> stringList = new ArrayList<>();
            return stringList;
        }
        ListIterator<DocRef> itr = list.listIterator();
        List<String> stringList = new ArrayList<>();
        Long time = System.nanoTime();
        while (itr.hasNext()){
            DocRef dr = itr.next();
            DocumentImpl doc = (DocumentImpl) dr.getDoc();
            dr.getDoc().setLastUseTime(time);
            if (isOnDisk(doc.getKey())){
                this.heap.insert(new DocRef(doc.getKey()));
                this.count++;
                this.memoryCount += doc.getSize();
                this.table.put(doc.getKey(), doc);
                overCapacity();
            }
            this.heap.reHeapify(new DocRef(dr.getDoc().getKey()));
            stringList.add(dr.getDoc().getDocumentAsTxt());
        }
        return stringList;
    }

    @Override
    public List<byte[]> searchPDFs(String keyword) {
        keyword = keyword.toUpperCase();
        String finalKeyword = keyword;
        Comparator<DocRef> comparator = (DocRef doc1, DocRef doc2) -> {
            if (doc1.getDoc().wordCount(finalKeyword) > doc2.getDoc().wordCount(finalKeyword)){
                return -1;
            }
            if (doc1.getDoc().wordCount(finalKeyword) < doc2.getDoc().wordCount(finalKeyword)){
                return 1;
            }
            return 0;
        };

        ArrayList<DocRef> list = (ArrayList<DocRef>) trie.getAllSorted(finalKeyword, comparator);
        List<byte[]> byteList = new ArrayList<>();
        if (list == null){
            return byteList;
        }
        Long time = System.nanoTime();
        ListIterator<DocRef> itr = list.listIterator();
        while (itr.hasNext()){
            DocRef dr = itr.next();
            DocumentImpl doc = (DocumentImpl) dr.getDoc();
            doc.setLastUseTime(time);
            if (isOnDisk(doc.getKey())){
                this.heap.insert(new DocRef(doc.getKey()));
                this.count++;
                this.memoryCount += doc.getSize();
                this.table.put(doc.getKey(), doc);
                overCapacity();
            }
            this.heap.reHeapify(new DocRef(dr.getDoc().getKey()));
            byteList.add(dr.getDoc().getDocumentAsPdf());
        }
        return byteList;
    }

    @Override
    public List<String> searchByPrefix(String prefix) {
        prefix = prefix.toUpperCase();
        String finalPrefix = prefix;
        Comparator<DocRef> comparator = (DocRef doc1, DocRef doc2) -> {
            int count1 = getPrefixCount((DocumentImpl) doc1.getDoc(), finalPrefix);
            int count2 = getPrefixCount((DocumentImpl) doc2.getDoc(), finalPrefix);
            if (count1 > count2){
                return -1;
            }
            if (count1 < count2){
                return 1;
            }
            return 0;
        };
        ArrayList<DocRef> list = (ArrayList<DocRef>) trie.getAllWithPrefixSorted(finalPrefix, comparator);

        if (list == null){
            List<String> stringList = new ArrayList<>();
            return stringList;
        }
        ListIterator<DocRef> itr = list.listIterator();
        List<String> stringList = new ArrayList<>();
        Long time = System.nanoTime();
        while (itr.hasNext()){
            DocRef dr = itr.next();
            DocumentImpl doc = (DocumentImpl) dr.getDoc();
            if (doc != null){
                doc.setLastUseTime(time);
                if (isOnDisk(doc.getKey())){
                    this.heap.insert(new DocRef(doc.getKey()));
                    this.count++;
                    this.memoryCount += doc.getSize();
                    this.table.put(doc.getKey(), doc);
                    overCapacity();
                }
                this.heap.reHeapify(new DocRef(dr.getDoc().getKey()));
                stringList.add(dr.getDoc().getDocumentAsTxt());
            }
        }
        Set<String> set = new HashSet<>(stringList);
        stringList = new ArrayList<>(set);
        return stringList;
    }

    @Override
    public List<byte[]> searchPDFsByPrefix(String prefix) {
        prefix = prefix.toUpperCase();
        String finalPrefix = prefix;
        Comparator<DocRef> comparator = (DocRef doc1, DocRef doc2) -> {
            int count1 = getPrefixCount((DocumentImpl) doc1.getDoc(), finalPrefix);
            int count2 = getPrefixCount((DocumentImpl) doc2.getDoc(), finalPrefix);
            if (count1 > count2){
                return -1;
            }
            if (count1 < count2){
                return 1;
            }
            return 0;
        };
        ArrayList<DocRef> list = (ArrayList<DocRef>) trie.getAllWithPrefixSorted(finalPrefix, comparator);
        Set<DocRef> set = new HashSet<>(list);
        list = new ArrayList<>(set);
        List<byte[]> byteList = new ArrayList<>();
        if (list == null){
            return byteList;
        }
        ListIterator<DocRef> itr = list.listIterator();
        Long time = System.nanoTime();
        while (itr.hasNext()){
            DocumentImpl doc = (DocumentImpl) itr.next().getDoc();
            if (doc != null){
                doc.setLastUseTime(time);
                if (isOnDisk(doc.getKey())){
                    this.heap.insert(new DocRef(doc.getKey()));
                    this.count++;
                    this.memoryCount += doc.getSize();
                    this.table.put(doc.getKey(), doc);
                    overCapacity();
                }
                this.heap.reHeapify(new DocRef(doc.getKey()));
                byteList.add(doc.getDocumentAsPdf());
            }
        }
        return byteList;
    }

    private int getPrefixCount(DocumentImpl doc, String prefix){
        String text = doc.getDocumentAsTxt();
        text.replaceAll("[^a-zA-Z0-9]", "");
        String [] split = text.split(" ");
        int count = 0;
        for (int i = 0; i < split.length; i++){
            if ((split[i].length() >= prefix.length()) && split[i].substring(0, prefix.length()).equals(prefix)){
                count++;
            }
        }
        return count;
    }

    @Override
    public Set<URI> deleteAll(String key) {
        CommandSet<URI> commandSet = new CommandSet<>();
        Set<DocRef> set = trie.deleteAll(key);
        Set<URI> uriSet = new HashSet<>();
        for(DocRef dr : set){
            uriSet.add(dr.getURI());
        }
        for (URI uri: uriSet){
            commandSet.addCommand(this.addManyDeleteCommand(uri, (DocumentImpl) this.table.get(uri)));
            deleteManyDocument(uri);
        }
        this.stack.push(commandSet);
        return uriSet;
    }

    @Override
    public Set<URI> deleteAllWithPrefix(String prefix) {
        CommandSet<URI> commandSet = new CommandSet<>();
        Set<DocRef> set = trie.deleteAllWithPrefix(prefix);
        Set<URI> uriSet = new HashSet<>();
        for(DocRef dr: set){
            uriSet.add(dr.getURI());
        }
        for (URI uri: uriSet){
            commandSet.addCommand(this.addManyDeleteCommand(uri, (DocumentImpl) this.table.get(uri)));
            deleteManyDocument(uri);
        }
        this.stack.push(commandSet);
        return uriSet;
    }

    private void deleteFromHeap(DocumentImpl doc){
        if (!isOnDisk(doc.getKey())){
            doc.setLastUseTime(Long.MIN_VALUE);
            heap.reHeapify(new DocRef(doc.getKey()));
            heap.removeMin();
        }
    }

    private void overCapacity(){
        DocumentImpl doc = null;
        if (this.maxByteCount != -1 && this.maxDocCount != -1){
            while ((this.count > this.maxDocCount ) || this.memoryCount > this.maxByteCount){
                removeMin();
            }
        }
        else if (this.maxByteCount != -1){
            while (this.memoryCount > this.maxByteCount){
                removeMin();
            }
        }
        else if (this.maxDocCount != -1){
            while (this.count > this.maxDocCount ){
                removeMin();
            }
        }
    }

    private void removeMin(){
        DocRef cDoc = this.heap.removeMin();
        DocumentImpl doc = (DocumentImpl) cDoc.getDoc();
        try {
            this.table.moveToDisk(doc.getKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (doc != null){
            this.memoryCount -= doc.getSize();
            /*
            Set<String> set = setOfWords(doc);
            for (String string: set){
                string = string.toUpperCase();
                trie.delete(string, new DocRef(doc.getKey()));
            }
            table.put(doc.getKey(), null);
            URI uri = doc.getKey();
            if (this.stack.peek() == null){
                return;
            }
            StackImpl<Undoable> holder = new StackImpl();
            for (Undoable command =  this.stack.peek(); command != null; command = this.stack.peek()){
                if (command instanceof CommandSet){
                    if (((CommandSet) command).containsTarget(uri)){
                        Iterator itr = ((CommandSet) command).iterator();
                        while (itr.hasNext()){
                            GenericCommand gc = (GenericCommand) itr.next();
                            if (gc.getTarget().equals(uri)){
                                itr.remove();
                                if (((CommandSet) command).size() == 0){
                                    this.stack.pop();
                                }
                                else {
                                    holder.push(this.stack.pop());
                                }
                            }
                        }
                    }
                }
                else if (((GenericCommand) command).getTarget().equals(uri)){
                    this.stack.pop();
                }
                else {
                    holder.push(this.stack.pop());
                }
            }
            for (Undoable e = holder.pop(); e != null; e = holder.pop()) { this.stack.push(e); }
             */
            this.count--;
        }
    }

    public void setMaxDocumentCount(int limit) {
        this.maxDocCount = limit;
        overCapacity();
    }

    public void setMaxDocumentBytes(int limit) {
        this.maxByteCount = limit;
        overCapacity();
    }

    protected boolean isOnDisk (URI uri){
        DocumentImpl doc = null;
        try {
            doc = pm.backDoorDeserializer(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc == null){
            return false;
        }
        return true;
    }

}
