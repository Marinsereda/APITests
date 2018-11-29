package apiTesting;

import org.testng.Assert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiSteps extends TestBase{
    StringBuffer response;
    int responseCode;


    void sendGetUsers() throws IOException {
        URL obj = new URL(TestData.url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");
        responseCode = con.getResponseCode();
        System.out.println("Response CODE is " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        Assert.assertTrue(responseCode == 200 && response.toString().contains("name")); //worked with link to users

    }

    boolean isGetRequestSuccessful (){
        return responseCode == 200 && response.toString().contains("name");

    }


}
