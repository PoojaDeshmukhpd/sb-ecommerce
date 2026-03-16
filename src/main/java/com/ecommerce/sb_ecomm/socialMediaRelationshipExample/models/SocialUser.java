package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SocialUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* Seconds Way : if SocialUser is the owner of the relationship
     if want to make SocialProfile as owner of the relationship do vice versa*/
    @OneToOne
    @JoinColumn(name = "social_profile_id")
   SocialProfile socialProfile;

    // Many items are represented using List
    @OneToMany(mappedBy = "socialUser")
    private List<Post> postsList = new ArrayList<>();


    /*First Way : if i only add this annotation in SocialProfile then it will unidirectional
     but now i have added OneToOne here so it's bidirectional
     so here socialProfile PK is working as FK in SocialUser and
     Social User PK key is working as FK in SocialProfile
     So this is the redundant data this is not good */
//    @OneToOne
//    private SocialProfile socialProfile;
}
