import files.payload;
import io.restassured.path.json.JsonPath;
import org.testng.reporters.XMLConstants;

import java.net.StandardSocketOptions;


/*
1.Print No of courses returned by API
2.Print Purchase Amount
3.Print Title of the first course
4.Print All course titles and their respective Prices
5.Print no of copies sold by RPA Course
6.Verify if Sum of all Course prices matches with Purchase Amount
*/


public class complexJsonParse {
    public static void main(String[] args) {

        JsonPath js = new JsonPath(payload.CoursePrice());

        // Print No of courses returned by API
        int count = js.getInt("courses.size()");
        System.out.println(count);
        System.out.println("______________________________________________");


        //Print Purchase Amount
        int totalAmount = js.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);
        System.out.println("______________________________________________");

        //Print Title of the first course
        String firstCourseTitle = js.get("courses[0].title");
        System.out.println(firstCourseTitle);
        System.out.println("______________________________________________");

        //Print All course titles and their respective Prices
        for (int i = 0; i < count; i++) {
            String courseTitles = js.get("courses[" + i + "].title");
            int CoursePrice = js.getInt("courses[" + i + "].price");
            System.out.println(courseTitles);
            System.out.println(CoursePrice);
            System.out.println("______________________________________________");
        }

        //Print no of copies sold by RPA Course
        for (int i = 0; i < count; i++) {
            String courseTitles = js.get("courses[" + i + "].title");
            if (courseTitles.equalsIgnoreCase("RPA")) {
                //Copies Sold
                int copies = js.getInt("courses[" + i + "].copies");
                System.out.println(copies);
                System.out.println("______________________________________________");
                break;
            }
        }
    }
}
