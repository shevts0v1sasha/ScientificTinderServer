package ru.tinder.repository.userRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tinder.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String name);
}
