package poly.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import poly.edu.dao.*;
import poly.edu.entity.*;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private AuthorityDAO authorityDAO;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private OrderDAO orderDAO;

    // ✅ Lấy toàn bộ danh sách tài khoản
    public List<Account> findAll() {
        return accountDAO.findAll();
    }

    // ✅ Xóa tài khoản (chỉ khi không có đơn hàng)
    @Transactional
    public void delete(String username) {
        Account acc = accountDAO.findById(username).orElse(null);
        if (acc == null) return;

        // Kiểm tra có đơn hàng không
        List<Order> orders = orderDAO.findByUsername(username); // ✅ tìm bằng username
        if (orders != null && !orders.isEmpty()) {
            throw new RuntimeException("⚠️ Không thể xóa tài khoản vì đã có đơn hàng!");
        }

        accountDAO.delete(acc); // ✅ an toàn để xóa
    }


    // ✅ Cấp quyền ADMIN
    @Transactional
    public void grantAdmin(String username) {
        Account acc = accountDAO.findById(username).orElse(null);
        Role adminRole = roleDAO.findById("ADMIN").orElse(null);
        if (acc == null || adminRole == null) return;

        // Kiểm tra quyền ADMIN đã tồn tại chưa
        boolean exists = authorityDAO.existsByAccountAndRole(acc, adminRole);
        if (!exists) {
            Authority authority = new Authority();
            authority.setAccount(acc);
            authority.setRole(adminRole);
            authorityDAO.save(authority);
        }
    }

    // ✅ Thu hồi quyền ADMIN
    @Transactional
    public void revokeAdmin(String username) {
        Account acc = accountDAO.findById(username).orElse(null);
        if (acc == null) return;

        List<Authority> authorities = authorityDAO.findByAccount(acc);
        for (Authority auth : authorities) {
            if ("ADMIN".equalsIgnoreCase(auth.getRole().getId())) {
                authorityDAO.delete(auth);
            }
        }
    }
}
