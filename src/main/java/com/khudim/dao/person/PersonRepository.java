package com.khudim.dao.person;


import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * @author hudyshkin
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class PersonRepository {

    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    public Person getPerson(String code){
        return (Person) getSession().get(Person.class,code);
    }

    @SuppressWarnings("unchecked")
    public List<Person> getAllPersons(){
       return createEntityCriteria().list();
    }

    public void createPerson(Person person) {
        getSession().persist(person);
    }

    public void deletePerson(String personCode) {
        getSession().delete(personCode);
    }

    public void edit(Person person) {
        getSession().update(person);
    }

    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }

    protected Criteria createEntityCriteria(){
        return getSession().createCriteria(Person.class);
    }

    public void updatePerson(Person person) {
        getSession().merge(person);
    }
}
