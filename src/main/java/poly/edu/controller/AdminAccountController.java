package poly.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import poly.edu.service.AccountService;

@Controller
@RequestMapping("/admin/accounts")
public class AdminAccountController {

    @Autowired
    AccountService accountService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("accounts", accountService.findAll());
        return "admin/accounts";
    }

    @PostMapping("/grant/{username}")
    public String grantAdmin(@PathVariable String username) {
        accountService.grantAdmin(username);
        return "redirect:/admin/accounts";
    }

    @PostMapping("/revoke/{username}")
    public String revokeAdmin(@PathVariable String username) {
        accountService.revokeAdmin(username);
        return "redirect:/admin/accounts";
    }

    @PostMapping("/delete/{username}")
    public String deleteAccount(@PathVariable String username) {
        accountService.delete(username);
        return "redirect:/admin/accounts";
    }
}
