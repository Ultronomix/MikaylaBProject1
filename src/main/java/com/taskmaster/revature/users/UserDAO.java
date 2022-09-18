package com.taskmaster.revature.users;

import com.taskmaster.revature.common.datasource.ConnectionFactory;
import com.taskmaster.revature.common.exceptions.DataSourceException;
import com.taskmaster.revature.common.exceptions.ResourceNotFoundException;
//import com.taskmaster.revature.common.exceptions.ResourceNotFoundException;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
//import java.util.UUID;

// DAO = Data Access Object
public class UserDAO {

    private final String baseSelect = "SELECT eu.user_id, eu.username, eu.email, eu.password, " +
            "eu.given_name, eu.surname, " +
            "eu.is_active, eur.role_id, eur.role " +
            "FROM ers_users eu " +
            "JOIN ers_user_roles eur ON eu.role_id = eur.role_id ";

    public List<User> getAllUsers() {

        String sql = baseSelect;
        List<User> allUsers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            allUsers = mapResultSet(rs);

        } catch (SQLException e) {
            System.err.println("Something went wrong when communicating with the database");
            e.printStackTrace();
        }

        return allUsers;

    }

    public Optional<User> findUserById( String id) {

        String sql = baseSelect + "WHERE eu.user_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }

    }

    public String save(User user){
        String sql = "INSERT INTO ers_users (user_id, username, email, password, given_name, surname, is_active, role_id)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUser_Id().trim());
            pstmt.setString(2, user.getUsername().trim());
            pstmt.setString(3, user.getEmail().trim());
            pstmt.setString(4, user.getPassword().trim());
            pstmt.setString(5, user.getGivenName().trim());
            pstmt.setString(6, user.getSurname().trim());
            pstmt.setBoolean(7, user.getIs_active());
            pstmt.setString(8, user.getRole().getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }
        return user.getUsername() + " added.";
    }


    public Optional<User> findUserByRole(String role) {

        String sql = baseSelect + "WHERE eur.role = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);
            ResultSet rs = pstmt.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    public Optional<User> findUserByUsername(String username) {

        String sql = baseSelect + "WHERE username = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    public boolean isUsernameTaken(String username) {
        return findUserByUsername(username).isPresent();
    }

    public Optional<User> findUserByEmail(String email) {

        String sql = baseSelect + "WHERE email = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    public boolean isEmailTaken(String email) {
        return findUserByEmail(email).isPresent();
    }

    public Optional<User> findUserByUsernameAndPassword(String username, String password) {

        String sql = baseSelect + "WHERE eu.username = ? AND eu.password = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    public String updateUserAttribute(String attrib, String user_id, String new_value) {
        String sql = "UPDATE ers_users SET " + attrib + " = ? WHERE user_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, new_value);
            pstmt.setString(2, user_id);
            pstmt.executeUpdate();
            return "Email Changed"; //TODO change
        } catch (SQLException e) {
            //TODO log exception
            throw new DataSourceException(e);
        }
    }
    public String updateUserAttribute(String attrib, String user_id, Boolean new_value) {
        String sql = "UPDATE ers_users SET " + attrib + " = ? WHERE user_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setBoolean(1, new_value);
            pstmt.setString(2, user_id);
            pstmt.executeUpdate();
            return "Email Changed"; //TODO change
        } catch (SQLException e) {
            //TODO log exception
            throw new DataSourceException(e);
        }
    }

    private List<User> mapResultSet(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            User user = new User();
            user.setUser_Id(rs.getString("user_id"));
            user.setGivenName(rs.getString("given_name"));
            user.setSurname(rs.getString("surname"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(new Role(rs.getString("role_id"), rs.getString("role")));
            users.add(user);
        }
        return users;
    }

    public void log(String level, String message) {
        try {
            File logFile = new File("logs/app.log");
            logFile.createNewFile();
            BufferedWriter logWriter = new BufferedWriter(new FileWriter(logFile));
            logWriter.write(String.format("[%s] at %s logged: [%s] %s\n", Thread.currentThread().getName(), LocalDate.now(), level.toUpperCase(), message));
            logWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}