package com.ecommerce.sb_ecomm.payload;

import com.ecommerce.sb_ecomm.model.Categeory;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {
    List<CategoryDTO> categories;
}
