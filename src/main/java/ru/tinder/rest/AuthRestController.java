package ru.tinder.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinder.dto.auth.RegistrationRequestDto;
import ru.tinder.dto.user.UserDto;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello");
    }

    @PostMapping("/registration")
    public ResponseEntity<UserDto> registration(@RequestBody RegistrationRequestDto request) {

        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }
}
