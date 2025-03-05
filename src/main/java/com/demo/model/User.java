package com.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.NotFound;
import jakarta.persistence.Id;


import java.util.Set;

@Entity
@SequenceGenerator(name = "oms_user_sequence", sequenceName = "oms_user_seq")
@Table(name = "omsUser")
public class User {

    @Id
    @GeneratedValue(generator = "oms_user_sequence", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name="User_name")
    private String userName;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;



    public User(String username, String lastName, String firstName) {
        this.userName = username;
        this.lastName = lastName;
        this.firstName = firstName;
    }
    public User(User inputData) {
        this.userName = inputData.userName;
        this.lastName = inputData.lastName;
        this.firstName = inputData.firstName;
    }

    public User() { }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
