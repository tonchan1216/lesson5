package com.example.lesson5.bff.domain.service;

import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.web.model.UserResource;

import java.util.List;

public interface OrchestrateService {

    public List<UserResource> addUsers(List<UserResource> addUsers) throws BusinessException;

}
