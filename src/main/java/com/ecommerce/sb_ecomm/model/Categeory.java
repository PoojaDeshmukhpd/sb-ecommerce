package com.ecommerce.sb_ecomm.model;

public class Categeory {

    private Long categeoryId;
    private String categeoryName;

    public Categeory(Long categeoryId, String categeoryName) {
        this.categeoryId = categeoryId;
        this.categeoryName = categeoryName;
    }

    public Long getCategeoryId() {
        return categeoryId;
    }

    public void setCategeoryId(Long categeoryId) {
        this.categeoryId = categeoryId;
    }

    public String getCategeoryName() {
        return categeoryName;
    }

    public void setCategeoryName(String categeoryName) {
        this.categeoryName = categeoryName;
    }
}
