package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    // 🛒 Xem giỏ hàng
    @GetMapping
    public String viewCart(Model model) {
        model.addAttribute("cartItems", cartService.getCart().values());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("cartCount", cartService.getTotalQuantity());
        return "cart";
    }

    // 🆕 Thêm sản phẩm có size (POST)
    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") int productId,
                            @RequestParam("size") String size) {
        cartService.addToCart(productId, size);
        return "redirect:/cart";
    }

    // 🧮 Cập nhật số lượng
    @PostMapping("/update")
    public String updateQuantity(@RequestParam("id") int id,
                                 @RequestParam("quantity") int quantity,
                                 @RequestParam(value = "size", required = false) String size) {
        cartService.updateQuantity(id, size, quantity);
        return "redirect:/cart";
    }

    // ❌ Xóa sản phẩm theo ID + Size (đảm bảo chính xác)
    @GetMapping("/remove/{id}/{size}")
    public String removeItem(@PathVariable("id") int id,
                             @PathVariable("size") String size) {
        cartService.removeFromCart(id, size);
        return "redirect:/cart";
    }

    // 🧹 Xóa toàn bộ giỏ hàng
    @GetMapping("/clear")
    public String clearCart() {
        cartService.clear();
        return "redirect:/cart";
    }
}
