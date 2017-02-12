package com.khudim.parser;

import com.khudim.filter.Requirement;
import com.khudim.filter.ParseResult;
import com.khudim.helpers.Tag;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Beaver.
 */
public class XmlParser {

    private static Map<Tag, String> req = new HashMap<>();

    static {
        req.put(Tag.PRICE, null);
        req.put(Tag.URL, null);
    }

    public Map<Tag, String> parse(InputStream inputStream, Map<Tag, String> tags) throws IOException {

        DocumentBuilderFactory dbFactory;
        DocumentBuilder dBuilder;
        Document doc = null;
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (doc == null) {
            return null;
        }
        NodeList nodeList;
        Node node;
//        tags.forEach((k, v) -> System.out.println(k));
        for (Map.Entry<Tag, String> entry : tags.entrySet()) {
            nodeList = doc.getElementsByTagName(entry.getKey().tag());
            node = nodeList.item(0);
            if(node!=null) {
                tags.put(entry.getKey(), node.getTextContent());
            }
        }
        return tags;
     /*   for(String tag : tags) {
            nodeList = doc.getElementsByTagName(tag);
            System.out.println(nodeList.getLength());
            System.out.println(nodeList.item(0).getTextContent());
        }
        for (int temp = 0; temp < nodeList.getLength(); temp++) {
            Node node = nodeList.item(temp);
            System.out.println(node.getTextContent());
        }*/
    }

    public void take(Path path, Map<Tag, String> tags) {
        System.err.println(path.getFileName());
        if (path.toString().endsWith(".zip")) {
            try (ZipInputStream inputStream = new ZipInputStream(new FileInputStream(path.toFile()))) {
                while (inputStream.available() != 0) {
                    ZipEntry entry = inputStream.getNextEntry();
                    if (entry != null) {
                        ZipFile zipFile = new ZipFile(path.toFile());
                        parse(zipFile.getInputStream(entry), tags).forEach((k,v)-> System.out.println(k.tag() + " : " + v));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test() throws IOException {
        String zipFileDir = "C:/Video/";
        Files.walk(Paths.get(zipFileDir), FileVisitOption.FOLLOW_LINKS).forEach(p -> take(p, req));
    }
}
