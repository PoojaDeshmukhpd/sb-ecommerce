package com.ecommerce.sb_ecomm.repository;

import com.ecommerce.sb_ecomm.model.Role;
import com.ecommerce.sb_ecomm.model.UsersRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(UsersRole usersRole);
}
