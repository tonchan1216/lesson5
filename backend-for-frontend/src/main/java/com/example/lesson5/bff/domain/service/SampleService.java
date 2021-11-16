package com.example.lesson5.bff.domain.service;

import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.web.model.UserResource;

import java.util.List;

public interface SampleService {

    public UserResource getUser(Long userId) throws BusinessException;
    public boolean existsUserOfLoginId(String loginId);
    public List<UserResource> getUsers();

}
