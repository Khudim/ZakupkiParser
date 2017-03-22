package com.khudim.controller;

import com.khudim.dao.DataTableObject;
import com.khudim.dao.person.Person;
import com.khudim.dao.person.PersonService;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Beaver.
 */
@Controller
@Transactional(rollbackFor = Exception.class)
public class AdminController {

    @Autowired
    private PersonService personService;

    @RequestMapping(value = "/admin/editUser", method = RequestMethod.POST)
    @ResponseBody
    public DataTableObject editUser(@RequestParam Map<String, String> allRequestParams, @RequestParam(required = false) String action) {
        DataTableObject dataTableObject = new DataTableObject();
        List<Person> persons = parsePersons(allRequestParams);
        if ("edit".equals(action) || "create".equals(action)) {
            persons.forEach(p -> personService.updatePerson(p));
            dataTableObject.setData(persons);
        } else if ("remove".equals(action)) {
            persons.forEach(p -> personService.deletePerson(p));
        }
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
        String[] columns = param.split("(.+?)]\\[");
        if (columns.length < 2) {
            return;
        }
        String column = columns[1].split("]")[0];
        updateUser(value, person, column);
    }

    private void updateUser(String value, Person person, String column) {
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
    @JsonIgnore
    @RequestMapping(value = "/admin/getAllUsers", method = RequestMethod.POST)
    @ResponseBody
    public DataTableObject getAllUsers(@RequestParam(value = "draw") int draw) {
        List<Person> people = personService.getPersons();
        DataTableObject dataTableObject = new DataTableObject();
        dataTableObject.setDraw(draw);
        dataTableObject.setData(people);
        dataTableObject.setRecordsFiltered(people.size());
        dataTableObject.setRecordsTotal(people.size());
        return dataTableObject;
    }
}
