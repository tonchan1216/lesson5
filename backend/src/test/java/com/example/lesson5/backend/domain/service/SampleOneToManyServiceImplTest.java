package com.example.lesson5.backend.domain.service;

import com.example.lesson5.backend.config.TestConfig;
import com.example.lesson5.backend.domain.model.entity.Email;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.repository.EmailRepository;
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
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.experimental.runners.Enclosed;
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
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(Enclosed.class)
public class SampleOneToManyServiceImplTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = {
            TestConfig.UnitTestConfig.class,
            SampleOneToManyServiceImplTest.UnitTest.Config.class,
    }, webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @Category(com.example.lesson5.common.apinfra.test.junit.UnitTest.class)
    public static class UnitTest{

        @Configuration
        public static class Config{
            @Bean
            SampleOneToManyService sampleOneToManyService(){
                return new SampleOneToManyServiceImpl();
            }
        }

        @MockBean
        UserRepository userRepositoryMock;

        @MockBean
        EmailRepository emailRepositoryMock;

        @Autowired
        SampleOneToManyService sampleOneToManyService;

        @Before
        public void setUp(){
            long userId = 0;
            Email mockUpdatedEmail = Email.builder()
                    .userId(userId)
                    .emailNo(0)
                    .email("hanako.mynavi@debugroom.org")
                    .ver(0)
                    .lastUpdatedAt(DateUtil.now())
                    .build();
            User mockUpdatedUser = User.builder()
                    .userId(userId)
                    .emailsByUserId(List.of(mockUpdatedEmail))
                    .build();
            Email mockEmail1 = Email.builder()
                    .userId(userId)
                    .emailNo(0)
                    .email("taro.mynavi@debugroom.org")
                    .ver(0)
                    .lastUpdatedAt(DateUtil.now())
                    .build();
            Email mockEmail2 = Email.builder()
                    .userId(userId)
                    .emailNo(1)
                    .email("taro.mynavi@test.org")
                    .ver(0)
                    .lastUpdatedAt(DateUtil.now())
                    .build();
            User mockUser = User.builder()
                    .userId(userId)
                    .emailsByUserId(Arrays.asList(mockEmail1, mockEmail2))
                    .build();
            when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(mockUser));
        }

        @Test
        public void getEmailsOfAbnormalTest() {
            User user = User.builder().userId(1).build();
            Throwable exception = assertThrows(
                    BusinessException.class, () -> sampleOneToManyService.getEmailsOf(user)
            );
            assertThat(exception.getMessage(), is("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1"));
        }

        @Test
        public void addAbnormalTest() {
            Email email = Email.builder().userId(1).build();
            Throwable exception = assertThrows(
                    BusinessException.class, () -> sampleOneToManyService.add(email)
            );
            assertThat(exception.getMessage(), is("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1"));
        }

        @Test
        public void updateAbnormalTest() {
            Email email = Email.builder().userId(1).build();
            Throwable exception = assertThrows(
                    BusinessException.class, () -> sampleOneToManyService.update(email)
            );
            assertThat(exception.getMessage(), is("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1"));
        }

        @Test
        public void deleteAbnormalTest1() {
            Email email = Email.builder().userId(1).build();
            Throwable exception = assertThrows(
                    BusinessException.class, () -> sampleOneToManyService.delete(email)
            );
            assertThat(exception.getMessage(), is("指定されたキーやメールアドレスは存在しません。 Key : 1, Email : null"));
        }

        @Test
        public void deleteAbnormalTest2() {
            Email email = Email.builder().userId(1).emailNo(4).build();
            Throwable exception = assertThrows(
                    BusinessException.class, () -> sampleOneToManyService.delete(email)
            );
            assertThat(exception.getMessage(), is("指定されたキーやメールアドレスは存在しません。 Key : 1, Email : null"));
        }

        @Test
        public void deleteAbnormalTest3() {
            Email email = Email.builder().userId(1).email("shiro.mynavi@debugroom.org").build();
            Throwable exception = assertThrows(
                    BusinessException.class, () -> sampleOneToManyService.delete(email)
            );
            assertThat(exception.getMessage(), is("指定したメールアドレスは存在しません. Email : shiro.mynavi@debugroom.org"));
        }

        @Test
        public void deleteAllEmailAbnormalTest() {
            User user = User.builder().userId(1).build();
            Throwable exception = assertThrows(
                    BusinessException.class, () -> sampleOneToManyService.deleteAllEmail(user)
            );
            assertThat(exception.getMessage(), is("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1"));
        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = {
            TestConfig.ServiceTestConfig.class,
    }, webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
            DirtiesContextTestExecutionListener.class,
            TransactionalTestExecutionListener.class,
            DbUnitTestExecutionListener.class })
    @DbUnitConfiguration(dataSetLoader = SampleOneToManyServiceImplTest.
            IntegrationTest.CsvDataSetLoader.class)
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

        @Autowired
        SampleOneToManyService sampleOneToManyService;

        @Test
        public void getEmailsOfNormalTest() throws BusinessException{
            User user = User.builder().userId(1).build();
            List<Email> emails = sampleOneToManyService.getEmailsOf(user);
            assertThat(emails.size(), is(1));
            emails.forEach(email -> assertThat(email.getEmail(), is("hanako.mynavi@debugroom.org")));
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleOneToManyServiceImplTest/add",
                        table = "email", assertionMode = DatabaseAssertionMode.NON_STRICT),
        })
        public void addNormalTest() throws BusinessException{
            Email email1 = Email.builder()
                    .userId(0)
                    .email("taro.mynavi3@debugroom.org")
                    .build();
            Email email2 = Email.builder()
                    .userId(0)
                    .email("taro.mynavi4@debugroom.org")
                    .build();
            sampleOneToManyService.add(email1);
            sampleOneToManyService.add(email2);
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleOneToManyServiceImplTest/update",
                        table = "email", assertionMode = DatabaseAssertionMode.NON_STRICT),
        })
        public void updateNormalTest() throws BusinessException{
            Email email = Email.builder()
                    .userId(1)
                    .emailNo(0)
                    .email("taro.mynavi4@debugroom.org")
                    .build();
            sampleOneToManyService.update(email);
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleOneToManyServiceImplTest/delete1",
                        table = "email", assertionMode = DatabaseAssertionMode.NON_STRICT),
        })
        public void deleteNormalTest1() throws BusinessException{
            Email email = Email.builder()
                    .userId(0)
                    .emailNo(1)
                    .build();
            sampleOneToManyService.delete(email);
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleOneToManyServiceImplTest/delete2",
                        table = "email", assertionMode = DatabaseAssertionMode.NON_STRICT),
        })
        public void deleteNormalTest2() throws BusinessException{
            Email email = Email.builder()
                    .userId(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            sampleOneToManyService.delete(email);
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleOneToManyServiceImplTest/deleteAllEmail",
                        table = "email", assertionMode = DatabaseAssertionMode.NON_STRICT),
        })
        public void delete_AllEmailNormalTest() throws BusinessException{
            User user = User.builder().userId(0).build();
            sampleOneToManyService.deleteAllEmail(user);
        }

    }
}
