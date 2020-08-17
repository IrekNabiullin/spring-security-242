package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Controller

@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    public String printWelcome(ModelMap modelMap) {
        List<String> messages = new ArrayList<>();
        messages.add("Hello!");
        messages.add("This is security test application.");
        modelMap.addAttribute("messages", messages);
        modelMap.addAttribute("users", userService.listUsers());
        return "hello";
    }

    @GetMapping(value = "/profile")
    public String getProfile(@RequestParam(value = "id", required = true) String id, ModelMap modelMap) {
        Long userId = Long.parseLong(id);
        User user = userService.getUserById(userId);
        modelMap.addAttribute("users", user);
        return "profile";
    }
    @GetMapping(value = "/users")
    public String getUsers(@RequestParam(name = "locale", defaultValue = "en", required = false) String locale, ModelMap modelMap) {
        modelMap.addAttribute("users", userService.listUsers());
        ResourceBundle bundle = ResourceBundle.getBundle("language_" + locale);
        modelMap.addAttribute("usersHeadline", bundle.getString("usersHeadline"));
        return "users";
    }

    @GetMapping(value = "/users/edit")
    public String editPage(@RequestParam(value = "id") String id, ModelMap modelMap) {
        Long userId = Long.parseLong(id);
        User user = userService.getUserById(userId);
        modelMap.addAttribute("users", user);
        return "editPage";
    }

    @PostMapping(value = "/users/edit")
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

    @GetMapping(value = "/users/add")
    public String addForm() {
        return "addPage";
    }

    @PostMapping(value = "/users/add", produces = "text/html; charset=utf-8")
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

    @GetMapping("/users/delete")
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