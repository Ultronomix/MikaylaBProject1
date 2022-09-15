package com.taskmaster.revature.auth;

import com.taskmaster.revature.common.exceptions.AuthenticationException;
import com.taskmaster.revature.common.exceptions.InvalidRequestException;
import com.taskmaster.revature.users.UserDAO;
import com.taskmaster.revature.users.UserResponse;
public class AuthService {
    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public UserResponse authenticate(Credentials credentials) {

        if (credentials == null) {
            throw new InvalidRequestException("The provided credentials object was found to be null.");
        }

        if (credentials.getUsername().length() < 4) {
            throw new InvalidRequestException("The provided username was not the correct length (must be at least 4 characters).");
        }

        if (credentials.getPassword().length() < 8) {
            throw new InvalidRequestException("The provided password was not the correct length (must be at least 8 characters).");
        }

        return userDAO.findUserByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
                .map(UserResponse::new)
                .orElseThrow(AuthenticationException::new);

    }

}
