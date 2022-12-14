package ru.tinder.controller;

import ru.tinder.model.notification.Notification;
import ru.tinder.model.response.Response;

import java.util.List;

public interface NotificationLogic {

    Response<List<Notification>> getUserNotifications(Long userId);

    Response<Boolean> setShown(Long userId);

}
