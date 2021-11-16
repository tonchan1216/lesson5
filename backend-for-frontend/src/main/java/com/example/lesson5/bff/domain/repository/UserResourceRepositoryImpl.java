package com.example.lesson5.bff.domain.repository;

import com.example.lesson5.common.apinfra.exception.*;
import com.example.lesson5.common.web.model.UserResource;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Repository
public class UserResourceRepositoryImpl implements UserResourceRepository{

    private static final String SERVICE_NAME = "/backend";
    private static final String API_VERSION = "/api/v1";

    @Autowired
    MessageSource messageSource;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RestOperations restOperations;

    @Override
    public UserResource findOne(Long userId) throws BusinessException{
        String endpoint = SERVICE_NAME + API_VERSION + "/users/{userId}";
        try{
            return restOperations.getForObject(
                    UriComponentsBuilder.fromPath(endpoint).build(userId), UserResource.class);
        }catch (HttpClientErrorException e){
            try{
                ErrorResponse errorResponse = objectMapper.readValue(
                        e.getResponseBodyAsString(), ErrorResponse.class);
                if(errorResponse instanceof BusinessExceptionResponse){
                    throw ((BusinessExceptionResponse)errorResponse).getBusinessException();
                }else{
                    String errorCode = "SE0002";
                    throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                            new String[]{endpoint}, Locale.getDefault()), e);
                }
            }catch (IOException e2){
                String errorCode = "SE0002";
                throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                        new String[]{endpoint}, Locale.getDefault()), e2);
            }
        }catch (HttpServerErrorException e){
            String errorCode = "SE0001";
            throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                    new String[]{endpoint}, Locale.getDefault()), e);
        }
    }

    @Override
    public List<UserResource> findAll(){
        String endpoint = SERVICE_NAME + API_VERSION + "/users";
        try{
            return Arrays.asList(Objects.requireNonNull(restOperations.getForObject(endpoint, UserResource[].class)));
        } catch (HttpClientErrorException e){
            String errorCode = "SE0002";
            throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                    new String[]{endpoint}, Locale.getDefault()), e);
        } catch (HttpServerErrorException e){
            String errorCode = "SE0001";
            throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                    new String[]{endpoint}, Locale.getDefault()), e);
        }
    }

    @Override
    public UserResource save(UserResource userResource) throws BusinessException{
        String endpoint = SERVICE_NAME + API_VERSION + "/users/new";
        try{
            return restOperations.postForObject(endpoint, userResource, UserResource.class);
        }catch (HttpClientErrorException e){
            try{
                ErrorResponse errorResponse = objectMapper.readValue(
                        e.getResponseBodyAsString(), ErrorResponse.class);
                if(errorResponse instanceof BusinessExceptionResponse){
                    throw ((BusinessExceptionResponse)errorResponse).getBusinessException();
                }else if (errorResponse instanceof ValidationErrorResponse){
                    List<ValidationError> validationErrors = ((ValidationErrorResponse)errorResponse).getValidationErrors();
                    String errorCode = "BE0001";
                    throw new BusinessException(errorCode, messageSource.getMessage(errorCode,
                            new String[]{endpoint}, Locale.getDefault()), validationErrors);
                }else{
                    String errorCode = "SE0002";
                    throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                            new String[]{endpoint}, Locale.getDefault()), e);
                }
            }catch (IOException e2){
                String errorCode = "SE0002";
                throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                        new String[]{endpoint}, Locale.getDefault()), e2);
            }
        }catch (HttpServerErrorException e){
            String errorCode = "SE0001";
            throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                    new String[]{endpoint}, Locale.getDefault()), e);
        }
    }

    @Override
    public UserResource delete(Long userId) throws BusinessException {
        String endpoint = SERVICE_NAME + API_VERSION + "/users/{userId}";
        try{
            return restOperations.exchange(UriComponentsBuilder
                    .fromPath(endpoint).build(userId).toString(), HttpMethod.DELETE,
                    null, UserResource.class).getBody();
        }catch (HttpClientErrorException e){
            try{
                ErrorResponse errorResponse = objectMapper.readValue(
                        e.getResponseBodyAsString(), ErrorResponse.class);
                if(errorResponse instanceof BusinessExceptionResponse){
                    throw ((BusinessExceptionResponse)errorResponse).getBusinessException();
                }
                String errorCode = "SE0002";
                throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                        new String[]{endpoint}, Locale.getDefault()), e);
            }catch (IOException e2){
                String errorCode = "SE0002";
                throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                        new String[]{endpoint}, Locale.getDefault()), e2);
            }
        }catch (HttpServerErrorException e){
            String errorCode = "SE0001";
            throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                    new String[]{endpoint}, Locale.getDefault()), e);
        }
    }

    @Override
    public UserResource findByLoginId(String loginId) throws BusinessException {
        String endpoint = SERVICE_NAME + API_VERSION + "/users/loginId";
        try{
            return restOperations.getForEntity(UriComponentsBuilder.fromPath(
                    endpoint).queryParam("loginId", loginId).build().toUriString()
                    ,UserResource.class).getBody();
        }catch (HttpClientErrorException e){
            try{
                ErrorResponse errorResponse = objectMapper.readValue(
                    e.getResponseBodyAsString(), ErrorResponse.class);
                if(errorResponse instanceof BusinessExceptionResponse){
                    throw ((BusinessExceptionResponse)errorResponse).getBusinessException();
                }
                String errorCode = "SE0002";
                throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                        new String[]{endpoint}, Locale.getDefault()), e);
            } catch (IOException e2){
                String errorCode = "SE0002";
                throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                        new String[]{endpoint}, Locale.getDefault()), e2);
            }
        }catch (HttpServerErrorException e){
            String errorCode = "SE0001";
            throw new SystemException(errorCode, messageSource.getMessage(errorCode,
                    new String[]{endpoint}, Locale.getDefault()), e);
        }
    }

}
