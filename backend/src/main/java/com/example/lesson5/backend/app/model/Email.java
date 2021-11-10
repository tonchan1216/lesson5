package com.example.lesson5.backend.app.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Email implements Serializable {

    public static interface FindUserHave{}
    public static interface Add{}
    public static interface Update{}

    @Min(0)
    private long userId;
    @Min(0)
    private long emailNo;
    @NotNull(groups = {User.AddUser.class,
            FindUserHave.class, Add.class, Update.class})
    @javax.validation.constraints.Email
    @Size(max = 256)
    private String email;

}
