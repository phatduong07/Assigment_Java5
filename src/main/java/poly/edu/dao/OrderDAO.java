package poly.edu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.entity.Order;
import java.util.List;

@Repository
public interface OrderDAO extends JpaRepository<Order, Integer> {
    List<Order> findByUsername(String username);
}
