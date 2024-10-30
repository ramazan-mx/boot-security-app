package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.util.List;

@Controller()
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String adminDashboard() {
        return "admin/home-page";
    }

    @GetMapping("/users")
    public String getUsers(@RequestParam(value = "id", required = false) Integer id,
                           Model model) {
        if (id != null) {
            model.addAttribute("user", userService.getUserById(id));
            return "admin/user-id";
        } else {
            model.addAttribute("users", userService.getAll());
            return "admin/users";
        }
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        model.addAttribute("allRoles", userService.getRoles());
        return "admin/user-new";
    }

    @PostMapping("/users/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) Integer id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/users")
    public String create(@RequestParam("username") String username,
                         @RequestParam("firstname") String firstname,
                         @RequestParam("lastname") String lastname,
                         @RequestParam("email") String email,
                         @RequestParam("roles") List<Integer> roles) {
        userService.add(new User(username, firstname, lastname, email), roles);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit")
    public String edit(@RequestParam(value = "id", required = false) Integer id, Model model) {
        if (id == null) {
            return "redirect:/admin/users";
        }
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", userService.getRoles());
        return "admin/user-edit";
    }

    @PostMapping("/users/edit")
    public String editUser(@RequestParam("id") Integer id,
                           @RequestParam("username") String username,
                           @RequestParam("firstname") String firstname,
                           @RequestParam("lastname") String lastname,
                           @RequestParam("email") String email,
                           @RequestParam("roles") List<Integer> roles) {
        userService.update(id, new User(username, firstname, lastname, email), roles);
        return "redirect:/admin/users";
    }
}
