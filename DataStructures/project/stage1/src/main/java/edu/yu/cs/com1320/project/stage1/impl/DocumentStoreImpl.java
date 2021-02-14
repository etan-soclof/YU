package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import edu.yu.cs.com1320.project.stage1.DocumentStore;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.font.*;
import org.apache.pdfbox.text.PDFTextStripper;

public class DocumentStoreImpl implements DocumentStore {

    private HashTableImpl<URI, DocumentImpl> table;
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
        if (input == null){
            if (getDocumentAsTxt(uri)!= null){
                int code = getDocumentAsTxt(uri).hashCode();
                deleteDocument(uri);
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
        if (table.get(uri) != null && table.get(uri).hashCode() == stringHC){ return stringHC; }
        if (format == DocumentFormat.PDF){
            DocumentImpl doc = new DocumentImpl(uri, text, stringHC, bytes);
            table.put(uri, doc);
            return doc.getDocumentTextHashCode();
        }
        else{
            DocumentImpl doc = null;
            doc = new DocumentImpl(uri, text, stringHC);
            table.put(uri, doc);
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
        if (table.put(uri, null) == null){
            return false;
        }
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


}


