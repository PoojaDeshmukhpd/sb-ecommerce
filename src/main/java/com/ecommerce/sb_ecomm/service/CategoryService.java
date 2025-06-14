package com.ecommerce.sb_ecomm.service;

import com.ecommerce.sb_ecomm.model.Categeory;

import java.util.List;


public interface CategoryService {
    List<Categeory> getCategoryList();
    void createCategory(Categeory categeory);

    String deleteCategory(Long categoryId);

    Categeory updateCategory(Long categoryId, Categeory categeory);
}
