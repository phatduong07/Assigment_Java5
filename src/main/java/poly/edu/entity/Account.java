package poly.edu.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Accounts")
public class Account {

    @Id
    private String username;

    private String password;
    private String email;
    private String photo;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "fullname")
    private String fullName;

    // 🔹 Liên kết đến bảng Authorities (1 user có nhiều quyền)
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<Authority> authorities;

    // ===== Getter & Setter =====
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public List<Authority> getAuthorities() { return authorities; }
    public void setAuthorities(List<Authority> authorities) { this.authorities = authorities; }
}
