package com.khudim.dao.docs;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;

import static com.khudim.helpers.Tag.PRICE;

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
            if ((PRICE.tag()).equals(columnName)) {
                criteria.add(Restrictions.ge(columnName, Double.parseDouble(value)));
            } else {
                criteria.add(Restrictions.like(columnName, value, MatchMode.ANYWHERE));
            }
        });
        return criteria;
    }
    @SuppressWarnings("unchecked")
    public List<String> getAllRegions() {
        return getCriteria().setProjection(Projections.distinct(Projections.property("region"))).list();
    }
}
