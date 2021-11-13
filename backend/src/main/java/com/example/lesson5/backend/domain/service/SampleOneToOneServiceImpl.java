package com.example.lesson5.backend.domain.service;

import com.example.lesson5.backend.domain.model.entity.Address;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.repository.UserRepository;
import com.example.lesson5.backend.domain.repository.specification.FindUsersHavingAddressOfZipCode;
import com.example.lesson5.backend.domain.repository.specification.FindUsersNotHavingAddressOfZipCode;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.apinfra.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Transactional
@Service
public class SampleOneToOneServiceImpl implements SampleOneToOneService{

    @Autowired
    MessageSource messageSource;

    @Autowired
    UserRepository userRepository;


    @Override
    public Address findAddressOf(User user) throws BusinessException {
        Optional<User> optionalUser = userRepository.findById(user.getUserId());
        if(optionalUser.isPresent()){
            return optionalUser.get().getAddressByUserId();
        }else {
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{user.getUserId()}, Locale.getDefault()));
        }
    }

    @Override
    public List<User> findUsersHavingAddressOf(String zipCode) {
        return userRepository.findAll(
                FindUsersHavingAddressOfZipCode.builder().zipCd(zipCode).build());
    }

    @Override
    public List<User> findUsersNotHavingAddressOf(String zipCode) {
        return userRepository.findAll(
                FindUsersNotHavingAddressOfZipCode.builder().zipCd(zipCode).build());
    }

    @Override
    public Address update(Address address) throws BusinessException{
        Optional<User> optionalUser = userRepository.findById(address.getUserId());
        if(optionalUser.isPresent()){
            User updateUser = optionalUser.get();
            Address updateAddress = updateUser.getAddressByUserId();
            if(Objects.nonNull(address.getZipCode())){
                updateAddress.setZipCode(address.getZipCode());
            }
            if(Objects.nonNull(address.getAddress())){
                updateAddress.setAddress(address.getAddress());
            }
            updateAddress.setLastUpdatedAt(DateUtil.now());
            return userRepository.save(updateUser).getAddressByUserId();
        }else {
            String errorCode = "E0001";
            throw new BusinessException(errorCode, messageSource.getMessage(
                    errorCode, new Long[]{address.getUserId()}, Locale.getDefault()));
        }
    }

}
