package com.taskmaster.revature.users;

import java.util.Objects;

// POJO = Plain Ol' Java Objects
public class User {

    private String user_id;
    private String given_Name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private boolean is_active;
    private Role role;

    public String getUser_Id() {
        return user_id;
    }

    public void setUser_Id(String User_id) {
        this.user_id = User_id;
    }

    public String getGivenName() {
        return given_Name;
    }

    public void setGivenName(String givenName) {
        this.given_Name = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setIs_active (boolean is_active) {
        this.is_active =is_active;
    }

    public boolean getIs_active() {
        return is_active;
    }
    public void setRole (Role role) {
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public boolean checkRole(String role_check) {
        return role.getName().equals(role_check);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(user_id, user.user_id)
                && Objects.equals(given_Name, user.given_Name) && Objects.equals(surname, user.surname)
                && Objects.equals(email, user.email) && Objects.equals(username, user.username)
                && Objects.equals(password, user.password) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user_id, given_Name, surname, email, username, password,is_active, role);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + user_id + '\'' +
                ", givenName='" + given_Name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                '}';
    }


    }
