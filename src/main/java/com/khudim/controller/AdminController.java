package com.khudim.controller;

import com.khudim.dao.DataTableObject;
import com.khudim.dao.person.Person;
import com.khudim.dao.person.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Beaver.
 */
@Controller
public class AdminController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/admin/editUser", method = RequestMethod.POST)
    @ResponseBody
    public DataTableObject editUser(@RequestParam Map<String, String> allRequestParams) {
        List<Person> persons = parsePersons(allRequestParams);
        persons.forEach(p -> personService.updatePerson(p));
        DataTableObject dataTableObject = new DataTableObject();
        dataTableObject.setData(persons);
        return dataTableObject;
    }

    private List<Person> parsePersons(Map<String, String> params) {
        Map<String, Person> persons = new HashMap<>();
        return params.entrySet().stream().filter(k -> !"action".equals(k.getKey())).map(k -> {
            String personId = k.getKey().replaceAll("[^0-9]", "");
            Person person = new Person();
            if (persons.containsKey(personId)) {
                person = persons.get(personId);
            } else {
                persons.put(personId, person);
            }
            addValue(k.getKey(), k.getValue(), person);
            return person;
        }).collect(Collectors.toList());
    }

    private void addValue(String param, String value, Person person) {
        String column = param.split("(.+?)]\\[")[1].split("]")[0];
        switch (column) {
            case "code":
                person.setCode(value);
                break;
            case "password":
                person.setPassword(value);
                break;
            case "email":
                person.setEmail(value);
                break;
            case "role":
                person.setRole(value);
        }
    }


    @RequestMapping(value = "/admin/getAllUsers", method = RequestMethod.POST)
    @ResponseBody
    public DataTableObject getAllUsers(@RequestParam(value = "draw") int draw) {
        DataTableObject dataTableObject = new DataTableObject();
        dataTableObject.setDraw(draw);
        dataTableObject.setData(personService.getPersons());
        dataTableObject.setRecordsFiltered(1);
        dataTableObject.setRecordsTotal(2);
        return dataTableObject;
    }
}
