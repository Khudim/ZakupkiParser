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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private DocumentsService documentsService;

    @Autowired
    private PersonService personService;

    @Autowired
    private NotificationService notificationService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model) {
        String userName = (String) request.getSession().getAttribute("personCode");
        Person person = personService.getPerson(userName);
        model.addAttribute("user", person);
        return "index";
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
}