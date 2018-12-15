package requests;

import apiTesting.TestData;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Requests {

   public static String userId = "";

    static RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
            .setSocketTimeout(5000).build();//get from Internet - need explanation
    //    static HttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();

    static CloseableHttpClient httpclient = HttpClients.createDefault();// client which send requests to APIfinal
    //request.setConfig(requestConfig);

    Header []headers = new Header[2];







    protected static String findUserID(String data) {
        Matcher m = Pattern.compile("\"id\":(\\d+)").matcher(data);
        if (m.find())
            userId = m.group(1);

        return userId;
    }

    public static Boolean getUserInfo( String id, String URL) throws IOException {
        String data = sendGet(URL).get("entityBody");

        if(data.contains(id)){
            System.out.println( "Id is found."+ id);
           return true;}
        else{
            System.out.println( "No data found.");
            return false;}

//        Pattern findUserInfo = Pattern.compile("\\{[^\\{\\}]*\"id\":\"" + id + "\"[^\\{\\}]*\\}");
//        Matcher m = findUserInfo.matcher(data);
//        if (m.find()) {
//            return m.group();
//        } else {
//            return "No data found.";
//        }
    }


    public static HashMap<String, String> sendPost(String url, String newUserData) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(newUserData);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        HttpResponse response = httpclient.execute(httpPost);

        return getEntityData(response);
    }


    public static HashMap<String, String> sendGet(String URL) throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        httpGet.setHeader("Content-type", "application/json");

//        if (headers != null){
//            httpGet.setHeaders(headers);
//        }
        HttpResponse response = httpclient.execute(httpGet);

        return getEntityData(response);
    }


    public static HashMap<String, String> getEntityData(HttpResponse response) throws IOException {
        HashMap<String, String> entityData = new HashMap<>();

        String entityHeaders = response.getEntity().toString();
        String entityBody = EntityUtils.toString(response.getEntity());
        String statusCode = String.valueOf(response.getStatusLine().getStatusCode());

        System.out.println(entityHeaders);
        System.out.println(entityBody);

        entityData.put("entityHeaders", entityHeaders);
        entityData.put("entityBody", entityBody);
        entityData.put("statusCode", statusCode);
        userId = findUserID(entityBody);

        return entityData;
    }

    public static HashMap<String, String> sendPut(String userIdPut, String data) throws IOException  {
        HttpPut httpPut = new HttpPut(TestData.url+userIdPut );
        httpPut.setConfig(requestConfig);

//        httpPut.setHeader("Accept", "application/json");
        httpPut.setHeader("Content-type", "application/json");
        httpPut.setEntity(new StringEntity(data, "UTF-8"));

        HttpResponse response = httpclient.execute(httpPut);
        System.out.println(getEntityData(response).get("entityBody"));

        return getEntityData(response);
    }


    @Test
    public static HttpResponse sendDelete(String userId) throws IOException {
        HttpDelete request = new HttpDelete(TestData.url+userId);

//        request.setHeader("Content-type", "application/json");//is it important to set header ind DELETE request?
//        System.out.println(httpclient.execute(request).getStatusLine().getStatusCode()==(204));//no content

        return httpclient.execute(request);
    }

    public static HttpResponse sendGetNewPort(String URL) throws IOException {
        HttpGet httpGet = new HttpGet(URL);
        HttpResponse response = httpclient.execute(httpGet);

        return response;
    }



}

