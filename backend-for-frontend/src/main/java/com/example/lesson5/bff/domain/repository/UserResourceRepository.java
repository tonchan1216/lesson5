package com.example.lesson5.bff.domain.repository;

import java.util.List;

import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.web.model.UserResource;

public interface UserResourceRepository {

    public UserResource findOne(Long userId) throws BusinessException;
    public List<UserResource> findAll();
    public UserResource save(UserResource userResource) throws BusinessException;
    public UserResource delete(Long userId) throws BusinessException;
    public UserResource findByLoginId(String loginId) throws BusinessException;

}
