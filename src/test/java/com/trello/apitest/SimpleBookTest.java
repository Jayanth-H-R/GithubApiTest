package com.trello.apitest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

import com.github.javafaker.Faker;

import java.util.HashMap;
import java.util.List;

public class SimpleBookTest {
    public static String baseUrl = "https://simple-books-api.glitch.me";
    public static String token = "";
    public static String bookID;
    Faker faker;

    @Test(priority = 0)
    public void getAvailableBooks() {
        Response reqs = given().header("Content-Type", "application/json")
                .accept(ContentType.JSON).baseUri(baseUrl)
                .when().get("/books")
                .then().log().all().statusCode(200).extract().response();
        List<String> bookIds = reqs.jsonPath().getList("id");  // Assuming the response has a list of books with ids
        System.out.println("Available Book IDs: " + bookIds);
    }

    @Test(priority = 1)
    public void getAuthorization() {
        faker = new Faker();
        HashMap body = new HashMap<>();
        body.put("clientName", "Test");
        body.put("clientEmail", "test" + faker.number().numberBetween(1, 100) + "@gmail.com");
        Response reqs = given().log().all().header("Content-Type", "application/json").body(body)
                .accept(ContentType.JSON).baseUri(baseUrl)
                .when().post("/api-clients/")
                .then().statusCode(201).extract().response();
        token = reqs.getBody().jsonPath().getString("accessToken");
        System.out.println("Token----> " + token);
    }

    @Test(priority = 2)
    public void createAnOrder() {
        faker = new Faker();
        HashMap body = new HashMap<>();
        body.put("bookId", faker.number().numberBetween(1, 6));
        body.put("customerName", faker.name().firstName());
//        System.out.println(token);
        Response reqs = given().header("Content-Type", "application/json").body(body)
                .accept(ContentType.JSON).baseUri(baseUrl).header("Authorization", "Bearer " + token)
                .when().post("/orders")
                .then().statusCode(201).log().all().extract().response();
//        System.out.println(reqs.jsonPath().get("created"));
        Assert.assertTrue(reqs.jsonPath().get("created"));
        bookID = reqs.jsonPath().getString("orderId");
        System.out.println(reqs.getBody().asString());
    }

    @Test(priority = 3)
    public void getBookById() {
        Response reqs = given().header("Content-Type", "application/json")
                .accept(ContentType.JSON).baseUri(baseUrl).header("Authorization", "Bearer " + token)
                .when().get("/orders/" + bookID)
                .then().statusCode(200).log().all().extract().response();
    }

    @Test(priority = 5)
    public void getStatus() {
        Response reqs = given().header("Content-Type", "application/json")
                .accept(ContentType.JSON).baseUri(baseUrl)
                .when().get("/status")
                .then().statusCode(200).extract().response();
    }


    @Test(priority = 6)
    public void deleteAvailableBooks() {
        Response reqs = given().header("Content-Type", "application/json").header("Authorization", "Bearer " + token)
                .accept(ContentType.JSON).baseUri(baseUrl)
                .when().delete("/orders/" + bookID)
                .then().statusCode(204).extract().response();
    }

    @Test(priority = 4)
    public void updateBookById() {
        faker = new Faker();
        HashMap body = new HashMap<>();
        body.put("customerName", faker.name().fullName());
        Response reqs = given().header("Content-Type", "application/json").header("Authorization", "Bearer " + token)
                .body(body)
                .accept(ContentType.JSON).baseUri(baseUrl)
                .when().patch("/orders/" + bookID)
                .then().statusCode(204).extract().response();

    }
}
