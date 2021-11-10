package com.example.lesson5.backend.app.model;

import com.example.lesson5.common.web.model.GroupResource;

import java.util.List;
import java.util.stream.Collectors;

public interface GroupMapper {

    public static GroupResource map(Group group){
        return GroupResource.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .build();
    }

    public static Group mapToEntity(
            com.example.lesson5.backend.app.model.Group group){
        return Group.builder()
                .groupId(group.getGroupId())
                .groupName(group.getGroupName())
                .build();
    }

    public static List<GroupResource> map(List<Group> groups){
        return groups.stream().map(GroupMapper::map).collect(Collectors.toList());
    }

}
