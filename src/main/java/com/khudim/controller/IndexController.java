package com.khudim.controller;

/**
 * Created by Beaver.
 */


import com.khudim.dao.docs.DocumentsService;
import com.khudim.document.IParsedDocument;
import org.apache.derby.iapi.services.io.ArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.stream.Collectors;


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

    public IParsedDocument backDocumentFromBytes(byte[] bytes) {
        IParsedDocument document = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new ArrayInputStream(bytes))) {
            document = (IParsedDocument) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return document;
    }

}