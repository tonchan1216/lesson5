package com.example.lesson5.backend.domain.service;


import com.example.lesson5.backend.config.TestConfig;
import com.example.lesson5.backend.domain.model.entity.Address;
import com.example.lesson5.backend.domain.model.entity.Email;
import com.example.lesson5.backend.domain.model.entity.Membership;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.repository.GroupRepository;
import com.example.lesson5.backend.domain.repository.UserRepository;
import com.example.lesson5.common.apinfra.exception.BusinessException;
import com.example.lesson5.common.apinfra.util.DateUtil;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.annotation.ExpectedDatabases;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;
import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import lombok.extern.slf4j.Slf4j;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.csv.CsvDataSet;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.when;


@Slf4j
@RunWith(Enclosed.class)
public class SampleServiceImplTest {
    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = {
            TestConfig.UnitTestConfig.class,
            SampleServiceImplTest.UnitTest.Config.class,
    }, webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @Category(com.example.lesson5.common.apinfra.test.junit.UnitTest.class)
    public static class UnitTest{

        @Configuration
        public static class Config{

            @Bean
            SampleService sampleService(){
                return new SampleServiceImpl();
            }

        }

        @MockBean
        UserRepository userRepositoryMock;

        @MockBean
        GroupRepository groupRepositoryMock;

        @Autowired
        SampleService sampleService;

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Before
        public void setUp(){
            long userId = 0;
            Address mockAddress = Address.builder()
                    .userId(0)
                    .zipCode("000-0000")
                    .address("Tokyo Chiyoda")
                    .ver(0)
                    .lastUpdatedAt(DateUtil.now())
                    .build();
            Email mockEmail1 = Email.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("taro.mynavi@debugroom.org")
                    .build();
            Email mockEmail2 = Email.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("taro.mynavi@test.org")
                    .build();
            User mockUser = User.builder()
                    .userId(userId)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .addressByUserId(mockAddress)
                    .emailsByUserId(Arrays.asList(mockEmail1, mockEmail2))
                    .ver(0)
                    .build();
            Address updateAddress = Address.builder()
                    .userId(0)
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .build();
            Email updateEmail = Email.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("hanako.mynavi@debugroom.org")
                    .build();
            User updateUser = User.builder()
                    .userId(0L)
                    .familyName("mynavi")
                    .firstName("hanako")
                    .loginId("hanako.mynavi")
                    .addressByUserId(updateAddress)
                    .emailsByUserId(List.of(updateEmail))
                    .ver(0)
                    .build();
            Address addAddress = Address.builder()
                    .userId(2)
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .ver(0)
                    .build();
            Email addEmail1 = Email.builder()
                    .userId(2)
                    .emailNo(0)
                    .email("jiro.mynavi@debugroom.org")
                    .ver(0)
                    .build();
            Email addEmail2 = Email.builder()
                    .userId(2)
                    .emailNo(1)
                    .email("jiro.mynavi@test.org")
                    .ver(0)
                    .build();
            User addUser = User.builder()
                    .userId(2L)
                    .familyName("mynavi")
                    .firstName("jiro")
                    .loginId("jiro.mynavi")
                    .addressByUserId(updateAddress)
                    .emailsByUserId(Arrays.asList(addEmail1, addEmail2))
                    .ver(0)
                    .build();
            when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(mockUser));
            when(userRepositoryMock.existsByLoginId("hanako.mynavi")).thenReturn(false);
            when(userRepositoryMock.existsByLoginId("jiro.mynavi")).thenReturn(false);
            when(userRepositoryMock.existsByLoginId("taro.mynavi")).thenReturn(true);
            when(userRepositoryMock.save(addUser)).thenReturn(addUser);
            when(userRepositoryMock.save(updateUser)).thenReturn(updateUser);
            when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());
            when(userRepositoryMock.getMaxUserId()).thenReturn(Long.valueOf(1));
        }

        @Test
        public void findOneAbnormalTest() throws BusinessException {
            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1");
            User user = sampleService.findOne(
                    User.builder().userId(1L).build());
        }

        @Test
        public void addTestNormalCase() throws BusinessException{
            Address addAddress = Address.builder()
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .build();
            Email addEmail1 = Email.builder()
                    .emailNo(0)
                    .email("jiro.mynavi@debugroom.org")
                    .build();
            Email addEmail2 = Email.builder()
                    .emailNo(1)
                    .email("jiro.mynavi@test.org")
                    .build();
            Membership addMembership = Membership.builder()
                    .groupId(0)
                    .build();
            User addUser = User.builder()
                    .familyName("mynavi")
                    .firstName("jiro")
                    .loginId("jiro.mynavi")
                    .addressByUserId(addAddress)
                    .emailsByUserId(Arrays.asList(addEmail1, addEmail2))
                    .membershipsByUserId(List.of(addMembership))
                    .ver(0)
                    .build();
            User result = sampleService.add(addUser);
            assertThat(result.getLastUpdatedAt(), is(nullValue()));
        }

        @Test
        public void addTestAbnormalCase() throws BusinessException{
            User addUser = User.builder()
                    .loginId("taro.mynavi")
                    .build();

            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("使用できないログインIDです。 LoginID : taro.mynavi");
            sampleService.add(addUser);
        }

        @Test
        public void updateTestNormalCase() throws BusinessException{

            Address updateAddress = Address.builder()
                    .userId(0)
                    .zipCode("111-1111")
                    .address("Tonde Saitama")
                    .build();
            Email updateEmail = Email.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("hanako.mynavi@debugroom.org")
                    .build();
            User updateUser = User.builder()
                    .userId(0L)
                    .familyName("mynavi")
                    .firstName("hanako")
                    .loginId("hanako.mynavi")
                    .addressByUserId(updateAddress)
                    .emailsByUserId(List.of(updateEmail))
                    .ver(0)
                    .build();

            User result = sampleService.update(updateUser);

            assertThat(result.getFirstName(), is("hanako"));
            assertThat(result.getFamilyName(), is("mynavi"));
            assertThat(result.getLoginId(), is("hanako.mynavi"));
            assertThat(result.getAddressByUserId().getZipCode(), is("111-1111"));
            assertThat(result.getAddressByUserId().getAddress(), is("Tonde Saitama"));
            assertThat(result.getEmailsByUserId().stream()
                    .filter(email -> email.getEmailNo() == 0)
                    .findFirst().get().getEmail(), is("hanako.mynavi@debugroom.org"));
        }

        @Test
        public void updateTestAbnormalCase1() throws BusinessException{
            User updateUser = User.builder()
                    .userId(1L)
                    .build();

            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1");
            sampleService.update(updateUser);
        }

        @Test
        public void updateTestAbnormalCase2() throws BusinessException{

            User updateUser = User.builder()
                    .userId(0L)
                    .familyName("mynavi")
                    .firstName("hanako")
                    .loginId("taro.mynavi")
                    .ver(0)
                    .build();

            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("使用できないログインIDです。 LoginID : taro.mynavi");
            sampleService.update(updateUser);
        }

        @Test
        public void deleteTestAbnormalCase() throws BusinessException{
            User deleteUser = User.builder()
                    .userId(1L)
                    .build();
            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1");
            sampleService.delete(deleteUser);
        }

        @Test
        public void findUserHaveLoginIdAbnormalTest() throws BusinessException{
            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("指定されたユーザは存在しません。 LoginID : saburo.mynavi");
            sampleService.findUserHave("saburo.mynavi");
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = {
            TestConfig.ServiceTestConfig.class,
    }, webEnvironment =  SpringBootTest.WebEnvironment.NONE)
    @TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
            DirtiesContextTestExecutionListener.class,
            TransactionalTestExecutionListener.class,
            DbUnitTestExecutionListener.class })
    @DbUnitConfiguration(dataSetLoader = IntegrationTest.CsvDataSetLoader.class)
    @ActiveProfiles("dev")
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    @Category(com.example.lesson5.common.apinfra.test.junit.IntegrationTest.class)
    public static class IntegrationTest{

        public static class CsvDataSetLoader extends AbstractDataSetLoader {
            @Override
            protected IDataSet createDataSet(Resource resource)
                    throws Exception {
                return new CsvDataSet(resource.getFile());
            }
        }
        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @Autowired
        SampleService sampleService;

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/add",
                        table = "usr", assertionMode = DatabaseAssertionMode.NON_STRICT),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/add",
                        table = "address", assertionMode = DatabaseAssertionMode.NON_STRICT),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/add",
                        table = "email", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/add",
                        table = "membership", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
        })
        public void addNormalTest() throws BusinessException{
            Address addAddress = Address.builder()
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();
            Email addEmail1 = Email.builder()
                    .email("saburo.mynavi@debugroom.org")
                    .build();
            Email addEmail2 = Email.builder()
                    .email("saburo.mynavi@test.org")
                    .build();
            Membership membership1 = Membership.builder()
                    .groupId(0)
                    .build();
            User addUser = User.builder()
                    .firstName("saburo")
                    .familyName("mynavi")
                    .loginId("saburo.mynavi")
                    .isLogin(false)
                    .addressByUserId(addAddress)
                    .emailsByUserId(Arrays.asList(addEmail1, addEmail2))
                    .membershipsByUserId(List.of(membership1))
                    .build();
            sampleService.add(addUser);
        }

        @Test
        public void findOneNormalTest() throws BusinessException{
            User user = sampleService.findOne(
                    User.builder().userId(0L).build());
            assertThat(user.getLoginId(), is("taro.mynavi"));
        }

        @Test
        public void findAllNormalTest(){
            List<User> users = sampleService.findAll();
            assertThat(users.size(), is(3));
        }



        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/update",
                        table = "usr", assertionMode = DatabaseAssertionMode.NON_STRICT),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/update",
                        table = "address", assertionMode = DatabaseAssertionMode.NON_STRICT),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/update",
                        table = "email", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/update",
                        table = "membership", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
        })
        public void updateNormalTest() throws BusinessException{
            Address updateAddress = Address.builder()
                    .userId(0)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();
            Email updateEmail1 = Email.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("shiro.mynavi@debugroom.org")
                    .build();
            Email updateEmail2 = Email.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("shiro.mynavi@test.org")
                    .build();
            User updateUser = User.builder()
                    .userId(0)
                    .firstName("shiro")
                    .familyName("mynavi")
                    .loginId("shiro.mynavi")
                    .isLogin(false)
                    .addressByUserId(updateAddress)
                    .emailsByUserId(Arrays.asList(updateEmail1, updateEmail2))
                    .build();
            sampleService.update(updateUser);
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/delete",
                        table = "usr", assertionMode = DatabaseAssertionMode.NON_STRICT),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/delete",
                        table = "address", assertionMode = DatabaseAssertionMode.NON_STRICT),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/delete",
                        table = "email", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleServiceImplTest/delete",
                        table = "membership", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
        })
        public void deleteNormalTest() throws BusinessException{
            User deleteUser = User.builder()
                    .userId(3)
                    .build();
            sampleService.delete(deleteUser);
        }

    }
}
