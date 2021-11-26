package com.example.firebaseapp.User;

public class User {

    private String email,password,name,avatar,id;

    public User()
    {

    }
    public User(String name,String email, String password,String avatar,String id) {
        this.email = email;
        this.password = password;
        this.name=name;
        this.avatar=avatar;
        this.id=id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
