package com.example.lesson5.bff.domain.service;

import com.example.lesson5.bff.domain.repository.UserResourceRepository;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.web.model.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SampleServiceImpl implements SampleService{

    @Autowired
    UserResourceRepository userResourceRepository;

    @Override
    public UserResource getUser(Long userId) throws BusinessException {
        return userResourceRepository.findOne(userId);
    }

    @Override
    public List<UserResource> getUsers() {
        return userResourceRepository.findAll();
    }

    @Override
    public boolean existsUserOfLoginId(String loginId){
        try{
            userResourceRepository.findByLoginId(loginId);
        }catch (BusinessException e){
            return false;
        }
        return true;
    }

}
