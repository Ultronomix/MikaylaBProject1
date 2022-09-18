package com.taskmaster.revature.reimbursment;
import com.taskmaster.revature.common.ResourceCreationResponse;
import com.taskmaster.revature.common.exceptions.ResourceNotFoundException;
import com.taskmaster.revature.users.NewUserRequest;
import com.taskmaster.revature.users.User;
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
        return reimbDAO.getReimbById(id).map(ReimbResponse::new).orElseThrow(ResourceNotFoundException::new);
    }

    public ResourceCreationResponse approveDeny(String reimb_id, String resolver_id, String status) {
        if (reimb_id == null || status == null || resolver_id == null) {
            throw new RuntimeException("Provided request payload was null.");
        }
        Status reimb_status = Reimb.getStatusFromName(status);
        // TODO: validate status
        String newApproveDeny = reimbDAO.approveDeny(reimb_id, resolver_id, reimb_status);
        return new ResourceCreationResponse(newApproveDeny);
    }

    public ResourceCreationResponse createReimb(ReimbRequest newReimb) {
        if (newReimb == null) {
            throw new RuntimeException("Provided request payload was null.");
        }
        // TODO: Check reimb criteria make sure the type exists

        Reimb reimbToPersist = newReimb.extractEntity();
        String newRiemb = reimbDAO.create(reimbToPersist);
        return new ResourceCreationResponse(newRiemb);
    }
}
