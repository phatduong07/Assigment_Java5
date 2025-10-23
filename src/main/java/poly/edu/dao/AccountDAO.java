package poly.edu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.entity.Account;

@Repository
public interface AccountDAO extends JpaRepository<Account, String> {
    Account findByUsername(String username);
}
