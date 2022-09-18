package com.taskmaster.revature.users;

import com.taskmaster.revature.common.datasource.ConnectionFactory;
import com.taskmaster.revature.common.exceptions.DataSourceException;
import com.taskmaster.revature.common.exceptions.Request;
import com.taskmaster.revature.common.exceptions.ResourceNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class NewUserRequest implements Request<User> {
    private String given_name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private boolean is_active;

    private Role role;

    private Role getRoleFromName(String role_name) {
        String sql = "SELECT role_id, role FROM ers_user_roles WHERE role = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, role_name);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next()) {
                // TODO: fix me
                throw new ResourceNotFoundException();
            }
            return new Role(rs.getString("role_id"), rs.getString("role"));
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

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
        return this.given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean getIsActive() {
        return this.is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(String role_name) {
        this.role = this.getRoleFromName(role_name);
    }


    @Override
    public String toString() {
        return "NewUserRequest{" +
                "username = " + username + "' " +
                "given_name = " + given_name + "' " +
                "surname = " + surname + "' " +
                "password = " + password + "' " +
                "is_active = " + is_active + "' " +
                "role = " + role + "' " +
                "}";
    }

    @Override
    public User extractEntity() {
        User extractedEntity = new User();
        extractedEntity.setUser_Id(UUID.randomUUID().toString());
        extractedEntity.setGivenName(this.given_name);
        extractedEntity.setSurname(this.surname);
        extractedEntity.setEmail(this.email);
        extractedEntity.setUsername(this.username);
        extractedEntity.setPassword(this.password);
        extractedEntity.setRole(this.role);
        return extractedEntity;
    }

}