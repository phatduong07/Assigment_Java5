package poly.edu.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 🔹 Liên kết đến bảng Accounts
    @ManyToOne
    @JoinColumn(name = "Username") // đúng tên cột trong DB
    private Account account;

    // 🔹 Liên kết đến bảng Roles
    @ManyToOne
    @JoinColumn(name = "RoleId") // ⚠️ CHỖ NÀY QUAN TRỌNG — sửa lại từ role_id → RoleId
    private Role role;

    // ===== Getter & Setter =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
