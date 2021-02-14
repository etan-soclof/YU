import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class UndoTest {
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
        this.pdfTxt1 = "This is some PDF text for doc1, hat tip to Adobe.";
        this.pdfData1 = Utils.textToPdfData(this.pdfTxt1);

        //init possible values for doc2
        this.uri2 = new URI("http://edu.yu.cs/com1320/project/doc2");
        this.txt2 = "Text for doc2. A plain old String.";
        this.pdfTxt2 = "PDF content for doc2: PDF format was opened in 2008.";
        this.pdfData2 = Utils.textToPdfData(this.pdfTxt2);

        this.uri3 = new URI("http://edu.yu.cs/com1320/project/doc3");
        this.txt3 = "Text for doc3. A plain old String.";
        this.pdfTxt3 = "PDF content for doc3: PDF format was opened in 2008.";
        this.pdfData3 = Utils.textToPdfData(this.pdfTxt3);

        this.uri4 = new URI("http://edu.yu.cs/com1320/project/doc4");
        this.txt4 = "Text for doc4. A plain old String.";
        this.pdfTxt4 = "PDF content for doc4: PDF format was opened in 2008.";
        this.pdfData4 = Utils.textToPdfData(this.pdfTxt4);
    }

    @Test
    public void testUndoPutTopOfStack(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.undo();
        assertEquals("calling get on URI from which doc was deleted should've returned null", null, store.getDocumentAsPdf(this.uri1));
    }

    @Test
    public void testUndoPutMiddleOfStack(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3),this.uri3, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4),this.uri4, DocumentStore.DocumentFormat.PDF);
        store.undo(this.uri2);
        assertEquals("calling get on URI from which doc was deleted should've returned null", null, store.getDocumentAsPdf(this.uri2));
        assertEquals("failed to return correct pdf text",this.pdfTxt1,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri1)));
        assertEquals("failed to return correct pdf text",this.pdfTxt3,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri3)));
    }

    @Test
    public void testUndoPutMiddleOfStackReplace() {
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3),this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4),this.uri4, DocumentStore.DocumentFormat.PDF);
        assertEquals("failed to return correct pdf text",this.pdfTxt3,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri2)));
        store.undo(this.uri2);
        assertEquals("failed to return correct pdf text",this.pdfTxt2,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri2)));
    }

    @Test
    public void testExceptions(){
        DocumentStore store = new DocumentStoreImpl();
        try {
            store.undo();
            fail("should've thrown IllegalStateException");
        }catch(IllegalStateException e){}
        try {
            store.undo(uri1);
            fail("should've thrown IllegalStateException");
        }catch(IllegalStateException e){}
    }

    @Test
    public void testUndoDelete(){
        DocumentStore store = new DocumentStoreImpl();
        store.putDocument(new ByteArrayInputStream(this.pdfData1),this.uri1, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData2),this.uri2, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData3),this.uri3, DocumentStore.DocumentFormat.PDF);
        store.putDocument(new ByteArrayInputStream(this.pdfData4),this.uri4, DocumentStore.DocumentFormat.PDF);
        assertEquals("failed to return correct pdf text",this.pdfTxt2,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri2)));
        store.deleteDocument(uri2);
        assertEquals("calling get on URI from which doc was deleted should've returned null", null, store.getDocumentAsPdf(this.uri2));
        store.undo(uri2);
        assertEquals("failed to return correct pdf text",this.pdfTxt2,Utils.pdfDataToText(store.getDocumentAsPdf(this.uri2)));
    }

     */
}
