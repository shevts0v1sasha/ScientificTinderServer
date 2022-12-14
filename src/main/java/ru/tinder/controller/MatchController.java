package ru.tinder.controller;

import ru.tinder.model.response.Response;
import ru.tinder.model.user.User;

import java.util.List;

public interface MatchController {

    Response<Boolean> match(Long userId, Long matchedUser);
    Response<List<User>> available(Long id);

}
