package com.taskmaster.revature.users;

import com.taskmaster.revature.common.exceptions.Request;

import java.util.UUID;

public class NewUserRequest implements Request<User> {

  //  private String user_id;
    private String givenName;
    private String surname;
    private String email;
    private String username;
    private String password;
    private boolean is_active;


//    public String getUser_id() {
//        return this.user_id;
//    }
//
//    public void setUser_id(String user_id) {
//        this.user_id = user_id;
//    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGivenName() {
        return this.givenName;
    }

    public void setGiveName(String giveName) {
        this.givenName = giveName;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isIs_active() {
        return this.is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    @Override
    public String toString() {
        return "NewUserRequest{" +
               // "user_id = " + user_id + "' " +
                "username = " + username + "' " +
                "given_name = " + is_active + "' " +
                "surname = " + surname + "' " +
                "password = " + password + "' " +
                "is_active" + is_active + "' " +
                "}";
    }

    @Override
    public User extractEntity() {
        User extractedEntity = new User();
        extractedEntity.setUser_Id(UUID.randomUUID().toString());
        extractedEntity.setGivenName(this.givenName);
        extractedEntity.setSurname(this.surname);
        extractedEntity.setEmail(this.email);
        extractedEntity.setUsername(this.username);
        extractedEntity.setPassword(this.password);
        return extractedEntity;
    }

}