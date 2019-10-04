package com.pm.core.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.machinezoo.noexception.Exceptions;

public class Utils {
    static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String toJson(Object obj) {
        return Exceptions.sneak().get(() -> objectMapper.writeValueAsString(obj));
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return Exceptions.sneak().get(() -> objectMapper.readValue(json, clazz));
    }

    public static <T> T convert(Object from, Class<T> clazz) {
        return objectMapper.convertValue(from, clazz);
    }
}

