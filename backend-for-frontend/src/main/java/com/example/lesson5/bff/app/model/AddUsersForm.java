package com.example.lesson5.bff.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AddUsersForm implements Serializable {

    @Valid
    private List<User> users;

}
