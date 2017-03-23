package com.khudim.dao.docs;

import com.khudim.dao.notifications.Notification;
import com.khudim.document.IParsedDocument;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.khudim.dao.docs.Documents.*;
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

    public void updateDocument(IParsedDocument parsedDocument) {
        Documents document = new Documents();
        document.setGuid(parsedDocument.getGuid());
        document.setCreationDate(parseDocumentsDate(parsedDocument.getStartDate()));
        document.setUrl(parsedDocument.getUrl());
        document.setPrice(parsedDocument.getPrice());
        document.setRegion(parsedDocument.getCity());
        repository.updateDocument(document);
    }

    public List<Documents> getAllDocuments() {
        return repository.getAllDocuments();
    }

    public List<Documents> getPagingDocuments(int start, int length, int columnNumber, String order, Map<Integer, String> restrictions) {
        Order criteriaOrder = createOrder(columnNumber, order);
        return repository.getAllDocumentsFrom(start, length, criteriaOrder, restrictions);
    }

    public long getAllDocumentsCount() {
        return repository.getAllDocumentsCount();
    }

    public long getFilterCount(Map<Integer, String> searchedColumns) {
        return repository.getFilterCount(searchedColumns);
    }

    public List<String> getAllRegions() {
        return repository.getAllRegions();
    }

    public List<Documents> getSearchedDocuments(Notification notification, int start, int length, int columnNumber, String order) {
        List<SimpleExpression> restrictions = getSimpleExpressions(notification);
        Order criteriaOrder = createOrder(columnNumber, order);
        return repository.getAllDocuments(restrictions, start, length, criteriaOrder);
    }

    private List<SimpleExpression> getSimpleExpressions(Notification notification) {
        List<SimpleExpression> restrictions = new ArrayList<>();
        Double minPrice = notification.getMinPrice();
        if (minPrice != null) {
            restrictions.add(Restrictions.ge(PRICE, minPrice));
        }
        Double maxPrice = notification.getMaxPrice();
        if (maxPrice != null) {
            restrictions.add(Restrictions.ge(PRICE, maxPrice));
        }
        restrictions.add(Restrictions.ge(DATE, notification.getDate()));
        restrictions.add(Restrictions.like(REGION, notification.getRegions(), MatchMode.ANYWHERE));
        return restrictions;
    }

    public long getFilterCount(Notification notification) {
        List<SimpleExpression> restrictions = getSimpleExpressions(notification);
        return repository.getFilterCount(restrictions);
    }

    private Order createOrder(int columnNumber, String order) {
        Order criteriaOrder;
        if (StringUtils.isNotBlank(order)) {
            if (order.equals("asc")) {
                criteriaOrder = Order.asc(Documents.getColumnName(columnNumber));
            } else {
                criteriaOrder = Order.desc(Documents.getColumnName(columnNumber));
            }
        } else {
            criteriaOrder = Order.asc(Documents.getColumnName(2));
        }
        return criteriaOrder;
    }
}
