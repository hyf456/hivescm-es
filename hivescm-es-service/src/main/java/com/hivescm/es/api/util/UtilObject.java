package com.hivescm.es.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Author: hanyf
 * @Description:
 * @Date: Created by in 13:22 2017/10/30
 */
public class UtilObject {
    private static ObjectMapper mapper = new ObjectMapper();

    public static String objectToString(Object object){
        String s = null;
        try {
            s = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static Object stringToObject(String s, Class<?> cla){
        Object o = null;
        try {
            o = mapper.readValue(s, cla);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
}
