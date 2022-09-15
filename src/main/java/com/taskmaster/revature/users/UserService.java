package com.taskmaster.revature.users;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public List<UserResponse> getAllUsers() {
//         Imperative (more explicit about what is being done)
//        List<UserResponse> result = new ArrayList<>();
//       List<User> users = userDAO.getAllUsers();
//        for (User user : users) {
//           result.add(new UserResponse(user));
//       }
//        return result;

        // Functional approach (more declarative)
        return userDAO.getAllUsers()
                .stream()
                .map(UserResponse::new)
                .collect(Collectors.toList());

    }

    public UserResponse getUserById(String id) {

        if (id == null || id.length() <= 0) {
            throw new RuntimeException("A non-empty id must be provided!");
        }

        try {

            UUID uuid = UUID.fromString(id);
            return userDAO.findUserById(uuid)
                    .map(UserResponse::new)
                    .orElseThrow(RuntimeException::new);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("An invalid UUID string was provided.");
        }



    }

    public ResourceCreationResponse register(NewUserRequest newUser) {

//        if (newUser == null) {
//            throw new RuntimeException("Provided request payload was null.");
//        }
//
//        if (newUser.getGivenName() == null || newUser.getGivenName().length() <= 0 ||
//                newUser.getSurname() == null || newUser.getSurname().length() <= 0)
//        {
//            throw new RuntimeException("A non-empty given name and surname must be provided");
//        }
//
//        if (newUser.getEmail() == null || newUser.getEmail().length() <= 0) {
//            throw new RuntimeException("A non-empty email must be provided.");
//        }
//
//        if (newUser.getUsername() == null || newUser.getUsername().length() < 4) {
//            throw new RuntimeException("A username with at least 4 characters must be provided.");
//        }
//
//        if (newUser.getPassword() == null || newUser.getPassword().length() < 8) {
//            throw new RuntimeException("A password with at least 8 characters must be provided.");
//        }
//            System.out.println("email");
//        if (userDAO.isEmailTaken(newUser.getEmail())) {
//            throw new RuntimeException("Resource not persisted! The provided email is already taken.");
//        }
//            System.out.println("username");
//        if (userDAO.isUsernameTaken(newUser.getUsername())) {
//            throw new RuntimeException("Resource not persisted! The provided username is already taken.");
//        }
//
        User userToPersist = newUser.extractEntity();
        String newUserId = userDAO.save(userToPersist);
        return new ResourceCreationResponse(newUserId);

    }
}