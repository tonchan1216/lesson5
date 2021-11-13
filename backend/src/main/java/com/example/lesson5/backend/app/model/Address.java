package com.example.lesson5.backend.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Address implements Serializable {

    @Min(0)
    private long userId;

    @NotNull(groups = {User.AddUser.class})
    @Pattern(regexp = "[0-9\\-]*")
    private String zipCode;

    @NotNull(groups = {User.AddUser.class})
    @Pattern(regexp="^[^=#$%&./<>?^~\\[\\]()]+$")
    private String address;
}
