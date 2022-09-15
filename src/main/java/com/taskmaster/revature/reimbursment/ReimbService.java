package com.taskmaster.revature.reimbursment;
import com.taskmaster.revature.users.UserResponse;

import java.util.ArrayList;
import java.util.List;



public class ReimbService {

    private final ReimbDAO reimbDAO;

    public ReimbService(ReimbDAO reimbDAO) {
        this.reimbDAO = reimbDAO;
    }

    public List<com.taskmaster.revature.reimbursment.ReimbResponse> getAllReimb() {


        List<com.taskmaster.revature.reimbursment.ReimbResponse> result = new ArrayList<>();
        List<Reimb> reimbs = reimbDAO.getAllReimb();

        for (Reimb reimb : reimbs) {
            result.add(new com.taskmaster.revature.reimbursment.ReimbResponse(reimb));
        }

        return result;

    }

    public com.taskmaster.revature.reimbursment.ReimbResponse getReimbById(String id) {


        if (id == null || id.trim().length() <= 0) {

            throw new RuntimeException("A user's id must be provided");
        }
        return reimbDAO.getReimbById(id).map(ReimbResponse::new).orElseThrow(RuntimeException::new);

    }

        public List<com.taskmaster.revature.reimbursment.ReimbResponse> getReimbByStatus (String status) {

            if (status == null || (!status.toUpperCase().trim().equals("APPROVED")
                    && !status.toUpperCase().trim().equals("PENDING")
                    && !status.toUpperCase().trim().equals("DENIED"))) {
                throw new RuntimeException("Status cannot be empty. Enter 'Approved', 'Pending', " +
                        " or 'Denied'");
            }


            List<com.taskmaster.revature.reimbursment.ReimbResponse> result = new ArrayList<>();
            List<Reimb> reimbs = reimbDAO.getReimbByStatus(status);

            for (Reimb reimb : reimbs) {
                result.add(new com.taskmaster.revature.reimbursment.ReimbResponse(reimb));
            }

            return result;

        }

        public List<com.taskmaster.revature.reimbursment.ReimbResponse> getReimbByType (String type){


            if (type == null || (!type.toUpperCase().trim().equals("LODGING")
                    && !type.toUpperCase().trim().equals("TRAVEL")
                    && !type.toUpperCase().trim().equals("FOOD")
                    && !type.toUpperCase().trim().equals("OTHER"))) {

                throw new RuntimeException("Type must be 'Lodging', 'Travel', " +
                        "'Food', or 'Other'");

            }

            List<com.taskmaster.revature.reimbursment.ReimbResponse> result = new ArrayList<>();
            List<Reimb> reimbs = reimbDAO.getReimbByType(type);

            for (Reimb reimb : reimbs) {
                result.add(new com.taskmaster.revature.reimbursment.ReimbResponse(reimb));
            }

            return result;

        }

    }
