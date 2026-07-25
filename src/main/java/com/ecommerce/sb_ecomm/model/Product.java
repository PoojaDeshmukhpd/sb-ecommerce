package com.ecommerce.sb_ecomm.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private String image;
    private String description;
    private Integer quantity;
    private double price;  // 100 rs
    private double discount; // 25 rs
    private double specialPrice; // 75 rs -> 100 -(25/100) * 100(actual_price) = 25

    @ManyToOne
    @JoinColumn(name = "category_Id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Users user;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<CartItem> products = new ArrayList<>();
}
