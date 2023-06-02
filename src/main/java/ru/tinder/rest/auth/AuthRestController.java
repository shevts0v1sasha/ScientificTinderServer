package ru.tinder.rest.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinder.controller.AuthController;
import ru.tinder.dto.auth.LoginRequest;
import ru.tinder.dto.auth.RegistrationRequest;
import ru.tinder.dto.auth.RegistrationResponse;
import ru.tinder.model.response.Response;
import ru.tinder.model.user.User;
import ru.tinder.model.user.UserInfo;
import ru.tinder.service.userService.UserService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthController authController;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationResponse> registration(@RequestBody RegistrationRequest request) {
//        User user = new User(
//                System.currentTimeMillis(),
//                request.getUsername(),
//                request.getPassword(),
//                new UserInfo(
//                        request.getName(), request.getSurname(), request.getPatronymic(), request.getSpeciality(), request.getJobTitle(), request.getAreaOfScientificInterests(),
//                        request.getAcademicTitle(), request.getAcademicDegree(), request.getLinksToQualifyingPapers(), request.getLinksToPublications()
//                ),
//                request.getRole()
//        );
//        HttpStatus res = userService.registration(user);
//        if (res == HttpStatus.CREATED) {
//            return new ResponseEntity<>(RegistrationResponse.create(user), res);
//        } else {
//            return new ResponseEntity<>(res);
//        }
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest request) {
        Response<User> user = authController.login(request);
        if (user.getData() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(user.getData());
    }

    public void lastTest() {}

    public void esheOdinlastTest() {}
}
