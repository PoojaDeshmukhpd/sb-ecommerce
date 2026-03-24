package com.ecommerce.sb_ecomm.socialMediaRelationshipExample.dataInitializer;

import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.Post;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.SocialGroup;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.SocialProfile;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.models.SocialUser;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.repositories.PostRepository;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.repositories.SocialGroupRepository;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.repositories.SocialProfileRepository;
import com.ecommerce.sb_ecomm.socialMediaRelationshipExample.repositories.SocialUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    private final SocialUserRepository socialUserRepository;
    private final SocialGroupRepository  socialGroupRepository;
    private final SocialProfileRepository socialProfileRepository;
    private final PostRepository postRepository;

    public DataInitializer(SocialUserRepository socialUserRepository, SocialGroupRepository socialGroupRepository, SocialProfileRepository socialProfileRepository, PostRepository postRepository) {
        this.socialUserRepository = socialUserRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.socialProfileRepository = socialProfileRepository;
        this.postRepository = postRepository;
    }

    @Bean
    public CommandLineRunner initializeData() {
        return (args -> {
            SocialUser user1 = new SocialUser();
            SocialUser user2 = new SocialUser();
            SocialUser user3 = new SocialUser();

            socialUserRepository.save(user1);
            socialUserRepository.save(user2);
            socialUserRepository.save(user3);

            // Create Some Groups
            SocialGroup group1 = new SocialGroup();
            SocialGroup group2 = new SocialGroup();

            // Add Users to Group
            group1.getSocialUsers().add(user1);
            group1.getSocialUsers().add(user2);

            group2.getSocialUsers().add(user2);
            group2.getSocialUsers().add(user3);

            // Associate users with groups
            user1.getSocialGroups().add(group1);
            user2.getSocialGroups().add(group1);

            user2.getSocialGroups().add(group2);
            user3.getSocialGroups().add(group2);

            // Save Groups to the Database
            socialGroupRepository.save(group1);
            socialGroupRepository.save(group2);

            // save users back to database to update association
            socialUserRepository.save(user1);
            socialUserRepository.save(user2);
            socialUserRepository.save(user3);



            // Create Some Post
            Post post1 = new Post();
            Post post2 = new Post();
            Post post3 = new Post();

            // Associates Post with users
            post1.setSocialUser(user1);
            post2.setSocialUser(user2);
            post3.setSocialUser(user3);

            // Save post into database
            postRepository.save(post1);
            postRepository.save(post2);


            // Create some social profiles
            SocialProfile profile1 = new SocialProfile();
            SocialProfile profile2 = new SocialProfile();
            SocialProfile profile3 = new SocialProfile();

            // Associate profile with users
            profile1.setSocialUser(user1);
            profile2.setSocialUser(user2);
            profile3.setSocialUser(user3);

            // Save Social Profile
            socialProfileRepository.save(profile1);
            socialProfileRepository.save(profile2);
            socialProfileRepository.save(profile3);

        });
    }
}
