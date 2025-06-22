package com.ecommerce.sb_ecomm.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "Categories")
public class Categeory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categeoryId;
    private String categeoryName;


    // ✅ Required by Hibernate
    public Categeory() {}

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
