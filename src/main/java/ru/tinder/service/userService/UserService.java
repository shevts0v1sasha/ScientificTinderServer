package ru.tinder.service.userService;

import ru.tinder.model.user.Role;
import ru.tinder.model.user.User;
import ru.tinder.model.user.UserInfo;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User register(User user);

    List<User> getAll();

    Optional<User> findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

    void saveRole(Role role);

    Role findRole(String roleName);

    List<Role> getAllRoles();

    Optional<UserInfo> getUserInfo(String username);

    void saveUserInfo(UserInfo userInfo);

}
