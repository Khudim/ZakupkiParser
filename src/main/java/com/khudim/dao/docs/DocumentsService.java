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

import static com.khudim.dao.docs.DocumentsFields.*;
import static com.khudim.helpers.ParseHelper.parseDocumentsDate;

/**
 * Created by Beaver.
 */
@Service
public class DocumentsService {

    private final DocumentsRepository repository;

    @Autowired
    public DocumentsService(DocumentsRepository repository) {
        this.repository = repository;
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

    public List<Documents> getPagingDocuments(int start, int length, int columnNumber, String order, Map<Integer, String> restrictions) {
        List<SimpleExpression> expressions = createExpressionsFromString(restrictions);
        Order criteriaOrder = createOrder(columnNumber, order);
        return repository.getAllDocuments(expressions, start, length, criteriaOrder);
    }

    public List<Documents> getSearchedDocuments(Notification notification, int start, int length, int columnNumber, String order) {
        List<SimpleExpression> restrictions = createExpressionsFromNotification(notification);
        Order criteriaOrder = createOrder(columnNumber, order);
        return repository.getAllDocuments(restrictions, start, length, criteriaOrder);
    }

    public long getFilterCount(Notification notification) {
        List<SimpleExpression> restrictions = createExpressionsFromNotification(notification);
        return repository.getFilterCount(restrictions);
    }

    private List<SimpleExpression> createExpressionsFromNotification(Notification notification) {
        List<SimpleExpression> restrictions = new ArrayList<>();
        Double minPrice = notification.getMinPrice();
        if (minPrice != null) {
            restrictions.add(Restrictions.ge(PRICE, minPrice));
        }
        Double maxPrice = notification.getMaxPrice();
        if (maxPrice != null) {
            restrictions.add(Restrictions.le(PRICE, maxPrice));
        }
        long date = System.currentTimeMillis() - notification.getDate() * 86400000;
        restrictions.add(Restrictions.ge(DATE, date));
        restrictions.add(Restrictions.like(REGION, notification.getRegions(), MatchMode.ANYWHERE));
        return restrictions;
    }

    private List<SimpleExpression> createExpressionsFromString(Map<Integer, String> restrictions) {
        List<SimpleExpression> expressions = new ArrayList<>();
        restrictions.forEach((column, value) -> {
            String columnName = columns.get(column);
            if ((PRICE).equals(columnName)) {
                if (StringUtils.isNumeric(value)) {
                    expressions.add(Restrictions.ge(columnName, Double.parseDouble(value)));
                }
            } else {
                expressions.add(Restrictions.like(columnName, value, MatchMode.ANYWHERE));
            }
        });
        return expressions;
    }

    public long getAllDocumentsCount() {
        return repository.getAllDocumentsCount();
    }

    public long getFilterCount(Map<Integer, String> searchedColumns) {
        return repository.getFilterCount(createExpressionsFromString(searchedColumns));
    }

    public List<String> getAllRegions() {
        return repository.getAllRegions();
    }

    private Order createOrder(int columnNumber, String order) {
        Order criteriaOrder;
        if (StringUtils.isNotBlank(order)) {
            if (order.equals("asc")) {
                criteriaOrder = Order.asc(columns.get(columnNumber));
            } else {
                criteriaOrder = Order.desc(columns.get(columnNumber));
            }
        } else {
            criteriaOrder = Order.asc(columns.get(2));
        }
        return criteriaOrder;
    }
}
