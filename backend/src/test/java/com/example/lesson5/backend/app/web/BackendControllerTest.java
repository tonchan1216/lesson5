package com.example.lesson5.backend.app.web;

import com.example.lesson5.backend.config.TestConfig;
import com.example.lesson5.backend.domain.model.entity.Address;
import com.example.lesson5.backend.domain.model.entity.Email;
import com.example.lesson5.backend.domain.model.entity.User;
import com.example.lesson5.backend.domain.service.SampleManyToManyService;
import com.example.lesson5.backend.domain.service.SampleOneToManyService;
import com.example.lesson5.backend.domain.service.SampleOneToOneService;
import com.example.lesson5.backend.domain.service.SampleService;
import com.example.lesson5.common.apinfra.exception.*;
import com.example.lesson5.common.web.model.AddressResource;
import com.example.lesson5.common.web.model.EmailResource;
import com.example.lesson5.common.web.model.GroupResource;
import com.example.lesson5.common.web.model.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;


@RunWith(Enclosed.class)
public class BackendControllerTest {
    @RunWith(SpringRunner.class)
    @Category(com.example.lesson5.common.apinfra.test.junit.UnitTest.class)
    @WebMvcTest(controllers = com.example.lesson5.backend.app.web.BackendController.class)
    public static class UnitTest{

        @Autowired
        ObjectMapper objectMapper;

        @Autowired
        MockMvc mockMvc;

        @MockBean
        SampleService sampleService;

        @MockBean
        SampleOneToOneService sampleOneToOneServiceService;

        @MockBean
        SampleOneToManyService sampleOneToManyServiceService;

        @MockBean
        SampleManyToManyService sampleManyToManyServiceService;

        @Before
        public void setUp() throws Exception{
            Address mockAddress1 = Address.builder()
                    .userId(0)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();
            Address mockAddress2 = Address.builder()
                    .userId(1)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();
            Email mockEmail1 = Email.builder()
                    .userId(0)
                    .emailNo(0)
                    .email("taro.mynavi1@debugroom.org")
                    .build();
            Email mockEmail2 = Email.builder()
                    .userId(0)
                    .emailNo(1)
                    .email("taro.mynavi2@debugroom.org")
                    .build();
            Email mockEmail3 = Email.builder()
                    .userId(1)
                    .emailNo(0)
                    .email("hanako.mynavi1@debugroom.org")
                    .build();
            Email mockEmail4 = Email.builder()
                    .userId(1)
                    .emailNo(1)
                    .email("hanako.mynavi2@debugroom.org")
                    .build();
            User mockUser1 = User.builder()
                    .userId(0)
                    .firstName("taro")
                    .familyName("mynavi")
                    .loginId("taro.mynavi")
                    .isLogin(false)
                    .addressByUserId(mockAddress1)
                    .emailsByUserId(Arrays.asList(mockEmail1, mockEmail2))
                    .build();
            User mockUser2 = User.builder()
                    .userId(1)
                    .firstName("hanako")
                    .familyName("mynavi")
                    .loginId("hanako.mynavi")
                    .isLogin(false)
                    .addressByUserId(mockAddress2)
                    .emailsByUserId(Arrays.asList(mockEmail3, mockEmail4))
                    .build();
            Mockito.when(sampleService.findAll()).thenReturn(
                    Arrays.asList(mockUser1, mockUser2));
            Mockito.when(sampleService.findOne(mockUser1)).thenReturn(mockUser1);
            Mockito.when(sampleService.findOne(mockUser2)).thenReturn(mockUser2);
            Mockito.when(sampleService.findOne(User.builder().userId(3).build()))
                    .thenThrow(new BusinessException("E0001", "", (Object[]) new Long[]{3L}));
            Mockito.when(sampleService.add(mockUser2)).thenReturn(mockUser2);
            Mockito.when(sampleService.update(mockUser2)).thenReturn(mockUser2);
            Mockito.when(sampleService.findUserHave("taro.mynavi"))
                    .thenReturn(mockUser1);
            Mockito.when(sampleService.findUserHave("jiro.mynavi"))
                    .thenThrow(new BusinessException("E0008", "", (Object[]) new String[]{"jiro.mynavi"}));

        }

        @Test
        public void getUserNormalTest() throws Exception{
            AddressResource addressResource = AddressResource.builder()
                    .userId(1)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();
            EmailResource emailResource1 = EmailResource.builder()
                    .userId(1)
                    .emailNo(0)
                    .email("hanako.mynavi1@debugroom.org")
                    .build();
            EmailResource emailResource2 = EmailResource.builder()
                    .userId(1)
                    .emailNo(1)
                    .email("hanako.mynavi2@debugroom.org")
                    .build();
            UserResource userResource = UserResource.builder()
                    .userId(1)
                    .firstName("hanako")
                    .familyName("mynavi")
                    .loginId("hanako.mynavi")
                    .address(addressResource)
                    .emailList(Arrays.asList(emailResource1, emailResource2))
                    .build();
            mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/v1/users/{userId}", 1)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                    .andExpect(MockMvcResultMatchers.content().string(
                            objectMapper.writeValueAsString(userResource)));
        }

        @Test
        public void getUserAbnormalTest() throws Exception{
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(
                            "/api/v1/users/{userId}", "3"))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();

            BusinessExceptionResponse businessExceptionResponse = (BusinessExceptionResponse)
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                            ErrorResponse.class);

            assertThat(businessExceptionResponse.getBusinessException().getCode(), is("E0001"));
            assertThat(businessExceptionResponse.getBusinessException().getArgs(), is(new Integer[]{3}));
        }

        @Test
        public void addUserInputParamNormalTest() throws Exception{
            com.example.lesson5.backend.app.model.Address address =
                    com.example.lesson5.backend.app.model.Address.builder()
                            .userId(1)
                            .zipCode("300-0000")
                            .address("Tonde Saitama")
                            .build();
            com.example.lesson5.backend.app.model.Email email1 =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(1)
                            .emailNo(0)
                            .email("hanako.mynavi1@debugroom.org")
                            .build();
            com.example.lesson5.backend.app.model.Email email2 =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(1)
                            .emailNo(1)
                            .email("hanako.mynavi2@debugroom.org")
                            .build();
            com.example.lesson5.backend.app.model.User user =
                    com.example.lesson5.backend.app.model.User.builder()
                            .userId(1)
                            .firstName("hanako")
                            .familyName("mynavi")
                            .loginId("hanako.mynavi")
                            .address(address)
                            .emailList(
                                    Arrays.asList(email1, email2))
                            .build();
            AddressResource addressResource = AddressResource.builder()
                    .userId(1)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();
            EmailResource emailResource1 = EmailResource.builder()
                    .userId(1)
                    .emailNo(0)
                    .email("hanako.mynavi1@debugroom.org")
                    .build();
            EmailResource emailResource2 = EmailResource.builder()
                    .userId(1)
                    .emailNo(1)
                    .email("hanako.mynavi2@debugroom.org")
                    .build();
            UserResource userResource = UserResource.builder()
                    .userId(1)
                    .firstName("hanako")
                    .familyName("mynavi")
                    .loginId("hanako.mynavi")
                    .address(addressResource)
                    .emailList(Arrays.asList(emailResource1, emailResource2))
                    .build();
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/v1/users/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user)))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                    .andExpect(MockMvcResultMatchers.content().string(
                            objectMapper.writeValueAsString(userResource)));

        }

        @Test
        public void addUserInputParamAbnormalTest() throws Exception {
            com.example.lesson5.backend.app.model.Address address =
                    com.example.lesson5.backend.app.model.Address.builder()
                            .userId(1)
                            .zipCode("300+0000")
                            .address("")
                            .build();
            com.example.lesson5.backend.app.model.Email email1 =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(1)
                            .emailNo(0)
                            .email("hanako.mynavi1debugroom.org")
                            .build();
            com.example.lesson5.backend.app.model.Email email2 =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(1)
                            .emailNo(1)
                            .email("hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi.hanako.mynavi@debugroom.org")
                            .build();
            com.example.lesson5.backend.app.model.User user =
                    com.example.lesson5.backend.app.model.User.builder()
                            .userId(-1)
                            .firstName("hanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanakohanako")
                            .familyName("mynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavimynavi")
                            .loginId("hanako.mynavi.hanako.mynavi.hanako.mynavi")
                            .address(address)
                            .emailList(
                                    Arrays.asList(email1, email2))
                            .build();
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/v1/users/new")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user)))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();

            List<ValidationError> validationErrors = ((ValidationErrorResponse)
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                            ErrorResponse.class)).getValidationErrors();

            assertThat(validationErrors.size(), is(8));
            validationErrors.forEach(
                    validationError -> {
                        switch (validationError.getField()){
                            case "userId" :
                                assertThat(validationError.getDefaultMessage(), is("must be greater than or equal to 0"));
                                break;
                            case "firstName" :
                            case "familyName" :
                                assertThat(validationError.getDefaultMessage(), is("size must be between 1 and 50"));
                                break;
                            case "loginId" :
                                assertThat(validationError.getDefaultMessage(), is("size must be between 1 and 32"));
                                break;
                            case "emailList[0].email" :
                                assertThat(validationError.getDefaultMessage(), is("must be a well-formed email address"));
                                break;
                            case "emailList[1].email" :
                                assertThat(validationError.getDefaultMessage(), is("must be a well-formed email address"));
                                break;
                            case "address.zipCode" :
                                assertThat(validationError.getDefaultMessage(), is("must match \"[0-9\\-]*\""));
                                break;
                            case "address.address" :
                                assertThat(validationError.getDefaultMessage(), is("must match \"^[^=#$%&./<>?^~\\[\\]()]+$\""));
                                break;
                            default: fail();
                        }
                    }
            );

        }

        @Test
        public void updateUserInputParamNormalTest() throws Exception {
            com.example.lesson5.backend.app.model.Address address =
                    com.example.lesson5.backend.app.model.Address.builder()
                            .userId(1)
                            .zipCode("300-0000")
                            .address("Tonde Saitama")
                            .build();
            com.example.lesson5.backend.app.model.Email email1 =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(1)
                            .emailNo(0)
                            .email("hanako.mynavi1@debugroom.org")
                            .build();
            com.example.lesson5.backend.app.model.Email email2 =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(1)
                            .emailNo(1)
                            .email("hanako.mynavi2@debugroom.org")
                            .build();
            com.example.lesson5.backend.app.model.User user =
                    com.example.lesson5.backend.app.model.User.builder()
                            .userId(1)
                            .firstName("hanako")
                            .familyName("mynavi")
                            .loginId("hanako.mynavi")
                            .address(address)
                            .emailList(
                                    Arrays.asList(email1, email2))
                            .build();
            AddressResource addressResource = AddressResource.builder()
                    .userId(1)
                    .zipCode("300-0000")
                    .address("Tonde Saitama")
                    .build();
            EmailResource emailResource1 = EmailResource.builder()
                    .userId(1)
                    .emailNo(0)
                    .email("hanako.mynavi1@debugroom.org")
                    .build();
            EmailResource emailResource2 = EmailResource.builder()
                    .userId(1)
                    .emailNo(1)
                    .email("hanako.mynavi2@debugroom.org")
                    .build();
            UserResource userResource = UserResource.builder()
                    .userId(1)
                    .firstName("hanako")
                    .familyName("mynavi")
                    .loginId("hanako.mynavi")
                    .address(addressResource)
                    .emailList(Arrays.asList(emailResource1, emailResource2))
                    .build();
            mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/v1/users/{userId}", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(user)))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                    .andExpect(MockMvcResultMatchers.content().string(
                            objectMapper.writeValueAsString(userResource)));
        }

        @Test
        public void findUserByLoginIdInputParamAbNormalTest() throws Exception{
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/v1/users/loginId").param("loginId", "taro.mynavi.hanako.mynavi.jiro.mynavi"))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();
            List<ValidationError> validationErrors = ((ValidationErrorResponse)
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                            ErrorResponse.class)).getValidationErrors();

            assertThat(validationErrors.size(), is(1));
            assertThat(validationErrors.get(0).getDefaultMessage(), is("size must be between 1 and 32"));
        }

        @Test
        public void updateAddressInputParamAbnormalTest() throws Exception{
            com.example.lesson5.backend.app.model.Address address =
                    com.example.lesson5.backend.app.model.Address.builder()
                            .userId(1)
                            .zipCode("300+0000")
                            .address("")
                            .build();
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/v1/users/{userId}/address", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(address)))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();

            List<ValidationError> validationErrors = ((ValidationErrorResponse)
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                            ErrorResponse.class)).getValidationErrors();

            assertThat(validationErrors.size(), is(2));
            validationErrors.forEach(
                    validationError -> {
                        switch (validationError.getField()){
                            case "zipCode" :
                                assertThat(validationError.getDefaultMessage(), is("must match \"[0-9\\-]*\""));
                                break;
                            case "address" :
                                assertThat(validationError.getDefaultMessage(), is("must match \"^[^=#$%&./<>?^~\\[\\]()]+$\""));
                                break;
                            default: fail();
                        }
                    }
            );
        }

        @Test
        public void findUserHavingEmailInputParamAbnormalTest() throws Exception{

            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .get("/api/v1/emails/email/user")
                            .param("userId", "1")
                            .param("emailNo", "1"))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();

            List<ValidationError> validationErrors = ((ValidationErrorResponse)
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                            ErrorResponse.class)).getValidationErrors();

            assertThat(validationErrors.size(), is(1));
            validationErrors.forEach(validationError ->
                    assertThat(validationError.getDefaultMessage(), is("must not be null")));
        }

        @Test
        public void addEmailInputParamAbnormalTest() throws Exception{
            com.example.lesson5.backend.app.model.Email email =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(1)
                            .emailNo(0)
                            .email("hanako.mynavi1debugroom.org")
                            .build();
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/v1/users/{userId}/emails/new", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(email)))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();

            List<ValidationError> validationErrors = ((ValidationErrorResponse)
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                            ErrorResponse.class)).getValidationErrors();

            assertThat(validationErrors.size(), is(1));
            validationErrors.forEach(validationError ->
                    assertThat(validationError.getDefaultMessage(), is("must be a well-formed email address")));
        }

        @Test
        public void updateEmailInputParamAbnormalTest() throws Exception{
            com.example.lesson5.backend.app.model.Email email =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(-1)
                            .emailNo(0)
                            .email("hanako.mynavi1debugroom.org")
                            .build();
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .put("/api/v1/users/{userId}/emails/{emailNo}", 1, 0)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(email)))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();

            List<ValidationError> validationErrors = ((ValidationErrorResponse)
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                            ErrorResponse.class)).getValidationErrors();

            assertThat(validationErrors.size(), is(2));
            validationErrors.forEach(
                    validationError -> {
                        switch (validationError.getField()){
                            case "userId" :
                                assertThat(validationError.getDefaultMessage(), is("must be greater than or equal to 0"));
                                break;
                            case "email" :
                                assertThat(validationError.getDefaultMessage(), is("must be a well-formed email address"));
                                break;

                            default: fail();
                        }
                    }
            );
        }

        @Test
        public void deleteEmailInputParamAbnormalTest() throws Exception{
            com.example.lesson5.backend.app.model.Email email =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(-1)
                            .emailNo(0)
                            .build();
            MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                            .delete("/api/v1/users/{userId}/emails/{emailNo}", 1, 0)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(email)))
                    .andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value()))
                    .andReturn();

            List<ValidationError> validationErrors = ((ValidationErrorResponse)
                    objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                            ErrorResponse.class)).getValidationErrors();

            assertThat(validationErrors.size(), is(1));
            validationErrors.forEach(validationError ->
                    assertThat(validationError.getDefaultMessage(), is("must be greater than or equal to 0")));

        }

    }

    @RunWith(SpringRunner.class)
    @SpringBootTest(classes = TestConfig.ControllerTestConfig.class,
            webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    @FixMethodOrder(MethodSorters.NAME_ASCENDING)
    @ActiveProfiles("dev")
    public static class IntegrationTest{

        private static final String testServer = "localhost";
        @Autowired
        TestRestTemplate testRestTemplate;

        @LocalServerPort
        int port;

        private String testServerURL;

        @Before
        public void setUp(){
            testServerURL = "http://" + testServer + ":" + port;
        }

        @Test
        public void getUsersNormalTest(){
            ResponseEntity<UserResource[]> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/users", UserResource[].class);

            List<UserResource> userResources = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

            assertThat(userResources.size(), is(3));
            userResources.forEach(userResource -> {
                switch (Long.toString(userResource.getUserId())){
                    case "1":
                        assertThat(userResource.getFirstName(), is("hanako"));
                        assertThat(userResource.getFamilyName(), is("mynavi"));
                        break;
                    case "2":
                        assertThat(userResource.getFirstName(), is("jiro"));
                        assertThat(userResource.getFamilyName(), is("mynavi"));
                        break;
                    case "3":
                        assertThat(userResource.getFirstName(), is("saburo"));
                        assertThat(userResource.getFamilyName(), is("mynavi"));
                        break;
                    default: fail();
                }
            });
        }

        @Test
        public void getUserNormalTest(){

            ResponseEntity<UserResource> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/users/1", UserResource.class);

            UserResource userResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(userResource).getFirstName(), is("hanako"));
            assertThat(userResource.getFamilyName(), is("mynavi"));
            assertThat(userResource.getLoginId(), is("hanako.mynavi"));
            assertThat(userResource.getAddress().getZipCode(), is("300-0000"));
            assertThat(userResource.getAddress().getAddress(), is("Tonde Saitama"));
            assertThat(userResource.getEmailList().get(0).getEmail(), is("hanako.mynavi@debugroom.org"));

        }

        @Test
        public void getUserAbnormalTest(){

            ResponseEntity<ErrorResponse> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/users/4", ErrorResponse.class);

            BusinessExceptionResponse businessExceptionResponse = (BusinessExceptionResponse)
                    responseEntity.getBody();

            assertThat(Objects.requireNonNull(businessExceptionResponse).getBusinessException().getCode(), is("E0001"));
            assertThat(businessExceptionResponse.getBusinessException().getArgs(), is(new Integer[]{4}));

        }

        @Test
        public void addUserNormalTest(){
            com.example.lesson5.backend.app.model.Address addAddress =
                    com.example.lesson5.backend.app.model.Address.builder()
                            .zipCode("300-0000")
                            .address("Tonde Saitama")
                            .build();
            com.example.lesson5.backend.app.model.Email addEmail1 =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .email("saburo.mynavi1@debugroom.org")
                            .build();
            com.example.lesson5.backend.app.model.Email addEmail2 =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .email("saburo.mynavi2@debugroom.org")
                            .build();
            com.example.lesson5.backend.app.model.User addUser =
                    com.example.lesson5.backend.app.model.User.builder()
                            .firstName("saburo")
                            .familyName("mynavi")
                            .loginId("saburo.mynavi")
                            .address(addAddress)
                            .emailList(Arrays.asList(addEmail1, addEmail2))
                            .build();

            UserResource addUserResource = testRestTemplate.postForObject(
                    UriComponentsBuilder.fromHttpUrl(testServerURL + "/api/v1/users/new")
                            .build().toUri(), addUser, UserResource.class);

            assertThat(addUserResource.getFirstName(), is("saburo"));
            assertThat(addUserResource.getFamilyName(), is("mynavi"));
            assertThat(addUserResource.getLoginId(), is("saburo.mynavi"));
            assertThat(addUserResource.getAddress().getZipCode(), is("300-0000"));
            assertThat(addUserResource.getAddress().getAddress(), is("Tonde Saitama"));
            assertThat(addUserResource.getEmailList().get(0).getEmail(), is("saburo.mynavi1@debugroom.org"));
            assertThat(addUserResource.getEmailList().get(1).getEmail(), is("saburo.mynavi2@debugroom.org"));

        }

        @Test
        public void updateUserNormalTest(){
            com.example.lesson5.backend.app.model.Address updateAddress =
                    com.example.lesson5.backend.app.model.Address.builder()
                            .userId(2)
                            .zipCode("300-0000")
                            .address("Tonde Saitama")
                            .build();
            com.example.lesson5.backend.app.model.Email updateEmail =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(2)
                            .emailNo(0)
                            .email("shiro.mynavi@debugroom.org")
                            .build();
            com.example.lesson5.backend.app.model.User updateUser =
                    com.example.lesson5.backend.app.model.User.builder()
                            .userId(2)
                            .firstName("shiro")
                            .familyName("mynavi")
                            .loginId("shiro.mynavi")
                            .address(updateAddress)
                            .emailList(List.of(updateEmail))
                            .build();

            ResponseEntity<UserResource> updateResponseEntity
                    = testRestTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(testServerURL + "/api/v1/users/{userId}")
                            .build().expand(updateUser.getUserId()).toUri(),
                    HttpMethod.PUT, new HttpEntity<>(updateUser),
                    UserResource.class);

            UserResource updateUserResource = updateResponseEntity.getBody();

            assertThat(Objects.requireNonNull(updateUserResource).getFirstName(), is("shiro"));
            assertThat(updateUserResource.getFamilyName(), is("mynavi"));
            assertThat(updateUserResource.getLoginId(), is("shiro.mynavi"));
            assertThat(updateUserResource.getAddress().getZipCode(), is("300-0000"));
            assertThat(updateUserResource.getAddress().getAddress(), is("Tonde Saitama"));
            assertThat(updateUserResource.getEmailList().get(0).getEmail(), is("shiro.mynavi@debugroom.org"));

        }

        @Test
        public void deleteUserNormalTest(){

            com.example.lesson5.backend.app.model.User deleteUser =
                    com.example.lesson5.backend.app.model.User.builder()
                            .userId(0)
                            .build();

            ResponseEntity<UserResource> updateResponseEntity = testRestTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(testServerURL + "/api/v1/users/{userId}")
                            .build().expand(deleteUser.getUserId()).toUri(), HttpMethod.DELETE,
                    new HttpEntity<>(deleteUser),
                    UserResource.class);

            UserResource deleteUserResource = updateResponseEntity.getBody();

            assertThat(Objects.requireNonNull(deleteUserResource).getFirstName(), is("taro"));
        }

        @Test
        public void findUserOfLoginIdTest(){
            ResponseEntity<UserResource> responseEntity = testRestTemplate
                    .getForEntity(UriComponentsBuilder.fromHttpUrl(
                                    testServerURL + "/api/v1/users/loginId")
                            .queryParam("loginId", "jiro.mynavi")
                            .build().toUri(), UserResource.class);

            UserResource userResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(userResource).getFirstName(), is("jiro"));

        }


        @Test
        public void findAddressOfUserNormalTest(){
            ResponseEntity<AddressResource> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/users/1/address",
                            AddressResource.class);

            AddressResource addressResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(addressResource).getUserId(), is(1L));
            assertThat(addressResource.getZipCode(), is("300-0000"));
            assertThat(addressResource.getAddress(), is("Tonde Saitama"));

        }

        @Test
        public void findUsersHavingAddressOfZipCodeNormalTest(){
            ResponseEntity<UserResource[]> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/zipCodes/300-0000/users",
                            UserResource[].class);

            List<UserResource> userResources = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

            assertThat(userResources.size(), is(2));
            userResources.forEach(userResource -> {
                switch (Long.toString(userResource.getUserId())){
                    case "1":
                        assertThat(userResource.getFirstName(), is("hanako"));
                        break;
                    case "3":
                        assertThat(userResource.getFirstName(), is("saburo"));
                        break;
                    default: fail();
                }
            });

        }

        @Test
        public void findUsersNotHavingAddressOfZipCodeNormalTest(){
            ResponseEntity<UserResource[]> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/zipCodes/300-0000/not-users",
                            UserResource[].class);

            List<UserResource> userResources = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

            assertThat(userResources.size(), is(1));
            userResources.forEach(userResource -> {
                if ("2".equals(Long.toString(userResource.getUserId()))) {
                    assertThat(userResource.getFirstName(), is("jiro"));
                } else {
                    fail();
                }
            });

        }

        @Test
        public void updateAddressNormalTest(){
            com.example.lesson5.backend.app.model.Address updateAddress =
                    com.example.lesson5.backend.app.model.Address.builder()
                            .userId(2)
                            .zipCode("100-0000")
                            .address("Tokyo Chiyoda")
                            .build();

            ResponseEntity<AddressResource> updateResponseEntity = testRestTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(testServerURL + "/api/v1/users/{userId}/address")
                            .build().expand(updateAddress.getUserId()).toUri(),
                    HttpMethod.PUT, new HttpEntity<>(updateAddress),
                    AddressResource.class);

            AddressResource addressResource = updateResponseEntity.getBody();

            assertThat(Objects.requireNonNull(addressResource).getZipCode(), is("100-0000"));
            assertThat(addressResource.getAddress(), is("Tokyo Chiyoda"));

        }

        @Test
        public void findEmailsOfUserNormalTest(){
            ResponseEntity<EmailResource[]> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/users/2/emails",
                            EmailResource[].class);

            List<EmailResource> emailResources = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

            assertThat(emailResources.size(), is(1));
            emailResources.forEach(emailResource -> {
                if ("0".equals(Long.toString(emailResource.getEmailNo()))) {
                    assertThat(emailResource.getEmail(), is("jiro.mynavi@debugroom.org"));
                } else {
                    fail();
                }
            });

        }

        @Test
        public void findUserHavingEmailNormalTest(){

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.set("email", "jiro.mynavi@debugroom.org");

            ResponseEntity<UserResource> responseEntity = testRestTemplate
                    .getForEntity(UriComponentsBuilder.fromHttpUrl(
                                            testServerURL + "/api/v1/emails/email/user")
                                    .queryParams(params).build().toUri(),
                            UserResource.class);

            UserResource userResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(userResource).getFirstName(), is("jiro"));

        }

        @Test
        public void addEmailNormalTest(){

            com.example.lesson5.backend.app.model.Email addEmail =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(2)
                            .email("jiro.mynavi2@debugroom.org")
                            .build();

            ResponseEntity<EmailResource> responseEntity = testRestTemplate.postForEntity(
                    UriComponentsBuilder.fromHttpUrl(
                            testServerURL + "/api/v1/users/2/emails/new").build().toUri(),
                    addEmail, EmailResource.class);

            EmailResource emailResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(emailResource).getEmailNo(), is(1L));
            assertThat(emailResource.getEmail(), is("jiro.mynavi2@debugroom.org"));

        }

        @Test
        public void updateEmailNormalTest(){

            com.example.lesson5.backend.app.model.Email updateEmail =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(2)
                            .emailNo(0)
                            .email("jiro.mynavi1@debugroom.org")
                            .build();

            ResponseEntity<EmailResource> responseEntity = testRestTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(
                            testServerURL + "/api/v1/users/2/emails/1").build().toUri(),
                    HttpMethod.PUT, new HttpEntity<>(updateEmail),
                    EmailResource.class);

            EmailResource emailResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(emailResource).getEmail(), is("jiro.mynavi1@debugroom.org"));

        }

        @Test
        public void deleteEmailNormalTest(){

            com.example.lesson5.backend.app.model.Email deleteEmail =
                    com.example.lesson5.backend.app.model.Email.builder()
                            .userId(2)
                            .emailNo(1)
                            .build();

            ResponseEntity<EmailResource> responseEntity = testRestTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(
                            testServerURL + "/api/v1/users/2/emails/1").build().toUri(),
                    HttpMethod.DELETE, new HttpEntity<>(deleteEmail),
                    EmailResource.class);
            EmailResource emailResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(emailResource).getEmail(), is("jiro.mynavi2@debugroom.org"));

        }

        @Test
        public void findGroupsOfUserNormalTest(){
            ResponseEntity<GroupResource[]> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/users/1/groups",
                            GroupResource[].class);

            List<GroupResource> groupResources = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

            assertThat(groupResources.size(), is(1));
            groupResources.forEach(groupResource -> {
                if ("1".equals(Long.toString(groupResource.getGroupId()))) {
                    assertThat(groupResource.getGroupName(), is("GroupB"));
                } else {
                    fail();
                }
            });

        }

        @Test
        public void findUsersOfGroupNormalTest(){
            ResponseEntity<UserResource[]> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/groups/1/users",
                            UserResource[].class);

            List<UserResource> userResources = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

            assertThat(userResources.size(), is(1));
            userResources.forEach(userResource -> {
                if ("1".equals(Long.toString(userResource.getUserId()))) {
                    assertThat(userResource.getFirstName(), is("hanako"));
                } else {
                    fail();
                }
            });

        }

        @Test
        public void findUsersOfNotGroupNormalTest(){
            ResponseEntity<UserResource[]> responseEntity = testRestTemplate
                    .getForEntity(testServerURL + "/api/v1/groups/1/not-users",
                            UserResource[].class);

            List<UserResource> userResources = Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));

            assertThat(userResources.size(), is(2));
            userResources.forEach(userResource -> {
                switch (Long.toString(userResource.getUserId())){
                    case "2":
                        assertThat(userResource.getFirstName(), is("jiro"));
                        break;
                    case "3":
                        assertThat(userResource.getFirstName(), is("saburo"));
                        break;
                    default: fail();
                }
            });

        }

        @Test
        public void addUserToGroupNormalTest(){

            ResponseEntity<UserResource> responseEntity = testRestTemplate.postForEntity(
                    UriComponentsBuilder.fromHttpUrl(
                            testServerURL + "/api/v1/groups/2/users/2").build().toUri(),
                    null, UserResource.class);

            UserResource userResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(userResource).getFirstName(), is("jiro"));

        }

        @Test
        public void deleteUserFromGroupNormalTest(){
            ResponseEntity<UserResource> responseEntity = testRestTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(
                            testServerURL + "/api/v1/groups/2/users/2").build().toUri(),
                    HttpMethod.DELETE, null, UserResource.class);
            UserResource userResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(userResource).getFirstName(), is("jiro"));

        }

        @Test
        public void deleteGroupNormalTest(){
            ResponseEntity<GroupResource> responseEntity = testRestTemplate.exchange(
                    UriComponentsBuilder.fromHttpUrl(
                            testServerURL + "/api/v1/groups/0").build().toUri(),
                    HttpMethod.DELETE, null, GroupResource.class);

            GroupResource groupResource = responseEntity.getBody();

            assertThat(Objects.requireNonNull(groupResource).getGroupName(), is("GroupA"));

        }

    }
}
