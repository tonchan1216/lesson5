package com.example.lesson5.common.web.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GroupResource implements Serializable {

    private long groupId;
    private String groupName;
    private List<UserResource> users;

}
