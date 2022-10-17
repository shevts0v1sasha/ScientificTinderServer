package ru.tinder.security.jwt;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class RefreshJwtRequest {

    private String refreshToken;

}
