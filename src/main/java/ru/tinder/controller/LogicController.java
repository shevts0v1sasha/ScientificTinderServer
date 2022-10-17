package ru.tinder.controller;

public interface LogicController {

    /**
     * Auth
     */
    void registration();
    void login();
    void logout();

    /**
     * Jwt
     */
    void getNewAccessToken();
    void getNewRefreshToken();

    /**
     * User
     */
    void getUserInfo();

}
