package com.taskmaster.revature.reimbursment;

import com.taskmaster.revature.common.Request;
public class ReimbRequest implements Request<Reimb> {

    private String reimb_id;
    private int amount;
    private String submitted;

    private String resolved;
    private String description;
    private String payment_id;
    private String author_id;

    private String resolver_id;
    private String type;

    public String getReimb_id() {
        return reimb_id;
    }

    public void setReimb_id(String reimb_id) {
        this.reimb_id = reimb_id;
    }

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

    public String getType() {
        return type;
    }

    public void setType_id(String type) {
        this.type = type;
    }



    @Override
    public String toString() {
        return "NewReimbRequest {" +
                "reimb_id = '" + reimb_id + "' " +
                "amount = '" + amount + "' " +
                "submitted = " + submitted + "' " +
                "description = '" + description + "' " +
                "payment_id = '" + payment_id + "' " +
                "author_id = '" + author_id + "' " +
                "type = '" + type + "'}";
    }

    @Override
    public Reimb extractEntity() {
        Reimb extractEntity = new Reimb();
        extractEntity.setReimb_id(this.reimb_id);
        extractEntity.setAmount(this.amount);
        extractEntity.setSubmitted(this.submitted);
        extractEntity.setDescription(this.description);
        extractEntity.setPayment_id(this.payment_id);
        extractEntity.setAuthor_id(this.author_id);
        extractEntity.setType(this.type);
        return null;
    }
}