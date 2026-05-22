package api.tests;

import io.restassured.response.Response;
import api.base.BaseTest;
import api.core.ApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class SampleApiTest extends BaseTest {
    @Test
    public void testGetPosts() {
        Response response = ApiClient.sendRequest("GET", "/posts");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("userId"));


        String title = response.jsonPath().getString("find { it.userId == 5 }.title");
        Assert.assertEquals(title, "non est facere");

        // Use JsonPath to filter
        List<String> titles = response.jsonPath().getList("findAll { it.userId == 5 }.title");
        Assert.assertTrue(titles.contains("non est facere"), "userId 5 does not have the expected title!");
    }

    @Test
    public void testDeletePost() {

        // Attempt to delete post with id 1
        Response response = ApiClient.sendRequest("DELETE", "/posts/1");

        // JSONPlaceholder returns 200 for DELETE, but some APIs may return 204
        Assert.assertTrue(response.getStatusCode() == 200 || response.getStatusCode() == 204,
                "Expected status code 200 or 204, but got: " + response.getStatusCode());

        // JSONPlaceholder returns an empty body for DELETE
        Assert.assertTrue(response.getBody().asString().isEmpty() || response.getBody().asString().equals("{}"),
                "Expected empty response body for DELETE");
    }
}
