package com.example.lesson5.backend.domain.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.lesson5.backend.domain.model.entity.Email;
import com.example.lesson5.backend.domain.model.entity.EmailPK;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.repository.EmailRepository;
import com.example.lesson5.backend.domain.repository.UserRepository;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.apinfra.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SampleOneToManyServiceImpl implements SampleOneToManyService{

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailRepository emailRepository;

    @Override
    public List<Email> getEmailsOf(User user) throws BusinessException {
        Optional<User> optionalUser = userRepository.findById(user.getUserId());
        if(optionalUser.isPresent()){
            return optionalUser.get().getEmailsByUserId().stream().collect(Collectors.toList());
        }else{
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{user.getUserId()}, Locale.getDefault()));
        }
    }

    @Override
    public User findUserHaving(String email) {
        return emailRepository.findByEmail(email).getUsrByUserId();
    }

    @Override
    public Email add(Email email) throws BusinessException{
        Optional<User> optionalUser = userRepository.findById(email.getUserId());
        if(optionalUser.isPresent()){
            Collection<Email> emails = optionalUser.get().getEmailsByUserId();
            email.setEmailNo(
                    emails.stream().sorted(Comparator.comparing(Email::getEmailNo).reversed())
                            .findFirst().get().getEmailNo()+1);
            email.setVer(0);
            email.setLastUpdatedAt(DateUtil.now());
            emails.add(email);
            return email;
        }else {
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{email.getUserId()}, Locale.getDefault()));
        }
    }

    @Override
    public Email update(Email email) throws BusinessException{
        Optional<User> optionalUser = userRepository.findById(email.getUserId());
        if(optionalUser.isPresent()){
            Email updateEmail = optionalUser.get().getEmailsByUserId().stream().filter(
                email1 -> email1.getEmailNo() == email.getEmailNo()).findFirst().get();
        if(Objects.nonNull(email.getEmail())){
            updateEmail.setEmail(email.getEmail());
            updateEmail.setLastUpdatedAt(DateUtil.now());
        }
        return updateEmail;
        } else {
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{email.getUserId()}, Locale.getDefault()));
        }
    }

    @Override
    public Email delete(Email email) throws BusinessException {
        Email deleteEmail = null;
        if(Objects.isNull(email.getEmail())){
            Optional<Email> optionalEmail = emailRepository.findById(
                    EmailPK.builder()
                            .userId(email.getUserId())
                            .emailNo(email.getEmailNo())
                            .build());
            if(optionalEmail.isPresent()){
                deleteEmail = optionalEmail.get();
            }else {
                String errorCode = "E0004";
                throw new BusinessException(errorCode, messageSource.getMessage(
                        errorCode, new String[]{Long.toString(email.getUserId()), email.getEmail()},
                        Locale.getDefault()));
            }
        }else {
            deleteEmail = emailRepository.findByEmail(email.getEmail());
            if(Objects.isNull(deleteEmail)){
                String errorCode = "E0003";
                throw new BusinessException(errorCode, messageSource.getMessage(
                        errorCode, new String[]{email.getEmail()},
                        Locale.getDefault()));
            }
        }
        emailRepository.delete(deleteEmail);
        return deleteEmail;
    }

    @Override
    public List<Email> deleteAllEmail(User user) throws BusinessException{
        Optional<User> optionalUser = userRepository.findById(user.getUserId());
        if(optionalUser.isPresent()){
            User deleteUser = optionalUser.get();
            List<Email> deleteEmails = deleteUser.getEmailsByUserId()
                .stream().collect(Collectors.toList());
            deleteUser.getEmailsByUserId().clear();
            return deleteEmails;
        }else {
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{user.getUserId()}, Locale.getDefault()));
        }
    }

}
