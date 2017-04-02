package com.khudim.controller;

import com.khudim.dao.DataTableObject;
import com.khudim.dao.docs.Documents;
import com.khudim.dao.docs.DocumentsService;
import com.khudim.dao.notifications.Notification;
import com.khudim.dao.person.Person;
import com.khudim.dao.person.PersonService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Beaver.
 */
public class IndexControllerTest {

    private DocumentsService documentsService = Mockito.mock(DocumentsService.class);

    private PersonService personService = Mockito.mock(PersonService.class);

    private IndexController indexController = new IndexController(documentsService, personService);

    @Before
    public void setUp() {
    }

    @Test
    public void shouldGetAllNotificationDocuments() {
        Documents document = new Documents();
        document.setGuid("1");
        document.setRegion("Moskva");
        document.setPrice(15d);
        List<Documents> documents = new ArrayList<>();
        documents.add(document);
        Notification notification = new Notification();
        notification.setMinPrice(10d);
        notification.setMaxPrice(20d);
        notification.setId(1);
        notification.setRegions("Moskva");
        Person person = new Person();
        person.setCode("1");
        person.setNotifications(Collections.singletonList(notification));
        Mockito.when(personService.getCurrentUser()).thenReturn(person);
        int start = 0;
        int length = 10;
        int columnNumber = 1;
        String order = "asc";
        Mockito.when(documentsService.getSearchedDocuments(notification, start, length, columnNumber, order))
                .thenReturn(documents);
        DataTableObject object = indexController.getAllNotificationDocuments(start, 1, order, columnNumber, length);
        Assert.assertArrayEquals(documents.toArray(), object.getData().toArray());
    }

    @Test
    public void shouldUpdateNotification() {
        Person person = new Person();
        person.setCode("2");
        person.setNotifications(Collections.emptyList());
        Mockito.when(personService.getCurrentUser()).thenReturn(person);
        String minPrice = "10";
        String maxPrice = "20";
        String city = "Spb";
        String date = "2323";
        String rate = "1";
        Notification notification = indexController.updateNotification(minPrice, maxPrice, city, date, rate);
        Assert.assertEquals(maxPrice, String.valueOf(Math.round(notification.getMaxPrice())));
        Assert.assertEquals(minPrice, String.valueOf(Math.round(notification.getMinPrice())));
        Assert.assertEquals(city, notification.getRegions());
        Assert.assertEquals(date, String.valueOf(notification.getDate()));
        Assert.assertEquals(rate, String.valueOf(notification.getRate()));
    }
}
