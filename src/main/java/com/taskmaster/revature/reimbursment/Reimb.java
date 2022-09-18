package com.taskmaster.revature.reimbursment;

import com.taskmaster.revature.common.datasource.ConnectionFactory;
import com.taskmaster.revature.common.exceptions.DataSourceException;
import com.taskmaster.revature.common.exceptions.ResourceNotFoundException;
import com.taskmaster.revature.users.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

    public class Reimb {
        private String reimb_id;
        private int amount;
        private String submitted;
        private String resolved;
        private String description;
        private String payment_id;
        private String author_id;
        private String resolver_id;
        private Status status;
        private Type type;

        final static public Type getTypeFromName(String type_name) {
            String sql = "SELECT type_id, type FROM ers_reimbursement_types WHERE type = ?";
            try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, type_name);
                ResultSet rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    // TODO: fix me
                    throw new ResourceNotFoundException();
                }
                return new Type(rs.getString("type_id"), rs.getString("type"));
            } catch (SQLException e) {
                throw new DataSourceException(e);
            }
        }

        final static public Status getStatusFromName(String status_name) {
            String sql = "SELECT status_id, status FROM ers_reimbursement_statuses WHERE status = ?";
            try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, status_name);
                ResultSet rs = preparedStatement.executeQuery();
                if (!rs.next()) {
                    // TODO: fix me
                    throw new ResourceNotFoundException();
                }
                return new Status(rs.getString("status_id"), rs.getString("status"));
            } catch (SQLException e) {
                throw new DataSourceException(e);
            }
        }

        public String getReimb_id() {
            return this.reimb_id;
        }
        public void setReimb_id(String reimb_id) {
            this.reimb_id = reimb_id;
        }

        public int getAmount() {
            return this.amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getSubmitted() {
            return this.submitted;
        }

        public void setSubmitted(String submitted) {
            this.submitted = submitted;
        }

        public String getResolved() {
            return this.resolved;
        }

        public void setResolved(String resolved) {
            this.resolved = resolved;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPayment_id() {
            return this.payment_id;
        }

        public void setPayment_id(String payment_id) {
            this.payment_id = payment_id;
        }

        public String getAuthor_id() {
            return this.author_id;
        }

        public void setAuthor_id(String author_id) {
            this.author_id = author_id;
        }

        public String getResolver_id() {
            return this.resolver_id;
        }

        public void setResolver_id(String resolver_id) {
            this.resolver_id = resolver_id;
        }

        public Status getStatus() {
            return this.status;
        }

        public void setStatus(Status status) {
            this.status  = status;
        }

        public Type getType() {
            return this.type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Reimb reimb = (Reimb) o;
            return Objects.equals(reimb_id, reimb.reimb_id) && Objects.equals(amount, reimb.amount)
                    && Objects.equals(submitted, reimb.submitted) && Objects.equals(resolved, reimb.resolved)
                    && Objects.equals(description, reimb.description) && Objects.equals(payment_id, reimb.payment_id)
                    && Objects.equals(author_id, reimb.author_id) && Objects.equals(resolver_id, reimb.resolver_id)
                    && Objects.equals(status, reimb.status) && Objects.equals(type, reimb.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(reimb_id, amount, submitted, resolved, description, payment_id, author_id,
                    resolver_id, status, type);
        }

        @Override
        public String toString() {
            return "Reimbursement {" +
                    "reimb_id = '" + reimb_id + "' " +
                    "amount = '" + amount + "' " +
                    "submitted = '" + submitted + "' " +
                    "resolved = '" + resolved + "' " +
                    "description = '" + description + "' " +
                    "payment_id = '" + payment_id + "' " +
                    "author_id = '" + author_id + "' " +
                    "resolver_id = '" + resolver_id + "' " +
                    "status = '" + status.getName() + "' " +
                    "type = '" + type.getName() + "'}";
        }
    }