package apiTesting;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class apiTests extends TestBase {
    public WebDriver browser;

    static String url = "http://37.59.228.229:3000/API/users";

//    apiTests a = new apiTests();

//    a.sendGet;
//    a.sendPost;

@Test(description = "Try test the request GET")
     void sendGet() throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        int responseCode = con.getResponseCode();
         System.out.println("Response CODE is " + responseCode);

         BufferedReader in = new BufferedReader(
                 new InputStreamReader(con.getInputStream()));
         String inputLine;
         StringBuffer response = new StringBuffer();

         while ((inputLine = in.readLine()) != null) {
             response.append(inputLine);
         }
         in.close();

         //print result
         System.out.println(response.toString());

    Assert.assertTrue(responseCode==200 && response.toString().contains("name")); //worked with link to users

     }






















}



