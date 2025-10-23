package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import poly.edu.dao.CategoryDAO;
import poly.edu.dao.ProductDAO;
import poly.edu.entity.Category;
import poly.edu.entity.Product;
import poly.edu.service.ProductService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    CategoryDAO categoryDAO;

    @Autowired
    ProductService productService; // ✅ Thêm dòng này để dùng được searchByName

    // Hiển thị sản phẩm theo danh mục
    @GetMapping("/category/{cid}")
    public String showProductsByCategory(Model model, @PathVariable("cid") String categoryId) {
        Optional<Category> categoryOpt = categoryDAO.findById(categoryId);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            List<Product> productList = productDAO.findByCategoryId(categoryId);
            model.addAttribute("products", productList);
            model.addAttribute("pageTitle", category.getName());
        } else {
            return "redirect:/";
        }
        return "products";
    }

    
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword,
                         @RequestParam(required = false) Double min,
                         @RequestParam(required = false) Double max,
                         Model model) {

        List<Product> results;

        if (min != null || max != null) {
            double minPrice = (min != null) ? min : 0.0;
            double maxPrice = (max != null) ? max : Double.MAX_VALUE;
            results = productDAO.findByPriceRange(minPrice, maxPrice);
            model.addAttribute("pageTitle", "Lọc theo giá");
        } 
        else if (keyword != null && !keyword.isEmpty()) {
            results = productDAO.findByNameContainingIgnoreCase(keyword);
            model.addAttribute("pageTitle", "Kết quả tìm kiếm");
        } 
        else {
            results = productDAO.findAll();
            model.addAttribute("pageTitle", "Tất cả sản phẩm");
        }

        model.addAttribute("products", results);
        model.addAttribute("keyword", keyword);
        return "search-results";
    }

    
    @GetMapping("/detail/{id}")
    public String productDetail(@PathVariable("id") Integer id, Model model) {
        Product product = productDAO.findById(id).orElse(null);
        if (product == null) return "redirect:/"; // Nếu không có sản phẩm

        // Lấy sản phẩm liên quan theo category
        List<Product> relatedProducts = productDAO.findTop4ByCategoryAndIdNot(product.getCategory(), id);

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", relatedProducts);

        return "product-detail";
    }


    
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");

        // ✅ Kiểm tra quyền
        if (role == null || !role.equals("ADMIN")) {
            model.addAttribute("error", "🚫 Bạn không có quyền xóa sản phẩm!");
            return "error"; // Tạo file error.html để hiển thị thông báo
        }

        productDAO.deleteById(id);
        return "redirect:/"; // hoặc redirect:/products nếu bạn muốn quay về danh sách sản phẩm
    }
    
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Integer id, HttpSession session, Model model) {
        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN")) {
            model.addAttribute("error", "🚫 Bạn không có quyền chỉnh sửa sản phẩm!");
            return "error";
        }

        Product product = productDAO.findById(id).orElse(null);
        if (product == null) {
            return "redirect:/";
        }

        model.addAttribute("product", product);
        return "product-edit"; // file product-edit.html
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Integer id,
                                @RequestParam("price") Double price,
                                HttpSession session,
                                Model model) {
        String role = (String) session.getAttribute("role");

        if (role == null || !role.equals("ADMIN")) {
            model.addAttribute("error", "🚫 Bạn không có quyền cập nhật sản phẩm!");
            return "error";
        }

        Product product = productDAO.findById(id).orElse(null);
        if (product != null) {
            product.setPrice(price);
            productDAO.save(product);
        }

        return "redirect:/products/detail/" + id;
    }


}
