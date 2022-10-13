package ru.tinder.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinder.dto.auth.RegistrationRequestDto;
import ru.tinder.dto.user.UserDto;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody RegistrationRequestDto request) {
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}
