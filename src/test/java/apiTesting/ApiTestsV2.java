package apiTesting;

import org.apache.http.conn.HttpHostConnectException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import requests.Requests;

import java.io.IOException;
import java.util.HashMap;

public class ApiTestsV2 {
    static String json = "{\"id\":1,\"name\":\"Test-MF\"}";
    static String jsonBadRoleType = "{\"id\":1,\"name\":\"Test-MF\",\"role\": \"AdministratorWrong\"}";
//    static String json1 = "{\"id\":130,\"name\":\"Test-MF_Put\"}"; //new User data for PUT requests
    static String adminInfo = "{\"id\":1,\"name\":\"Test-MF\",\"role\": \"Administrator\"}";

    public static Boolean contentType (HashMap<String,String> entityData ){
        return entityData.get("entityHeaders").contains("Content-Type: application/json");
    }

    private static void checkRoleType(String entityBody) {
        Assert.assertTrue(entityBody.contains("\"role\":\"Administrator\""));
    }

    @Test(description = "First and Second point" )
    public void sendGet () throws IOException {
        Assert.assertTrue(contentType(Requests.sendGet(TestData.url)));
        Assert.assertTrue(Requests.getUserInfo("67", TestData.url));

    }


    @DataProvider
    public Object[][] saveUserData() {
        return new Object[][] { { "\"role\":\"Administrator\""} };
    }

    @Test(description = "Third point", dataProvider = "saveUserData" )
    public void sendPutTest (String data) throws IOException {
        Requests.sendPut("129", '{' + data + '}');

        Assert.assertTrue(Requests.sendGet(TestData.url+"129").get("entityBody").contains(data));
        // after adding TimeConfig - java.net.SocketTimeoutException: Read timed out
    }

    @Test(description = "Fourth point - add new User, get it's ID")
    public static void addNewUserTest () throws IOException {
        String body = Requests.sendPost(TestData.url, json).get("entityBody");
        System.out.println(body);
        System.out.println(Requests.userId);

        Assert.assertTrue(body.contains("{\"id\":"+Requests.userId));//passed
        checkRoleType(body);//false
        contentType(Requests.sendPost(TestData.url, json));//passed
    }

    @Test(description = "Sixth point - create Admin by putting ADMIN in ROLE field")
    public static void addNewAdmin () throws IOException {
        String body = Requests.sendPost(TestData.url, adminInfo).get("entityBody");
        System.out.println(body);
        Assert.assertTrue(body.contains("\"role\":\"Administrator\""));//passed
        checkRoleType(body);//true
        contentType(Requests.sendPost(TestData.url, json));//passed

    }

    @Test(description = "Seventh point - delete option")
    public void deleteUser() throws IOException {
       int statusCode = Requests.sendDelete("129").getStatusLine().getStatusCode();
        Assert.assertTrue(statusCode==(200)|| statusCode==(204));//passed //add argument userIdForDelete
        contentType(Requests.sendPost(TestData.url, json));//false

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

    @Test(description = "12th point - wrong RoleType in PostRequest")
    public void wrongRole () throws IOException {
        String body = Requests.sendPost(TestData.url, jsonBadRoleType).get("statusCode");
        System.out.println(body);
//        Assert.assertTrue(body.indexOf("\"role\":\"Administrator\"") >0);//passed
        Assert.assertTrue(body.contains("401"));//passed
    }

    @Test(description = "13th point - delete incorrect id")
    public void testIncorrectContentType() throws IOException {
        int statusCode = Requests.sendDelete("130").getStatusLine().getStatusCode();
        System.out.println(statusCode);
        Assert.assertTrue(statusCode==(404));//passed

    }
}
