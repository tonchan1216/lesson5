package com.example.lesson5.backend.domain.repository;

import com.example.lesson5.backend.domain.model.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupRepository extends JpaRepository<Group, Long>,
        JpaSpecificationExecutor<Group> {

    public Group findByGroupName(String groupName);

}
