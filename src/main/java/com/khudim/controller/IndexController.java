package com.khudim.controller;

/**
 * Created by Beaver.
 */

import com.khudim.dao.DataTableObject;
import com.khudim.dao.docs.Documents;
import com.khudim.dao.docs.DocumentsService;
import com.khudim.dao.notifications.Notification;
import com.khudim.dao.notifications.NotificationService;
import com.khudim.dao.person.Person;
import com.khudim.dao.person.PersonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@Transactional(rollbackFor = Exception.class)
public class IndexController {

    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model) {
        String userName = getUser();
        Person person = personService.getPerson(userName);
        List<String> regions = documentsService.getAllRegions();
        model.addAttribute("regions", regions);
        model.addAttribute("user", person);
        return "index";
    }

    @RequestMapping(value = "/updateNotification", method = RequestMethod.POST)
    @ResponseBody
    public Notification updateNotification(@RequestParam(value = "minPrice", required = false) String minPrice,
                                           @RequestParam(value = "maxPrice", required = false) String maxPrice,
                                           @RequestParam(value = "city", required = false) String city,
                                           @RequestParam(value = "date", required = false) String date,
                                           @RequestParam(value = "rate", required = false) String rate) {
        Person person = personService.getPerson(getUser());
        Notification notification = person.getNotifications().stream().findFirst().orElse(null);
        if (notification == null) {
            notification = new Notification();
            person.getNotifications().add(notification);
        }
        if (StringUtils.isNotBlank(minPrice)) {
            notification.setMinPrice(Double.parseDouble(minPrice));
        }
        if (StringUtils.isNotBlank(maxPrice)) {
            notification.setMaxPrice(Double.parseDouble(maxPrice));
        }
        if (StringUtils.isNotBlank(city)) {
            notification.setRegions(city);
        }
        if (StringUtils.isNotBlank(rate)) {
            notification.setRate(Integer.parseInt(rate));
        }
        if (StringUtils.isNotBlank(date)) {
            notification.setDate(Long.parseLong(date));
        }
        return notification;
    }

    @RequestMapping(value = "/getAllNotificationDocuments", method = RequestMethod.POST)
    @ResponseBody
    public DataTableObject getResult(@RequestParam(value = "start") int start,
                                     @RequestParam(value = "draw") int draw,
                                     @RequestParam(value = "order[0][dir]") String order,
                                     @RequestParam(value = "order[0][column]") int columnOrder,
                                     @RequestParam(value = "length") int length) {
        Person person = personService.getPerson(getUser());
        List<Documents> documents = getNotificationResult(person, start, order, columnOrder, length);
        Long count = person.getNotifications()
                .stream()
                .map(notification -> documentsService.getFilterCount(notification))
                .mapToLong(Long::longValue).sum();
        DataTableObject dataTableObject = new DataTableObject();
        dataTableObject.setDraw(draw);
        dataTableObject.setRecordsTotal(count);
        dataTableObject.setRecordsFiltered(count);
        dataTableObject.setData(documents);
        return dataTableObject;
    }

    private String getUser() {
        String userName = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
        } else {
            userName = principal.toString();
        }
        return userName;
    }

    @RequestMapping(value = "/getAllDocuments", method = RequestMethod.POST)
    @ResponseBody
    public DataTableObject getAllDocuments(HttpServletRequest request,
                                           @RequestParam(value = "start") int start,
                                           @RequestParam(value = "draw") int draw,
                                           @RequestParam(value = "order[0][dir]") String order,
                                           @RequestParam(value = "order[0][column]") int columnOrder,
                                           @RequestParam(value = "length") int length) {
        Map<Integer, String> searchedColumns = findSearchParam(request);
        long documentSize = documentsService.getAllDocumentsCount();
        long filterCount = documentsService.getFilterCount(searchedColumns);
        List<Documents> documents = documentsService.getPagingDocuments(start, length, columnOrder, order, searchedColumns);
        DataTableObject dataTableObject = new DataTableObject();
        dataTableObject.setDraw(draw);
        dataTableObject.setData(documents);
        dataTableObject.setRecordsTotal(documentSize);
        dataTableObject.setRecordsFiltered(filterCount);
        return dataTableObject;
    }


    private Map<Integer, String> findSearchParam(HttpServletRequest request) {
        Map<Integer, String> searchedColumns = new HashMap<>();
        for (int i = 0; i < Documents.columns.length; i++) {
            String param = request.getParameter("columns[" + i + "][search][value]");
            if (StringUtils.isNotBlank(param)) {
                searchedColumns.put(i, param);
            }
        }
        return searchedColumns;
    }

    private List<Documents> getNotificationResult(Person person, int columnOrder, String order, int start, int length) {
        return person.getNotifications().stream()
                .map(notification -> documentsService.getSearchedDocuments(notification, start, length, columnOrder, order))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}