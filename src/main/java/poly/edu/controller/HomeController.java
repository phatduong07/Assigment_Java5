package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import poly.edu.dao.ProductDAO;
import poly.edu.entity.Product;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    ProductDAO productDAO;

    // Trang chủ - load sản phẩm mới nhất
    @GetMapping({"/", "/index", "/home"})
    public String index(Model model) {
        List<Product> productList = productDAO.findAll(Sort.by(Sort.Direction.DESC, "createDate"));
        model.addAttribute("products", productList);
        model.addAttribute("pageTitle", "Trang chủ");
        return "index"; // trỏ đến templates/index.html
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("pageTitle", "Giới thiệu");
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("pageTitle", "Liên hệ");
        return "contact";
    }

    @GetMapping("/bestseller")
    public String bestseller(Model model) {
        // Tạm thời hiển thị tất cả sản phẩm
        List<Product> productList = productDAO.findAll();
        model.addAttribute("products", productList);
        model.addAttribute("pageTitle", "Sản phẩm bán chạy");
        return "products"; // sử dụng trang templates/products.html
    }
}
