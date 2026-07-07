package com.ecommerce.sb_ecomm.repository;

import com.ecommerce.sb_ecomm.model.Users;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Boolean existsByUsername(@NotBlank @Size(min=3, max=20) String username);

    Boolean existsByEmail(@NotBlank @Size(max = 50) @Email String email);
}
