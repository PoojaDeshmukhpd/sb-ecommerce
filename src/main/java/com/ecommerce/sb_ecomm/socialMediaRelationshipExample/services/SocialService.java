package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.services;

import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.SocialUser;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.repositories.SocialUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocialService {

    @Autowired
    SocialUserRepository socialUserRepository;


    public List<SocialUser> getAllSocialUsers() {
        return socialUserRepository.findAll();
    }

    public SocialUser saveSocialUser(SocialUser socialUser) {
        if (socialUser == null) {
            return null;
        }

        return socialUserRepository.save(socialUser);
    }
}
