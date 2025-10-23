package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import poly.edu.dao.OrderDAO;
import poly.edu.entity.*;
import poly.edu.service.CartService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderDAO orderDAO;

    // 🛒 Hiển thị trang thanh toán
    @GetMapping("/checkout")
    public String checkoutPage(@RequestParam(required = false) String success,
                               Model model, HttpSession session) {

        Account user = (Account) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartService.getCart().values());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("success", success != null && success.equals("true")); // bật popup nếu có ?success=true
        return "checkout";
    }

    // 💳 Xử lý thanh toán
    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String paymentMethod,
                                  @RequestParam String address,
                                  @RequestParam String phoneNumber,
                                  HttpSession session) {

        Account user = (Account) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        // 🧩 Cập nhật lại số điện thoại nếu người dùng vừa nhập mới
        user.setPhoneNumber(phoneNumber);
        session.setAttribute("loggedInUser", user);

        // 🧾 Tạo đơn hàng mới
        Order order = new Order();
        order.setUsername(user.getUsername());
        order.setPaymentMethod(paymentMethod);
        order.setTotal(cartService.getTotal());
        order.setAddress(address);

        // 🧩 Tạo chi tiết đơn hàng
        List<OrderDetail> details = new ArrayList<>();
        for (CartItem item : cartService.getCart().values()) {
            OrderDetail d = new OrderDetail();
            d.setOrder(order);
            d.setProductId(item.getProductId());
            d.setProductName(item.getName());
            d.setImage(item.getImage()); // nếu có ảnh sản phẩm
            d.setPrice(item.getPrice());
            d.setQuantity(item.getQuantity());
            d.setTotal(item.getTotal());
            d.setSize(item.getSize()); // ✅ thêm dòng này để lưu size vào DB
            details.add(d);
        }

        order.setOrderDetails(details);

        // ✅ Lưu đơn hàng
        orderDAO.save(order);

        // 🧹 Xóa giỏ hàng
        cartService.clear();

        // 👉 Redirect lại /checkout?success=true để hiện popup + truyền orderId
        return "redirect:/checkout?success=true&orderId=" + order.getId();
    }


    // 🧾 Trang hóa đơn (invoice)
    @GetMapping("/invoice/{orderId}")
    public String showInvoice(@PathVariable("orderId") int orderId, Model model, HttpSession session) {
        Account user = (Account) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Order order = orderDAO.findById(orderId).orElse(null);
        if (order == null) {
            return "redirect:/";
        }

        model.addAttribute("order", order);
        model.addAttribute("user", user);
        return "invoice";
    }

    // 📜 Trang lịch sử mua hàng
    @GetMapping("/lichsumuahang")
    public String orderHistory(Model model, HttpSession session) {
        Account user = (Account) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("orders", orderDAO.findByUsername(user.getUsername()));
        return "order-history";
    }
}
