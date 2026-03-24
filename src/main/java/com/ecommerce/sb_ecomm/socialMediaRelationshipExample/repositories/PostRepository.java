package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.repositories;

import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
