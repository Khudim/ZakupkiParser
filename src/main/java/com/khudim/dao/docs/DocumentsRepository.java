package com.khudim.dao.docs;

import com.khudim.dao.AbstractDao;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.khudim.dao.docs.DocumentsFields.REGION;

/**
 * Created by Beaver.
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class DocumentsRepository extends AbstractDao<String, Documents> {

    void updateDocument(Documents documents) {
        getSession().saveOrUpdate(documents);
    }

    long getAllDocumentsCount() {
        return (long) createEntityCriteria()
                .setProjection(Projections.rowCount())
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    List<String> getAllRegions() {
        return createEntityCriteria()
                .setProjection(Projections.distinct(Projections.property(REGION)))
                .list();
    }

    @SuppressWarnings("unchecked")
    List<Documents> getAllDocuments(List<SimpleExpression> restrictions, int start, int length, Order order) {
        Criteria criteria = createEntityCriteria();
        restrictions.forEach(criteria::add);
        return criteria.setFirstResult(start)
                .setMaxResults(length)
                .addOrder(order)
                .list();
    }

    long getFilterCount(List<SimpleExpression> restrictions) {
        Criteria criteria = createEntityCriteria();
        restrictions.forEach(criteria::add);
        return (long) criteria.setProjection(Projections.rowCount()).uniqueResult();
    }
}
