package poly.edu.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.dao.ProductDAO;
import poly.edu.entity.CartItem;
import poly.edu.entity.Product;

import java.util.*;

@Service
public class CartService {

    @Autowired
    HttpSession session;

    @Autowired
    ProductDAO productDAO;

    // Lấy giỏ hàng từ session
    @SuppressWarnings("unchecked")
    public Map<String, CartItem> getCart() {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // ✅ Thêm sản phẩm kèm size
    public void addToCart(int productId, String size) {
        Map<String, CartItem> cart = getCart();
        String key = productId + "-" + size;

        CartItem item = cart.get(key);
        if (item == null) {
            Product p = productDAO.findById(productId).orElse(null);
            if (p != null) {
                item = new CartItem(p.getId(), p.getName(), p.getPrice(), 1, p.getImage(), size);
                cart.put(key, item);
            }
        } else {
            item.setQuantity(item.getQuantity() + 1);
        }
        session.setAttribute("cart", cart);
    }

    // ✅ Xóa sản phẩm (theo productId + size)
    public void removeFromCart(int productId, String size) {
        Map<String, CartItem> cart = getCart();
        cart.remove(productId + "-" + size);
        session.setAttribute("cart", cart);
    }

    // ✅ Cập nhật số lượng
    public void updateQuantity(int productId, String size, int quantity) {
        Map<String, CartItem> cart = getCart();
        String key = productId + "-" + size;
        CartItem item = cart.get(key);
        if (item != null) {
            if (quantity > 0) {
                item.setQuantity(quantity);
            } else {
                cart.remove(key);
            }
        }
        session.setAttribute("cart", cart);
    }

    // ✅ Tổng tiền
    public double getTotal() {
        return getCart().values().stream()
                .mapToDouble(CartItem::getTotal)
                .sum();
    }

    // ✅ Xóa toàn bộ giỏ hàng
    public void clear() {
        session.removeAttribute("cart");
    }

    // ✅ Tổng số lượng sản phẩm
    public int getTotalQuantity() {
        return getCart().values().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
