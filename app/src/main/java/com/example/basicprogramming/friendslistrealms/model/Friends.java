package com.example.basicprogramming.friendslistrealms.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Friends extends RealmObject {

    @PrimaryKey
    private String id;
    private String name;
    private String email;
    private String profile;
    private Double salary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }
}
