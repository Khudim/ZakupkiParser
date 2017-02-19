package com.khudim.parser;

import com.khudim.dao.docs.DocumentsService;
import com.khudim.document.ContractDocument;
import com.khudim.document.ExplanationDocument;
import com.khudim.document.PurchaseDocument;
import com.khudim.helpers.ParseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static com.khudim.dao.docs.DocumentsType.CONTRACT;
import static com.khudim.dao.docs.DocumentsType.EXPLANATION;
import static com.khudim.dao.docs.DocumentsType.PURCHASE;
import static com.khudim.helpers.ParseHelper.isLastDayModified;

/**
 * Created by Beaver.
 */
@Component
public class XmlParser {

    @Autowired
    private DocumentsService service;

    @SuppressWarnings("unchecked")
    public void findDocuments(String dir) {
        try {
            Path paths = Paths.get(dir);
            Files.walk(paths, FileVisitOption.FOLLOW_LINKS)
                    .filter(ParseHelper::isLastDayModified)
                    .forEach(this::addParsedDocumentToDataBase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addParsedDocumentToDataBase(Path path) {
        if (path.toString().endsWith(".zip")) {
            try (ZipInputStream inputStream = new ZipInputStream(new FileInputStream(path.toFile()))) {
                while (inputStream.available() != 0) {
                    ZipEntry entry = inputStream.getNextEntry();
                    if (entry != null) {
                        ZipFile zipFile = new ZipFile(path.toFile());
                        Document document = parse(zipFile.getInputStream(entry));
                        fillDocument(document, path);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Document parse(InputStream inputStream) throws IOException {
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
        return doc;
    }

    private void fillDocument(Document document, Path path) {
        if (path.toString().contains(CONTRACT.type())) {
            service.updateDocument(new ContractDocument(document), CONTRACT.type());
        } else if (path.toString().contains(EXPLANATION.type())) {
            service.updateDocument(new ExplanationDocument(document), EXPLANATION.type());
        } else if (path.toString().contains(PURCHASE.type())) {
            service.updateDocument(new PurchaseDocument(document), PURCHASE.type());
        }
    }

}
