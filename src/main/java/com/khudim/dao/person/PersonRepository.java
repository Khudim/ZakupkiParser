package com.khudim.dao.person;


import com.khudim.dao.AbstractDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author hudyshkin
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class PersonRepository extends AbstractDao<String,Person> {

    @SuppressWarnings("unchecked")
    public Object getPerson(String code) {
        return getSession().get(Person.class, code);
    }

    @SuppressWarnings("unchecked")
    public List<Person> getAllPersons() {
        return createEntityCriteria().list();
    }

    public void createPerson(Person person) {
        getSession().persist(person);
    }

    public void deletePerson(Person person) {
        getSession().delete(person);
    }

    public void edit(Person person) {
        getSession().update(person);
    }

    public void updatePerson(Person person) {
        getSession().merge(person);
    }

}
