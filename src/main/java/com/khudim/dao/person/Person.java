package com.khudim.dao.person;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.khudim.dao.notifications.Notification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "person")
public class Person implements Serializable{

    private String code;

    private String password;

    private String email;

    private PersonRole role;

    private Set<Notification> notifications = new HashSet<>();

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

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "person")
    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }

    public void encodePassword(){
       password = new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public String toString() {
        return "Person{" +
                "code='" + code + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", notifications=" + notifications +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (code != null ? !code.equals(person.code) : person.code != null) return false;
        if (password != null ? !password.equals(person.password) : person.password != null) return false;
        if (email != null ? !email.equals(person.email) : person.email != null) return false;
        if (role != person.role) return false;
        return notifications != null ? notifications.equals(person.notifications) : person.notifications == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (notifications != null ? notifications.hashCode() : 0);
        return result;
    }
}