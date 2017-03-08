package com.khudim.dao.docs;

import com.khudim.document.IParsedDocument;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.khudim.helpers.ParseHelper.parseDocumentsDate;

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
        Documents document = new Documents();
        document.setGuid(parsedDocument.getGuid());
        document.setCreationDate(parseDocumentsDate(parsedDocument.getStartDate()));
        document.setDocumentType(type);
        document.setUrl(parsedDocument.getUrl());
        document.setPrice(parsedDocument.getPrice());
        document.setRegion(parsedDocument.getCity());
        repository.updateDocument(document);
    }

    public List<Documents> getAllDocuments(){
        return repository.getAllDocuments();
    }

    public List<Documents> getPagingDocuments(int start, int lentgh){
        return repository.getAllDocumentsFrom(start,lentgh);
    }

    public long getAllDocumentsCount() {
        return repository.getAllDocumentsCount();
    }
}
