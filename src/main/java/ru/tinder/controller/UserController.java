package ru.tinder.controller;

import ru.tinder.model.response.Response;
import ru.tinder.model.user.User;

import java.util.List;

public interface UserController {
    Response<User> getUserInfo(Long userId);
    Response<List<User>> getAllUsers();

    Response<List<User>> getLikedUsers(Long userId);

}
