package com.khudim.dao.docs;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public Documents getDocumentByGuid(String uuid) {
        return (Documents) getSession().get(Documents.class, uuid);
    }

    public void updateDocument(Documents documents) {
        getSession().saveOrUpdate(documents);
    }

    @SuppressWarnings("unchecked")
    public List<Documents> getAllDocumentsOnPage(int page, int maxResult) {
        return getCriteria().setFirstResult(page * maxResult).setMaxResults(maxResult).list();
    }
}
