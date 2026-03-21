package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class SocialGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "socialGroups")
    private Set<SocialUser> users = new HashSet<>();
}
