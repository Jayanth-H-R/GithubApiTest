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
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utilities.ExtentReports;

import java.io.File;
import java.io.IOException;


import static io.restassured.RestAssured.given;

@Listeners(ExtentReports.class)
public class GithubTestCases extends BaseClass {

    public static String repoName;
    public static String ownerName;

    public RequestSpecification setUp() {
        RequestSpecBuilder spec = new RequestSpecBuilder();

        return spec.setAccept(ContentType.JSON).addHeader("Content-Type", "application/json").setBaseUri(baseUrl)
                .addHeader("Authorization", "Bearer " + token).build();
    }

    @Test(priority = 0)
    public void getAllAuthenticatedRepos() {
        Response resp = given(setUp()).log().headers()
                .when().get("user/repos")
                .then().extract().response();
        Assert.assertEquals(Reusables.getStatusCode(resp), 200);
    }

    @Test(priority = 1)
    public void createAuthenticatedRepo() throws IOException {
        File requestBody = new File("./src/main/resources/CreateRepo.json");
        Response resp = given(setUp()).log().headers().body(requestBody)
                .when().post("user/repos")
                .then().log().all().extract().response();
        Assert.assertEquals(Reusables.getStatusCode(resp), 201);
        repoName = Reusables.getAttributeValue(resp, "name");
        ownerName = Reusables.getAttributeValue(resp, "owner.login");
        System.out.println("Owner name of the account:---------------> " + ownerName);
        System.out.println("Repo name created:-----------> " + repoName);
        Assert.assertEquals(Reusables.getAttributeValue(resp, "name"), repoName);
    }


    @Test(priority = 2)
    public void updateAuthenticatedRepo() {
        File requestBody = new File("./src/main/resources/updateRepo.json");

        Response resp = given(setUp()).log().all().body(requestBody)
                .when().patch("repos/" + ownerName + "/" + repoName)
                .then().statusCode(200).log().all().extract().response();
        Assert.assertEquals(Reusables.getStatusCode(resp), 200);
        Assert.assertEquals(Reusables.getAttributeBooleanValue(resp, "has_issues"), false);
    }

    @Test(priority = 3)
    public void deleteAuthenticatedRepo() {
        Response resp = given(setUp()).log().all()
                .when().delete("repos/" + ownerName + "/" + repoName)
                .then().log().all().extract().response();
        Assert.assertEquals(Reusables.getStatusCode(resp), 204);

    }

}
