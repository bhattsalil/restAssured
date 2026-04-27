package files;

public class JiraPayload {
    public static String CreateBug() {
        return "{\n" +
                "    \"fields\": {\n" +
                "       \"project\":\n" +
                "       {\n" +
                "          \"key\": \"SAB\"\n" +
                "       },\n" +
                "       \"summary\": \"Rest_assured - Bug003_Test \",\n" +
                "       \"issuetype\": {\n" +
                "          \"name\": \"Bug\"\n" +
                "       }\n" +
                "   }\n" +
                "}";
    }


}
