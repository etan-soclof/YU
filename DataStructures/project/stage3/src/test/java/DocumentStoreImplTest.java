import static org.junit.Assert.assertTrue;

public class DocumentStoreImplTest {
    /*
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
        this.pdfData1 = edu.yu.cs.com1320.project.Utils.textToPdfData(this.pdfTxt1);

        //init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Text for doc2. A plain old String.";
        this.pdfTxt2 = "AB B AB";
        this.pdfData2 = edu.yu.cs.com1320.project.Utils.textToPdfData(this.pdfTxt2);

        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "Text for doc3. A plain old String.";
        this.pdfTxt3 = "AB A AB CDE";
        this.pdfData3 = edu.yu.cs.com1320.project.Utils.textToPdfData(this.pdfTxt3);

        this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        this.txt4 = "Text for doc4. A plain old String.";
        this.pdfTxt4 = "EFG hi";
        this.pdfData4 = edu.yu.cs.com1320.project.Utils.textToPdfData(this.pdfTxt4);
    }

    @Test
    public void testPutPdfDocumentNoPreviousDocAtURI(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
    }

    @Test
    public void testPutTxtDocumentNoPreviousDocAtURI(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.txt1.hashCode());
    }

    @Test
    public void testPutDocumentWithNullArguments(){
        DocumentStore store = new DocumentStoreImpl();
        try {
            store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), null, DocumentStore.DocumentFormat.TXT);
            fail("null URI should've thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){}
        try {
            store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, null);
            fail("null format should've thrown IllegalArgumentException");
        }catch(IllegalArgumentException e){}
    }

    @Test
    public void testPutNewVersionOfDocumentPdf(){
        //put the first version
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
        assertEquals("failed to return correct pdf text",this.pdfTxt1,edu.yu.cs.com1320.project.Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));

        //put the second version, testing both return value of put and see if it gets the correct text
        returned = store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue("should return hashcode of old text",this.pdfTxt1.hashCode() == returned || this.pdfTxt2.hashCode() == returned);
        assertEquals("failed to return correct pdf text", this.pdfTxt2,edu.yu.cs.com1320.project.Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
    }

    @Test
    public void testPutNewVersionOfDocumentTxt(){
        //put the first version
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.txt1.hashCode());
        assertEquals("failed to return correct text",this.txt1,store.getDocumentAsTxt(this.uri1));

        //put the second version, testing both return value of put and see if it gets the correct text
        returned = store.putDocument(new ByteArrayInputStream(this.txt2.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue("should return hashcode of old text",this.txt1.hashCode() == returned || this.txt2.hashCode() == returned);
        assertEquals("failed to return correct text",this.txt2,store.getDocumentAsTxt(this.uri1));
    }

    @Test
    public void testGetTxtDocAsPdf(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.txt1.hashCode());
        assertEquals("failed to return correct pdf text",this.txt1,edu.yu.cs.com1320.project.Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
    }

    @Test
    public void testGetTxtDocAsTxt(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.txt1.getBytes()),this.uri1, DocumentStore.DocumentFormat.TXT);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.txt1.hashCode());
        assertEquals("failed to return correct text",this.txt1,store.getDocumentAsTxt(this.uri1));
    }

    @Test
    public void testGetPdfDocAsPdf(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
        assertEquals("failed to return correct pdf text",this.pdfTxt1,edu.yu.cs.com1320.project.Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
    }

    @Test
    public void testGetPdfDocAsTxt(){
        DocumentStore store = new DocumentStoreImpl();
        int returned = store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //TODO allowing for student following old API comment. To be changed for stage 2 to insist on following new comment.
        assertTrue(returned == 0 || returned == this.pdfTxt1.hashCode());
        assertEquals("failed to return correct text",this.pdfTxt1,store.getDocumentAsTxt(this.uri1));
    }

    @Test
    public void testDeleteDoc(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.deleteDocument(this.uri1);
        assertEquals("calling get on URI from which doc was deleted should've returned null", null, store.getDocumentAsPdf(this.uri1));
    }

    @Test
    public void testDeleteDocReturnValue(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        //should return true when deleting a document
        assertEquals("failed to return true when deleting a document",true,store.deleteDocument(this.uri1));
        //should return false if I try to delete the same doc again
        assertEquals("failed to return false when trying to delete that which was already deleted",false,store.deleteDocument(this.uri1));
        //should return false if I try to delete something that was never there to begin with
        assertEquals("failed to return false when trying to delete that which was never there to begin with",false,store.deleteDocument(this.uri2));
    }

    @Test
    public void testTrie(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3),this.uri3, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4),this.uri4, DocumentStore.DocumentFormat.PDF);

        ArrayList<String> list = (ArrayList<String>) store.search("ab");
        ArrayList<String> compare = new ArrayList<String>();
        compare.add(this.pdfTxt2);
        compare.add(this.pdfTxt3);
        assertEquals(list, compare);
        store.deleteAll("ab");
        compare.remove(this.pdfTxt2);
        compare.remove(this.pdfTxt3);
        list = (ArrayList<String>) store.search("ab");
        assertEquals(compare, list);
        list = (ArrayList<String>) store.search("b");
        //assertEquals(compare, list);
        list = (ArrayList<String>) store.search("efg");
        compare.add(this.pdfTxt4);
        assertEquals(compare, list);
        store.undo();
        compare.remove(this.pdfTxt4);
        compare.add(this.pdfTxt3);
        compare.add(this.pdfTxt2);
        list = (ArrayList<String>) store.search("ab");
        assertEquals(compare, list);
    }

    @Test
    public void testDeleteDocumet(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3),this.uri3, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4),this.uri4, DocumentStore.DocumentFormat.PDF);
        ArrayList<String> list = (ArrayList<String>) store.search("ab");
        ArrayList<String> compare = new ArrayList<String>();
        compare.add(this.pdfTxt2);
        compare.add(this.pdfTxt3);
        assertEquals(list, compare);
        store.deleteDocument(this.uri3);
        compare.remove(this.pdfTxt3);
        list = (ArrayList<String>) store.search("ab");
        assertEquals(compare, list);
        store.undo();
        compare.add(this.pdfTxt3);
        list = (ArrayList<String>) store.search("ab");
        assertEquals(compare, list);
    }

    @Test
    public void deleteAllWithPrefix(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3),this.uri3, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4),this.uri4, DocumentStore.DocumentFormat.PDF);
        ArrayList<String> list = (ArrayList<String>) store.searchByPrefix("a");
        ArrayList<String> compare = new ArrayList<String>();
        compare.add(this.pdfTxt1);
        compare.add(this.pdfTxt2);
        compare.add(this.pdfTxt3);
        assertEquals(compare, list);
        store.deleteAllWithPrefix("a");
        compare.remove(this.pdfTxt1);
        compare.remove(this.pdfTxt2);
        compare.remove(this.pdfTxt3);
        list = (ArrayList<String>) store.searchByPrefix("a");
        assertEquals(compare, list);
        store.undo();
        compare.add(this.pdfTxt1);
        compare.add(this.pdfTxt2);
        compare.add(this.pdfTxt3);
        list = (ArrayList<String>) store.searchByPrefix("a");
        assertEquals(compare, list);
    }

    @Test
    public void testUndo() {
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3),this.uri3, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4),this.uri4, DocumentStore.DocumentFormat.PDF);

        ArrayList<String> list = (ArrayList<String>) store.search("ab");
        ArrayList<String> compare = new ArrayList<String>();
        compare.add(this.pdfTxt2);
        compare.add(this.pdfTxt3);
        assertEquals(list, compare);
        store.deleteAll("ab");
        compare.remove(this.pdfTxt2);
        compare.remove(this.pdfTxt3);
        list = (ArrayList<String>) store.search("ab");
        assertEquals(compare, list);
        store.undo(this.uri3);
        compare.add(this.pdfTxt3);
        list = (ArrayList<String>) store.search("ab");
        assertEquals(compare, list);
    }

     */
}
