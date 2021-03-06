package com.khudim.controller;

/**
 * Created by Beaver.
 */

import com.khudim.dao.DataTableObject;
import com.khudim.dao.docs.Documents;
import com.khudim.dao.docs.DocumentsService;
import com.khudim.dao.notifications.Notification;
import com.khudim.dao.person.Person;
import com.khudim.dao.person.PersonService;
import com.khudim.mail.EmailUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.khudim.dao.docs.DocumentsFields.columns;

@Controller
@Transactional(rollbackFor = Exception.class)
public class IndexController {

    private final DocumentsService documentsService;

    private final PersonService personService;

    @Autowired
    private EmailUtil emailUtil;

    @Autowired
    public IndexController(DocumentsService documentsService, PersonService personService) {
        this.documentsService = documentsService;
        this.personService = personService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        Person person = personService.getCurrentUser();
        List<String> regions = documentsService.getAllRegions();
        model.addAttribute("regions", regions);
        model.addAttribute("user", person);
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/sendMail", method = RequestMethod.GET)
    public String sendMail() {
        Person person = personService.getCurrentUser();
        Long count = person.getNotifications()
                .stream()
                .map(documentsService::getFilterCount)
                .mapToLong(Long::longValue)
                .sum();
        String message = "Нашлось " + count + " документов по вашим параметрам поиска. \n" +
                "Результаты поиска можно посмотреть на сайте в разделе с результатами последнего поиска.";
        emailUtil.sendEmail(person.getEmail(), "Результаты поиска", message, "NotificationZakupkiResult@gmail.com", "NotificationZakupkiResult", "smeni321");
        return "Ok!";
    }

    @RequestMapping(value = "/getAllDocuments", method = RequestMethod.POST)
    @ResponseBody
    public DataTableObject getAllDocuments(HttpServletRequest request,
                                           @RequestParam(value = "start") int start,
                                           @RequestParam(value = "draw") int draw,
                                           @RequestParam(value = "order[0][dir]") String order,
                                           @RequestParam(value = "order[0][column]") int columnNumber,
                                           @RequestParam(value = "length") int length) {
        Map<Integer, String> searchedColumns = findSearchParam(request);
        long documentSize = documentsService.getAllDocumentsCount();
        long filterCount = documentsService.getFilterCount(searchedColumns);
        List<Documents> documents = documentsService.getPagingDocuments(start, length, columnNumber, order, searchedColumns);
        DataTableObject dataTableObject = new DataTableObject();
        dataTableObject.setDraw(draw);
        dataTableObject.setData(documents);
        dataTableObject.setRecordsTotal(documentSize);
        dataTableObject.setRecordsFiltered(filterCount);
        return dataTableObject;
    }

    @RequestMapping(value = "/getAllNotificationDocuments", method = RequestMethod.POST)
    @ResponseBody
    public DataTableObject getAllNotificationDocuments(@RequestParam(value = "start") int start,
                                                       @RequestParam(value = "draw") int draw,
                                                       @RequestParam(value = "order[0][dir]") String order,
                                                       @RequestParam(value = "order[0][column]") int columnNumber,
                                                       @RequestParam(value = "length") int length) {
        Person person = personService.getCurrentUser();
        List<Documents> documents = getNotificationResult(person, columnNumber, order, start, length);
        Long count = person.getNotifications()
                .stream()
                .map(documentsService::getFilterCount)
                .mapToLong(Long::longValue)
                .sum();
        DataTableObject dataTableObject = new DataTableObject();
        dataTableObject.setDraw(draw);
        dataTableObject.setRecordsTotal(count);
        dataTableObject.setRecordsFiltered(count);
        dataTableObject.setData(documents);
        return dataTableObject;
    }

    @RequestMapping(value = "/updateNotification", method = RequestMethod.POST)
    @ResponseBody
    public Notification updateNotification(@RequestParam(value = "minPrice", required = false) String minPrice,
                                           @RequestParam(value = "maxPrice", required = false) String maxPrice,
                                           @RequestParam(value = "city", required = false) String city,
                                           @RequestParam(value = "date", required = false) String date,
                                           @RequestParam(value = "rate", required = false) String rate) {
        Person person = personService.getCurrentUser();
        Notification notification = person.getNotifications()
                .stream()
                .findFirst()
                .orElse(null);
        if (notification == null) {
            notification = new Notification();
            person.setNotifications(Collections.singletonList(notification));
        }
        if (isCorrect(minPrice)) {
            notification.setMinPrice(Double.parseDouble(minPrice));
        }
        if (isCorrect(maxPrice)) {
            notification.setMaxPrice(Double.parseDouble(maxPrice));
        }
        if (StringUtils.isNotBlank(city)) {
            notification.setRegions(city);
        }
        if (isCorrect(rate)) {
            notification.setRate(Integer.parseInt(rate));
        }
        if (isCorrect(date)) {
            notification.setDate(Long.parseLong(date));
        }
        return notification;
    }

    private Map<Integer, String> findSearchParam(HttpServletRequest request) {
        Map<Integer, String> searchedColumns = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            String param = request.getParameter("columns[" + i + "][search][value]");
            if (StringUtils.isNotBlank(param)) {
                searchedColumns.put(i, param);
            }
        }
        return searchedColumns;
    }

    private List<Documents> getNotificationResult(Person person, int columnNumber, String order, int start, int length) {
        return person.getNotifications().stream()
                .map(notification -> documentsService.getSearchedDocuments(notification, start, length, columnNumber, order))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private boolean isCorrect(String field) {
        return StringUtils.isNotBlank(field) && StringUtils.isNumeric(field);
    }
}