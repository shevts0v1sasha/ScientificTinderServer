package ru.tinder.rest.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinder.controller.UserController;
import ru.tinder.model.response.Response;
import ru.tinder.model.response.ResponseStatus;
import ru.tinder.model.user.User;
import ru.tinder.service.userService.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserController userController;

    @GetMapping("/info/{userId}")
    public ResponseEntity<User> getUserInfo(@PathVariable("userId") Long userId) {
        Response<User> user = userController.getUserInfo(userId);
        if (user.getData() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user.getData());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userController.getAllUsers().getData());
    }

    @GetMapping("/liked")
    public ResponseEntity<List<User>> getLikedUsers(@RequestParam("userId") Long userId) {
        Response<List<User>> likedUsers = userController.getLikedUsers(userId);
        if (likedUsers.getResponseStatus() == ResponseStatus.OK) {
            return ResponseEntity.ok(likedUsers.getData());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
