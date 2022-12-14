package ru.tinder.controller;

import ru.tinder.model.match.Match;
import ru.tinder.model.response.Response;
import ru.tinder.model.user.User;

import java.util.List;

public interface MatchController {

    Response<Match> match(Match match);
    Response<List<User>> available(Long id);

}
