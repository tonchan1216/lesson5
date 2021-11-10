package com.example.lesson5.common.apinfra.test.junit;

import java.util.Objects;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import com.example.lesson5.common.apinfra.exception.SystemException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SystemExceptionMatcher extends TypeSafeMatcher<SystemException> {

    SystemException systemException;

    @Override
    protected boolean matchesSafely(SystemException e) {
        if(!Objects.equals(systemException.getCode(), e.getCode())){
            return false;
        }
        if(!Objects.deepEquals(systemException.getArgs(), e.getArgs())){
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendValue(systemException);
    }

}
