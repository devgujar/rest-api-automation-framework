package api.tests;

import api.base.BaseApiClient;
import api.clients.CustomerApiClient;
import io.restassured.response.Response;
import api.base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

public class CustomerApiTest extends BaseTest {
    CustomerApiClient client = new CustomerApiClient();

    @Test
    public void testCustomerCreate() throws Exception {

        String customerId = "1";
        String basePayload = client.getDefaultCustomerPayload();
        client.deleteCustomerIfExists(customerId);

        // 3. Create new record
        Response response = client.createCustomer(basePayload);
        Assert.assertTrue(response.getStatusCode() == 201);
        Assert.assertTrue(response.jsonPath().getString("message")
                        .equalsIgnoreCase("Customer Record Created."), "Failed to create customer record");

        // 4. Try to create same record again (should fail)
        Response duplicateResponse = client.createCustomer(basePayload);
        Assert.assertTrue(duplicateResponse.getStatusCode() == 409);
        String errorMsg = duplicateResponse.getBody().asString();
        Assert.assertTrue(errorMsg.toLowerCase().contains("Record Exists".toLowerCase()) ,
                "Expected duplicate error message, got: " + errorMsg);

        Assert.assertFalse(duplicateResponse.getStatusCode() == 201);
    }

    @Test(dependsOnMethods = "testCustomerCreate")
    public void testCustomerUpdate() throws Exception {
        String customerId = "1";

        String updatePayload = client.getDefaultCustomerPayload()
                .replace("testUser1", "testUser1_Updated")
                .replace("Pune", "Pune_Updated");

        // 1. Update the record
        Response updateResponse = client.updateCustomer(updatePayload);
        Assert.assertEquals(updateResponse.getStatusCode(), 200, "ERROR : Customer update failed");

        // 2. Verify via GET
        Response updatedResponse = client.getCustomer(customerId);
        Assert.assertEquals(updatedResponse.getStatusCode(), 200, "ERROR :Customer record not Found");

        String name = updatedResponse.jsonPath().getString("name");
        String city = updatedResponse.jsonPath().getString("address.city");
        Assert.assertEquals(name, "testUser1_Updated", "Name not updated");
        Assert.assertEquals(city, "Pune_Updated", "City not updated");
    }

    @Test
    public void testDeleteMultiple() throws Exception {
        String customerId = "100";

        String basePayload = client.getCustomerPayload( customerId ,"testUser100","testUser100@testUser100.com");
        client.createCustomerIfNotExists(basePayload, customerId);

        // 3. Delete Record
        Response deleteResponse = client.deleteCustomer(customerId);
        Assert.assertTrue(deleteResponse.getStatusCode() == 200 , "ERROR : Failed to delete existing customer record");
        Assert.assertTrue(deleteResponse.jsonPath().getString("message")
                        .equalsIgnoreCase("Customer Record Deleted.")
                , "ERROR : Failed to delete existing customer record");


        // 4. Try to create same record again (should fail)
        Response duplicateResponse = client.deleteCustomer(customerId);
        Assert.assertTrue(duplicateResponse.getStatusCode() == 404);
        Assert.assertTrue(duplicateResponse.jsonPath().getString("message")
                .equalsIgnoreCase("Customer Record Not Found."));

        Assert.assertFalse(duplicateResponse.getStatusCode() == 200);
        Assert.assertFalse(duplicateResponse.jsonPath().getString("message")
                        .equalsIgnoreCase("Customer Record Deleted."));

    }


    @Test
    public void testAllCustomerResponse() throws Exception {

        String customerId = "1000";
        String payload = client.getCustomerPayload(
                customerId ,
                "testUser1000",
                "testUser1000@testUser100.com",
                "India","Pune","411001");
        client.createCustomerIfNotExists(payload, customerId);


        Response response = BaseApiClient.sendRequest("GET", "/customer");
        List<Map<String, Object>> customers = response.jsonPath().getList("");

        System.out.println(customers);


    }


}
