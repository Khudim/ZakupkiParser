package com.khudim.dao.docs;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.khudim.dao.docs.Documents.PRICE;
import static com.khudim.dao.docs.Documents.REGION;

/**
 * Created by Beaver.
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class DocumentsRepository {

    @Autowired
    private SessionFactory sessionFactory;

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    private Criteria getCriteria() {
        return getSession().createCriteria(Documents.class);
    }

    Documents getDocumentByGuid(String uuid) {
        return (Documents) getSession().get(Documents.class, uuid);
    }

    void updateDocument(Documents documents) {
        getSession().saveOrUpdate(documents);
    }

    @SuppressWarnings("unchecked")
    List<Documents> getAllDocumentsFrom(int loadFrom, int maxResult, Order order, Map<Integer, String> restrictions) {
        return addRestrictions(restrictions)
                .setFirstResult(loadFrom)
                .setMaxResults(maxResult)
                .addOrder(order)
                .list();
    }

    @SuppressWarnings("unchecked")
    List<Documents> getAllDocuments() {
        return getCriteria().list();
    }

    long getAllDocumentsCount() {
        return (long) getCriteria().setProjection(Projections.rowCount()).uniqueResult();
    }

    long getFilterCount(Map<Integer, String> restrictions) {
        return (long) addRestrictions(restrictions).setProjection(Projections.rowCount()).uniqueResult();
    }

    private Criteria addRestrictions(Map<Integer, String> restrictions) {
        Criteria criteria = getCriteria();
        restrictions.forEach((column, value) -> {
            String columnName = Documents.getColumnName(column);
            if ((PRICE).equals(columnName)) {
                criteria.add(Restrictions.ge(columnName, Double.parseDouble(value)));
            } else {
                criteria.add(Restrictions.like(columnName, value, MatchMode.ANYWHERE));
            }
        });
        return criteria;
    }

    @SuppressWarnings("unchecked")
    public List<String> getAllRegions() {
        return getCriteria().setProjection(Projections.distinct(Projections.property(REGION))).list();
    }

    @SuppressWarnings("unchecked")
    public List<Documents> getAllDocuments(List<SimpleExpression> restrictions, int start, int length, Order order) {
        Criteria criteria = getCriteria();
        criteria.setFirstResult(start);
        criteria.addOrder(order);
        if(length != 0) {
            criteria.setMaxResults(length);
        }
        restrictions.forEach(criteria::add);
        return criteria.list();
    }

    public long getFilterCount(List<SimpleExpression> restrictions) {
        Criteria criteria = getCriteria();
        restrictions.forEach(criteria::add);
        return (long) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }
}
