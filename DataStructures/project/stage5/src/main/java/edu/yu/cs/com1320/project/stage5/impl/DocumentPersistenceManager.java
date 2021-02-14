package edu.yu.cs.com1320.project.stage5.impl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.PersistenceManager;
//import jdk.internal.org.objectweb.asm.TypeReference;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * created by the document store and given to the BTree via a call to BTree.setPersistenceManager
 */
public class DocumentPersistenceManager implements PersistenceManager<URI, Document> {

    private String baseDir = System.getProperty("user.dir");

    private class DocumentSerializer implements JsonSerializer<DocumentImpl> {

        @Override
        public JsonElement serialize(DocumentImpl document, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject object = new JsonObject();
            Gson gson = new Gson();
            String text = document.getDocumentAsTxt();
            URI uri = document.getKey();
            int hc = document.getDocumentTextHashCode();
            Map<String, Integer> map = document.getWordMap();
            object.addProperty("text", text);
            object.addProperty("uri", uri.toString());
            object.addProperty("hc", hc);
            object.addProperty("map", gson.toJson(map));
            return object;
        }
    }

    private class DocumentDeserialiser implements JsonDeserializer<DocumentImpl> {

        @Override
        public DocumentImpl deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            Gson gson = new Gson();
            String text = json.getAsJsonObject().get("text").getAsString();
            try {
                URI uri = new URI (json.getAsJsonObject().get("uri").getAsString());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            int hc = json.getAsJsonObject().get("hc").getAsInt();
            Type type1 = new TypeToken<HashMap<String, Integer>>(){}.getType();
            HashMap<String, Integer> map = gson.fromJson(json.getAsJsonObject().get("map").getAsString(), type1);
            DocumentImpl doc = null;
            try {
                doc = new DocumentImpl(new URI(json.getAsJsonObject().get("uri").getAsString()), text, hc);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            doc.setWordMap(map);
            return doc;
        }
    }

    public DocumentPersistenceManager(){
    }

    public DocumentPersistenceManager(File baseDir){
        this.baseDir = baseDir.toString();
    }

    @Override
    public void serialize(URI uri, Document val) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(DocumentImpl.class, new DocumentSerializer()).setPrettyPrinting().create();
        Type docType = new TypeToken<DocumentImpl>() {
        }.getType();
        //gson.toJson(val, docType);
        String host = uri.getHost().replace("/", File.separator);
        host = uri.getHost().replace("\\", File.separator);
        String path = uri.getPath().replace("/", File.separator);
        path = uri.getPath().replace("\\", File.separator);
        String fileName = this.baseDir + File.separator + host + path + ".json";
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(gson.toJson(val, docType));
        writer.close();
    }

    @Override
    public DocumentImpl deserialize(URI uri) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(DocumentImpl.class, new DocumentDeserialiser()).create();
        Type docType = new TypeToken<DocumentImpl>() {}.getType();
        String host = uri.getHost().replace("/", File.separator);
        host = uri.getHost().replace("\\", File.separator);
        String path = uri.getPath().replace("/", File.separator);
        path = uri.getPath().replace("\\", File.separator);
        String fileName = this.baseDir + File.separator + host + path + ".json";
        File tmpDir = new File(fileName);
        if(tmpDir.exists()){
            DocumentImpl doc = gson.fromJson(new String(Files.readAllBytes(Paths.get(fileName))), docType);
            tmpDir.delete();
            tmpDir = tmpDir.getParentFile();
            while(tmpDir != null){
                File holder = tmpDir;
                tmpDir.delete();
                tmpDir = holder.getParentFile();
            }
            return doc;
        }
        return null;
    }

    protected DocumentImpl backDoorDeserializer(URI uri) throws IOException {
        Gson gson = new GsonBuilder().registerTypeAdapter(DocumentImpl.class, new DocumentDeserialiser()).create();
        Type docType = new TypeToken<DocumentImpl>() {}.getType();
        String fileName = this.baseDir + "/" + uri.getHost() + uri.getPath() + ".json";
        File tmpDir = new File(fileName);
        if(tmpDir.exists()){
            DocumentImpl doc = gson.fromJson(new String(Files.readAllBytes(Paths.get(fileName))), docType);
            return doc;
        }
        return null;
    }

/*
    public static void main(String[] args) throws IOException {
        String text = "hello";
        URI uri1 = null;
        URI uri2 = null;
        try {
            uri1 = new URI("http://www.edu.yu.cs/com1320/doc1");
            uri2 = new URI("http://www.edu.yu.cs/com1320/doc2");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        DocumentImpl doc1 = new DocumentImpl(uri1, text, text.hashCode());
        DocumentImpl doc2 = new DocumentImpl(uri2, text, text.hashCode());
        DocumentPersistenceManager pm = new DocumentPersistenceManager();
        pm.serialize(uri1, doc1);
        pm.serialize(uri2, doc2);
        DocumentImpl deserializedDoc1 = (DocumentImpl) pm.deserialize(uri1);
        DocumentImpl deserializedDoc2 = (DocumentImpl) pm.deserialize(uri2);
        System.out.println(deserializedDoc1.getDocumentAsTxt());
    } */
}
