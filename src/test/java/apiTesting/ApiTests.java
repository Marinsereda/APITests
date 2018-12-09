package apiTesting;

import org.apache.http.HttpRequest;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApiTests {
    String baseURL = "http://37.59.228.229:3000/API/users/";
    String userId = "";
    String data1= "\"id\":\"1\",\"name\":\"John\"";

    private void findUserID(String data) {
        Matcher m = Pattern.compile("\"id\":\"(\\d+)").matcher(data);
        if (m.find())
            userId = m.group(1);
    }

    private void checkContentType(String headers) {
        Assert.assertTrue(headers.contains("Content-Type: application/json"));
    }

    @Test(description = "Second requirement - getting user list")
    void getUsers() throws IOException {
        String[] responseData = ApiSteps.sendGet(baseURL);
        System.out.println(responseData[1]);
        System.out.println(responseData[0]);
        System.out.println(responseData[1].indexOf("\"id\""));

//        Assert.assertTrue(responseData[1].contains("\\[^\\{^\\\"id\":\"\"\\}\\]"));
        Assert.assertTrue(responseData[1].contains("\"id\""));

        findUserID(responseData[1]);
        checkContentType(responseData[0]);
    }

    @DataProvider
    public Object[][] saveUserData() {
        return new Object[][] { { "\"role\": \"Administrator\"", true }, { "sadadd", false } };
    }

    @Test(description = "Third requirement - saving users", dataProvider = "saveUserData")
    void saveUser(String data, Boolean expectedResult) throws IOException {
        String[] responseData = ApiSteps.sendPut(baseURL + userId, '{' + data + '}');
        Assert.assertEquals((Boolean) ApiSteps.getUserInfo(baseURL, userId).contains(data), expectedResult);
        checkContentType(responseData[0]);
    }

    @Test(description = "Fourth requirement - adding new user")
    void addUser () throws IOException {
        String[] response = ApiSteps.sendPost(baseURL, data1);
        System.out.println(response[1]);
        System.out.println(response[0]);
        Assert.assertTrue(response.equals(data1));

    }

//    @Test
//    void sendPostNew() throws IOException, URISyntaxException {
//       String responseData = Requests.sendPost();
//        System.out.println(responseData);
//        Assert.assertTrue(responseData.contains("John"));
//    }

//    @Test(description = "Fifth requirement - adding new user")





//    static ApiSteps a;
//
//    static {
//        try {
//            a = new ApiSteps();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    static String url = "http://37.59.228.229:3000/API/users";
//
//
//    @Test(description = "Try test the request GET")
//    public void testGetUsersRequest() throws IOException {
//        a.sendGetUsers();
//        Assert.assertTrue(a.isGetRequestSuccessful()); //worked with link to users
//
//    }
    }





