package com.taskmaster.revature.users;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.ArrayList;

import com.taskmaster.revature.common.ResourceCreationResponse;
import com.taskmaster.revature.common.exceptions.InvalidRequestException;
import com.taskmaster.revature.common.exceptions.ResourceNotFoundException;
import com.taskmaster.revature.common.exceptions.ResourcePersistenceException;


public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<UserResponse> getAllUsers() {
        // Imperative (more explicit about what is being done)

        List<UserResponse> result = new ArrayList<>();
        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
           result.add(new UserResponse(user));
        }
        // Functional approach (more declarative)
        return userDAO.getAllUsers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

    }



    public UserResponse getUserById (String id) {

        if (id == null || id.length() <= 0) {
            throw new InvalidRequestException("A non-empty id must be provided!");
        }
            // UUID uuid = UUID.fromString(id);
            return userDAO.findUserById(id).map(UserResponse::new)
                    .orElseThrow(ResourceNotFoundException::new);
    }
    public UserResponse getUserbyUsername (String username) {
        if (username == null || username.trim().length() < 4) {
            throw new InvalidRequestException("A username must be at least 4 characters");
        }
        return userDAO.findUserByUsername(username).map(UserResponse::new)
                .orElseThrow(ResourceNotFoundException::new);
    }

    //? Search for User by role
    public UserResponse getUserByRole (String role) {
        if (role == null || role.trim().length() <= 0) {
            throw new InvalidRequestException("A role must be provided.");
        }
        return userDAO.findUserByRole(role).map(UserResponse::new)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public ResourceCreationResponse register(NewUserRequest newUser) {
        if (newUser == null) {
            throw new RuntimeException("Provided request payload was null.");
        }

        if (newUser.getGivenName() == null || newUser.getGivenName().length() == 0 ||
                newUser.getSurname() == null || newUser.getSurname().length() == 0)
        {
            throw new RuntimeException("A non-empty given name and surname must be provided");
        }

        if (newUser.getEmail() == null || newUser.getEmail().length() <= 0) {
            throw new RuntimeException("A non-empty email must be provided.");
        }

        if (newUser.getUsername() == null || newUser.getUsername().length() < 4) {
            throw new RuntimeException("A username with at least 4 characters must be provided.");
        }

        if (newUser.getPassword() == null || newUser.getPassword().length() < 8) {
            throw new RuntimeException("A password with at least 8 characters must be provided.");
        }
        if (userDAO.isEmailTaken(newUser.getEmail())) {
            throw new RuntimeException("Resource not persisted! The provided email is already taken.");
        }
        if (userDAO.isUsernameTaken(newUser.getUsername())) {
            throw new RuntimeException("Resource not persisted! The provided username is already taken.");
        }

        User userToPersist = newUser.extractEntity();
        String newUserId = userDAO.save(userToPersist);
        return new ResourceCreationResponse(newUserId);
    }
    public ResourceCreationResponse updateUser(String user_id, NewUserRequest update_user) {
        // check which info to update
        if (update_user == null) {
            throw new InvalidRequestException("Provided request payload was null.");
        }
        if (update_user.getUsername() != null && !userDAO.isUsernameTaken(update_user.getUsername())) {
            userDAO.updateUserAttribute("username", user_id, update_user.getUsername());
        }
        if (update_user.getPassword() != null && update_user.getPassword().trim().length() > 3) {
            userDAO.updateUserAttribute("password", user_id, update_user.getPassword());
        }
        if (update_user.getEmail() != null && update_user.getEmail().trim().length() > 3 && !userDAO.isEmailTaken(update_user.getEmail())) {
            userDAO.updateUserAttribute("email", user_id, update_user.getEmail());
        }
        if (update_user.getGivenName() != null) {
            userDAO.updateUserAttribute("given_name", user_id, update_user.getGivenName());
        }
        if (update_user.getSurname() != null) {
            userDAO.updateUserAttribute("surname", user_id, update_user.getSurname());
        }
        if (update_user.getIsActive() == false || update_user.getIsActive() == true) {
            userDAO.updateUserAttribute("is_active", user_id, update_user.getIsActive());
        }
        return new ResourceCreationResponse(update_user.toString());
    }
}