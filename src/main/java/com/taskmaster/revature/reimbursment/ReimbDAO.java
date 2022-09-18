package com.taskmaster.revature.reimbursment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.taskmaster.revature.common.datasource.ConnectionFactory;
import com.taskmaster.revature.common.exceptions.DataSourceException;
import com.taskmaster.revature.users.User;

public class ReimbDAO {
    private final String select = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, " +
            "er.description, er.payment_id, er.author_id, er.resolver_id, " +
            "ers.status, ers.status_id, ert.type, ert.type_id " +
            "FROM ers_reimbursements er " +
            "JOIN ers_reimbursement_statuses ers ON er.status_id = ers.status_id " +
            "JOIN ers_reimbursement_types ert ON er.type_id = ert.type_id ";

    public String create(Reimb reimb){
        String sql = "INSERT INTO ers_reimbursements (reimb_id, amount, submitted, description, author_id, status_id, type_id)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reimb.getReimb_id());
            pstmt.setDouble(2, reimb.getAmount());
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(4, reimb.getDescription().trim());
            pstmt.setString(5, reimb.getAuthor_id());
            pstmt.setString(6, Reimb.getStatusFromName("PENDING").getId());
            pstmt.setString(7, reimb.getType().getId());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DataSourceException(e);
        }
        return "hi";
//        return user.getUsername() + " added.";
    }

    public List<Reimb> getAllReimb () {
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);
            return mapResultSet(rs);
        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }

    }
    public String approveDeny(String reimb_id, String resolver_id, Status reimb_status) {
        String sql = "UPDATE ers_reimbursements SET (resolver_id, status_id, resolved)= (?,?,?) WHERE reimb_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, resolver_id);
            pstmt.setString(2, reimb_status.getId());
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setString(4, reimb_id);
            pstmt.executeUpdate();
            return "reimb changed"; //TODO change
        } catch (SQLException e) {
            //TODO log exception
            throw new DataSourceException(e);
        }
    }

    public Optional<Reimb> getReimbById (String id) {
        String sqlId = select + "WHERE er.reimb_id = ?";
        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement(sqlId);
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            return mapResultSet(rs).stream().findFirst();
        } catch (SQLException e) {
            throw new DataSourceException(e);
        }
    }

    private List<Reimb> mapResultSet(ResultSet rs) throws SQLException {
        List<Reimb> reimbs = new ArrayList<>();
        while (rs.next()) {
            Reimb reimb = new Reimb();
            reimb.setReimb_id(rs.getString("reimb_id"));
            reimb.setAmount(rs.getInt("amount"));
            reimb.setSubmitted(rs.getString("submitted"));
            reimb.setResolved(rs.getString("resolved"));
            reimb.setDescription(rs.getString("description"));
            reimb.setPayment_id(rs.getString("payment_id"));
            reimb.setAuthor_id(rs.getString("author_id"));
            reimb.setResolver_id(rs.getString("resolver_id"));
            reimb.setStatus(new Status(rs.getString("status_id"),rs.getString("status")));
            reimb.setType(new Type(rs.getString("type_id"),rs.getString("type")));
            reimbs.add(reimb);
        }
        return reimbs;
    }
}
