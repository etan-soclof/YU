package edu.yu.cs.com1320.project.stage2.impl;


import edu.yu.cs.com1320.project.Command;
import edu.yu.cs.com1320.project.impl.*;

import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;
import edu.yu.cs.com1320.project.stage2.impl.DocumentImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.function.Function;

public class DocumentStoreImpl implements DocumentStore {

    private HashTableImpl<URI, DocumentImpl> table;
    private StackImpl<Command> stack = new StackImpl();
    private int count = 0;
/*
    public static void main (String[] args){
        DocumentStoreImpl docStore = new DocumentStoreImpl();
        docStore.textToPDF("o");
    }*/

    public DocumentStoreImpl (){
        this.table = new HashTableImpl();
    }

    @Override
    public int putDocument(InputStream input, URI uri, DocumentFormat format) {
        if (uri == null || format == null){ throw new IllegalArgumentException("Must enter valid uri and format"); }
        //If the input is null, delete the document at the designated URI
        if (input == null){
            if (getDocumentAsTxt(uri)!= null){
                int code = getDocumentAsTxt(uri).hashCode();
                DocumentImpl doc = table.put(uri, null);
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

        //If the document already exists at the same uri, do nothing
        if (table.get(uri) != null && table.get(uri).hashCode() == stringHC){ return stringHC; }

        if (format == DocumentFormat.PDF){
            DocumentImpl doc = new DocumentImpl(uri, text, stringHC, bytes);
            DocumentImpl replaced = table.put(uri, doc);
            //Add the put command to the stack
            this.addPutCommand(uri, replaced);
            count++;
            return doc.getDocumentTextHashCode();
        }
        else{
            DocumentImpl doc = null;
            doc = new DocumentImpl(uri, text, stringHC);
            DocumentImpl replaced = table.put(uri, doc);
            //Add the put command to the stack
            this.addPutCommand(uri, replaced);
            count++;
            return doc.getDocumentTextHashCode();
        }
    }

    @Override
    public byte[] getDocumentAsPdf(URI uri) {
        DocumentImpl doc = table.get(uri);
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

        return output.toByteArray();
    }

    @Override
    public String getDocumentAsTxt(URI uri) {
        if (table.get(uri) == null){
            return null;
        }
        return table.get(uri).getDocumentAsTxt();
    }

    @Override
    public boolean deleteDocument(URI uri) {
        DocumentImpl doc = table.put(uri, null);
        if (doc == null){
            return false;
        }
        count--;
        this.addDeleteCommand(uri, doc);
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
        if (this.stack.peek() == null || this.count == 0){
            throw new IllegalStateException();
        }
        Command command = this.stack.peek();
        command.undo();
    }

    @Override
    public void undo(URI uri) throws IllegalStateException {
        if (this.stack.peek() == null || this.count == 0){
            throw new IllegalStateException();
        }
        StackImpl<Command> holder = new StackImpl();
        for (Command command = this.stack.peek(); command != null; command = this.stack.peek()){
            if (command.getUri().equals(uri)){
                command.undo();
                break;
            }
            holder.push(this.stack.pop());
        }
        for (Command e = holder.pop(); e != null; e = holder.pop()) { this.stack.push(e); }
    }

    private void addPutCommand(URI put, DocumentImpl replaced){
        Function<URI, Boolean> function = (URI uri1) -> {
            //If the put you want to undo is on the top of the stack, just undo it
            if (this.stack.peek() == null){
                return false;
            }
            if (this.stack.peek().getUri().equals(uri1)){
                //Note that a delete command is added. This must be removed before exiting the lamda
                this.deleteDocument(uri1);
                if (replaced != null){
                    table.put(replaced.getKey(), replaced);
                }
                this.stack.pop();
                //Delete the extra delete command
                if (this.stack.peek() != null){
                    this.stack.pop();
                }
                return true;
            }
            return true; };
        this.stack.push(new Command (put, function));
    }

    private void addDeleteCommand(URI uri, DocumentImpl doc){
        Function<URI, Boolean> function = (URI uri1) -> {
            //If the put you want to undo is on the top of the stack, just undo it
            if (this.stack.peek() == null){
                return false;
            }
            if (this.stack.peek().getUri().equals(uri1)){
                table.put(uri, doc);
                this.stack.pop();
                return true;
            }
            return true; };
        this.stack.push(new Command (uri, function));
    }

    /**
     * @return the Document object stored at that URI, or null if there is no such
    Document */
    protected Document getDocument(URI uri){
        return table.get(uri);
    }

}
