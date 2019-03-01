package com.khoinguyen.caphekhoinguyen.model;

public class User {
    private String id;
    private String email;
    private String userName;

    public User(String id, String email, String userName) {
        this.id = id;
        this.email = email;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
