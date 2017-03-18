package com.khudim.parser;

import com.khudim.dao.docs.DocumentsService;
import com.khudim.document.IParsedDocument;
import com.khudim.document.ParsedDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Created by Beaver.
 */
@Component
@EnableAsync
@EnableScheduling
@PropertySource(value = {"classpath:application.properties"})
public class XmlParser {

    private DocumentsService service;

    private String tempDir;

    @Async
    @Scheduled(cron = "${scheduler.xmlParser.cron}")
    public void findDocuments() {
        System.out.println("Start search documents " + tempDir);
        try {
            Path paths = Paths.get(tempDir);
            Files.walk(paths, FileVisitOption.FOLLOW_LINKS)
                    //.filter(ParseHelper::isLastDayModified)
                    .forEach(this::addParsedDocumentToDataBase);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("im done parse xml");
        }
    }
    @Autowired
    public XmlParser(@Value("${parser.tempFolder}") String tempDir, DocumentsService service) {
        this.tempDir = tempDir;
        this.service = service;
    }

    private void addParsedDocumentToDataBase(Path path) {
        if (path.toString().endsWith(".zip")) {
            try (ZipInputStream inputStream = new ZipInputStream(new FileInputStream(path.toFile()))) {
                while (inputStream.available() != 0) {
                    ZipEntry entry = inputStream.getNextEntry();
                    if (entry != null) {
                        ZipFile zipFile = new ZipFile(path.toFile());
                        Document document = parse(zipFile.getInputStream(entry));
                        saveDocument(document);
                    }
                }
            } catch (IOException e) {
               // e.printStackTrace();
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

    private void saveDocument(Document document) {
        IParsedDocument parsedDocument = new ParsedDocument();
        parsedDocument.fillDocument(document);
        if(parsedDocument.isIncorrectDocument()){
            return;
        }
        service.updateDocument(parsedDocument);
    }

}
