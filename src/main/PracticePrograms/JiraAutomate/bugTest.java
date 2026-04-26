package JiraAutomate;//import files.JiraPayload;
import files.JiraPayload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.File;

import static io.restassured.RestAssured.given;

public class bugTest {

    static String projectPath = System.getProperty("user.dir");
    static File imageFile = new File(projectPath + "/src/images/images.jpg");

    public static void main(String[] args) {

        RestAssured.baseURI = "https://sabbee.atlassian.net";

        //Basic Auth
        String email = "salilbhatt92@gmail.com";
        String apiToken = "ATATT3xFfGF0v2wwiZPPmQkURwsZL8lhqm40lwsV-ofeRXGoaEfAoHVRTGy5muH2PMjon2sMr_RCvcicJy4407WjQnk_FPWYk6PbF7dJzPNfQ8cdTrPXmUfYAeXNAJhwfQWJj9mzVaWR0vSdF97d9sT-uqCFFRAhiInYj8oQUULuI8Z4-WV0cd4=5ADB3AFC";

        String createIssueResponse = given()
                .auth().preemptive().basic(email, apiToken)
                .header("Content-Type", "application/json")
                .body(JiraPayload.CreateBug())
                .log().all()
                .when()
                .post("/rest/api/3/issue")
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .extract()
                .response()
                .asString();

        JsonPath js = new JsonPath(createIssueResponse);

        String issueId = js.getString("id");
        String issueKey = js.getString("key");

        System.out.println("Issue created successfully");
        System.out.println("Issue ID: " + issueId);
        System.out.println("Issue Key: " + issueKey);

        given()
                .auth().preemptive().basic(email, apiToken)
                .pathParam("key", issueKey)
                .header("X-Atlassian-Token", "no-check")
                .multiPart("file", imageFile)
                .log().all()
                .when()
                .post("/rest/api/3/issue/{key}/attachments")
                .then()
                .log().all()
                .assertThat()
                .statusCode(200);

        System.out.println("Attachment uploaded successfully");
    }
}