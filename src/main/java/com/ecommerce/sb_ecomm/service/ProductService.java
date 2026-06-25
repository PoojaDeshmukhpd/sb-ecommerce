package com.ecommerce.sb_ecomm.service;

import com.ecommerce.sb_ecomm.model.Product;
import com.ecommerce.sb_ecomm.payload.ProductDTO;
import com.ecommerce.sb_ecomm.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(Long categoryId, ProductDTO product);

    ProductResponse getAllProducts();
    ProductResponse searchByCategory(Long categoryId);

    ProductResponse searchProductByKeyword(String keyword);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProduct(Long productId, ProductDTO productDTO);
}
