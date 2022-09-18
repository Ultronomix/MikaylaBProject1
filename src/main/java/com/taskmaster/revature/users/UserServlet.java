package com.taskmaster.revature.users;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.taskmaster.revature.common.ErrorResponse;
import com.taskmaster.revature.common.ResourceCreationResponse;
import com.taskmaster.revature.common.exceptions.AuthenticationException;
import com.taskmaster.revature.common.exceptions.DataSourceException;
import com.taskmaster.revature.common.exceptions.InvalidRequestException;
import com.taskmaster.revature.common.exceptions.ResourceNotFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class UserServlet extends HttpServlet {

    private final ObjectMapper jsonMapper;
    private final UserService userService;


    public UserServlet(UserService userService, ObjectMapper jsonMapper) {
        this.userService = userService;
        this.jsonMapper = jsonMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");

        HttpSession userSession = req.getSession(false);
        if (userSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in")));
            return;
        }
        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");
        String idToSearchFor = req.getParameter("id");
        String roleToSearchFor = req.getParameter("role");
        String usernameToSearchFor = req.getParameter("username");

        if (!requester.getRole().equals("ADMIN")) {
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {
            if (idToSearchFor == null) {
                List<UserResponse> allUsers = userService.getAllUsers();
                resp.getWriter().write(jsonMapper.writeValueAsString(allUsers));
            }
            if (idToSearchFor != null) {
                UserResponse foundUser = userService.getUserById(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }
            if (usernameToSearchFor != null) {
                UserResponse foundUser = userService.getUserbyUsername(usernameToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }
            if (roleToSearchFor != null) {
                UserResponse foundUser = userService.getUserByRole(roleToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundUser));
            }
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400); // BAD REQUEST;
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (ResourceNotFoundException e) {
            resp.setStatus(404); // NOT FOUND; the sought resource could not be located
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
        } catch (DataSourceException e) {
            resp.setStatus(500); // INTERNAL SERVER ERROR; general error indicating a problem with the server
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        // Access the HTTP session
        HttpSession userSession = req.getSession(false);
        // if null, this mean that the requester is not authenticated with server
        if (userSession == null) {
            resp.setStatus(401); // Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in.")));
            return;
        }
        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");
        // Only ADMIN access
        if (!requester.getRole().equals("ADMIN")) {
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }
        try {
            com.taskmaster.revature.common.ResourceCreationResponse responseBody = userService
                    .register(jsonMapper.readValue(req.getInputStream(), NewUserRequest.class));
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);// * bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            resp.setStatus(409); // * conflit; indicates provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        // Access the HTTP session
        HttpSession userSession = req.getSession(false);
        // if null, this mean that the requester is not authenticated with server
        if (userSession == null) {
            resp.setStatus(401); // Unauthorized
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requester is not authenticated with server, log in.")));
            return;
        }
        UserResponse requester = (UserResponse) userSession.getAttribute("authUser");
        // Only ADMIN access
        if (!requester.getRole().equals("ADMIN")) {
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }
        // Get updated info
        try {
            String idToSearchFor = req.getParameter("id");
            // Make sure the user exists
            userService.getUserById(idToSearchFor);
            ResourceCreationResponse responseBody = userService
                    .updateUser(idToSearchFor, jsonMapper.readValue(req.getInputStream(), NewUserRequest.class));
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));
        }  catch (ResourceNotFoundException e) {
            resp.setStatus(404); // NOT FOUND; the sought resource could not be located
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
        }
        catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);// * bad request
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (AuthenticationException e) {
            resp.setStatus(409); // * conflict; indicate that provided resource could not be saved
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(409, e.getMessage())));
        } catch (DataSourceException e) {
            resp.setStatus(500); // * internal error
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }
}
