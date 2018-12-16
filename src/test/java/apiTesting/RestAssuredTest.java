package apiTesting;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class RestAssuredTest {
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://37.59.228.229:3000/api/users/";
    }

    @DataProvider
    public Object[][] newUserData() {
        return new Object[][]{{"{\"name\":\"Test MF\",\"phone\":\"7777\"}"}};
//                { "{\"name\":\"Test AdminMF\",\"phone\":\"7777\",\"role\":\"Administrator\"}" },
//                { "{\"name\":\"Test SupportMF\",\"phone\":\"7777\",\"role\":\"Support\"}" } };

    }

    @Test(description = "PutRequest", dataProvider = "newUserData")
    public void sendPut(String data){

        Response res = RestAssured.given().header("Content-Type", "application/json").body(data).request("PUT", "129");

        res.peek().then().statusCode(200).header("Content-Type", "application/json; charset=utf-8").body("id", Is.isA(Integer.class));
    }
}
