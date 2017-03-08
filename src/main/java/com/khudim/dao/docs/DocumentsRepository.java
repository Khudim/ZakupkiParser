package com.khudim.dao.docs;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.print.Doc;
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
    public List<Documents> getAllDocumentsFrom(int loadFrom, int maxResult) {
        return getCriteria()
                .setFirstResult(loadFrom)
                .setMaxResults(maxResult)
                .list();
    }

    @SuppressWarnings("unchecked")
    public List<Documents> getAllDocuments(){
        return getCriteria().list();
    }

    public long getAllDocumentsCount() {
       return (long)getCriteria().setProjection(Projections.rowCount()).uniqueResult();
    }
}
