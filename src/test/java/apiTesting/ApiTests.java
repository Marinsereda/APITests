package apiTesting;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class ApiTests extends TestBase {
    public WebDriver browser;
    static ApiSteps a= new ApiSteps();

    static String url = "http://37.59.228.229:3000/API/users";


    @Test(description = "Try test the request GET")
    public void testGetUsersRequest() throws IOException {
        a.sendGetUsers();
        Assert.assertTrue(a.isGetRequestSuccessful()); //worked with link to users

    }
}




