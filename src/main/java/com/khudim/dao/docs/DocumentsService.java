package com.khudim.dao.docs;

import com.khudim.document.IParsedDocument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by Beaver.
 */
@Service
public class DocumentsService {

    @Autowired
    private DocumentsRepository repository;

    public Documents getDocumentByGuid(String guid) {
        if (StringUtils.isNotBlank(guid)) {
            return repository.getDocumentByGuid(guid);
        }
        return null;
    }

    public void updateDocument(IParsedDocument parsedDocument, String type) {
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        byte[] bytes = new byte[0];
        try (ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            outputStream.writeObject(parsedDocument);
            bytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Documents document = new Documents();
        document.setGuid(parsedDocument.getGuid());
        document.setCreationDate(System.currentTimeMillis());
        document.setDocumentType(type);
        document.setContent(bytes);
        repository.updateDocument(document);
    }

    public List<Documents> getAllDocumentsOnPage(String page, int maxResult) {
        int parsedPage = 0;
        if(StringUtils.isNotBlank(page)) {
            try {
                parsedPage = Integer.parseInt(page);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return repository.getAllDocumentsOnPage(parsedPage, maxResult);
    }
}
