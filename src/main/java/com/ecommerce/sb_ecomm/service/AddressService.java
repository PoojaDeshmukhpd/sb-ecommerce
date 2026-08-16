package com.ecommerce.sb_ecomm.service;

import com.ecommerce.sb_ecomm.model.Users;
import com.ecommerce.sb_ecomm.payload.AddressDTO;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO, Users user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressesById(Long addressId);

    List<AddressDTO> getUserAddresses(Users user);
}
