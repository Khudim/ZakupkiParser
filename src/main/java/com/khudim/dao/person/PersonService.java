package com.khudim.dao.person;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService implements UserDetailsService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person getPerson(String code) {
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Argument 'code' must not be blank.");
        }
        Object object = personRepository.getPerson(code);
        return object==null ? null : (Person) object;
    }

    public List<Person> getPersons() {
        return personRepository.getAllPersons();
    }

    public void deletePerson(Person person) {
        personRepository.deletePerson(person);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Person person = getPerson(username);
        if (person != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + person.getRole()));
            return new User(person.getCode(), person.getPassword(),true,true,true,true, authorities);
        }
        throw new UsernameNotFoundException("User " + username + " not found.");
    }

    public void updatePerson(Person person) {
        Person oldPerson = getPerson(person.getCode());
        if(oldPerson==null || !oldPerson.getPassword().equals(person.getPassword())){
            person.encodePassword();
        }
        personRepository.updatePerson(person);
    }
}
