package pojo;

public class OrderDetails {
    private String country;
    private String productOrderedId;

    public String getProductOrderedId() {
        return productOrderedId;
    }

    public void setProductOrderedId(String productOrderedId) {
        this.productOrderedId = productOrderedId;
    }

    // Alias methods for readability in test code while keeping API payload key intact.
    public String getOrderID() {
        return productOrderedId;
    }

    public void setOrderID(String orderID) {
        this.productOrderedId = orderID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

