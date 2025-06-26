package com.ecommerce.sb_ecomm.repository;

import com.ecommerce.sb_ecomm.model.Categeory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Categeory, Long> {
    //findBY fire select query Categeory is the model name and Name is the that i want to find
    Categeory findByCategeoryName(@NotBlank @Size(min = 5, message = "Category Name must be 5 characters") String categeoryName);
}