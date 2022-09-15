package com.taskmaster.revature.reimbursment;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
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

        if ((!requester.getRole().equals("CEO") && !requester.getRole().equals("FINANCE MANAGER")) && !requester.getUser_Id().equals(idToSearchFor)) {
            resp.setStatus(403); // Forbidden
            resp.getWriter().write(jsonMapper.writeValueAsString(new ErrorResponse(403, "Requester not permitted to communicate with this endpoint.")));
            return;
        }

        try {
            if (idToSearchFor == null && statusToSearchFor == null && typeToSearchFor == null) {
                List<ReimbResponse> allReimb = reimbService.getAllReimb();
                resp.getWriter().write(jsonMapper.writeValueAsString(allReimb));

            }
            if (idToSearchFor != null) {
                ReimbResponse foundRequest = reimbService.getReimbById(idToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundRequest));

            }
            if (statusToSearchFor != null) {
                List<ReimbResponse> foundStatus = reimbService.getReimbByStatus(statusToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundStatus));
            }
            if (typeToSearchFor != null) {

                List<ReimbResponse> foundType = reimbService.getReimbByType(typeToSearchFor);
                resp.getWriter().write(jsonMapper.writeValueAsString(foundType));
            }
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

        resp.getWriter().write("Reimb GET authorization end");
    }

        @Override
        protected void doPost (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

            ObjectMapper jsonMapper = new ObjectMapper();
            resp.setContentType("application/json");

            HttpSession reimbSession = req.getSession(false);

            if (reimbSession == null) {
                resp.setStatus(401);
                resp.getWriter().write(jsonMapper
                        .writeValueAsString(new ErrorResponse(401, "Requestor not authenticated with server, log in")));
                return;
            }

            resp.getWriter().write("Post to /reimb work");
        }

        @Override
        protected void doPut (HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

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

            if ((!requester.getRole().equals("CEO") && !requester.getRole().equals("FINANCE MANGER"))
                    && !requester.getUser_Id().equals(idToSearchFor)) {
                resp.getWriter().write("test put constraint");
            }


            resp.getWriter().write("Put to /reimb work");
        }
    }

