import io.restassured.response.Response;
import pojo.GetCourse;
import pojo.OAuthTokenResponse;

import static io.restassured.RestAssured.*;

public class OauthDemo {

    private static final String DEFAULT_TOKEN_URL = "https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token";
    private static final String DEFAULT_COURSE_URL = "https://rahulshettyacademy.com/oauthapi/getCourseDetails";

    public static void main(String[] args) {

        String clientId = readEnvOrDefault("OAUTH_CLIENT_ID", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com");
        String clientSecret = readEnvOrDefault("OAUTH_CLIENT_SECRET", "erZOWM9g3UtwNRj340YYaK_W");
        String grantType = readEnvOrDefault("OAUTH_GRANT_TYPE", "client_credentials");
        String scope = readEnvOrDefault("OAUTH_SCOPE", "trust");
        String tokenUrl = readEnvOrDefault("OAUTH_TOKEN_URL", DEFAULT_TOKEN_URL);
        String courseUrl = readEnvOrDefault("OAUTH_COURSE_URL", DEFAULT_COURSE_URL);

        Response tokenResponse = given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("client_id", clientId)
                .formParam("client_secret", clientSecret)
                .formParam("grant_type", grantType)
                .formParam("scope", scope)
                .when()
                .post(tokenUrl)
                .then()
                .extract()
                .response();

        if (tokenResponse.statusCode() != 200) {
            throw new IllegalStateException("Token request failed with status " + tokenResponse.statusCode()
                    + ". Response: " + tokenResponse.asString());
        }

        OAuthTokenResponse tokenPayload = tokenResponse.as(OAuthTokenResponse.class);
        String accessToken = tokenPayload.getAccessToken();

        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalStateException("Token response does not contain access_token. Error: "
                    + tokenPayload.getError() + ", description: " + tokenPayload.getErrorDescription()
                    + ", raw response: " + tokenResponse.asString());
        }

        //get course detail

        GetCourse gc = given()
                .queryParam("access_token", accessToken)
                .when()
                .get(courseUrl)
                .then()
                .statusCode(200)
                .extract()
                .as(GetCourse.class);

        System.out.println(gc.getLinkedin());
        System.out.println(gc.getInstructor());
    }

    private static String readEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value;
    }
}
