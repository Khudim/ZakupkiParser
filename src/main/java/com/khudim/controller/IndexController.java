package com.khudim.controller;

/**
 * Created by Beaver.
 */


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.khudim.dao.DataTableObject;
import com.khudim.dao.docs.Documents;
import com.khudim.dao.docs.DocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private DocumentsService service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/getAllDocuments", method = RequestMethod.POST)
    public void getAllDocuments(HttpServletResponse response,
                                @RequestParam(value = "start") int start,
                                @RequestParam(value = "draw") int draw,
                                @RequestParam(value = "length") int length) throws IOException {
        response.setContentType("application/json");
        long documentSize = service.getAllDocumentsCount();
        List<Documents> documents = service.getPagingDocuments(start, length);
        DataTableObject dataTableObject = new DataTableObject();
        dataTableObject.setDraw(draw);
        dataTableObject.setData(documents);
        dataTableObject.setRecordsTotal(documentSize);
        dataTableObject.setRecordsFiltered(documentSize);
        PrintWriter out = response.getWriter();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(dataTableObject);
        out.print(json);
//        return service.getPagingDocuments(start, length);
    }

    @RequestMapping(value = "/getAllDocuments", method = RequestMethod.GET)
    public void getAllDocuments(HttpServletResponse response) throws IOException {
        getAllDocuments(response, 0, 10, 10);
    }
}