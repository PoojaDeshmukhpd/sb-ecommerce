package com.ecommerce.sb_ecomm.controller;

import com.ecommerce.sb_ecomm.model.Categeory;
import com.ecommerce.sb_ecomm.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategeoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/categeories")
//    @RequestMapping(value = "/public/categeories", method = RequestMethod.GET)
    public ResponseEntity<List<Categeory>> getAllCategory() {
        List<Categeory> categoryList = categoryService.getCategoryList();
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @PostMapping("public/categories")
    public ResponseEntity<String> createCategory(@RequestBody Categeory categeory) {
        categoryService.createCategory(categeory);
        return new ResponseEntity<>("category Added Successfully", HttpStatus.CREATED);
    }


    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") Long categoryId) {
        try {
            String status = categoryService.deleteCategory(categoryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
//            return ResponseEntity.ok(status);
//            return ResponseEntity.status(HttpStatus.OK).body(status);

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<String> updateCategory(@PathVariable("categoryId") Long categoryId, @RequestBody Categeory categeory) {
        try {
            Categeory savedCategory = categoryService.updateCategory(categoryId, categeory);
            return new ResponseEntity<>("Category Updated " + categoryId, HttpStatus.OK);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatusCode());
        }
    }
}
