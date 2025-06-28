package com.ecommerce.sb_ecomm.service;

import com.ecommerce.sb_ecomm.exception.APIException;
import com.ecommerce.sb_ecomm.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecomm.model.Categeory;
import com.ecommerce.sb_ecomm.repository.CategoryRepository;
import io.micrometer.common.KeyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Categeory> getCategoryList() {
        List<Categeory> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("Category List is Empty");
        }
        return categories;
    }

    @Override
    public void createCategory(Categeory categeory) {
        Categeory savedCategory = categoryRepository.findByCategeoryName(categeory.getCategeoryName());
        if (savedCategory != null) {
            throw new APIException("Categeory with " + categeory.getCategeoryName() + " name already exists!!!");
        }
        categoryRepository.save(categeory);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Categeory deleteCategory = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "categeoryId", categoryId));
        categoryRepository.delete(deleteCategory); // we need category object to remove from database repository
        return "Categeory removed successfully " + categoryId;
    }

    @Override
    public Categeory updateCategory(Long categoryId, Categeory addCategeory) {
        Categeory savedCategory = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "categeoryId", categoryId));
        addCategeory.setCategeoryId(categoryId);
        categoryRepository.save(addCategeory);
        return savedCategory;
    }
}
