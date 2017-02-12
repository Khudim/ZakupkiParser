package com.khudim.person;

import com.khudim.filter.Requirement;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "person")
@NamedQueries({
        @NamedQuery(name = "Person.findById",
                query = "SELECT person FROM Person person WHERE person.code = :code "),
        @NamedQuery(name = "Person.findAll",
                query = "SELECT person FROM Person person")
})
public class Person {

    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "email", nullable = false)
    private String email;
    @Transient
    @Column(name = "role", nullable = false)
    private PersonRole role;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    private List<Requirement> requirements = new ArrayList<>();

    public Person() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role == null ? null : role.role();
    }

    public PersonRole getPersonRole(){ return role;}

    public void setRole(String role){
       this.role= PersonRole.findByRole(role);
    }

    public void setRole(PersonRole role) {
        this.role = role;
    }

    public List<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements) {
        this.requirements = requirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return code != null ? code.equals(person.code) : person.code == null;
    }

    @Override
    public int hashCode() {
        return code != null ? code.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Person{" +
                "code = " + (code != null ? code : "null") +
                "}";
    }
}