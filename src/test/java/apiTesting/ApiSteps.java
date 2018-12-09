package apiTesting;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiSteps {

    static CloseableHttpClient httpclient = HttpClients.createDefault();// client which will send requests to API

    //@Parameters("apiUrl")
    @Test(description = "REST api")
    public static String[] sendGet(String URL) throws IOException {
        HttpGet httpGet = new HttpGet(TestData.url);
        return getData(httpclient.execute(httpGet));
    }


    public static String [] sendPost(String URL, String data) throws IOException {
        HttpPost request = new HttpPost(URL);
//        request.setEntity(new StringEntity(data, "UTF-8"));

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "John"));
        params.add(new BasicNameValuePair("password", "pass"));
        request.setEntity(new UrlEncodedFormEntity(params));

        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        return getData(httpclient.execute(request));



    }

    public static String[] sendPut(String URL, String data) throws IOException {
        HttpPut request = new HttpPut(URL);
        request.setEntity(new StringEntity(data, "UTF-8"));
        return getData(httpclient.execute(request));
    }

    public static String[]sendDelete(String URL, String data) throws IOException {
        HttpDelete request = new HttpDelete(URL);
        ((HttpResponse) request).setEntity(new StringEntity(data, "UTF-8"));
        return getData(httpclient.execute(request));
    }

    private static String[] getData(HttpResponse response) throws IOException {
        HttpEntity entity = response.getEntity();

        String[] responseData = new String[2];

        responseData[0] = response.getEntity().toString();
        responseData[1] = entity != null ? EntityUtils.toString(entity) : "No response data.";
        return responseData;
    }

    public static Header[] getHeaders (HttpResponse response){
        Header[] headers = response.getAllHeaders();
        for (Header header : headers) {
            System.out.println("Key : " + header.getName()
                    + " ,Value : " + header.getValue());

        }

        System.out.println("\nGet Response Header By Key ...\n");
        String server = response.getFirstHeader("Server").getValue();

        if (server == null) {
            System.out.println("Key 'Server' is not found!");
        } else {
            System.out.println("Server - " + server);
        }

        System.out.println("\n Done");
        return headers;

    }

    public static String getUserInfo(String URL, String id) throws IOException {
        String data = sendGet(URL)[0];

        Pattern findUserInfo = Pattern.compile("\\{[^\\{\\}]*\"id\":\"" + id + "\"[^\\{\\}]*\\}");
        Matcher m = findUserInfo.matcher( data);
        if (m.find()) {
            return m.group();
        } else {
            return "No data found.";
        }
    }


//    StringBuffer response;
//    int responseCode;
//    URL obj = new URL(TestData.url);
//    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//    public ApiSteps() throws IOException {
//    }
//
//
//    void sendGetUsers() throws IOException {
////        URL obj = new URL(TestData.url);
////        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        con.setRequestMethod("GET");
//        con.setRequestProperty("Content-Type", "application/json");
//        responseCode = con.getResponseCode();
//        System.out.println("Response CODE is " + responseCode);
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        //print result
//        System.out.println(response.toString());
//
//        Assert.assertTrue(responseCode == 200 && response.toString().contains("name")); //worked with link to users
//
//    }
//
//    boolean isGetRequestSuccessful (){
//        return responseCode == 200 && response.toString().contains("name");
//
//    }



}
