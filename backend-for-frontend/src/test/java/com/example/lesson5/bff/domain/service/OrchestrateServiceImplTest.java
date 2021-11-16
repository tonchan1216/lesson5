package com.example.lesson5.bff.domain.service;

import com.example.lesson5.bff.domain.repository.UserResourceRepository;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.apinfra.exception.SystemException;
import com.example.lesson5.common.web.model.AddressResource;
import com.example.lesson5.common.web.model.EmailResource;
import com.example.lesson5.common.web.model.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class OrchestrateServiceImplTest {
    @Configuration
    public static class Config{

        @Bean
        ObjectMapper testObjectMapper(){
            return new ObjectMapper();
        }

        @Bean
        OrchestrateService orchestrateService(){
            return new OrchestrateServiceImpl();
        }
    }


    @Nested
    @ExtendWith(SpringExtension.class)
    @SpringBootTest(classes = {
            OrchestrateServiceImplTest.Config.class
    }, webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @Tag("UnitTest")
    public class UnitTest{

        @Autowired
        ObjectMapper objectMapper;

        @MockBean
        UserResourceRepository userResourceRepositoryMock;

        @Autowired
        OrchestrateService orchestrateService;

        @BeforeEach
        public void setUp() throws Exception{

            long userId1 = 0;
            long userId2 = 1;
            long userId3 = 2;
            AddressResource mockAddress1 = AddressResource.builder()
                        .userId(userId1)
                        .zipCode("000-0000")
                        .address("Tokyo Chiyoda")
                        .build();
            EmailResource mockEmail1 = EmailResource.builder()
                        .userId(userId1)
                        .emailNo(0)
                        .email("taro.mynavi@debugroom.org")
                        .build();
            EmailResource mockEmail2 = EmailResource.builder()
                        .userId(userId1)
                        .emailNo(1)
                        .email("taro.mynavi@test.org")
                        .build();
            UserResource mockUser1 = UserResource.builder()
                        .userId(userId1)
                        .firstName("taro")
                        .familyName("mynavi")
                        .loginId("taro.mynavi")
                        .address(mockAddress1)
                        .emailList(Arrays.asList(mockEmail1, mockEmail2))
                        .build();
            AddressResource mockAddress2 = AddressResource.builder()
                        .userId(userId2)
                        .zipCode("111-1111")
                        .address("Tonde Saitama")
                        .build();
            EmailResource mockEmail3 = EmailResource.builder()
                        .userId(userId2)
                        .emailNo(0)
                        .email("hanako.mynavi@debugroom.org")
                        .build();
            UserResource mockUser2 = UserResource.builder()
                        .userId(userId2)
                        .familyName("mynavi")
                        .firstName("hanako")
                        .loginId("hanako.mynavi")
                        .address(mockAddress2)
                        .emailList(List.of(mockEmail3))
                        .build();
            AddressResource mockAddress3 = AddressResource.builder()
                    .userId(userId3)
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .build();
            EmailResource mockEmail4 = EmailResource.builder()
                    .userId(userId3)
                    .emailNo(0)
                    .email("hanako.mynavi@debugroom.org")
                    .build();
            UserResource mockUser3 = UserResource.builder()
                    .userId(userId3)
                    .familyName("mynavi")
                    .firstName("hanako")
                    .loginId("hanako.mynavi")
                    .address(mockAddress3)
                    .emailList(List.of(mockEmail4))
                    .build();
            when(userResourceRepositoryMock.findOne(userId1)).thenReturn(mockUser1);
            when(userResourceRepositoryMock.findOne(userId2)).thenReturn(mockUser2);
            when(userResourceRepositoryMock.save(mockUser1)).thenReturn(mockUser1);
            when(userResourceRepositoryMock.save(mockUser2)).thenThrow(BusinessException.class);
            when(userResourceRepositoryMock.save(mockUser3)).thenReturn(mockUser3);
            when(userResourceRepositoryMock.delete(userId1)).thenReturn(mockUser1);
            when(userResourceRepositoryMock.delete(userId3)).thenThrow(SystemException.class);

        }

        @Test
        public void addUsersAbnormalTest1() {

            long userId1 = 0;
            long userId2 = 1;
            AddressResource address1 = AddressResource.builder()
                    .userId(userId1)
                    .zipCode("000-0000")
                    .address("Tokyo Chiyoda")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(userId1)
                    .emailNo(0)
                    .email("taro.mynavi@debugroom.org")
                    .build();
            EmailResource email2 = EmailResource.builder()
                    .userId(userId1)
                    .emailNo(1)
                    .email("taro.mynavi@test.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(userId1)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .address(address1)
                    .emailList(Arrays.asList(email1, email2))
                    .build();
            AddressResource address2 = AddressResource.builder()
                    .userId(userId2)
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .build();
            EmailResource email3 = EmailResource.builder()
                    .userId(userId2)
                    .emailNo(0)
                    .email("hanako.mynavi@debugroom.org")
                    .build();
            UserResource user2 = UserResource.builder()
                    .userId(userId2)
                    .familyName("mynavi")
                    .firstName("hanako")
                    .loginId("hanako.mynavi")
                    .address(address2)
                    .emailList(List.of(email3))
                    .build();

            assertThrows(
                    BusinessException.class, () -> orchestrateService.addUsers(Arrays.asList(
                            user1, user2))
            );
        }

        @Test
        public void addUsersAbnormalTest2() {


            long userId3 = 2;
            long userId2 = 1;
            AddressResource address1 = AddressResource.builder()
                    .userId(userId3)
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .build();
            EmailResource email1 = EmailResource.builder()
                    .userId(userId3)
                    .emailNo(0)
                    .email("hanako.mynavi@debugroom.org")
                    .build();
            UserResource user1 = UserResource.builder()
                    .userId(userId3)
                    .firstName("hanako")
                    .familyName("mynavi")
                    .loginId("hanako.mynavi")
                    .address(address1)
                    .emailList(List.of(email1))
                    .build();
            AddressResource address2 = AddressResource.builder()
                    .userId(userId2)
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .build();
            EmailResource email3 = EmailResource.builder()
                    .userId(userId2)
                    .emailNo(0)
                    .email("hanako.mynavi@debugroom.org")
                    .build();
            UserResource user2 = UserResource.builder()
                    .userId(userId2)
                    .familyName("mynavi")
                    .firstName("hanako")
                    .loginId("hanako.mynavi")
                    .address(address2)
                    .emailList(List.of(email3))
                    .build();

            assertThrows(
                    SystemException.class, () -> orchestrateService.addUsers(Arrays.asList(
                            user1, user2))
            );
        }


    }
}
