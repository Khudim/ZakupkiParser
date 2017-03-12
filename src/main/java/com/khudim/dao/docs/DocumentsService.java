package com.khudim.dao.docs;

import com.khudim.document.IParsedDocument;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public List<Documents> getAllDocuments() {
        return repository.getAllDocuments();
    }

    public List<Documents> getPagingDocuments(int start, int length, int columnNumber, String order, Map<Integer,String> restrictions) {
        Order criteriaOrder;
        if (StringUtils.isNotBlank(order)) {
            if (order.equals("asc")) {
                criteriaOrder = Order.asc(Documents.getColumnName(columnNumber));
            } else {
                criteriaOrder = Order.desc(Documents.getColumnName(columnNumber));
            }
        } else {
            criteriaOrder = Order.asc(Documents.getColumnName(0));
        }
        return repository.getAllDocumentsFrom(start, length, criteriaOrder, restrictions);
    }

    public long getAllDocumentsCount() {
        return repository.getAllDocumentsCount();
    }

    public long getFilterCount(Map<Integer, String> searchedColumns) {
        return repository.getFilterCount(searchedColumns);
    }
}
