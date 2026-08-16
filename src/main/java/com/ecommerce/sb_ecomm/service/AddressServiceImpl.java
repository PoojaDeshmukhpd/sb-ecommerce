package com.ecommerce.sb_ecomm.service;

import com.ecommerce.sb_ecomm.exception.ResourceNotFoundException;
import com.ecommerce.sb_ecomm.model.Address;
import com.ecommerce.sb_ecomm.model.Users;
import com.ecommerce.sb_ecomm.payload.AddressDTO;
import com.ecommerce.sb_ecomm.repository.AddressRepository;
import com.ecommerce.sb_ecomm.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    AuthUtil authUtil;

    @Autowired
    ModelMapper  modelMapper;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, Users user) {
        Address address = modelMapper.map(addressDTO, Address.class);
        List<Address> addressList = user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);

        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        List<AddressDTO> addressDTOS = addresses.stream().map(
                address -> modelMapper.map(address, AddressDTO.class)
        ).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddresses(Users user) {
        List<Address> addresses = user.getAddresses();
        List<AddressDTO> addressDTOS = addresses.stream().map(
                address -> modelMapper.map(address, AddressDTO.class)
        ).toList();
        return addressDTOS;
    }
}
