package stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.APIResources;
import resources.TestDataBuild;
import resources.Utils;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class StepDefinitions extends Utils {
    RequestSpecification res;
    ResponseSpecification respec;
    Response response;
    TestDataBuild data = new TestDataBuild();
    static String place_id;


    @Given("Add Place Payload with {string} {string} {string} {string} {string}")
    public void addPlacePayloadWith(String name, String language, String address, String phone, String website) throws IOException {

        res = given().log().all().spec(requestSpecification())
                .body(data.addPlacePayLoad(name, language, address, phone, website));
    }

    @When("User calls {string} with {string} http request")
    public void userCallsWithHttpRequest(String resource, String method) {
        APIResources resourceAPI = APIResources.valueOf(resource);
        System.out.println(resourceAPI.getResource());
        respec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

       if ("POST".equalsIgnoreCase(method)) {
           response = res.when().post(resourceAPI.getResource());
       }
       else if ("GET".equalsIgnoreCase(method)) {
           response = res.when().get(resourceAPI.getResource());
       }
    }

    @Then("API call got success with status code {int}")
    public void apiCallGotSuccessWithStatusCode(int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode());
    }

    @And("{string} in response body is {string}")
    public void inResponseBodyIs(String keyValue, String ExpectedValue) {
       assertEquals(ExpectedValue, getJsonPath(response, keyValue));

    }

    @Then("verify place_id created maps to {string} using {string}")
    public void verify_place_id_created_maps_to_using(String expectedName, String resource) throws IOException {

        place_id = getJsonPath(response, "place_id");
        res = given().spec(requestSpecification()).queryParam("place_id", place_id);
        userCallsWithHttpRequest(resource, "GET");
        String actualName = getJsonPath(response, "name");
        assertEquals(expectedName, actualName);

    }

    @Given("DeletePlace Payload")
    public void delete_place_payload() throws IOException {
        res = given().log().all().spec(requestSpecification())
                .body(data.deletePlacePayload(place_id));
    }
}
