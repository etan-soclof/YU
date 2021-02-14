import edu.yu.cs.com1320.project.stage1.DocumentStore;
import edu.yu.cs.com1320.project.stage1.impl.DocumentStoreImpl;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class TestCode {

    private DocumentStoreImpl docStore = new DocumentStoreImpl();
    private String string1 = "a";
    private String string2 = "b";
    private  String string3 = "c";
    private String string4 = "d";
    private String string5 = "e";
    private String string6 = "f";
    private String string7 = "g";
    private String string8 = "h";
    private String string9 = "i";
    private String string10 = "j";
    private String string11 = "k";
    private String string12 = "l";
    private String string13 = "m";
    private String string14 = "n";
    private String string15 = "o";
    private String string16 = "p";
    private String string17 = "q";
    private String string18 = "r";
    private String string19 = "s";
    private String string20 = "t\nt";
    private String text1 = "k\nk";
    int count = 0;


    private InputStream in1 = new ByteArrayInputStream(string1.getBytes());
    private InputStream in2 = new ByteArrayInputStream(string2.getBytes());
    private InputStream in3 = new ByteArrayInputStream(string3.getBytes());
    private InputStream in4 = new ByteArrayInputStream(string4.getBytes());
    private InputStream in5 = new ByteArrayInputStream(string5.getBytes());
    private InputStream in6 = new ByteArrayInputStream(string6.getBytes());
    private InputStream in7 = new ByteArrayInputStream(string7.getBytes());
    private InputStream in8 = new ByteArrayInputStream(string8.getBytes());
    private InputStream in9 = new ByteArrayInputStream(string9.getBytes());
    private InputStream in10 = new ByteArrayInputStream(string10.getBytes());
    private InputStream in11 = new ByteArrayInputStream(string11.getBytes());
    private InputStream in12 = new ByteArrayInputStream(string12.getBytes());
    private InputStream in13 = new ByteArrayInputStream(string13.getBytes());
    private InputStream in14 = new ByteArrayInputStream(string14.getBytes());
    private InputStream in15 = new ByteArrayInputStream(string15.getBytes());
    private InputStream in16 = new ByteArrayInputStream(string16.getBytes());
    private InputStream in17 = new ByteArrayInputStream(string17.getBytes());
    private InputStream in18 = new ByteArrayInputStream(string18.getBytes());
    private InputStream in19 = new ByteArrayInputStream(string19.getBytes());
    private InputStream in20 = new ByteArrayInputStream(string20.getBytes());

    private URI uri1 = new URI ("aa");
    private URI uri2 = new URI ("bb");
    private URI uri3 = new URI ("cc");
    private URI uri4 = new URI ("dd");
    private URI uri5 = new URI ("ee");
    private URI uri6 = new URI ("ff");
    private URI uri7 = new URI ("gg");
    private URI uri8 = new URI ("hh");
    private URI uri9 = new URI ("ii");
    private URI uri10 = new URI ("jj");
    private URI uri11 = new URI ("kk");
    private URI uri12 = new URI ("ll");
    private URI uri13 = new URI ("mm");
    private URI uri14 = new URI ("nn");
    private URI uri15 = new URI ("oo");
    private URI uri16 = new URI ("pp");
    private URI uri17 = new URI ("qq");
    private URI uri18 = new URI ("rr");
    private URI uri19 = new URI ("ss");
    private URI uri20 = new URI ("tt");


    public TestCode() throws URISyntaxException {
    }

    @Before
    public void initialize () throws URISyntaxException, IOException {

        docStore.putDocument(in1, uri1, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in2, uri2, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in3, uri3, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in4, uri4, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in5, uri5, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in6, uri6, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in7, uri7, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in8, uri8, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in9, uri9, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in10, uri10, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in11, uri11, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in12, uri12, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in13, uri13, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in14, uri14, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in15, uri15, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in16, uri16, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in17, uri17, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in18, uri18, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in19, uri19, DocumentStore.DocumentFormat.TXT);
        docStore.putDocument(in20, uri20, DocumentStore.DocumentFormat.TXT);


        PDDocument pdf1 = new PDDocument();

        PDPage page1 = new PDPage();
        pdf1.addPage(page1);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        text1 = text1.trim();
        text1 = text1.replace("\n", "");
        text1 = text1.replace("\t", "");
        text1 = text1.replace("\r", "");
        try (PDPageContentStream contents = new PDPageContentStream(pdf1, page1)){
            contents.beginText();
            contents.setFont(font, 11);
           // contents.newLineAtOffset(100, 700);
            contents.showText(text1);
            contents.endText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream out11 = new ByteArrayOutputStream();
        pdf1.save(out11);
        pdf1.close();
        ByteArrayInputStream in11 = new ByteArrayInputStream(out11.toByteArray());

        docStore.putDocument(in11, uri11, DocumentStore.DocumentFormat.PDF);

    }

    @Test
    public void testReplace(){
        String string = "hello\nhello";
        string = string.replace("\n", "");
        System.out.println(string);
    }

    @Test
    public void testPutDoc () throws IOException, URISyntaxException {
        assertEquals(string1, docStore.getDocumentAsTxt(uri1));
        assertEquals(string2, docStore.getDocumentAsTxt(uri2));
        assertEquals(string3, docStore.getDocumentAsTxt(uri3));
        assertEquals(string4, docStore.getDocumentAsTxt(uri4));
        assertEquals(string5, docStore.getDocumentAsTxt(uri5));
        assertEquals(string6, docStore.getDocumentAsTxt(uri6));
        assertEquals(string7, docStore.getDocumentAsTxt(uri7));
        assertEquals(string8, docStore.getDocumentAsTxt(uri8));
        assertEquals(string9, docStore.getDocumentAsTxt(uri9));
        assertEquals(string10, docStore.getDocumentAsTxt(uri10));
        assertEquals("kk", docStore.getDocumentAsTxt(uri11));

    }

    @Test
    public void testGetAsPDF () throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        byte[] doc = docStore.getDocumentAsPdf(uri20);
        System.out.println(stripper.getText(PDDocument.load(doc)) + "sup");
    }

    @Test
    public void testDelete (){
        assertEquals(string4, docStore.getDocumentAsTxt(uri4));
        docStore.deleteDocument(uri1);
        docStore.deleteDocument(uri6);
        docStore.deleteDocument(uri11);
        docStore.deleteDocument(uri4);
        docStore.deleteDocument(uri15);


        assertEquals(null, docStore.getDocumentAsTxt(uri1));
        //assertEquals(null, docStore.getDocumentAsTxt(uri11));
        assertEquals(null, docStore.getDocumentAsTxt(uri4));
        assertEquals(null, docStore.getDocumentAsTxt(uri15));
        assertEquals(null, docStore.getDocumentAsTxt(uri6));
        assertEquals(string2, docStore.getDocumentAsTxt(uri2));
        assertEquals(string3, docStore.getDocumentAsTxt(uri3));
        assertEquals(string7, docStore.getDocumentAsTxt(uri7));
        assertEquals(string8, docStore.getDocumentAsTxt(uri8));
        assertEquals(string9, docStore.getDocumentAsTxt(uri9));
        assertEquals(string10, docStore.getDocumentAsTxt(uri10));
        assertEquals(string14, docStore.getDocumentAsTxt(uri14));
        assertEquals(string16, docStore.getDocumentAsTxt(uri16));

    }






}