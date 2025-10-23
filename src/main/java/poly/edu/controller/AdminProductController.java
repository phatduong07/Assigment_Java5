package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.entity.Product;
import poly.edu.service.ProductService;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    // ✅ Danh sách sản phẩm
    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.findAll());
        return "admin/products"; // templates/admin/products.html
    }

    // ✅ Form sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("product", product); // ✅ đổi "item" → "product"
        return "admin/product-edit";
    }



    // ✅ Lưu lại khi admin chỉnh sửa
    @PostMapping("/save")
    public String save(Product product) {
        Product old = productService.findById(product.getId()); // lấy sản phẩm cũ
        if (old != null) {
            // Giữ lại các thông tin không thay đổi
            product.setCategory(old.getCategory()); // giữ category cũ
            product.setAvailable(old.getAvailable());
            product.setCreateDate(old.getCreateDate());
        }

        productService.save(product);
        return "redirect:/index";
    }

    // ✅ Xóa sản phẩm
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }
}
