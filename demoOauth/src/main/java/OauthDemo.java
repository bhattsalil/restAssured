import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class OauthDemo {
    public static void main(String[] args) {

        String client_id = "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com";
        String client_secret = "erZOWM9g3UtwNRj340YYaK_W";
        String grant_type = "client_credentials"; // usually hardcoded
        String scope = "trust";                   // usually hardcoded
        String redirectUri = "https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token";


        String response = given()
                .formParams("client_id", client_id)
                .formParam("client_secret", client_secret)
                .formParam("grant_type", grant_type)
                .formParam("scope", scope)
                .when().log().all()
                .post(redirectUri).asString();
        System.out.println(response);
        JsonPath js = new JsonPath(response);
        String accessToken = js.get("access_token");
        System.out.println("_______________");
        System.out.println(accessToken);

        //get course detail

        String response2 = given()
                .queryParam("access_token", accessToken)
                .when().log().all()
                .get("https://rahulshettyacademy.com/oauthapi/getCourseDetails").asString();
        System.out.println(response2);
    }
}
