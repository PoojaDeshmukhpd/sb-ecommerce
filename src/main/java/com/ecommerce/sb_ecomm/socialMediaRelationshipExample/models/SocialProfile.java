package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models;

import jakarta.persistence.*;
import org.springframework.data.repository.cdi.Eager;

@Entity
public class SocialProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    @JoinColumn(name ="social_user") // this will be foreign key, if want to follow naming convention in project then use this annotation
    // eplicitly enabled goreign key using JoinColumn
    private SocialUser socialUser; // this will add the one column in db as SOCIAL_USER_ID, if object name is user then column name will be USER_ID
}
