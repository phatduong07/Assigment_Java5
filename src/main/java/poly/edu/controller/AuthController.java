package poly.edu.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.dao.AccountDAO;
import poly.edu.entity.Account;
import poly.edu.entity.Authority;

import java.util.List;

@Controller
public class AuthController {

    @Autowired
    AccountDAO accountDAO;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // =================== TRANG ĐĂNG NHẬP ===================
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    // =================== XỬ LÝ ĐĂNG NHẬP ===================
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {

        Account acc = accountDAO.findByUsername(username);

        if (acc == null) {
            model.addAttribute("error", "❌ Tên đăng nhập không tồn tại!");
            return "login";
        }

        if (!passwordEncoder.matches(password, acc.getPassword())) {
            model.addAttribute("error", "⚠️ Mật khẩu không chính xác!");
            return "login";
        }

        // ✅ Lấy quyền của tài khoản
        String role = "USER";
        List<Authority> authorities = acc.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            role = authorities.get(0).getRole().getId(); // "ADMIN" hoặc "USER"
        }

        // ✅ Lưu session
        session.setAttribute("loggedInUser", acc);
        session.setAttribute("role", role);

        // ✅ Dù là ADMIN hay USER đều về trang chủ
        return "redirect:/";
    }

    // =================== ĐĂNG XUẤT ===================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // =================== ĐĂNG KÝ ===================
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("account", new Account());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("account") Account account, Model model) {
        if (accountDAO.existsById(account.getUsername())) {
            model.addAttribute("message", "⚠️ Tên đăng nhập đã tồn tại!");
            return "register";
        }

        if (account.getUsername().isEmpty() || account.getPassword().isEmpty() || account.getEmail().isEmpty()) {
            model.addAttribute("message", "⚠️ Vui lòng nhập đầy đủ thông tin!");
            return "register";
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));
        if (account.getPhoto() == null || account.getPhoto().isEmpty()) {
            account.setPhoto("default.jpg");
        }

        accountDAO.save(account);
        model.addAttribute("message", "✅ Đăng ký thành công! Hãy đăng nhập.");
        model.addAttribute("account", new Account());
        return "register";
    }

    // =================== ĐỔI MẬT KHẨU ===================
    @GetMapping("/change-password")
    public String showChangePasswordForm() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {

        Account acc = (Account) session.getAttribute("loggedInUser");

        if (acc == null) {
            model.addAttribute("error", "⚠️ Bạn cần đăng nhập trước!");
            return "change-password";
        }

        if (!passwordEncoder.matches(oldPassword, acc.getPassword())) {
            model.addAttribute("error", "❌ Mật khẩu cũ không đúng!");
            return "change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "⚠️ Mật khẩu xác nhận không khớp!");
            return "change-password";
        }

        acc.setPassword(passwordEncoder.encode(newPassword));
        accountDAO.save(acc);
        model.addAttribute("message", "✅ Đổi mật khẩu thành công!");
        return "change-password";
    }
}
