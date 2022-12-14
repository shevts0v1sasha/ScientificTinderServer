package ru.tinder.rest.match;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinder.controller.MatchController;
import ru.tinder.model.response.Response;
import ru.tinder.model.user.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/matches")
@RequiredArgsConstructor
public class MatchRestController {

    private final MatchController matchController;

    @PutMapping
    public ResponseEntity match(@RequestParam("userId") Long userId, @RequestParam("matchedUser") Long matchedUser) {
        matchController.match(userId, matchedUser);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> available(@RequestParam("id") Long id) {
        Response<List<User>> collect = matchController.available(id);
        return ResponseEntity.ok(collect.getData());
    }
}
