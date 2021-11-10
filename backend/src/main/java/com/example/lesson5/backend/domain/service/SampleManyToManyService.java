package com.example.lesson5.backend.domain.service;

import com.example.lesson5.backend.domain.model.entity.Group;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.common.apinfra.exception.BusinessException;

import java.util.List;

public interface SampleManyToManyService {

    public List<Group> getGroupsOf(User user);
    public List<User> getUsersOf(Group group);
    public List<User> getUsersOfNot(Group group);
    public User addUserTo(Group group, User addUser) throws BusinessException;
    public User deleteUserFrom(Group group, User deleteUser) throws BusinessException;
    public Group delete(Group group) throws BusinessException;
    public User delete(User user) throws BusinessException;

}
