package ru.tinder.service.userService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinder.model.user.Role;
import ru.tinder.model.user.Status;
import ru.tinder.model.user.User;
import ru.tinder.model.user.UserInfo;
import ru.tinder.repository.userRepository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserInfoRepository userInfoRepository;



    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        if (roleUser == null) {
            Role role = new Role();
            role.setName("ROLE_USER");
            roleUser = roleRepository.save(role);
        }
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        userInfoRepository.save(user.getUserInfo());
        User registeredUser = userRepository.save(user);

        log.info("IN register - user: {} successfully", registeredUser);
        return registeredUser;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        User result = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", result, username);
        return Optional.ofNullable(result);
    }

    @Override
    public User findById(Long id) {
        User result = userRepository.findById(id).orElse(null);
        if (result == null) {
            log.warn("IN findById - no user found by id: {}", id);
            return null;
        }
        log.info("IN findById - user: {} found by id: {}", result, id);
        return null;
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.warn("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public Role findRole(String roleName) {
        return roleRepository.findByName(roleName);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<UserInfo> getUserInfo(String username) {
        Optional<User> user = findByUsername(username);
        if (user.isPresent()) {
            UserInfo userInfo = user.get().getUserInfo();
            return Optional.of(userInfo);
        }
        return Optional.empty();
    }

    @Override
    public void saveUserInfo(UserInfo userInfo) {

    }

}
