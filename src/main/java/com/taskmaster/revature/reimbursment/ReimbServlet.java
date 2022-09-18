package com.taskmaster.revature.reimbursment;

import java.io.IOException;
import java.net.IDN;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmaster.revature.users.NewUserRequest;
import com.taskmaster.revature.users.UserResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.taskmaster.revature.common.ErrorResponse;
import com.taskmaster.revature.common.exceptions.InvalidRequestException;
import com.taskmaster.revature.common.exceptions.DataSourceException;
import com.taskmaster.revature.common.exceptions.ResourceNotFoundException;
import com.taskmaster.revature.common.ResourceCreationResponse;
import com.taskmaster.revature.common.exceptions.AuthenticationException;
import jakarta.servlet.http.HttpSession;

public class ReimbServlet extends HttpServlet {

    private final ReimbService reimbService;

    public ReimbServlet(ReimbService reimbService) {
        this.reimbService = reimbService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        HttpSession reimbSession = req.getSession(false);
        if (reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }
        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");
        String idToSearchFor = req.getParameter("id");
        String statusToSearchFor = req.getParameter("status");
        String typeToSearchFor = req.getParameter("type");
        String sort = req.getParameter("sort");

        // Manager can see all employee can only see theirs
        if (requester.getRole().equals("ADMIN") || (!requester.getRole().equals("MANAGER") && (requester.getRole().equals("EMPLOYEE") && !requester.getUser_Id().equals(idToSearchFor)))) {
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {
            List<ReimbResponse> allReimb = reimbService.getAllReimb();
            if (idToSearchFor != null) {
                if (requester.getRole().equals("MANAGER")) {
                    allReimb.removeIf(reimb -> reimb.getResolver_id() == null || !reimb.getResolver_id().equals(idToSearchFor));
                } else {
                    allReimb.removeIf(reimb -> !reimb.getAuthor_id().equals(idToSearchFor));
                }
            }
            if (statusToSearchFor != null) {
                allReimb.removeIf(reimb -> !reimb.getStatus().getName().equals(statusToSearchFor));
            }
            if (typeToSearchFor != null) {
                allReimb.removeIf(reimb -> !reimb.getType().getName().equals(typeToSearchFor));
            }
            if (sort != null && sort.equals("true")) {
                allReimb.sort(Comparator.comparing(ReimbResponse::getSubmitted));
            }
            resp.getWriter().write(jsonMapper.writeValueAsString(allReimb));
        } catch (InvalidRequestException | JsonMappingException e) {
            resp.setStatus(400);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(400, e.getMessage())));
        } catch (ResourceNotFoundException e) {
            resp.setStatus(404);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(404, e.getMessage())));
        } catch (DataSourceException e) {
            resp.setStatus(500);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(500, e.getMessage())));
        }
    }

    @Override
    protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        resp.setContentType("application/json");
        HttpSession reimbSession = req.getSession(false);
        if (reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }
        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");
        // ONLY EMPLOYEES CAN CREATE REIMB REQ
        if (!requester.getRole().equals("EMPLOYEE")) {
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }
        try {
            ReimbRequest reimb = jsonMapper.readValue(req.getInputStream(), ReimbRequest.class);
            com.taskmaster.revature.common.ResourceCreationResponse responseBody = reimbService
                    .createReimb(reimb);
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
        HttpSession reimbSession = req.getSession(false);
        if (reimbSession == null) {
            resp.setStatus(401);
            resp.getWriter().write(jsonMapper
                    .writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
            return;
        }

        UserResponse requester = (UserResponse) reimbSession.getAttribute("authUser");
        String idToSearchFor = req.getParameter("id");
        String status = req.getParameter("status");
        ReimbResponse reimb = reimbService.getReimbById(idToSearchFor);
        // Manager can see all employee can only see theirs
        if (requester.getRole().equals("ADMIN") || (!requester.getRole().equals("MANAGER") &&
                (requester.getRole().equals("EMPLOYEE") && !reimb.getAuthor_id().equals(requester.getUser_Id()) && !reimb.getStatus().getName().equals("PENDING")))) {
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }
        if (requester.getRole().equals("MANAGER")) {
            ResourceCreationResponse responseBody = reimbService.approveDeny(reimb.getReimb_id(), requester.getUser_Id(), status);
            resp.getWriter().write(jsonMapper.writeValueAsString(responseBody));
            return;
        }

    }
    }

