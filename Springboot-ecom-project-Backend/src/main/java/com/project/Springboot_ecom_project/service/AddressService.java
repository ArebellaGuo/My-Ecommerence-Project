package com.project.Springboot_ecom_project.service;

import com.project.Springboot_ecom_project.payload.AddressDTO;
import com.project.Springboot_ecom_project.payload.AddressResponse;
import jakarta.validation.Valid;

public interface AddressService {
    AddressDTO createAddress(@Valid AddressDTO addressDTO);

    AddressResponse getAddresses(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    AddressDTO getAddressesById(Long addressId);

    AddressResponse getUserAddresses(Integer pageNumber,Integer pageSize,String sortOrder,String sortBy);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
