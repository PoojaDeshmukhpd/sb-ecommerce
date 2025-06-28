package com.ecommerce.sb_ecomm.service;

import com.ecommerce.sb_ecomm.model.Categeory;
import com.ecommerce.sb_ecomm.payload.CategoryDTO;
import com.ecommerce.sb_ecomm.payload.CategoryResponse;

import java.util.List;


public interface CategoryService {
    CategoryResponse getCategoryList();
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
}
