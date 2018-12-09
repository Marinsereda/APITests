package requests;

import apiTesting.TestData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Requests {

   public static String userId = "";
    String json = "{\"id\":77,\"name\":\"Test-MF\"}";
    String json1 = "{\"id\":130,\"name\":\"Test-MF_Put\"}";

    static CloseableHttpClient httpclient = HttpClients.createDefault();// client which will send requests to API

    protected static String findUserID(String data) {
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(data);
        if (m.find())
            userId = m.group(1);

        return userId;
    }

    public static String getUserInfo( String id) throws IOException {
        String data = sendGet().get("entityBody");

        Pattern findUserInfo = Pattern.compile("\\{[^\\{\\}]*\"id\":\"" + id + "\"[^\\{\\}]*\\}");
        Matcher m = findUserInfo.matcher(data);
        if (m.find()) {
            return m.group();
        } else {
            return "No data found.";
        }
    }


    public static HashMap<String, String> sendPost(String url, String newUserData) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(newUserData);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");


        HttpResponse response = httpclient.execute(httpPost);

//        String entityHeaders = response.getEntity().toString();
//        String entityBody = EntityUtils.toString(response.getEntity());
//
//    System.out.println(entityHeaders);
//    System.out.println(entityBody);
//    System.out.println(findUserID(entityBody));

        Assert.assertTrue(response.getStatusLine().getStatusCode() == (200)); //passed!
        return getEntityData(response);
    }


//    @Test

    public static HashMap<String, String> sendGet() throws IOException {
        HttpGet httpGet = new HttpGet(TestData.url);
        HttpResponse response = httpclient.execute(httpGet);
//        String entityHeaders = response.getEntity().toString();
//        String entityBody = EntityUtils.toString(response.getEntity());
//
//        System.out.println(entityHeaders);
//        System.out.println(entityBody);
//        System.out.println(entityHeaders.contains("application/json"));


//        Assert.assertTrue(response.getStatusLine().getStatusCode()==(200)); //passed!
////        Assert.assertFalse(response.getHeaders("Content-type").toString().contains("application/json"));//passed! - false
//        Assert.assertFalse(entityHeaders.contains("application/json"));//passed! - false

        return getEntityData(response);
    }


    public static HashMap<String, String> getEntityData(HttpResponse response) throws IOException {
        HashMap<String, String> entityData = new HashMap<>();

        String entityHeaders = response.getEntity().toString();
        String entityBody = EntityUtils.toString(response.getEntity());

        System.out.println(entityHeaders);
        System.out.println(entityBody);

        entityData.put("entityHeaders", entityHeaders);
        entityData.put("entityBody", entityBody);
        userId = findUserID(entityBody);

        return entityData;
    }

    @DataProvider
    public Object[][] saveUserData() {
        return new Object[][]{{"\"role\": \"Administrator\""}};
    }

    //    @Test(dataProvider = "saveUserData" )
    public static HashMap<String, String> sendPut(String data) throws IOException {
        HttpPut httpPut = new HttpPut(TestData.url );

//        httpPut.addHeader("Content-type", "application/json");
        httpPut.setEntity(new StringEntity('{' + data + '}', "UTF-8"));

        HttpResponse response = httpclient.execute(httpPut);

        return getEntityData(response);

    }


    @Test
    public void sendDelete() throws IOException {
        HttpDelete request = new HttpDelete(TestData.url);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        System.out.println(httpclient.execute(request).getStatusLine());//no content

        System.out.println(request.getRequestLine());

//        ((HttpResponse) request).setEntity(new StringEntity(json, "UTF-8"));
//        getEntityData(httpclient.execute(request));
    }


}

