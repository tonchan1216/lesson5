package com.example.lesson5.common.web.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AddressResource implements Serializable {

    private long userId;
    private String zipCode;
    private String address;

}
