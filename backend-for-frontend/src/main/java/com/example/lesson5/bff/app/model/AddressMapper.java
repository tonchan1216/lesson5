package com.example.lesson5.bff.app.model;

import com.example.lesson5.common.web.model.AddressResource;

public interface AddressMapper {

    public static Address map(AddressResource addressResource){
        return Address.builder()
                .userId(addressResource.getUserId())
                .zipCode(addressResource.getZipCode())
                .address(addressResource.getAddress())
                .build();
    }

    public static AddressResource mapToResource(Address address){
        return AddressResource.builder()
                .userId(address.getUserId())
                .zipCode(address.getZipCode())
                .address(address.getAddress())
                .build();
    }

}
