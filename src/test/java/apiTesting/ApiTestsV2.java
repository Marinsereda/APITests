package apiTesting;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import requests.Requests;


import java.io.IOException;

public class ApiTestsV2 {
    static String json = "{\"id\":1,\"name\":\"Test-MF\"}";


    @Test(description = "First requirement - all requests have Content-Type: application/json ")
    public void contentTypeTest() throws IOException {
        Requests.sendGet();
//        Assert.assertTrue(Requests.sendGet().get("entityHeaders").contains("^{\\[Content-Type: text/html$\\]\\}"));

        Assert.assertTrue(Requests.sendGet().get("entityHeaders").indexOf("Content-Type: text/html") <0);
        Assert.assertTrue(Requests.sendPost(TestData.url, json).get("entityHeaders").indexOf("Content-Type: application/json") >0);

    }

    @DataProvider
    public Object[][] saveUserData() {
        return new Object[][] { { "\"role\": \"Administrator\"" } };
    }

    @Test( dataProvider = "saveUserData" )
    public void sendPutTest (String data) throws IOException {
        Requests.sendPut(data);
        Assert.assertTrue(Requests.sendGet().get("entityBody").indexOf(data) >0);
        // failed- org.apache.http.NoHttpResponseException: 37.59.228.229:3000 failed to respond
    }

    @Test(description = "Forth point - add new User, get it's ID")
    public static void addNewUserTest () throws IOException {
        String body = Requests.sendPost(TestData.url, json).get("entityBody");
        System.out.println(body);
        System.out.println(Requests.userId);

        Assert.assertTrue(body.contains("{\"id\":"+Requests.userId));
    }



//    @Test
//    public void deleteUser() throws IOException {
//        String body =Requests.sendDelete(TestData.url, json).get("entityBody");
//        System.out.println(body);
//
//    }












}
