package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.stage1.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class DocumentImpl implements Document {

    private String text;
    private URI uri;
    private int hc;
    private byte [] pdf;

    public DocumentImpl (URI uri, String text, int hc) {
        this.hc = hc;
        this.uri = uri;
        this.text = text;
        try {
            this.pdf = textToPDF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DocumentImpl (URI uri, String text, int hc, byte [] pdf){
        this.hc = hc;
        this.uri = uri;
        this.text = text;
        this.pdf = pdf;
    }

    @Override
    public byte[] getDocumentAsPdf() {
        return this.pdf;
    }

    @Override
    public String getDocumentAsTxt() {
        return this.text;
    }

    @Override
    public int getDocumentTextHashCode() {
        return this.hc;
    }

    @Override
    public URI getKey() {
        return this.uri;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj){
            return true;
        }
        if (obj == null){
            return false;
        }
        if (getClass()!=obj.getClass()) {
            return false;
        }
        DocumentImpl doc = (DocumentImpl) obj;
        if (doc.getDocumentAsTxt().equals(this.text) && doc.getKey().equals(this.uri)){
            return true;
        }
        return false;
    }

    private byte[] textToPDF (String text) throws IOException {
        text = text.trim();
        text = text.replace("\n", "");
        text = text.replace("\t", "");
        text = text.replace("\r", "");
        PDDocument pdf1 = new PDDocument();
        PDPage page = new PDPage();
        pdf1.addPage(page);
        PDFont font = PDType1Font.HELVETICA_BOLD;
        //System.out.println(text + "narishkeit");
        try (PDPageContentStream contents = new PDPageContentStream(pdf1, page)){
            contents.beginText();
            contents.setFont(font, 11);
            contents.newLineAtOffset(100, 700);
            contents.showText(text);
            contents.endText();
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        pdf1.save(output);
        pdf1.close();

        return output.toByteArray();
    }
}
