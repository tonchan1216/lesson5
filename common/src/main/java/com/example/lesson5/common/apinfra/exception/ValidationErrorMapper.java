package com.example.lesson5.common.apinfra.exception;

import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public interface ValidationErrorMapper {

    public static FieldError mapToFieldError(ValidationError validtionError){
        return new FieldError(validtionError.getObjectName(),
                validtionError.getField(), validtionError.getDefaultMessage());
    }

    public static ValidationError map(FieldError fieldError){
        return ValidationError.builder()
                .objectName(fieldError.getObjectName())
                .field(fieldError.getField())
                .defaultMessage(fieldError.getDefaultMessage())
                .build();
    }

    public static List<FieldError> mapToFieldError(List<ValidationError> validationErrors){
        return validationErrors.stream().map(ValidationErrorMapper::mapToFieldError)
                .collect(Collectors.toList());
    }

    public static List<ValidationError> map(List<FieldError> fieldErrors){
        return fieldErrors.stream().map(ValidationErrorMapper::map)
                .collect(Collectors.toList());
    }

}
