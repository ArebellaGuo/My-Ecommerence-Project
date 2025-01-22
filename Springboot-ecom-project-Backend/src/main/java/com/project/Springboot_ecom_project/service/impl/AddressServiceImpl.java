package com.project.Springboot_ecom_project.service.impl;

import com.project.Springboot_ecom_project.exception.APIException;
import com.project.Springboot_ecom_project.exception.ResourceNotFound;
import com.project.Springboot_ecom_project.model.Address;
import com.project.Springboot_ecom_project.model.User;
import com.project.Springboot_ecom_project.payload.AddressDTO;
import com.project.Springboot_ecom_project.payload.AddressResponse;
import com.project.Springboot_ecom_project.repository.AddressRepository;
import com.project.Springboot_ecom_project.repository.UserRepository;
import com.project.Springboot_ecom_project.service.AddressService;
import com.project.Springboot_ecom_project.utils.AuthUtil;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthUtil authUtil;

    @Override
    @Transactional
    public AddressDTO createAddress(AddressDTO addressDTO) {
        User user = authUtil.loggedInUser();
        if (user == null) {
            throw new APIException("You need to log in first!");
        }
        //if user logged in, connect user with address
        Address address = modelMapper.map(addressDTO, Address.class);
        address.setUser(user);
        //update user addressList
        if (user.getAddresses() == null) {
            user.setAddresses(new ArrayList<>());
        }
        user.getAddresses().add(address);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public AddressResponse getAddresses(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);
        Page<Address> addressPage =addressRepository.findAll(pageable);
        List<Address> addressList = addressPage.getContent();

        List<AddressDTO> addressDTOList = addressList.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
        return new AddressResponse(addressDTOList,addressPage.getNumber(),addressPage.getSize(),addressPage.getTotalElements(),addressPage.getTotalPages(),addressPage.isLast());
    }

    @Override
    public AddressDTO getAddressesById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFound("Address", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressResponse getUserAddresses(Integer pageNumber,Integer pageSize,String sortOrder,String sortBy) {
        User user = authUtil.loggedInUser();
        if (user == null) {
            throw new APIException("You need to log in first!");
        }

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sortByAndOrder);

        Page<Address> addressPage = addressRepository.findByUserId(user.getUserId(),pageable);
        List<Address> addressList = addressPage.getContent();

        List<AddressDTO> addressDTOList = addressList.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class))
                .collect(Collectors.toList());

       return new AddressResponse(addressDTOList,addressPage.getNumber(),addressPage.getSize(),addressPage.getTotalElements(),addressPage.getTotalPages(),addressPage.isLast());

    }

    @Override
    @Transactional
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFound("Address", addressId));

        if (addressDTO.getAddressName() != null&& !addressDTO.getAddressName().isEmpty()){
            addressFromDatabase.setAddressName(addressDTO.getAddressName());
        }
        if (addressDTO.getEirCode() != null&& !addressDTO.getEirCode().isEmpty()) {
            addressFromDatabase.setEirCode(addressDTO.getEirCode());
        }

        Address updatedAddress = addressRepository.save(addressFromDatabase);
        return modelMapper.map(updatedAddress, AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDatabase = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFound("Address",  addressId));
        User user = addressFromDatabase.getUser();
        user.getAddresses().remove(addressFromDatabase);
        addressRepository.delete(addressFromDatabase);
        return String.format("Address deleted successfully with addressId: %d", addressId);
    }
}
