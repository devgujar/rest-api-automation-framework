package api.clients;

import api.base.BaseApiClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class CustomerApiClient extends BaseApiClient {

    String CUSTOMER_BASE_PATH = "/customer" ;

    // getSpecificCustomer
    public Response getCustomer( String customerId ){
        Response response = BaseApiClient.sendRequest("GET", CUSTOMER_BASE_PATH + "/"+ customerId);
        return response;
    }

    // getAllCustomers
    public Response getAllCustomers(  ){
        Response response = BaseApiClient.sendRequest("GET", CUSTOMER_BASE_PATH);
        return response;
    }

    // CREATE
    public Response createCustomer(String basePayload){
        Response response = BaseApiClient.sendRequest("POST", CUSTOMER_BASE_PATH, basePayload);
        return response;
    }

    // DELETE
    public Response deleteCustomer( String customerId ){
        Response response = BaseApiClient.sendRequest("DELETE", CUSTOMER_BASE_PATH + "/"+ customerId);
        return response;
    }

    // UPDATE
    public Response updateCustomer( String basePayload ){
        Response response = BaseApiClient.sendRequest("PUT", CUSTOMER_BASE_PATH, basePayload);
        return response;
    }



    // checkIfExists
    public boolean isCustomerRecordExists( String customerId ){
        Response response = BaseApiClient.sendRequest("GET", CUSTOMER_BASE_PATH + "/"+ customerId);
        return response.getStatusCode() == 200 ? true : false;
    }

    // deleteCustomerIfExists
    public void deleteCustomerIfExists( String customerId ){
        if(isCustomerRecordExists(customerId)) {
            deleteCustomer(customerId);
        }
    }

    // createCustomerIfNotExists
    public void createCustomerIfNotExists( String basePayload , String customerId ){
        if(!isCustomerRecordExists(customerId)) {
            createCustomer(basePayload);
        }
    }


    public String getCustomerPayload(String number, String testUser100, String mail) throws JsonProcessingException {
        return getCustomerPayload(number,testUser100,mail,null,null,null );
    }

    public String getCustomerPayload(
            String number,
            String testUser100,
            String mail,
            String country,
            String city,
            String pin_code
    ) throws JsonProcessingException {

        Map<String, Object> address = new HashMap<>();
        address.put("country", country);
        address.put("city", city);
        address.put("pin_code", pin_code);

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", number);
        payload.put("name", testUser100);
        payload.put("email", mail);
        payload.put("address", address);

        ObjectMapper mapper = new ObjectMapper();
        String basePayload = mapper.writeValueAsString(payload);
        return basePayload;
    }

    public String getDefaultCustomerPayload() throws JsonProcessingException {
        return getCustomerPayload(
                "1",
                "testUser1",
                "testUser1@testUser1.com",
                "India",
                "Pune",
                "411001");
    }

}
