package com.example.lesson5.common.apinfra.test.junit;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.example.lesson5.common.apinfra.exception.BusinessException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BusinessExceptionMatcher extends TypeSafeMatcher<BusinessException> {

    BusinessException businessException;

    @Override
    protected boolean matchesSafely(BusinessException e) {
        if(!Objects.equals(businessException.getCode(), e.getCode())){
            return false;
        }
        if (!Objects.deepEquals(businessException.getArgs(), e.getArgs())){
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(businessException);
    }

}
