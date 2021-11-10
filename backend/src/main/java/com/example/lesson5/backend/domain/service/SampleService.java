package com.example.lesson5.backend.domain.service;

import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.common.apinfra.exception.BusinessException;

import java.util.List;

public interface SampleService {

    public User findOne(User user) throws BusinessException;
    public List<User> findAll();
    public User add(User user) throws BusinessException;
    public User update(User user) throws BusinessException;
    public User delete(User user) throws BusinessException;
    public User findUserHave(String loginId) throws BusinessException;

}
