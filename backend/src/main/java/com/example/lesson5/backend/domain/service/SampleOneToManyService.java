package com.example.lesson5.backend.domain.service;

import com.example.lesson5.backend.domain.model.entity.Email;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.common.apinfra.exception.BusinessException;

import java.util.List;

public interface SampleOneToManyService {

    public List<Email> getEmailsOf(User user) throws BusinessException;
    public User findUserHaving(String email);
    public Email add(Email email) throws BusinessException;
    public Email update(Email email) throws BusinessException;
    public Email delete(Email email) throws BusinessException;
    public List<Email> deleteAllEmail(User user) throws BusinessException;

}
