package com.github.testapis;

import com.github.common.BaseClass;

import com.github.javafaker.Faker;
import com.github.reusables.Reusables;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import io.restassured.specification.RequestSpecification;
import org.json.JSONTokener;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;



import static io.restassured.RestAssured.given;

public class GithubTestCases extends BaseClass {


    public RequestSpecification setUp() {
        RequestSpecBuilder spec = new RequestSpecBuilder();

        return spec.setAccept(ContentType.JSON).addHeader("Content-Type", "application/json").setBaseUri(baseUrl)
                .addHeader("Authorization", "Bearer " + token).build();
    }

    @Test(priority = 0)
    public void getAllAuthenticatedRepos() {
        Response resp = given(setUp()).log().headers()
                .when().get("user/repos")
                .then().log().all().extract().response();
        Assert.assertEquals(Reusables.getStatusCode(resp), 200);
    }

    @Test(priority = 1)
    public void createAuthenticatedRepo() throws IOException {
        File requestBody = new File("./src/main/resources/CreateRepo.json");
        Response resp = given(setUp()).log().headers().body(requestBody)
                .when().post("user/repos")
                .then().log().all().extract().response();
        Assert.assertEquals(Reusables.getStatusCode(resp), 201);
        Assert.assertEquals(Reusables.getAttributeValue(resp,"name"),"Hello-World");
    }

    @Test(priority = 2)
    public void updateAuthenticatedRepo() {
        Response resp = given(setUp()).log().all().body("{\n" +
                        "    \"has_issues\":false,\"has_projects\":true\n" +
                        "}")
                .when().patch("repos/Jayanth-H-R/Hello-World")
                .then().statusCode(200).log().all().extract().response();
        Assert.assertEquals(Reusables.getStatusCode(resp), 200);
        Assert.assertEquals(Reusables.getAttributeBooleanValue(resp,"has_issues"),false);

    }

    @Test(priority = 3)
    public void deleteAuthenticatedRepo() {
        Response resp = given(setUp()).log().all()
                .when().delete("repos/Jayanth-H-R/Hello-World")
                .then().log().all().extract().response();
        Assert.assertEquals(Reusables.getStatusCode(resp), 204);

    }

}
