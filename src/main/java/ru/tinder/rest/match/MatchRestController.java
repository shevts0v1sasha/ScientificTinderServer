package ru.tinder.rest.match;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinder.controller.MatchController;
import ru.tinder.model.match.Match;
import ru.tinder.model.response.Response;
import ru.tinder.model.response.ResponseStatus;
import ru.tinder.model.user.User;
import ru.tinder.utils.IdGenerator;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchRestController {

    private final MatchController matchController;

    @PutMapping(params = {"userId", "matchedUserId", "isMatch"})
    public ResponseEntity match(
            @RequestParam("userId") long userId,
            @RequestParam("matchedUserId") long matchedUser,
            @RequestParam("isMatch") boolean isMatch
    ) {
        Match match = new Match(
                IdGenerator.getUniqueId(),
                userId,
                matchedUser,
                isMatch,
                LocalDateTime.now()
        );
        Response<Match> res = matchController.match(match);
        if (res.getResponseStatus() == ResponseStatus.CREATED) {
            return ResponseEntity.ok(res.getData());
        }
        return new ResponseEntity(HttpStatus.CONFLICT);
    }

    @GetMapping
    public ResponseEntity<List<User>> available(@RequestParam("id") Long id) {
        Response<List<User>> collect = matchController.available(id);
        return ResponseEntity.ok(collect.getData());
    }
}
