package poly.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import poly.edu.dao.ProductDAO;
import poly.edu.entity.Product;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductDAO productDAO;

    public List<Product> findAll() {
        return productDAO.findAll();
    }

    public Product findById(Integer id) {
        return productDAO.findById(id).orElse(null);
    }

    public List<Product> searchByName(String keyword) {
        return productDAO.findByNameContainingIgnoreCase(keyword);
    }

    public Product save(Product product) {
        if (product.getAvailable() == null) {
            product.setAvailable(true); // ✅ Gán mặc định là còn hàng
        }
        return productDAO.save(product);
    }


    public void delete(Integer id) {
        productDAO.deleteById(id);
    }
}
