package com.ecommerce.sb_ecomm.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "addresses")
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be atleast 5 character")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building name must be atleast 5 character")
    private String buildingName;

    @NotBlank
    @Size(min = 5, message = "City name must be atleast 5 character")
    private String city;

    @NotBlank
    @Size(min = 5, message = "State name must be atleast 5 character")
    private String state;

    @NotBlank
    @Size(min = 5, message = "Country name must be atleast 5 character")
    private String country;

    @NotBlank
    @Size(min = 6, message = "Pincode  must be atleast 5 character")
    private String pincode;

    @ToString.Exclude // exclude this field
    @ManyToMany(mappedBy = "addresses")
    private List<Users> users = new ArrayList<>();
}
