package poly.edu.entity;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int productId;
    private String name;
    private double price;
    private int quantity;
    private String image;
    private String size; // ✅ thêm size

    public CartItem() {}

    // ✅ constructor có size
    public CartItem(int productId, String name, double price, int quantity, String image, String size) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
        this.size = size;
    }

    // Getter & Setter
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public double getTotal() {
        return this.price * this.quantity;
    }
}
