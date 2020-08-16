package web.dao;

import web.model.User;

import java.util.List;

public interface UserDao {
    void addUser(User user);

    List<User> listUsers();

    User getUserById(Long id);

    void updateUser(User user);

    void deleteUser(Long id);

    void deleteAllUsersFromTable();
}
