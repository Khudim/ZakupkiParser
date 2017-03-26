package com.khudim.dao.notifications;

import com.khudim.dao.AbstractDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author hudyshkin
 */

@Repository
@Transactional(rollbackFor = Exception.class)
public class NotificationRepository extends AbstractDao<Long, Notification> {

    public void updateNotification(Notification notification){
        getSession().saveOrUpdate(notification);
    }

}
