package com.example.lesson5.backend.domain.service;

import com.example.lesson5.backend.domain.model.entity.Address;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.common.apinfra.exception.BusinessException;

import java.util.List;

public interface SampleOneToOneService {

    public Address findAddressOf(User user) throws BusinessException;
    public List<User> findUsersHavingAddressOf(String zipCode);
    public List<User> findUsersNotHavingAddressOf(String zipCode);
    public Address update(Address address) throws BusinessException;

}
