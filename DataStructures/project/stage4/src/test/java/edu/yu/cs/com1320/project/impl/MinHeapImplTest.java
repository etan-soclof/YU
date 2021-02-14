package edu.yu.cs.com1320.project.impl;

public class MinHeapImplTest {
/*
    private MinHeapImpl<String> heap = new MinHeapImpl<>();

    @Test
    public void testHeap(){
        String s1 = "p";
        String s2 = "i";
        String s3 = "n";
        String s4 = "e";
        String s5 = "o";
        String s6 = "s";
        String s7 = "a";
        String s8 = "r";
        String s9 = "t";
        heap.insert(s1);
        heap.insert(s2);
        heap.insert(s3);
        heap.insert(s4);
        heap.insert(s5);
        heap.insert(s6);
        heap.insert(s7);
        heap.insert(s8);
        heap.insert(s9);
        heap.getElements();
        s8 = "b";
        heap.getElements();
        heap.reHeapify(s8);
        heap.getElements();
    }



    //variables to hold possible values for doc1
    private URI uri1;
    private String txt1;
    private byte[] pdfData1;
    private String pdfTxt1;

    //variables to hold possible values for doc2
    private URI uri2;
    private String txt2;
    private byte[] pdfData2;
    private String pdfTxt2;

    private URI uri3;
    private String txt3;
    private byte[] pdfData3;
    private String pdfTxt3;

    private URI uri4;
    private String txt4;
    private byte[] pdfData4;
    private String pdfTxt4;

    @Before
    public void init() throws Exception {
        //init possible values for doc1
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
        this.pdfTxt1 = "b afghd";
        this.pdfData1 = Utils.textToPdfData(this.pdfTxt1);

        //init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Text for doc2. A plain old String.";
        this.pdfTxt2 = "AB B AB";
        this.pdfData2 = Utils.textToPdfData(this.pdfTxt2);

        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "Text for doc3. A plain old String.";
        this.pdfTxt3 = "AB A AB CDE";
        this.pdfData3 = Utils.textToPdfData(this.pdfTxt3);

        this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        this.txt4 = "Text for doc4. A plain old String.";
        this.pdfTxt4 = "a EFG hi";
        this.pdfData4 = Utils.textToPdfData(this.pdfTxt4);
    }

    @Test
    public void testHeap() {
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1), this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2), this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3), this.uri3, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4), this.uri4, DocumentStore.DocumentFormat.PDF);

        List<String> array = store.searchByPrefix("a");
        Set<URI> array1 = store.deleteAll("ab");
        store.undo();

    }

    @Test
    public void testMemory(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1), this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2), this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3), this.uri3, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4), this.uri4, DocumentStore.DocumentFormat.PDF);
        store.setMaxDocumentCount(3);
        long count = store.getDocumentAsTxt(this.uri2).getBytes().length + store.getDocumentAsPdf(this.uri2).length;
        store.setMaxDocumentBytes(1000);
    }

    @Test
    public void testMemoryCommands(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1), this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2), this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3), this.uri3, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4), this.uri4, DocumentStore.DocumentFormat.PDF);
        store.deleteAll("a");
        store.putDocument(new ByteArrayInputStream(this.pdfData4), this.uri4, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData1), this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2), this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3), this.uri3, DocumentStore.DocumentFormat.PDF);
        store.setMaxDocumentCount(3);
    }
    */
}