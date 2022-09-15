package com.taskmaster.revature.users;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {

    private final UserService userService;


    public UserServlet(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        String idToSearchFor = req.getParameter("id");

        try {

            if (idToSearchFor == null) {
                List<UserResponse> allUsers = userService.getAllUsers();
                resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));
            } else {
                UserResponse foundUser = userService.getUserById(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }

        }


        catch (RuntimeException e) {

            resp.setStatus(500); // BAD REQUEST;
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
        }

//        catch (InvalidRequestException | JsonMappingException e) {
//
//            // TODO encapsulate error response creation into its own utility method
//            resp.setStatus(400); // BAD REQUEST;
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("statusCode", 400);
//            errorResponse.put("message", e.getMessage());
//            errorResponse.put("timestamp", System.currentTimeMillis()); // TODO replace with LocalDateTime.now()
//            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
//
//        } catch (ResourceNotFoundException e) {
//
//            resp.setStatus(404); // NOT FOUND; the sought resource could not be located
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("statusCode", 404);
//            errorResponse.put("message", e.getMessage());
//            errorResponse.put("timestamp", System.currentTimeMillis()); // TODO replace with LocalDateTime.now()
//            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
//
//        } catch (DataSourceException e) {
//
//            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("statusCode", 500);
//            errorResponse.put("message", e.getMessage());
//            errorResponse.put("timestamp", System.currentTimeMillis()); // TODO replace with LocalDateTime.now()
//            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
//
//        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

      //  resp.getWriter().write(jsonMapper.writeValueAsString("doPost"));

        try {
            NewUserRequest requestBody = jsonMapper.readValue(req.getInputStream(), NewUserRequest.class);
//            ResourceCreationResponse responseBody = userService.register(jsonMapper.readValue(req.getInputStream(), NewUserRequest.class));
//            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));

           // ResourceCreationResponse responseBody = userService.register()


            String username = requestBody.getUsername();
            ResourceCreationResponse responseBody = userService.register(jsonMapper.readValue(req.getInputStream(),NewUserRequest.class));

            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));
        }

        // TODO remove this and uncomment the code below once custom exceptions are added
        catch (RuntimeException e) {
            // TODO encapsulate error response creation into its own utility method
            resp.setStatus(500); // BAD REQUEST;
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("statusCode", 500);
            errorResponse.put("message", e.getMessage());
            errorResponse.put("timestamp", LocalDateTime.now().toString());
            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
            e.printStackTrace();
        }

//        catch (InvalidRequestException | JsonMappingException e) {
//
//            // TODO encapsulate error response creation into its own utility method
//            resp.setStatus(400); // BAD REQUEST;
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("statusCode", 400);
//            errorResponse.put("message", e.getMessage());
//            errorResponse.put("timestamp", System.currentTimeMillis()); // TODO replace with LocalDateTime.now()
//            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
//
//        } catch (ResourcePersistenceException e) {
//
//            resp.setStatus(409); // CONFLICT; indicates that the provided resource could not be saved without conflicting with other data
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("statusCode", 409);
//            errorResponse.put("message", e.getMessage());
//            errorResponse.put("timestamp", System.currentTimeMillis()); // TODO replace with LocalDateTime.now()
//            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
//
//        } catch (DataSourceException e) {
//
//            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("statusCode", 500);
//            errorResponse.put("message", e.getMessage());
//            errorResponse.put("timestamp", System.currentTimeMillis()); // TODO replace with LocalDateTime.now()
//            resp.getWriter().write(jsonMapper.writeValueAsString(errorResponse));
//
//        }

    }
}