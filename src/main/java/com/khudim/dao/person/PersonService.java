package com.khudim.dao.person;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PersonService implements UserDetailsService {

    private PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person getRandomPerson(List<Person> personsToExclude) {
        if (personsToExclude == null) {
            throw new IllegalArgumentException("Argument 'personsToExclude' must be not NULL.");
        }

        List<Person> possiblePersons = new ArrayList<>(getPersons());
        possiblePersons.removeAll(personsToExclude);

        Person person = null;

        if (possiblePersons.size() > 0) {
            int reviewerIndex = ThreadLocalRandom.current().nextInt(0, possiblePersons.size());
            person = possiblePersons.get(reviewerIndex);
        }

        return person;
    }

    public Person getPerson(String code) {
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Argument 'code' must not be blank.");
        }
        return personRepository.getPerson(code);
    }

    public List<Person> getPersons() {
        return personRepository.getAllPersons();
    }

    public void deletePerson(Person person) {
        personRepository.deletePerson(person);
    }

    public void createPerson(Person person) {
        if (personRepository.getPerson(person.getCode()) != null) {
            throw new IllegalArgumentException("Person with code " + person.getCode() + " already exist");
        }
        personRepository.createPerson(person);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Person person = personRepository.getPerson(username);
        if (person != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + person.getRole()));
            return new User(person.getCode(), person.getPassword(), authorities);
        }
        throw new UsernameNotFoundException("User " + username + " not found.");
    }

    public void editPerson(String oldPersonCode, Person editedPerson) {
        if (oldPersonCode.equals(editedPerson.getCode())) {
            personRepository.edit(editedPerson);
        } else if (personRepository.getPerson(editedPerson.getCode()) != null) {
            throw new IllegalArgumentException("Person with code " + editedPerson.getCode() + " already exist");
        } else {
            /*personRepository.deletePerson(oldPersonCode);*/
            personRepository.createPerson(editedPerson);
        }

    }

    public void updatePerson(Person person) {
        personRepository.updatePerson(person);
    }
}
