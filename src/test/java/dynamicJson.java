import files.payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class dynamicJson {

    @Test(dataProvider = "booksData")
    public void addBook(String isbn, String aisle) {

        RestAssured.baseURI = "http://216.10.245.166";

        String response = given()
                .header("Content-Type", "application/json")
                .body(payload.addBook(isbn, aisle))
                .when()
                .post("/Library/Addbook.php")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        JsonPath js = resuableMethods.rawToJson(response);
        String bookId = js.getString("ID");
        System.out.println("Added Book ID: " + bookId);
        // This will run deleteBook method
        deleteBook(bookId);
        //This will Verify Book is delete
        verifyBookDeleted(bookId);
    }

    public void deleteBook(String id) {

        RestAssured.baseURI = "http://216.10.245.166";

        String response = given()
                .header("Content-Type", "application/json")
                .body(payload.deleteBook(id))
                .when()
                .post("/Library/DeleteBook.php")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response()
                .asString();

        JsonPath js = new JsonPath(response);

        String msg = js.getString("msg");

        System.out.println("Delete Response: " + msg);

        Assert.assertEquals(msg, "book is successfully deleted");
    }

    public void verifyBookDeleted(String id) {

        RestAssured.baseURI = "http://216.10.245.166";

        String response = given()
                .header("Content-Type", "application/json")
                .body(payload.deleteBook(id))
                .when()
                .post("/Library/DeleteBook.php")
                .then()
                .assertThat()
                .statusCode(404)
                .extract()
                .response()
                .asString();

        JsonPath js = new JsonPath(response);
        String msg = js.getString("msg");
        System.out.println("Verify Delete Response: " + msg);
        Assert.assertEquals(msg, "Delete Book operation failed, looks like the book doesnt exists");
    }

    @DataProvider(name = "booksData")
    public Object[][] getData() {
        return new Object[][]{
                {"Hello", "1123"},
                {"Zin", "1098"},
                {"Xyzz", "3432"}
        };
    }
}