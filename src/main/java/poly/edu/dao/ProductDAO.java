package poly.edu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import poly.edu.entity.Category;
import poly.edu.entity.Product;
import java.util.List;

public interface ProductDAO extends JpaRepository<Product, Integer> {

    List<Product> findByCategoryId(String cid);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findTop4ByCategoryAndIdNot(Category category, Integer id);

    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :min AND :max")
    List<Product> findByPriceRange(@Param("min") double min, @Param("max") double max);
}
