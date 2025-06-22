package com.ecommerce.sb_ecomm.service;

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

    private Long nextId = 1L;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public List<Categeory> getCategoryList() {
        return categoryRepository.findAll();
    }

    @Override
    public void createCategory(Categeory categeory) {
        categoryRepository.save(categeory);
    }

    @Override
    public String deleteCategory(Long categoryId) {
//        List<Categeory> categoryList = categoryRepository.findAll();
//        Categeory categeory = categoryList.stream()
//                .filter(c -> c.getCategeoryId().equals(categoryId)).findFirst()
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));

        Categeory deleteCategory = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));
        categoryRepository.delete(deleteCategory); // we need category object to remove from database repository
        return "Categeory removed successfully " + categoryId;
    }


    @Override
    public Categeory updateCategory(Long categoryId, Categeory addCategeory) {
//        Optional<Categeory> categoryList = categoryRepository.findById(categoryId);
//        Categeory savedCategory = categoryList.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category Not Found"));

        Categeory savedCategory = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Response Not Found"));
        addCategeory.setCategeoryId(categoryId);
        categoryRepository.save(addCategeory);
        return savedCategory;
    }
}
