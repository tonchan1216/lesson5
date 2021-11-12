package com.example.lesson5.backend.domain.repository;

import com.example.lesson5.backend.domain.model.entity.Address;
import com.example.lesson5.backend.domain.model.entity.Group;
import com.example.lesson5.backend.domain.model.entity.Membership;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.repository.specification.FindUsersByGroup;
import com.example.lesson5.backend.domain.repository.specification.FindUsersByNotGroup;
import com.example.lesson5.backend.domain.repository.specification.FindUsersHavingAddressOfZipCode;
import com.example.lesson5.backend.domain.repository.specification.FindUsersNotHavingAddressOfZipCode;
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
import java.util.Optional;

import static org.hamcrest.CoreMatchers.either;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@Slf4j
@Category(UnitTest.class)
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UserRepository userRepository;

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
        Address address1 = Address.builder()
                .userId(userIdA)
                .zipCode("000-0000")
                .address("Tokyo Chiyoda")
                .ver(0)
                .lastUpdatedAt(DateUtil.now())
                .build();
        Address address2 = Address.builder()
                .userId(userIdB)
                .zipCode("300-0000")
                .address("Tonde Saitama")
                .ver(0)
                .lastUpdatedAt(DateUtil.now())
                .build();
        testEntityManager.persist(
                User.builder()
                        .userId(userIdA)
                        .firstName("taro")
                        .familyName("mynavi")
                        .loginId("taro.mynavi")
                        .addressByUserId(address1)
                        .membershipsByUserId(
                                Arrays.asList(membership1, membership3))
                        .ver(0)
                        .lastUpdatedAt(DateUtil.now())
                        .build());
        testEntityManager.persist(
                User.builder()
                        .userId(userIdB)
                        .firstName("hanako")
                        .familyName("mynavi")
                        .loginId("hanako.mynavi")
                        .addressByUserId(address2)
                        .membershipsByUserId(
                                List.of(membership2))
                        .ver(0)
                        .lastUpdatedAt(DateUtil.now())
                        .build());
    }

    @Test
    public void testFindByLoginIdNormalCase(){
        Optional<User> optionalUser = userRepository.findByLoginId("taro.mynavi");
        User user = optionalUser.get();
        assertThat(user.getUserId(), equalTo(0L));
        assertThat(user.getFirstName(), equalTo("taro"));
    }

    @Test
    public void testFindByLoginIdAbnormalCase(){
        Optional<User> optionalUser = userRepository.findByLoginId("jiro.mynavi");
        assertThat(optionalUser.isPresent(), equalTo(false));
    }

    @Test
    public void testExistsByLoginIdNormalCase(){
        assertThat(userRepository.existsByLoginId("taro.mynavi"), equalTo(true));
    }

    @Test
    public void testExistsByLoginIdAbnormalCase(){
        assertThat(userRepository.existsByLoginId("jiro.mynavi"), equalTo(false));
    }

    @Test
    public void testFindUsersHavingAddressOfZipCodeNormalCase(){
        List<User> users = userRepository.findAll(
                FindUsersHavingAddressOfZipCode.builder().zipCd("300-0000").build());
        User result = users.get(0);
        assertThat(result.getLoginId(), equalTo("hanako.mynavi"));
    }

    @Test
    public void testFindUsersNotHavingAddressOfZipCodeNormalCase(){
        List<User> users = userRepository.findAll(
                FindUsersNotHavingAddressOfZipCode.builder().zipCd("300-0000").build());
        User result = users.get(0);
        assertThat(result.getLoginId(), equalTo("taro.mynavi"));
    }

    @Test
    public void testFindUsersByGroupNormalCase1(){
        Group group = Group.builder().groupName("GroupA").build();
        List<User> users = userRepository.findAll(
                FindUsersByGroup.builder().group(group).build());
        assertThat(users.size(), equalTo(2));
        users.forEach(
                user -> assertThat(user.getUserId(), either(equalTo(0L)).or(equalTo(1L)))
        );
    }
    @Test
    public void testFindUsersByGroupNormalCase2(){
        Group group = Group.builder().groupId(0).build();
        List<User> users = userRepository.findAll(
                FindUsersByGroup.builder().group(group).build());
        assertThat(users.size(), equalTo(2));
        users.forEach(
                user -> assertThat(user.getUserId(), either(equalTo(0L)).or(equalTo(1L)))
        );
    }

    @Test
    public void testFindUsersByNotGroupNormalCase1(){
        Group group = Group.builder().groupName("GroupB").build();
        List<User> users = userRepository.findAll(
                FindUsersByNotGroup.builder().group(group).build());
        assertThat(users.size(), equalTo(1));
        users.forEach(
                user -> assertThat(user.getUserId(), equalTo(1L))
        );
    }

    @Test
    public void testFindUsersByNotGroupNormalCase2(){
        Group group = Group.builder().groupId(1).build();
        List<User> users = userRepository.findAll(
                FindUsersByNotGroup.builder().group(group).build());
        assertThat(users.size(), equalTo(1));
        users.forEach(
                user -> assertThat(user.getUserId(), equalTo(1L))
        );
    }

    @Test
    public void testGetMaxUserIdNormalCase(){
        assertThat(userRepository.getMaxUserId(), equalTo(1L));
    }

}
