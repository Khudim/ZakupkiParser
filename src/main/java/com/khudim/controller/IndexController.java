package com.khudim.controller;

/**
 * Created by Beaver.
 */


import com.khudim.dao.docs.DocumentsService;
import com.khudim.document.IParsedDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.List;
import java.util.stream.Collectors;

import static com.khudim.helpers.ParseHelper.backDocumentFromBytes;


@Controller
public class IndexController {

    @Autowired
    private DocumentsService service;

    @RequestMapping(value = "/index/{page}", method = RequestMethod.GET)
    public String index(Model model, @PathVariable("page") String page) {
        List<IParsedDocument> parsedDocuments = service.getAllDocumentsOnPage(page, 50)
                .stream()
                .map(doc -> backDocumentFromBytes(doc.getContent()))
                .collect(Collectors.toList());
        model.addAttribute("documents", parsedDocuments);
        return "index";
    }
}