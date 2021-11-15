package com.example.lesson5.backend.domain.service;

import com.example.lesson5.backend.config.TestConfig;
import com.example.lesson5.backend.domain.model.entity.Address;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.repository.UserRepository;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.apinfra.util.DateUtil;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.annotation.ExpectedDatabases;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class SampleOneToOneServiceImplTest {

    @Nested
    @ExtendWith(SpringExtension.class)
    @SpringBootTest(classes = {
            TestConfig.UnitTestConfig.class,
            SampleOneToOneServiceImplTest.UnitTest.Config.class,
    }, webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @Tag("UnitTest")
    public static class UnitTest{

        @Configuration
        public static class Config{
            @Bean
            SampleOneToOneService sampleOneToOneService(){
                return new SampleOneToOneServiceImpl();
            }
        }

        @MockBean
        UserRepository userRepositoryMock;

        @Autowired
        SampleOneToOneService sampleOneToOneService;

        @BeforeEach
        public void setUp(){
            long userId = 0;
            Address mockUpdatedAddress = Address.builder()
                    .userId(userId)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .ver(0)
                    .lastUpdatedAt(DateUtil.now())
                    .build();
            User mockUpdatedUser = User.builder()
                    .userId(userId)
                    .addressByUserId(mockUpdatedAddress)
                    .build();
            Address mockAddress = Address.builder()
                    .userId(userId)
                    .zipCode("000-0000")
                    .address("Tokyo Chiyoda")
                    .ver(0)
                    .lastUpdatedAt(DateUtil.now())
                    .build();
            User mockUser = User.builder()
                    .userId(userId)
                    .addressByUserId(mockAddress)
                    .build();
            when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userRepositoryMock.save(mockUser)).thenReturn(mockUpdatedUser);
        }

        @Test
        public void findAddressOfAbnormalTest() {
            User user = User.builder().userId(1).build();
            Throwable exception = assertThrows(
                    BusinessException.class, () -> sampleOneToOneService.findAddressOf(user)
            );
            assertThat(exception.getMessage(), is("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1"));
        }

        @Test
        public void updateNormalTest() throws BusinessException{
            Address updateAddress = Address.builder()
                    .userId(0)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();
            Address result = sampleOneToOneService.update(updateAddress);
            assertThat(result.getZipCode(), is("300-0000"));
            assertThat(result.getAddress(), is("Tonde Saitama"));
        }

        @Test
        public void updateAbnormalTest() {
            Address updateAddress = Address.builder()
                    .userId(1)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();
            Throwable exception = assertThrows(
                    BusinessException.class, () -> sampleOneToOneService.update(updateAddress)
            );
            assertThat(exception.getMessage(), is("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1"));
        }

    }

    @Nested
    @ExtendWith(SpringExtension.class)
    @SpringBootTest(classes = {
            TestConfig.ServiceTestConfig.class,
    }, webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
            DirtiesContextTestExecutionListener.class,
            TransactionalTestExecutionListener.class,
            DbUnitTestExecutionListener.class })
    @DbUnitConfiguration(dataSetLoader = IntegrationTest.CsvDataSetLoader.class)
    @ActiveProfiles("dev")
    @TestMethodOrder(MethodOrderer.MethodName.class)
    @Tag("IntegrationTest")
    public static class IntegrationTest{

        public static class CsvDataSetLoader extends AbstractDataSetLoader {
            @Override
            protected IDataSet createDataSet(Resource resource)
                    throws Exception {
                return new CsvDataSet(resource.getFile());
            }
        }

        @Autowired
        SampleOneToOneService sampleOneToOneService;

        @Test
        public void findAddressOfNormanlTest() throws BusinessException{
            User user = User.builder().userId(0).build();
            Address address = sampleOneToOneService.findAddressOf(user);
            assertThat(address.getZipCode(), is("000-0000"));
        }

        @Test
        public void findUserHavingAddressOfZipCodeNormalTest1(){
            List<User> users = sampleOneToOneService.findUsersHavingAddressOf("000-0000");
            assertThat(users.size(), is(1));
            users.forEach(user -> assertThat(user.getUserId(), is(0L)));
        }

        @Test
        public void findUserHavingAddressOfZipCodeNormalTest2(){
            List<User> users = sampleOneToOneService.findUsersHavingAddressOf("999-9999");
            assertThat(users.size(), is(0));
        }

        @Test
        public void findUserNotHavingAddressOfZipCodeNormalTest1(){
            List<User> users = sampleOneToOneService.findUsersNotHavingAddressOf("000-0000");
            assertThat(users.size(), is(2));
            users.forEach(user -> assertThat(user.getUserId(), either(is(1L)).or(is(2L))));
        }

        @Test
        public void findUserNotHavingAddressOfZipCodeNormalTest2(){
            List<User> users = sampleOneToOneService.findUsersNotHavingAddressOf("999-9999");
            assertThat(users.size(), is(3));
            users.forEach(user -> assertThat(user.getUserId(), either(is(1L)).or(is(0L)).or(is(2L))));
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleOneToOneServiceImplTest/update",
                        table = "address", assertionMode = DatabaseAssertionMode.NON_STRICT),
        })
        public void updateTestAbnormal() throws BusinessException{

            Address updateAddress = Address.builder()
                    .userId(0)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();

            sampleOneToOneService.update(updateAddress);

        }

    }

}
