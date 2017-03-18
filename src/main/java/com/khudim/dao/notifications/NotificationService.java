package com.khudim.dao.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hudyshkin
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repository;

    public void updateNotification(Notification notification){
        repository.updateNotification(notification);
    }
}
