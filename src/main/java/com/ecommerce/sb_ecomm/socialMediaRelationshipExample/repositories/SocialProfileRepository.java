package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.repositories;

import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.SocialProfile;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialProfileRepository extends JpaRepository<SocialProfile,Long> {
}
