package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.User;
import web.service.UserService;

import java.util.ResourceBundle;

@Controller

public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String getUsers(@RequestParam(name = "locale", defaultValue = "en", required = false) String locale, ModelMap modelMap) {
        modelMap.addAttribute("users", userService.listUsers());
        ResourceBundle bundle = ResourceBundle.getBundle("language_" + locale);
        modelMap.addAttribute("usersHeadline", bundle.getString("usersHeadline"));
        return "users";
    }

    @GetMapping(value = "/edit")
    public String editPage(@RequestParam(value = "id") String id, ModelMap modelMap) {
        Long userId = Long.parseLong(id);
        User user = userService.getUserById(userId);
        modelMap.addAttribute("users", user);
        return "editPage";
    }

    @PostMapping(value = "/edit")
    public String editUser(@RequestParam(value = "id") String id,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "last_name") String last_name,
                           @RequestParam(value = "email") String email,
                           @RequestParam(value = "login") String login,
                           @RequestParam(value = "password") String password,
                           ModelMap modelMap,
                           @RequestParam(name = "locale", defaultValue = "en", required = false) String locale) {
        Long userId = Long.parseLong(id);
        System.out.println("user Id = " + id);
        System.out.println("new userId = " + userId);
        User tempUser = new User(userId, name, last_name, email, login, password);
        tempUser.setId(userId);
        System.out.println("control new userId = " + tempUser.getId());
        userService.updateUser(tempUser);
        System.out.println("User updated:");
        System.out.println("id = " + tempUser.getId());
        System.out.println("name = " + tempUser.getFirstName());
        System.out.println("last_name = " + tempUser.getLastName());
        System.out.println("email = " + tempUser.getEmail());

        ResourceBundle bundle = ResourceBundle.getBundle("language_" + locale);
        modelMap.addAttribute("usersHeadline", bundle.getString("usersHeadline"));
        System.out.println("headline = " + bundle.getString("usersHeadline"));
        modelMap.addAttribute("users", userService.listUsers());
        return "users";
    }

    @GetMapping(value = "/add")
    public String addForm() {
        return "addPage";
    }

    @PostMapping(value = "/add", produces = "text/html; charset=utf-8")
    public String addNewUser(@RequestParam(value = "name") String name,
                             @RequestParam(value = "last_name") String last_name,
                             @RequestParam(value = "email") String email,
                             @RequestParam(value = "login") String login,
                             @RequestParam(value = "password") String password) {
        userService.addUser(new User(name, last_name, email, login, password));
        System.out.println("user added with name = " + name);
        System.out.println("user added with last_name = " + last_name);
        return "addPage";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam(value = "id") String id, ModelMap modelMap,
                             @RequestParam(name = "locale", defaultValue = "en", required = false) String locale) {
        System.out.println("Delete user with id = " + id);
        Long userId = Long.parseLong(id);
        userService.deleteUser(userId);
        ResourceBundle bundle = ResourceBundle.getBundle("language_" + locale);
        modelMap.addAttribute("usersHeadline", bundle.getString("usersHeadline"));
        System.out.println("headline = " + bundle.getString("usersHeadline"));
        modelMap.addAttribute("users", userService.listUsers());
        return "users";
    }
}
