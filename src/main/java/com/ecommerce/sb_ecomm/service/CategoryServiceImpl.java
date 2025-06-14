package com.ecommerce.sb_ecomm.service;

import com.ecommerce.sb_ecomm.model.Categeory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    public List<Categeory> categoryList = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    public List<Categeory> getCategoryList() {
        return categoryList;
    }

    @Override
    public void createCategory(Categeory categeory) {
        categeory.setCategeoryId(nextId++);
        categoryList.add(categeory);
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Categeory categeory = categoryList.stream()
                .filter(c -> c.getCategeoryId().equals(categoryId)).findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found"));
        categoryList.remove(categeory); // we need category object to remove it so use above strategy

        return "Categeory removed successfully " + categoryId;
    }


    @Override
    public Categeory updateCategory(Long categoryId, Categeory addCategeory) {
        Optional<Categeory> optionalCategeory = categoryList.stream()
                .filter(c -> c.getCategeoryId().equals(categoryId)).findFirst();

        if (optionalCategeory.isPresent()) {
            Categeory existingCategeory = optionalCategeory.get();
            existingCategeory.setCategeoryName(addCategeory.getCategeoryName());
            return existingCategeory;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource Not Found");
        }
    }
}
