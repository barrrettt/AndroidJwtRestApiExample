package com.barrrettt.androidjwtrestapiexample.data.user;

/**
 * Usuario para logIn
 */
public class User {

    private String name;
    private String jwt;

    public User(String name, String jwt) {
        this.name = name;
        this.jwt = jwt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}