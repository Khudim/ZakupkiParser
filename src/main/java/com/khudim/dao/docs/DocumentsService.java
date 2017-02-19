package com.khudim.dao.docs;

import com.khudim.document.IParsedDocument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import static com.khudim.helpers.ParseHelper.parseDocumentsDate;
import static com.khudim.helpers.ParseHelper.serializeObject;

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
        byte[] bytes = serializeObject(parsedDocument);
        Documents document = new Documents();
        document.setGuid(parsedDocument.getGuid());
        document.setCreationDate(parseDocumentsDate(parsedDocument.getStartDate()));
        document.setDocumentType(type);
        document.setContent(bytes);
        document.setRegion(parsedDocument.getCity());
        repository.updateDocument(document);
    }

    public List<Documents> getAllDocumentsOnPage(String page, int maxResult) {
        int parsedPage = 0;
        if (StringUtils.isNotBlank(page)) {
            try {
                parsedPage = Integer.parseInt(page);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return repository.getAllDocumentsOnPage(parsedPage, maxResult);
    }
}
