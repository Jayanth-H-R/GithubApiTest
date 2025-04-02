package com.trello.apitest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TrelloTestCases {

    public static String baseUrl = "https://api.trello.com/1/";
    String boardId;
    String apiKey = "f70a158dde35aae3f10368cdea25fd83";
    String apiToken = "ATTA6797e1dc5732542c7d07a2fd030aaf5a124b22b3539019a7c4b93b5e7ab22e1eACB95674";
    String toDoList;
    String doingList;
    String doneList;


    @Test(priority = 1)
    public void createBoard() {
        Response resp = given().header("Content-Type", "application/json").accept(ContentType.JSON).baseUri(baseUrl)

                .queryParam("name", "Qaptiol")
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .when().post("boards/")
                .then().statusCode(200).log().all().extract().response();
        boardId = resp.jsonPath().getString("id");

    }

    @Test(priority = 2)
    public void updateBoard() {
        HashMap reqsBody = new HashMap<>();
        reqsBody.put("name", "UpdatingName");
        Response resp = given().header("Content-Type", "application/json").accept(ContentType.JSON).baseUri(baseUrl)
                .body(reqsBody)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .queryParam("prefs/background", "green")
                .queryParam("prefs/backgroundColor", "%23228B22")
                .when().put("boards/" + boardId)
                .then().statusCode(200).log().all().extract().response();
        Assert.assertEquals(resp.jsonPath().getString("name"), "UpdatingName");
    }

@Test(priority = 3)
    public void getListOnBoard(){
        Response resp = given().header("Content-Type", "application/json").accept(ContentType.JSON).baseUri(baseUrl)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .when().get("boards/" + boardId + "/lists")
                .then().statusCode(200).log().all().extract().response();
        List<Object> listID = resp.jsonPath().getList("id");
        List<Object> listName = resp.jsonPath().getList("name");
        HashMap<Object, Object> listValues=new HashMap<>();
        for (int i = 0; i < listID.size(); i++) {
            listValues.put(listName.get(i),listID.get(i));
            
        }

        toDoList= (String) listValues.get("To Do");
        doingList= (String) listValues.get("Doing");
        doneList= (String) listValues.get("Done");
    System.out.println("To do list id: --->"+ toDoList);
    System.out.println("doing list id: --->"+ doingList);
    System.out.println("Done list list id: --->"+ doneList);
        System.out.println(listValues.entrySet());
    }

    @Test(priority = 4)
    public void deleteBoard() {
        Response resp = given().header("Content-Type", "application/json").accept(ContentType.JSON).baseUri(baseUrl)
                .queryParam("key", apiKey)
                .queryParam("token", apiToken)
                .when().delete("boards/" + boardId)
                .then().statusCode(200).log().all().extract().response();
    }




}
