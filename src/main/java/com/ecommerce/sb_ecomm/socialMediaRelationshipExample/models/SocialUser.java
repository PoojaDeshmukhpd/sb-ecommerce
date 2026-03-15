package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class SocialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
