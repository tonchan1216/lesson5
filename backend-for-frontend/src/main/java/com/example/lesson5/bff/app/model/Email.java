package com.example.lesson5.bff.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Email implements Serializable {

    @Min(0)
    private long userId;
    @Min(0)
    private long emailNo;

    @NotBlank
    @javax.validation.constraints.Email
    @Size(max = 256)
    private String email;

}
