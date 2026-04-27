package JiraAutomate;

import files.JiraPayload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import java.io.File;

import static io.restassured.RestAssured.given;

public class bugTest {

    static String projectPath = System.getProperty("user.dir");
    static File imageFile = new File(projectPath + "/src/test/resources/images/images.jpg");

    public static void main(String[] args) {

        RestAssured.baseURI = "https://sabbee.atlassian.net";

        String email = System.getenv("JIRA_EMAIL");
        String apiToken = System.getenv("JIRA_API_TOKEN");

        if (email == null || apiToken == null) {
            System.out.println("Set JIRA_EMAIL and JIRA_API_TOKEN before running this example.");
            return;
        }

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
