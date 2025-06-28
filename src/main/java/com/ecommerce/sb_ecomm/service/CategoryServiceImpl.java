package com.ecommerce.sb_ecomm.service;

import com.ecommerce.sb_ecomm.exception.APIException;
import com.ecommerce.sb_ecomm.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecomm.model.Categeory;
import com.ecommerce.sb_ecomm.payload.CategoryDTO;
import com.ecommerce.sb_ecomm.payload.CategoryResponse;
import com.ecommerce.sb_ecomm.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getCategoryList() {
        List<Categeory> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new APIException("Category List is Empty");
        }

        List<CategoryDTO> categoryDTOList = categories.stream()
                .map(categeory -> modelMapper.map(categeory, CategoryDTO.class))
                .toList();

        System.out.println("Mapped DTO List: " + categoryDTOList);

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategories(categoryDTOList);
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Categeory categeory = modelMapper.map(categoryDTO, Categeory.class);

        Categeory getFoundCategeoryFromDb = categoryRepository.findByCategoryName(categeory.getCategoryName());
        if (getFoundCategeoryFromDb != null) {
            throw new APIException("Categeory with " + categeory.getCategoryName() + " name already exists!!!");
        }
        Categeory categeory1 = categoryRepository.save(categeory);
        CategoryDTO savedCategoryDTO = modelMapper.map(categeory1, CategoryDTO.class);

        return savedCategoryDTO;
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Categeory deleteCategory = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "categeoryId", categoryId));
         categoryRepository.delete(deleteCategory); // we need category object to remove from database repository
        CategoryDTO categoryDTODeleted = modelMapper.map(deleteCategory, CategoryDTO.class);
        return categoryDTODeleted;
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Categeory categeory = modelMapper.map(categoryDTO, Categeory.class);

        Categeory savedCategory = categoryRepository.findById(categoryId).
                orElseThrow(() -> new ResourceNotFoundException("Category", "categeoryId", categoryId));
        categeory.setCategoryId(categoryId);
        categoryRepository.save(categeory);

        Categeory updatedCategory = categoryRepository.save(categeory);
        CategoryDTO updatedDTO = modelMapper.map(updatedCategory, CategoryDTO.class);
        return updatedDTO;
    }
}
