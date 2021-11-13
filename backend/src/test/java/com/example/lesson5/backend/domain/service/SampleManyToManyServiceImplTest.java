package com.example.lesson5.backend.domain.service;

import com.example.lesson5.backend.config.TestConfig;
import com.example.lesson5.backend.domain.model.entity.Group;
import com.example.lesson5.backend.domain.model.entity.Membership;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.repository.GroupRepository;
import com.example.lesson5.backend.domain.repository.UserRepository;
import com.example.lesson5.common.apinfra.exception.BusinessException;
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

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;


@RunWith(Enclosed.class)
public class SampleManyToManyServiceImplTest {

    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = {
            TestConfig.UnitTestConfig.class,
            SampleManyToManyServiceImplTest.UnitTest.Config.class
    }, webEnvironment = SpringBootTest.WebEnvironment.NONE)
    @Category(com.example.lesson5.common.apinfra.test.junit.UnitTest.class)
    public static class UnitTest{

        @Configuration
        public static class Config{
            @Bean
            SampleManyToManyService sampleManyToManyService(){
                return new SampleManyToManyServiceImpl();
            }
        }

        @Rule
        public ExpectedException expectedException = ExpectedException.none();

        @MockBean
        UserRepository userRepositoryMock;

        @MockBean
        GroupRepository groupRepositoryMock;

        @Autowired
        SampleManyToManyService sampleManyToManyService;

        @Before
        public void setUp(){
            Membership mockMembership = Membership.builder()
                    .groupId(0)
                    .userId(0)
                    .build();
            User mockUser1 = User.builder()
                    .userId(0)
                    .membershipsByUserId(List.of(mockMembership))
                    .build();
            User mockUser2 = User.builder().userId(1).build();
            Group mockGroup = Group.builder().groupId(3).build();
            when(userRepositoryMock.findById(mockUser1.getUserId()))
                    .thenReturn(Optional.of(mockUser1));
            when(userRepositoryMock.findById(mockUser2.getUserId()))
                    .thenReturn(Optional.empty());
            when(groupRepositoryMock.findById(mockGroup.getGroupId()))
                    .thenReturn(Optional.empty());
        }

        @Test
        public void addUserToGroupAbnormalTest1() throws BusinessException{
            Group group = Group.builder().groupId(0).build();
            User user = User.builder().userId(1).build();
            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1");
            sampleManyToManyService.addUserTo(group, user);
        }

        @Test
        public void addUserToGroupAbnormalTest2() throws BusinessException{
            Group group = Group.builder().groupId(0).build();
            User user = User.builder().userId(0).build();
            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("ユーザは既にグループに追加されています。UserID ：0, GroupID : 0");
            sampleManyToManyService.addUserTo(group, user);
        }

        @Test
        public void deleteUserFromGroupAbnormalTest1() throws BusinessException{
            Group group = Group.builder().groupId(0).build();
            User user = User.builder().userId(1).build();
            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1");
            sampleManyToManyService.deleteUserFrom(group, user);
        }

        @Test
        public void deleteUserFromGroupAbnormalTest2() throws BusinessException{
            Group group = Group.builder().groupId(2).build();
            User user = User.builder().userId(0).build();
            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("ユーザは指定されたグループに所属していません。UserID : 0, GroupID : 2");
            sampleManyToManyService.deleteUserFrom(group, user);
        }

        @Test
        public void deleteGroupAbnormalTest() throws BusinessException{
            Group group = Group.builder().groupId(3).build();
            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("指定されたグループは存在しないか、IDが誤っています。 GroupID : 3");
            sampleManyToManyService.delete(group);
        }

        @Test
        public void deleteUserAbnormalTest() throws BusinessException{
            User user = User.builder().userId(1).build();
            expectedException.expect(BusinessException.class);
            expectedException.expectMessage("指定されたユーザは存在しないか、IDが誤っています。 UserID : 1");
            sampleManyToManyService.delete(user);
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
    @DbUnitConfiguration(dataSetLoader = SampleManyToManyServiceImplTest
            .IntegrationTest.CsvDataSetLoader.class)
    @ActiveProfiles("dev")
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    @Category(com.example.lesson5.common.apinfra.test.junit.IntegrationTest.class)
    public static class IntegrationTest{

        public static class CsvDataSetLoader extends AbstractDataSetLoader{
            @Override
            protected IDataSet createDataSet(Resource resource)
                    throws Exception {
                return new CsvDataSet(resource.getFile());
            }
        }

        @Autowired
        SampleManyToManyService sampleManyToManyService;

        @Test
        public void getGroupOfUserNormalTest(){
            User user = User.builder().userId(2).build();
            List<Group> groups = sampleManyToManyService.getGroupsOf(user);
            assertThat(groups.size(), is(2));
            groups.forEach(group -> assertThat(group.getGroupName(),
                    either(is("GroupA"))
                            .or(is("GroupC"))));
        }

        @Test
        public void getUserOfGroupNormalTest(){
            Group group = Group.builder().groupName("GroupC").build();
            List<User> users = sampleManyToManyService.getUsersOf(group);
            assertThat(users.size(), is(1));
            users.forEach(user -> assertThat(user.getFirstName(), is("jiro")));
        }

        @Test
        public void getUserOfNotGroupNormalTest(){
            Group group = Group.builder().groupName("GroupC").build();
            List<User> users = sampleManyToManyService.getUsersOfNot(group);
            assertThat(users.size(), is(1));
            users.forEach(user -> assertThat(user.getFirstName(), is("hanako")));
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleManyToManyServiceImplTest/addUserToGroup",
                        table = "membership", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
        })
        public void addUserToGroupNormalTest() throws BusinessException{
            Group group = Group.builder().groupId(2).build();
            User user = User.builder().userId(2).build();
            sampleManyToManyService.addUserTo(group, user);
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleManyToManyServiceImplTest/deleteUserFromGroup",
                        table = "membership", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
        })
        public void deleteUserFromGroupNormalTest() throws BusinessException{
            Group group = Group.builder().groupId(0).build();
            User user = User.builder().userId(1).build();
            sampleManyToManyService.deleteUserFrom(group, user);
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleManyToManyServiceImplTest/deleteGroup",
                        table = "membership", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleManyToManyServiceImplTest/deleteGroup",
                        table = "grp", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
        })
        public void deleteGroupNormalTest() throws BusinessException{
            Group group = Group.builder().groupId(1).build();
            sampleManyToManyService.delete(group);
        }

        @Test
        @ExpectedDatabases({
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleManyToManyServiceImplTest/deleteUser",
                        table = "membership", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
                @ExpectedDatabase(
                        value = "classpath:/META-INF/dbunit/domain/service/SampleManyToManyServiceImplTest/deleteUser",
                        table = "usr", assertionMode = DatabaseAssertionMode.NON_STRICT_UNORDERED),
        })
        public void deleteUserNormalTest() throws BusinessException{
            User user = User.builder().userId(0).build();
            sampleManyToManyService.delete(user);
        }
    }


}
