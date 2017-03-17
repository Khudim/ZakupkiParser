package com.khudim.dao.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Entity
@Table(name = "person")
public class Person {

    private String code;

    private String password;

    private String email;

    private PersonRole role;

    public Person() {
    }
    @Id
    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "password", nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "role", nullable = false)
    public String getRole() {
        return role == null ? null : role.role();
    }

    @Transient
    public PersonRole getPersonRole(){ return role;}

    public void setRole(String role){
       this.role= PersonRole.findByRole(role);
    }

    public void setRole(PersonRole role) {
        this.role = role;
    }

    public void encodePassword(){
       password = new BCryptPasswordEncoder().encode(password);
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