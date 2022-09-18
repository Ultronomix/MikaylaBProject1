package com.taskmaster.revature.reimbursment;

import com.taskmaster.revature.common.Request;

import java.util.UUID;

public class ReimbRequest implements Request<Reimb> {
    private int amount;
    private String submitted;

    private String resolved;
    private String description;
    private String payment_id;
    private String author_id;

    private String resolver_id;
    private Status status;
    private Type type;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSubmitted() {
        return submitted;
    }

    public void setSubmitted(String submitted) {
        this.submitted = submitted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public Type getType() {
        return type;
    }

    public void setType(String type_name) {
        this.type = Reimb.getTypeFromName(type_name);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(String status_name) {
        this.status = Reimb.getStatusFromName(status_name);
    }

    @Override
    public String toString() {
        return "NewReimbRequest {" +
                "amount = '" + amount + "' " +
                "submitted = " + submitted + "' " +
                "description = '" + description + "' " +
                "payment_id = '" + payment_id + "' " +
                "author_id = '" + author_id + "' " +
                "status = '" + status + "' " +
                "type = '" + type + "'}";
    }

    @Override
    public Reimb extractEntity() {
        Reimb extractEntity = new Reimb();
        extractEntity.setReimb_id(UUID.randomUUID().toString());
        extractEntity.setAmount(this.amount);
        extractEntity.setSubmitted(this.submitted);
        extractEntity.setDescription(this.description);
        extractEntity.setPayment_id(this.payment_id);
        extractEntity.setAuthor_id(this.author_id);
        extractEntity.setStatus(this.status);
        extractEntity.setType(this.type);
        return extractEntity;
    }
}