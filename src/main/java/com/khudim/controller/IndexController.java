package com.khudim.controller;

/**
 * Created by Beaver.
 */

import com.khudim.dao.DataTableObject;
import com.khudim.dao.docs.Documents;
import com.khudim.dao.docs.DocumentsService;
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

    @RequestMapping(value = "/getAllNotificationDocuments", method = RequestMethod.POST)
    @ResponseBody
    public DataTableObject getResult(@RequestParam(value = "start") int start,
                                     @RequestParam(value = "draw") int draw,
                                     @RequestParam(value = "length") int length) {
        Person person = personService.getPerson(getUser());
        DataTableObject dataTableObject = new DataTableObject();
        List<Documents> documents = getNotificationResult(person);
        dataTableObject.setDraw(draw);
        dataTableObject.setRecordsTotal(documents.size());
        dataTableObject.setRecordsFiltered(documents.size());
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

    public List<Documents> getNotificationResult(Person person) {
        return person.getNotifications().stream()
                .map(notification -> documentsService.getSearchedDocuments(notification))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}