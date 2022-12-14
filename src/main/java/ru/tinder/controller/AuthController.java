package ru.tinder.controller;

import ru.tinder.dto.auth.LoginRequest;
import ru.tinder.model.response.Response;
import ru.tinder.model.user.User;

public interface AuthController {

    Response<User> login(LoginRequest request);
}
