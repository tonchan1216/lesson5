package com.example.lesson5.common.apinfra.util;

import java.sql.Timestamp;

public interface DateUtil {

    public static Timestamp now(){
        return new Timestamp(System.currentTimeMillis());
    }

}
