package com.github.common;

import utilities.FileUtility;

import java.io.IOException;


public class BaseClass {
    public static String token;
    //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
    // new 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
    public static String baseUrl;


    static {
        try {
            baseUrl = FileUtility.readData("baseUrl");
            token = FileUtility.readData("token");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
