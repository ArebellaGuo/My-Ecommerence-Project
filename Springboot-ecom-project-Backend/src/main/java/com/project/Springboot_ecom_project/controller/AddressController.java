package com.project.Springboot_ecom_project.controller;

import com.project.Springboot_ecom_project.config.PageInstants;
import com.project.Springboot_ecom_project.payload.AddressDTO;
import com.project.Springboot_ecom_project.payload.AddressResponse;
import com.project.Springboot_ecom_project.service.AddressService;
import com.project.Springboot_ecom_project.utils.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    AuthUtil authUtil;

    @Autowired
    AddressService addressService;

    @PostMapping("/user/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO){
        AddressDTO savedAddressDTO = addressService.createAddress(addressDTO);
        return new ResponseEntity<>(savedAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/admin/addresses")
    public ResponseEntity<AddressResponse> getAllAddresses(@RequestParam(defaultValue = PageInstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                           @RequestParam(defaultValue = PageInstants.PAGE_SIZE,required = false) Integer pageSize,
                                                           @RequestParam(defaultValue = PageInstants.SORT_ORDER,required = false) String sortOrder,
                                                           @RequestParam(defaultValue = PageInstants.SORT_ADDRESS_BY,required = false) String sortBy){
        AddressResponse addressResponse = addressService.getAddresses(pageNumber,pageSize,sortOrder,sortBy);
        return new ResponseEntity<>(addressResponse, HttpStatus.OK);
    }

    @GetMapping("/user/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO = addressService.getAddressesById(addressId);
        return new ResponseEntity<>(addressDTO, HttpStatus.OK);
    }


    @GetMapping("/user/current/addresses")
    public ResponseEntity<AddressResponse> getUserAddresses(@RequestParam(defaultValue = PageInstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                             @RequestParam(defaultValue = PageInstants.PAGE_SIZE,required = false) Integer pageSize,
                                                             @RequestParam(defaultValue = PageInstants.SORT_ORDER,required = false) String sortOrder,
                                                             @RequestParam(defaultValue = PageInstants.SORT_ADDRESS_BY,required = false) String sortBy){
        AddressResponse addressResponse = addressService.getUserAddresses(pageNumber,pageSize,sortOrder,sortBy);
        return new ResponseEntity<>(addressResponse, HttpStatus.OK);
    }

    @PutMapping("/user/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId
            , @RequestBody AddressDTO addressDTO){
        AddressDTO updatedAddress = addressService.updateAddress(addressId, addressDTO);
        return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
    }

    @DeleteMapping("/user/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId){
        String status = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

}
