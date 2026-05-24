package demo;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import pojo.LoginRequest;
import pojo.LoginResponse;
import pojo.OrderDetails;
import pojo.Orders;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class EcommerceAPITest {

    public static void main(String[] args) {
       RequestSpecification req =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

       LoginRequest loginRequest = new LoginRequest();
       loginRequest.setUserEmail("salilbhatt92@gmail.com");
       loginRequest.setUserPassword("Sh!v!nirmal04");

       RequestSpecification reqLogin = given().relaxedHTTPSValidation().log().all().spec(req).body(loginRequest);
        LoginResponse loginResponse = reqLogin.when().post("api/ecom/auth/login")
                .then().log().all().extract().response().as(LoginResponse.class);
        String token = loginResponse.getToken();
        String userId = loginResponse.getUserId();

        Assert.assertNotNull(token, "Login token is null");
        Assert.assertNotNull(userId, "Login userId is null");
        System.out.println("Login successful");

//Add product
        RequestSpecification addProductBaseRequest =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("authorization", token).build();

        RequestSpecification reqAddProduct = given().log().all().spec(addProductBaseRequest).param("productName", "Laptop")
                .param("productAddedBy", userId)
                .param("productCategory", "Electronics")
                .param("productSubCategory", "Laptop")
                .param("productPrice", "40000")
                .param("productDescription", "Lenovo ThinkPad E14 Gen 2 Intel Core i5 11th Gen 14 inch FHD Thin and Light Laptop (8GB/256GB SSD/Windows 10 Pro/Office 2019/Black/1.59 kg), 20TAS0A00H")
                .multiPart("productImage", new java.io.File("C:\\Users\\salil\\Downloads\\lenovo.jpg"));

        String addProductResponse =  reqAddProduct.when().post("/api/ecom/product/add-product")
                .then().log().all().extract().response().asString();
        JsonPath js = new JsonPath(addProductResponse);
        String productId = js.get("productId");
        System.out.println(productId);

//Create Order

        RequestSpecification createOrderBaseRequest =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                    .addHeader("authorization", token).setContentType(ContentType.JSON).build();

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCountry("India");
        orderDetails.setOrderID(productId);

        List<OrderDetails> orderDetailsList = new ArrayList<OrderDetails>();
        orderDetailsList.add(orderDetails);

        Orders orders = new Orders();
        orders.setOrders(orderDetailsList);

        RequestSpecification createOrderReq = given().log().all().spec(createOrderBaseRequest).body(orders);

       String responseAddOrder =  createOrderReq.when().post("/api/ecom/order/create-order")
                    .then().log().all().extract().response().asString();

       System.out.println(responseAddOrder);

       JsonPath orderJson = new JsonPath(responseAddOrder);
       String orderID = orderJson.getString("orders[0]");
       if (orderID == null || orderID.isBlank()) {
           orderID = orderJson.getString("orderId");
       }
       if (orderID == null || orderID.isBlank()) {
           throw new RuntimeException("Could not fetch orderId from create-order response: " + responseAddOrder);
       }
       System.out.println("Created orderID: " + orderID);

//Delete Order

        RequestSpecification deleteOrderBaseRequest =  new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                    .addHeader("authorization", token).setContentType(ContentType.JSON).build();

        Response deleteOrderResponse = given().log().all()
                .spec(deleteOrderBaseRequest)
                .pathParam("orderId", orderID)
                .when().delete("/api/ecom/order/delete-order/{orderId}")
                .then().log().all().extract().response();

        String deleteOrderResponseBody = deleteOrderResponse.asString();
        JsonPath deleteOrderJson = new JsonPath(deleteOrderResponseBody);
        String deleteMessage = deleteOrderJson.getString("message");

        Assert.assertTrue(deleteOrderResponse.getStatusCode() == 200 || deleteOrderResponse.getStatusCode() == 201,
                "Unexpected delete order status: " + deleteOrderResponse.getStatusCode() + ", body: " + deleteOrderResponseBody);
        Assert.assertTrue(deleteMessage != null && deleteMessage.toLowerCase().contains("deleted"),
                "Delete order message mismatch. Actual message: " + deleteMessage);

        System.out.println("Deleted orderID: " + orderID);
}

}
