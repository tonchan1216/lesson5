package com.example.lesson5.backend.app.model;

import com.example.lesson5.backend.domain.model.entity.Email;
import com.example.lesson5.common.web.model.EmailResource;

import java.util.List;
import java.util.stream.Collectors;

public interface EmailMapper {

    public static EmailResource map(Email email){
        return EmailResource.builder()
                .userId(email.getUserId())
                .emailNo(email.getEmailNo())
                .email(email.getEmail())
                .build();
    }

    public static Email mapToEntity(
            com.example.lesson5.backend.app.model.Email email){
        return Email.builder()
                .userId(email.getUserId())
                .emailNo(email.getEmailNo())
                .email(email.getEmail())
                .build();
    }

    public static List<EmailResource> map(List<Email> emailList){
        return emailList.stream().map(EmailMapper::map)
                .collect(Collectors.toList());
    }

}
