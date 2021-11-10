package com.example.lesson5.common.web.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmailResource implements Serializable {

    private long userId;
    private long emailNo;
    private String email;

}
