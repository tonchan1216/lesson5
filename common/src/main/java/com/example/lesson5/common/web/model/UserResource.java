package com.example.lesson5.common.web.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserResource implements Serializable {

    private long userId;
    private String firstName;
    private String familyName;
    private String loginId;
    private boolean isLogin;
    private AddressResource address;
    private List<EmailResource> emailList;
    private List<GroupResource> groups;
    private LocalDateTime lastUpdatedAt;

}
