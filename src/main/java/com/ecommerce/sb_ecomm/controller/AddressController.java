package com.ecommerce.sb_ecomm.controller;

import com.ecommerce.sb_ecomm.model.Users;
import com.ecommerce.sb_ecomm.payload.AddressDTO;
import com.ecommerce.sb_ecomm.service.AddressService;
import com.ecommerce.sb_ecomm.util.AuthUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        Users user = authUtil.loggedInUser();
        AddressDTO createdAddressDTO = addressService.createAddress(addressDTO, user);
        return new ResponseEntity<>(createdAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAddresses() {
        List<AddressDTO> listOfAddresse = addressService.getAddresses();
        return new ResponseEntity<>(listOfAddresse, HttpStatus.FOUND);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO>  getAddressesById(@PathVariable("addressId") Long addressId) {
        AddressDTO addressDTO = addressService.getAddressesById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddresses() {
        Users user = authUtil.loggedInUser();
        List<AddressDTO> addressDTOS = addressService.getUserAddresses(user);
        return new ResponseEntity<>(addressDTOS, HttpStatus.OK);
    }

}
