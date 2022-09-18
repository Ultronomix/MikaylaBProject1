package com.taskmaster.revature.reimbursment;

import java.util.Objects;

public class Status {
    private String id;
    private String name;

    public Status(String id, String status) {
        this.id = id;
        this.name = status;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status role1 = (Status) o;
        return Objects.equals(id, role1.id) && Objects.equals(name, role1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Status{" +
                "id='" + id + '\'' +
                ", status='" + name + '\'' +
                '}';
    }

}