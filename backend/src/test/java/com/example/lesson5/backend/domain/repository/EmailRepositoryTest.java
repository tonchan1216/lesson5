package com.example.lesson5.backend.domain.repository;

import com.example.lesson5.backend.domain.model.entity.Email;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.common.apinfra.test.junit.UnitTest;
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@Slf4j
@Category(UnitTest.class)
@RunWith(SpringRunner.class)
@DataJpaTest
public class EmailRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    EmailRepository emailRepository;

    @Before
    public void before(){
        long userId = 0;
        Email email1 = Email.builder()
                .userId(userId)
                .emailNo(0)
                .email("test1@debugroom.org")
                .build();
        Email email2 = Email.builder()
                .userId(userId)
                .emailNo(1)
                .email("test2@debugroom.org")
                .build();
        testEntityManager.persist(
                User.builder()
                        .userId(userId)
                        .emailsByUserId(Arrays.asList(new Email[]{email1, email2}))
                        .build());
    }

    @Test
    public void testFindByEmail(){
        Email result = emailRepository.findByEmail("test2@debugroom.org");
        assertThat(result.getUserId(), is(0L));
        assertThat(result.getEmailNo(), is(1L));
        assertThat(result.getEmail(), is("test2@debugroom.org"));
    }
}
