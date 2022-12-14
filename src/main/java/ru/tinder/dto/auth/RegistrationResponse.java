package ru.tinder.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinder.model.user.Role;
import ru.tinder.model.user.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {

    private Long userId;
    private String username;
    private Role role;

    public static RegistrationResponse create(User user) {
        return new RegistrationResponse(
                user.getId(),
                user.getUsername(),
                user.getRole()
        );
    }
}
