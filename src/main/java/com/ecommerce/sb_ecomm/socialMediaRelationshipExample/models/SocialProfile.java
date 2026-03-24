package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.cdi.Eager;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SocialProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Seconds Way : if SocialUser is the owner of the relationship
     "socialProfile" this is the object name coming from SocialProfile
     Here as like first way it will not create any column of the social_user_id because mappedBy is used here*/
    @OneToOne(mappedBy = "socialProfile")
//    @JoinColumn(name = "social_user")
    @JsonIgnore
    private SocialUser socialUser;

    // First Way : this will be foreign key, if want to follow naming convention in project then use this annotation
    // Explicitly enabled goreign key using JoinColumn
//    @OneToOne
//    @JoinColumn(name ="social_user")
//
//    private SocialUser socialUser; // this will add the one column in db as SOCIAL_USER_ID, if object name is user then column name will be USER_ID
}
