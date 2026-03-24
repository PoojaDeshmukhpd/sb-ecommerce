package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.controllers;

import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.SocialUser;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.services.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SocialController {

    @Autowired
    SocialService socialService;

    @GetMapping("/social/users")
    public ResponseEntity<List<SocialUser>> getAllSocialUsers() {
        return new ResponseEntity<>(socialService.getAllSocialUsers(), HttpStatus.OK);
    }

    @PostMapping("/social/users")
    public ResponseEntity<SocialUser> saveSocialUser(@RequestBody SocialUser socialUser) {
        return new ResponseEntity<>(socialService.saveSocialUser(socialUser), HttpStatus.OK);
    }
}
