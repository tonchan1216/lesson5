package com.example.lesson5.backend.domain.repository;

import com.example.lesson5.backend.domain.model.entity.Group;
import com.example.lesson5.backend.domain.model.entity.Membership;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.repository.specification.FindGroupsByUserId;
import com.example.lesson5.common.apinfra.test.junit.UnitTest;
import com.example.lesson5.common.apinfra.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@Slf4j
@Category(UnitTest.class)
@RunWith(SpringRunner.class)
@DataJpaTest
public class GroupRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    GroupRepository groupRepository;

    @Before
    public void before(){
        long userIdA = 0;
        long userIdB = 1;
        long groupIdA = 0;
        long groupIdB = 1;
        testEntityManager.persist(
                Group.builder()
                        .groupId(groupIdA)
                        .groupName("GroupA")
                        .ver(0)
                        .lastUpdatedAt(DateUtil.now())
                        .build());
        testEntityManager.persist(
                Group.builder()
                        .groupId(groupIdB)
                        .groupName("GroupB")
                        .ver(0)
                        .lastUpdatedAt(DateUtil.now())
                        .build());
        Membership membership1 = Membership.builder()
                .userId(userIdA)
                .groupId(groupIdA)
                .ver(0)
                .lastUpdatedAt(DateUtil.now())
                .build();
        Membership membership2 = Membership.builder()
                .userId(userIdB)
                .groupId(groupIdA)
                .ver(0)
                .lastUpdatedAt(DateUtil.now())
                .build();
        Membership membership3 = Membership.builder()
                .userId(userIdA)
                .groupId(groupIdB)
                .ver(0)
                .lastUpdatedAt(DateUtil.now())
                .build();
        testEntityManager.persist(
                User.builder()
                        .userId(userIdA)
                        .membershipsByUserId(
                                Arrays.asList(membership1, membership3))
                        .build());
        testEntityManager.persist(
                User.builder()
                        .userId(userIdB)
                        .membershipsByUserId(
                                List.of(membership2))
                        .build());
    }

    @Test
    public void testFindByGroupName(){
        Group group = groupRepository.findByGroupName("GroupA");
        assertThat(group.getGroupId(), is(0L));
    }

    @Test
    public void testFindGroupsByUserId(){
        List<Group> groupsOfUser = groupRepository.findAll(
                FindGroupsByUserId.builder()
                        .userId(0L)
                        .build());
        assertThat(groupsOfUser.size(), is(2));
        groupsOfUser.forEach(
                group -> assertThat(group.getGroupId(), either(is(0L)).or(is(1L)))
        );
    }
}
