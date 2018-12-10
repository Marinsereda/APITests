package apiTesting;

import org.apache.http.HttpResponse;
import org.apache.http.conn.HttpHostConnectException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import requests.Requests;


import java.io.IOException;

public class ApiTestsV2 {
    static String json = "{\"id\":1,\"name\":\"Test-MF\"}";
    static String adminInfo = "{\"id\":1,\"name\":\"Test-MF\", \"role\": \"Administrator\"}";


    @Test(description = "First requirement - all requests have Content-Type: application/json ")
    public void contentTypeTest() throws IOException {
        Requests.sendGet(TestData.url);
//        Assert.assertTrue(Requests.sendGet(TestData.url).get("entityHeaders").contains("Content-Type: text/html"));

        Assert.assertTrue(Requests.sendGet(TestData.url).get("entityHeaders").indexOf("Content-Type: text/html") <0);
        Assert.assertTrue(Requests.sendPost(TestData.url, json).get("entityHeaders").contains("Content-Type: application/json"));

    }

    @DataProvider
    public Object[][] saveUserData() {
        return new Object[][] { { "\"role\": \"Administrator\"" } };
    }

    @Test( dataProvider = "saveUserData" )
    public void sendPutTest (String data) throws IOException {
        Requests.sendPut("129", data);
        Assert.assertTrue(Requests.sendGet(TestData.url+"129").get("entityBody").indexOf(data) >0);
        // failed- org.apache.http.NoHttpResponseException: 37.59.228.229:3000 failed to respond
    }

    @Test(description = "Forth point - add new User, get it's ID")
    public static void addNewUserTest () throws IOException {
        String body = Requests.sendPost(TestData.url, json).get("entityBody");
        System.out.println(body);
        System.out.println(Requests.userId);

        Assert.assertTrue(body.contains("{\"id\":"+Requests.userId));//passed
        checkRoleType(body);//false
    }

    @Test(description = "Sixth point - create Admin by putting ADMIN in ROLE field")
    public static void addNewAdmin () throws IOException {
        String body = Requests.sendPost(TestData.url, adminInfo).get("entityBody");
        System.out.println(body);
//        Assert.assertTrue(body.indexOf("\"role\":\"Administrator\"") >0);//passed
        Assert.assertTrue(body.contains("\"role\":\"Administrator\""));//passed
        checkRoleType(body);//true

    }

    private static void checkRoleType(String entityBody) {
        Assert.assertTrue(entityBody.contains("\"role\":\"Administrator\""));
    }




    @Test(description = "Seventh point - delete option")
    public void deleteUser() throws IOException {
       int statusCode = Requests.sendDelete("130").getStatusLine().getStatusCode();
        Assert.assertTrue(statusCode==(200)|| statusCode==(204));//passed //add argument userIdForDelete

    }

    @Test(description = "Eighth point - change PORT = 20007")
    public void testNewPort() throws IOException {
        boolean connection=true;
        try {
            Requests.sendGetNewPort(TestData.urlTest);
//
        }
        catch (HttpHostConnectException e){
//            e.printStackTrace();
            System.out.println("Connection refused ");
            connection=false;
        }

        Assert.assertFalse(connection);//passed

    }

    @Test(description = "13th point - delete incorrect id")
    public void testIncorrectContentType() throws IOException {
        int statusCode = Requests.sendDelete("130").getStatusLine().getStatusCode();
        System.out.println(statusCode);
        Assert.assertTrue(statusCode==(404));//passed

    }












}
