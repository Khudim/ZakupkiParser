package com.khudim.person;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author hudyshkin
 */
@Repository
@Transactional(rollbackFor = Exception.class)
public class PersonRepository {

    @Autowired
    SessionFactory sessionFactory;

    private final static List<Person> people = new CopyOnWriteArrayList<>();

    static {
        Person user = new Person();
        user.setCode("user");
        user.setPassword("");
        user.setRole(PersonRole.USER);
        Person admin = new Person();
        admin.setCode("admin");
        admin.setPassword("");
        admin.setRole(PersonRole.ADMIN);
        people.add(user);
        people.add(admin);

    }
    protected Session getSession(){
        return sessionFactory.getCurrentSession();
    }
    @SuppressWarnings("unchecked")
    public Person getPerson(String code){
        /*if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Argument 'id' must not be NULL.");
        }
        return (Person) getSession().get(Person.class,code);
    */
        return people.stream().filter(p->p.getCode().equals(code)).findFirst().orElse(null);
    }

    public List<Person> getAllPersons(){
       /* TypedQuery<Person> query = entityManager.createNamedQuery("Person.findAll",Person.class);
        return query.getResultList();*/
       return people;
    }

    public void createPerson(Person person) {
        people.add(person);
        /*entityManager.persist(person);
    */
    }

    public void deletePerson(String personCode) {
        people.removeIf(p->p.getCode().equals(personCode));
        /*
        entityManager.remove(getPerson(personCode));
    */}

    public void edit(Person person) {

        //entityManager.merge(person);
    }
}
