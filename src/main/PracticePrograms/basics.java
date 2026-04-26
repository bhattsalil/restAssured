import files.payload;
import io.restassured.path.json.JsonPath;
import org.hamcrest.Matchers;
import org.testng.Assert;

import static io.restassured.RestAssured.*;

public class basics
{
public static void main(String[] args)
{
 // Validate id add plaved API is working as expected

 // Given : All input detail
 // When : Submit the API
 //Then : Validate the response


    //add place -> update place with new address

    baseURI = "https://rahulshettyacademy.com";
    String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
            .body(payload.AddPlace()).when().post("/maps/api/place/add/json")
            .then().log().all().assertThat().statusCode(200).body("scope", Matchers.equalTo("APP"))
            .header("server", "Apache/2.4.52 (Ubuntu)").extract().asString();

    System.out.println(response);
    JsonPath js = new  JsonPath(response); //for parsing json
    String placeId = js.getString("place_id");
    System.out.println(placeId);


    //Update Place
    String newAddress = "70 winter walk, Asia";

    given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
            .body("{\n" +
                    "\"place_id\":\""+placeId+"\",\n" +
                    "\"address\":\""+newAddress+"\",\n" +
                    "\"key\":\"qaclick123\"\n" +
                    "}")
            .when().put("/maps/api/place/update/json")
            .then().assertThat().log().all().statusCode(200).body("msg", Matchers.equalTo("Address successfully updated"));

    //Get Place
   String getPlaceResponse =  given().log().all().queryParam("key", "qaclick123")
            .queryParam("place_id", placeId)
            .when().get("/maps/api/place/get/json")
            .then().assertThat().log().all().statusCode(200).extract().response().asString();

    JsonPath js1 = resuableMethods.rawToJson(getPlaceResponse);
    String actualAddress = js1.getString("address");
    System.out.println(actualAddress);
    Assert.assertEquals(actualAddress, newAddress);

}
}