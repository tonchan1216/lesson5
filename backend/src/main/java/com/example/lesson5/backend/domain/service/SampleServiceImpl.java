package com.example.lesson5.backend.domain.service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.example.lesson5.backend.domain.repository.UserRepository;
import com.example.lesson5.backend.domain.model.entity.Membership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lesson5.backend.domain.model.entity.Address;
import com.example.lesson5.backend.domain.model.entity.Email;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.apinfra.util.DateUtil;

@Transactional
@Service
public class SampleServiceImpl implements SampleService{

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRepository userRepository;

    @Override
    public User findOne(User user) throws BusinessException{
        Optional<User> userOptional = userRepository.findById(user.getUserId());
        if(userOptional.isPresent()){
            return userOptional.get();
        }else {
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{user.getUserId()}, Locale.getDefault()), user.getUserId());
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User add(User user) throws BusinessException {
        if(!userRepository.existsByLoginId(user.getLoginId())){
            Long newUserId = userRepository.getMaxUserId() + 1;
            user.setUserId(newUserId);
            user.setVer(0);
            user.setLastUpdatedAt(DateUtil.now());
            Address addAddress = user.getAddressByUserId();
            if(Objects.nonNull(addAddress)){
                addAddress.setUserId(newUserId);
                addAddress.setVer(0);
                addAddress.setLastUpdatedAt(DateUtil.now());
            }
            if(Objects.nonNull(user.getEmailsByUserId())){
                List<Email> addEmails = user.getEmailsByUserId()
                        .stream().collect(Collectors.toList());
                AtomicLong i = new AtomicLong();
                addEmails.stream().forEach(
                        email -> {
                            email.setUserId(newUserId);
                            email.setEmailNo(i.getAndIncrement());
                            email.setVer(0);
                            email.setLastUpdatedAt(DateUtil.now());
                        }
                );
            }
            if(Objects.nonNull(user.getMembershipsByUserId())){
                List<Membership> addMemberships = user.getMembershipsByUserId()
                        .stream().collect(Collectors.toList());
                addMemberships.stream().forEach(
                        membership -> {
                            membership.setUserId(newUserId);
                            membership.setVer(0);
                            membership.setLastUpdatedAt(DateUtil.now());
                        }
                );
            }
        }else{
            String errorCode = "E0002";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new String[]{user.getLoginId()}, Locale.getDefault()), user.getLoginId());

        }
        return userRepository.save(user);
    }

    @Override
    public User update(User user) throws BusinessException {
        Optional<User> optionalUser = userRepository.findById(user.getUserId());
        if(optionalUser.isPresent()){
            User updateUser = optionalUser.get();
            if (Objects.nonNull(user.getFirstName())){
                updateUser.setFirstName(user.getFirstName());
            }
            if (Objects.nonNull(user.getFamilyName())){
                updateUser.setFamilyName(user.getFamilyName());
            }
            if (Objects.nonNull(user.getLoginId())){
                if(!userRepository.existsByLoginId(user.getLoginId())){
                    updateUser.setLoginId(user.getLoginId());
                }else {
                    String errorCode = "E0002";
                    throw new BusinessException(errorCode, messageSource.getMessage(
                            errorCode, new String[]{user.getLoginId()}, Locale.getDefault()), user.getLoginId());
                }
            }
            Address address = user.getAddressByUserId();
            if(Objects.nonNull(address)){
                if(Objects.nonNull(address.getZipCode())){
                    updateUser.getAddressByUserId().setZipCode(address.getZipCode());
                }
                if(Objects.nonNull(address.getAddress())){
                    updateUser.getAddressByUserId().setAddress(address.getAddress());
                }
            }
            Collection<Email> emails = user.getEmailsByUserId();
            if(Objects.nonNull(emails)){
                emails.stream().forEach(email1 -> updateUser.getEmailsByUserId().stream()
                .filter(email2 -> email2.getEmailNo() == email1.getEmailNo())
                .findFirst().get().setEmail(email1.getEmail())
                );
            }
            updateUser.setLastUpdatedAt(DateUtil.now());
            return userRepository.save(updateUser);
        }else{
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{user.getUserId()}, Locale.getDefault()), user.getUserId());
        }
    }

    @Override
    public User delete(User user) throws BusinessException {
        Optional<User> optionalUser = userRepository.findById(user.getUserId());
        if(optionalUser.isPresent()){
            User deleteUser = optionalUser.get();
            userRepository.delete(deleteUser);
            return deleteUser;
        }else {
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{user.getUserId()}, Locale.getDefault()), user.getUserId());
        }
    }

    @Override
    public User findUserHave(String loginId) throws BusinessException {
        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isPresent()){
            return optionalUser.get();
        }else {
            String errorCode = "E0008";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new String[]{loginId}, Locale.getDefault()), loginId);
        }
    }
}
