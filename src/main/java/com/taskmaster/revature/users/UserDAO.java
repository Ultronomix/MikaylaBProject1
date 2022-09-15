package com.taskmaster.revature.users;

import com.taskmaster.revature.common.datasource.ConnectionFactory;
import com.taskmaster.revature.common.exceptions.DataSourceException;
import com.taskmaster.revature.common.exceptions.ResourceNotFoundException;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// DAO = Data Access Object
public class UserDAO {

    private final String baseSelect = "SELECT user_id, given_name, surname, email, username, \"password\", eu.role_id, eur.name " +
            "FROM ers_users eu " +
            "JOIN ers_user_roles eur " +
            "ON eu.role_id = eur.role_id ";

    public List<User> getAllUsers() {

        List<User> allUsers = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(baseSelect);

            allUsers = mapResultSet(rs);

        } catch (SQLException e) {
            System.err.println("Something went wrong when communicating with the database");
            e.printStackTrace();
        }

        return allUsers;

    }

    public Optional<User> findUserById(UUID id) {

        String sql = baseSelect + "WHERE id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            // JDBC Statement objects are vulnerable to SQL injection
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setObject(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {
            e.printStackTrace();
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

        String sql = baseSelect + "WHERE au.username = ? AND au.password = ?";

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

    public String save(User user) {

        String sql = "INSERT INTO app_users (given_name, surname, email, username, password, role_id) " +
                "VALUES (?, ?, ?, ?, ?, '5a2e0415-ee08-440f-ab8a-778b37ff6874')";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement printStatement = conn.prepareStatement(sql, new String[] {"id"});
            printStatement.setString(1, user.getGivenName());
            printStatement.setString(2, user.getSurname());
            printStatement.setString(3, user.getEmail());
            printStatement.setString(4, user.getUsername());
            printStatement.setString(5, user.getPassword());

            printStatement.executeUpdate();

            ResultSet rs = printStatement.getGeneratedKeys();
            rs.next();
            user.setUser_Id(rs.getString("id"));

        } catch (SQLException e) {
            log("ERROR", e.getMessage());
        }

        log("INFO", "Successfully persisted new used with id: " + user.getUser_Id());

        return user.getUser_Id();

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
            user.setRole(String.valueOf(new Role(rs.getString("role_id"), rs.getString("name"))));
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