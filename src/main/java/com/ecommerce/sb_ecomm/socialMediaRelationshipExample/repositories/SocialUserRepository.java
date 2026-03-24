package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.repositories;

import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialUserRepository extends JpaRepository<SocialUser, Long> {
}
