package com.example.lesson5.bff.domain.service;

import com.example.lesson5.bff.domain.repository.UserResourceRepository;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.web.model.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrchestrateServiceImpl implements OrchestrateService {

    @Autowired
    UserResourceRepository userResourceRepository;

    @Override
    public List<UserResource> addUsers(List<UserResource> addUserResources)
            throws BusinessException{
        List<UserResource> userResources = new ArrayList<>();
        for (UserResource addUserResource : addUserResources){
            try{
                userResources.add(userResourceRepository.save(addUserResource));
            }catch (BusinessException e){
                // Rollback for SAGA Pattern.
                for(UserResource userResource : userResources){
                    userResourceRepository.delete(userResource.getUserId());
                }
                throw e;
            }
        }
        return userResources;
    }

}
