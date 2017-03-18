package com.khudim.dao.notifications;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hudyshkin
 */

@Repository
@Transactional(rollbackFor = Exception.class)
public class NotificationRepository {

    @Autowired
    private SessionFactory sessionFactory;

    public void updateNotification(Notification notification){
        getSession().saveOrUpdate(notification);
    }

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    protected Criteria createEntityCriteria(){
        return getSession().createCriteria(Notification.class);
    }

}
