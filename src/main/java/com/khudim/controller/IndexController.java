package com.khudim.controller;

/**
 * Created by Beaver.
 */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        List<String> result = new ArrayList<>();
        result.add("1");
        result.add("2");
        result.add("3");
        result.add("4");
        model.addAttribute("recipient", "World");
        model.addAttribute("results", result);
        return "index";
    }
}