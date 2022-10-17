package ru.tinder.service.authorizationServer;

import ch.qos.logback.core.status.Status;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.tinder.dto.jwt.JwtResponse;
import ru.tinder.dto.jwt.TokenValidateResultDto;
import ru.tinder.model.user.User;
import ru.tinder.model.user.UserInfo;


import javax.security.auth.message.AuthException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class AuthorizationServer {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtTokenProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        final User user = userService.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new AuthException("Пользователь не найден"));
//        String encode = passwordEncoder.encode(authRequest.getPassword());
        if (user.getPassword().equals(authRequest.getPassword())) {
            final String accessToken = jwtProvider.createAccessToken(user.getUsername(), user.getRoles());
            final String refreshToken = jwtProvider.createRefreshToken(user.getUsername(), user.getRoles());
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Неправильный пароль");
        }
    }

    public UserDto registration(@NonNull RegistrationRequestDto registrationRequestDto) throws RegistrationException {
        if (!registrationRequestDto.getUsername().isEmpty() && !registrationRequestDto.getPassword().isEmpty()) {
            if (userService.findByUsername(registrationRequestDto.getUsername()).isEmpty()) {
                User user = UserDtoFactory.createUserFromRequest(registrationRequestDto);
                UserInfo userInfo = new UserInfo(
                        registrationRequestDto.getFirstName(),
                        registrationRequestDto.getLastName(),
                        registrationRequestDto.getBirthday(),
                        registrationRequestDto.getDepartmentName(),
                        registrationRequestDto.getJobTitle(),
                        null,
                        null
                );
                userInfo.setStatus(Status.ACTIVE);
                user.setCreated(new Date());
                user.setUpdated(new Date());
                user.setUserInfo(userInfo);
//                user.setPassword(passwordEncoder.encode(user.getPassword()));
                User register = userService.register(user);

                return UserDtoFactory.createUserDtoFromUser(register);
            }
            throw new RegistrationException(HttpStatus.CONFLICT.value(), "Пользователь с таким логином уже существует");
        }
        throw new RegistrationException(HttpStatus.BAD_REQUEST.value(), "Одно из полей не было заполнено");
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        TokenValidateResultDto tokenValidateResult = jwtProvider.validateRefreshToken(refreshToken);
        if (tokenValidateResult.getStatus() == 0) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.findByUsername(username)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.createAccessToken(user.getUsername(), user.getRoles());
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        TokenValidateResultDto tokenValidateResult = jwtProvider.validateRefreshToken(refreshToken);
        if (tokenValidateResult.getStatus() == 0) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String username = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(username);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final User user = userService.findByUsername(username)
                        .orElseThrow(() -> new AuthException("Пользователь не найден"));
                final String accessToken = jwtProvider.createAccessToken(user.getUsername(), user.getRoles());
                final String newRefreshToken = jwtProvider.createRefreshToken(user.getUsername(), user.getRoles());
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }

}
