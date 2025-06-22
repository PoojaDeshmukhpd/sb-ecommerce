package com.ecommerce.sb_ecomm.repository;

import com.ecommerce.sb_ecomm.model.Categeory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categeory, Long> {
}