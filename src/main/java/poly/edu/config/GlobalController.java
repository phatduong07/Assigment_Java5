package poly.edu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import poly.edu.service.CartService;

@ControllerAdvice
public class GlobalController {

    @Autowired
    CartService cartService;

    @ModelAttribute("cartCount")
    public int getCartCount() {
        return cartService.getTotalQuantity();
    }
}
