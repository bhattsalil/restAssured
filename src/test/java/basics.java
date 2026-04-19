import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;

public class basics
{
public static void main(String[] args)
{
 // Validate id add plaved API is working as expected

 // Given : All input detail
 // When : Submit the API
 //Then : Validate the response


    RestAssured.baseURI = "https://rahulshettyacademy.com";
    given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
            .body("{\n" +
                    "  \"location\": {\n" +
                    "    \"lat\": -38.383494,\n" +
                    "    \"lng\": 33.427362\n" +
                    "  },\n" +
                    "  \"accuracy\": 50,\n" +
                    "  \"name\": \"NJ\",\n" +
                    "  \"phone_number\": \"(+91) 987 893 3937\",\n" +
                    "  \"address\": \"29, side layout, cohen 09\",\n" +
                    "  \"types\": [\n" +
                    "    \"shoe park\",\n" +
                    "    \"shop\"\n" +
                    "  ],\n" +
                    "  \"website\": \"http://twistedthreads.com\",\n" +
                    "  \"language\": \"French-IN\"\n" +
                    "}").when().post("/maps/api/place/add/json")
            .then().log().all().assertThat().statusCode(200);


}
}