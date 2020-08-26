package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.Roles;
import web.model.User;
import web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;



@Controller

@RequestMapping("/")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "admin", method = RequestMethod.GET)
    public String printWelcome() {
        return "admin";
    }

    @GetMapping(value = "/profile")
    public String getProfile(Authentication authentication, ModelMap modelMap) {
        User user = userService.getUserByName(authentication.getName());
        modelMap.addAttribute("user", user);
        return "profile";
    }

    @GetMapping(value = "/users")
    public String getUsers(@RequestParam(name = "locale", defaultValue = "en", required = false) String locale, ModelMap modelMap,
                           @ModelAttribute("user") User user, Authentication authentication, HttpServletRequest request) {
        modelMap.addAttribute("users", userService.listUsers());
        ResourceBundle bundle = ResourceBundle.getBundle("language_" + locale);
        modelMap.addAttribute("usersHeadline", bundle.getString("usersHeadline"));
        return "users";
    }


    @GetMapping(value = "/users/edit")
    public String editPage(@RequestParam(value = "id") String id, ModelMap modelMap) {
        Long userId = Long.parseLong(id);
        User user = userService.getUserById(userId);
        modelMap.addAttribute("user", user);
        return "editPage";
    }

    @PostMapping(value = "/users/edit")
    public String editUser(@RequestParam(value = "id") String id,
                           @RequestParam(value = "name") String name,
                           @RequestParam(value = "last_name") String last_name,
                           @RequestParam(value = "email") String email,
                           @RequestParam(value = "login") String login,
                           @RequestParam(value = "password") String password,
                           @RequestParam(value = "role") String roleOfUser,
                           ModelMap modelMap,
                           @RequestParam(name = "locale", defaultValue = "en", required = false) String locale) {

        Long userId = Long.parseLong(id);
        Collection<Roles> roles2 = new ArrayList<>(userService.getUserByName("USER").getRoles());
            if (roleOfUser.contains("ADMIN") || roleOfUser.contains("admin")) {
                    roles2.clear();
                    roles2.addAll(userService.getUserByName("ADMIN").getRoles());
                    System.out.println("User editing. Roles of user " + name + ": " + roles2.toString());
                }
        System.out.println("User editing. Roles of user " + name + ": " + roles2.toString());
        User tempUser = new User(userId, name, last_name, email, login, password, roles2);
        tempUser.setId(userId);
        userService.updateUser(tempUser);

        ResourceBundle bundle = ResourceBundle.getBundle("language_" + locale);
        modelMap.addAttribute("usersHeadline", bundle.getString("usersHeadline"));
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
                             @RequestParam(value = "password") String password,
                             @RequestParam(value = "role") String roleOfNewUser,
                             ModelMap modelMap) {

        Collection<Roles> rolesNewUser = new ArrayList<>(userService.getUserByName("USER").getRoles());
        rolesNewUser.clear();
        rolesNewUser.addAll(userService.getUserByName("USER").getRoles());
        System.out.println("Adding new user. Roles of user " + name + ": " + rolesNewUser.toString());
        if (roleOfNewUser.contains("ADMIN") || roleOfNewUser.contains("admin")) {
            rolesNewUser.clear();
            rolesNewUser.addAll(userService.getUserByName("ADMIN").getRoles());
            System.out.println("Adding new user. Roles of user " + name + ": " + rolesNewUser.toString());
        }

        System.out.println("Adding new user. Name: " + name);
        System.out.println("Adding new user. Last_name: " + last_name);
        System.out.println("Adding new user. Email: " + email);
        System.out.println("Adding new user. Login: " + login);
        System.out.println("Adding new user. Password: " + password);
        System.out.println("Adding new user. Roles: " + rolesNewUser.toString());

        userService.addUser(new User(name, last_name, email, login, password, rolesNewUser));

        modelMap.addAttribute("users", userService.listUsers());
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
        modelMap.addAttribute("users", userService.listUsers());
        return "users";
    }

    @GetMapping(value = "/logout")
    public String logOut(Authentication authentication, ModelMap modelMap) {
        return "logout";
    }

}