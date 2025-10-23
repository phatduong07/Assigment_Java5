package poly.edu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import poly.edu.entity.Authority;
import poly.edu.entity.Role;
import poly.edu.entity.Account;
import java.util.List;

public interface AuthorityDAO extends JpaRepository<Authority, Integer> {
    List<Authority> findByAccount(Account account);
    boolean existsByAccountAndRole(Account account, Role role);
}

