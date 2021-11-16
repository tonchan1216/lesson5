package com.example.lesson5.bff.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class User implements Serializable {

    @Min(0)
    private long userId;
    @NotBlank
    @Size(min = 1, max = 50)
    @Pattern(regexp="^[^=#$%&./<>?^~\\[\\]()]+$")
    private String firstName;
    @NotBlank
    @Size(min = 1, max = 50)
    @Pattern(regexp="^[^=#$%&./<>?^~\\[\\]()]+$")
    private String familyName;
    @NotBlank
    @Size(min = 1, max = 32)
    @Pattern(regexp = "[a-zA-Z0-9.\\-]*")
    private String loginId;
    private boolean isLogin;
    @Valid
    private Address address;
    @Valid
    private List<Email> emailList;

}
