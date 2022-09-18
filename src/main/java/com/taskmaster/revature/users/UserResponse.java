package com.taskmaster.revature.users;

import java.io.Serializable;
import java.util.Objects;

public class UserResponse implements Serializable {
    private String user_id;
    private String given_name;
    private String surname;
    private String email;
    private String username;
    private Role role;
    private boolean is_active;

    public UserResponse (User subject) {
        this.user_id = subject.getUser_Id();
        this.given_name = subject.getGivenName();
        this.surname = subject.getSurname();
        this.email = subject.getEmail();
        this.username = subject.getUsername();
        this.is_active = subject.getIs_active();
        this.role = subject.getRole();
    }

    public String getUser_Id()
    { return user_id; }

    public void setUser_Id(String user_id)
    { this.user_id = user_id; }

    public void setGiven_name(String given_name)
    { this.given_name = given_name; }

    public String getGiven_name()
    { return given_name; }

    public String getSurname()
    { return surname; }

    public void setSurname(String surname)
    { this.surname = surname; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setIs_active (boolean is_active) {
        this.is_active =is_active;
    }

    public boolean getIs_active() {
        return is_active;
    }

    public String getRole() {
        return role.getName();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id='" + user_id + '\'' +
                ", givenName='" + given_name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                "is_active = '" + is_active + "' " +
                ", role='" + role + '\'' +
                '}';
    }

}



