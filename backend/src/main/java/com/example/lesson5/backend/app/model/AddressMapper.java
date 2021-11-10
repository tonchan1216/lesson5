package com.example.lesson5.backend.app.model;

import com.example.lesson5.backend.domain.model.entity.Address;
import com.example.lesson5.common.web.model.AddressResource;

public interface AddressMapper {

    public static AddressResource map(com.example.lesson5.backend.domain.model.entity.Address address){
        return AddressResource.builder()
                .userId(address.getUserId())
                .zipCode(address.getZipCode())
                .address(address.getAddress())
                .build();
    }

    public static com.example.lesson5.backend.domain.model.entity.Address mapToEntity(
            com.example.lesson5.backend.app.model.Address address){
        return Address.builder()
                .userId(address.getUserId())
                .zipCode(address.getZipCode())
                .address(address.getAddress())
                .build();
    }

}
