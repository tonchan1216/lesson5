package com.example.lesson5.bff.app.model;

import com.example.lesson5.common.web.model.EmailResource;

import java.util.List;
import java.util.stream.Collectors;

public interface EmailMapper {

    public static Email map(EmailResource emailResource){
        return Email.builder()
                .userId(emailResource.getUserId())
                .emailNo(emailResource.getEmailNo())
                .email(emailResource.getEmail())
                .build();
    }

    public static EmailResource mapToResoure(Email email){
        return EmailResource.builder()
                .userId(email.getUserId())
                .emailNo(email.getEmailNo())
                .email(email.getEmail())
                .build();
    }

    public static List<Email> map(List<EmailResource> emailResources){
        return emailResources.stream().map(EmailMapper::map).collect(Collectors.toList());
    }

    public static List<EmailResource> mapToResource(List<Email> emails){
        return emails.stream().map(EmailMapper::mapToResoure).collect(Collectors.toList());
    }

}
