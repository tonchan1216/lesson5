package com.example.lesson5.bff.app.model;

import com.example.lesson5.common.web.model.UserResource;

import java.util.List;
import java.util.stream.Collectors;

public interface UserMapper {

    public static User map(UserResource userResource){
        return User.builder()
                .userId(userResource.getUserId())
                .firstName(userResource.getFirstName())
                .familyName(userResource.getFamilyName())
                .loginId(userResource.getLoginId())
                .isLogin(userResource.isLogin())
                .address(AddressMapper.map(userResource.getAddress()))
                .emailList(EmailMapper.map(userResource.getEmailList()))
                .build();
    }

    public static UserResource mapToResoure(User user){
        return UserResource.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .familyName(user.getFamilyName())
                .loginId(user.getLoginId())
                .isLogin(user.isLogin())
                .address(AddressMapper.mapToResource(user.getAddress()))
                .emailList(EmailMapper.mapToResource(user.getEmailList()))
                .build();
    }

    public static List<User> map(List<UserResource> userResources){
        return userResources.stream().map(UserMapper::map).collect(Collectors.toList());
    }

    public static List<UserResource> mapToResource(List<User> user){
        return user.stream().map(UserMapper::mapToResoure).collect(Collectors.toList());
    }

}
