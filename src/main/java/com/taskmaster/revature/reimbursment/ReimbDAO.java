package com.taskmaster.revature.reimbursment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.taskmaster.revature.common.datasource.ConnectionFactory;
import com.taskmaster.revature.common.exceptions.DataSourceException;

public class ReimbDAO {
    private final String select = "SELECT er.reimb_id, er.amount, er.submitted, er.resolved, " +
            "er.description, er.payment_id, er.author_id, er.resolver_id, " +
            "ers.status, ert.type " +
            "FROM ers_reimbursements er " +
            "JOIN ers_reimbursement_statuses ers ON er.status_id = ers.status_id " +
            "JOIN ers_reimbursement_types ert ON er.type_id = ert.type_id ";

    public static <T> Optional findReimbById(UUID uuid) {
        return null;
    }

    public List<Reimb> getAllReimb () {

        List<Reimb> allreimbs = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(select);

            allreimbs = mapResultSet(rs);

            return allreimbs;

        } catch (SQLException e) {
            // TODO add log
            throw new DataSourceException(e);
        }

    }

    public Optional<Reimb> getReimbById (String id) {

        String sqlId = select + "WHERE er.author_id = ?";

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement preparedStatement = conn.prepareStatement(sqlId);
            preparedStatement.setString(1, id);
            ResultSet rs = preparedStatement.executeQuery();

            return mapResultSet(rs).stream().findFirst();

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }

    }

    public List<Reimb> getReimbByStatus (String status) {


        String sqlStatus = select + "WHERE ers.status = ?";
        List<Reimb> reimbStatus = new  ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement preparedStatement = conn.prepareStatement(sqlStatus);
            preparedStatement.setString(1, status.toUpperCase());
            ResultSet rs = preparedStatement.executeQuery();

            reimbStatus = mapResultSet(rs);

            return reimbStatus;

        } catch (SQLException e) {

            throw new DataSourceException(e);
        }
    }

    public List<Reimb> getReimbByType (String type) {

        String sqlType = select + "WHERE ert.type = ?";
        List<Reimb> reimbType = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {

            PreparedStatement preparedStatement = conn.prepareStatement(sqlType);
            preparedStatement.setString(1, type.toUpperCase());
            ResultSet rs = preparedStatement.executeQuery();

            reimbType = mapResultSet(rs);

            return reimbType;

        } catch (Exception e) {

            throw new DataSourceException(e);
        }
    }

    private List<Reimb> mapResultSet(ResultSet rs) throws SQLException {

        List<Reimb> reimbs = new ArrayList<>();

        while (rs.next()) {
            Reimb reimb = new Reimb();
          //  reimbs.setReimb_id(rs.getString("reimb_id"));
            reimb.setAmount(rs.getInt("amount"));
            reimb.setSubmitted(rs.getString("submitted"));
            reimb.setResolved(rs.getString("resolved"));
            reimb.setDescription(rs.getString("description"));
            reimb.setPayment_id(rs.getString("payment_id"));
            reimb.setAuthor_id(rs.getString("author_id"));
            reimb.setResolver_id(rs.getString("resolver_id"));
            reimb.setStatus(rs.getString("status"));
            reimb.setType(rs.getString("type"));
            reimbs.add(reimb);
        }

        return reimbs;
    }
}
