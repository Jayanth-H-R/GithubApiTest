package com.github.reusables;

import com.github.common.BaseClass;
import io.restassured.response.Response;

import java.util.List;

public class Reusables extends BaseClass {

    public static int getStatusCode(Response resp) {

        return resp.getStatusCode();
    }

    public static String getAttributeValue(Response resp, String attribute) {
        return resp.jsonPath().get(attribute);

    }
    public static List<Object> getAttributeValues(Response resp, String attribute) {
        List<Object> list = resp.jsonPath().getList(attribute);
        return list;

    }

    public static boolean getAttributeBooleanValue(Response resp, String attribute) {
        return resp.jsonPath().getBoolean(attribute);

    }
}
