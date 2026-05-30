package resources;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.Properties;

public class Utils
{
    public static RequestSpecification req;
    public RequestSpecification requestSpecification() throws IOException {

        if (req == null) {
            PrintStream log = new PrintStream(new FileOutputStream("logging.txt"));
            req = new RequestSpecBuilder().setBaseUri(getGlobalValue("baseUrl")).addQueryParam("key", getGlobalValue("password"))
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .setContentType(ContentType.JSON).build();
            return req;
        }
        return req;
    }

    public static String getGlobalValue(String Key) throws IOException {
       Properties prop = new Properties();
       String globalPropPath = System.getProperty("user.dir") + File.separator + "src" + File.separator
               + "test" + File.separator + "java" + File.separator + "resources" + File.separator + "Global.properties";
       try (FileInputStream fis = new FileInputStream(globalPropPath)) {
           prop.load(fis);
       }
       return prop.getProperty(Key);

    }

    public String getJsonPath(Response response, String key) {
        return new JsonPath(response.asString()).getString(key);
    }

}
