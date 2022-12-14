package ru.tinder.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinder.dto.auth.LoginRequest;
import ru.tinder.model.TDate;
import ru.tinder.model.chat.Chat;
import ru.tinder.model.chat.ChatMessage;
import ru.tinder.model.notification.Notification;
import ru.tinder.model.notification.NotificationType;
import ru.tinder.model.response.Response;
import ru.tinder.model.response.ResponseStatus;
import ru.tinder.model.user.User;
import ru.tinder.service.chatService.ChatService;
import ru.tinder.service.notificationService.NotificationService;
import ru.tinder.service.userService.UserService;
import ru.tinder.utils.IdGenerator;
import ru.tinder.utils.Utils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class Controller implements AuthController, UserController, MatchController, NotificationLogic, ChatController {

    private final UserService userService;
    private final NotificationService notificationService;
    private final ConcurrentHashMap<Long, Set<Long>> matches = new ConcurrentHashMap<>() {{
        put(1L, new HashSet<>() {{ add(0L);}});
    }};

    private final ChatService chatService;

    @Override
    public Response<User> login(LoginRequest request) {
        Optional<User> optionalUser = userService.login(request);
        if (optionalUser.isPresent()) {
            return new Response<>(optionalUser.get(), ResponseStatus.OK, "");
        }
        return new Response<>(null, ResponseStatus.NOT_FOUND, "User doesn't exists or incorrect password");
    }

    @Override
    public Response<User> getUserInfo(Long userId) {
        Optional<User> optionalUser = userService.findUserById(userId);
        if (optionalUser.isPresent()) {
            return new Response<>(optionalUser.get(), ResponseStatus.OK, "");
        }
        return new Response<>(null, ResponseStatus.NOT_FOUND, String.format("Couldn't find user with id=%d", userId));
    }

    @Override
    public Response<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return new Response<>(allUsers, ResponseStatus.OK, "");
    }

    @Override
    public Response<Boolean> match(Long userId, Long matchedUser) {
        System.out.printf("Match userId=%d, matchedUser=%d%n", userId, matchedUser);
        if (!matches.containsKey(userId)) {
            matches.put(userId, new HashSet<>());
        }
        matches.get(userId).add(matchedUser);

        if (matches.containsKey(matchedUser) && matches.get(matchedUser).contains(userId)) {
            final TDate currentTime = Utils.getCurrentTime();
            Optional<User> userById = userService.findUserById(userId);
            Optional<User> matchedUserById = userService.findUserById(matchedUser);
            notificationService.sendNotification(new Notification(IdGenerator.getUniqueId(), userId, currentTime,
                    String.format("Новый метч с %s %s!", matchedUserById.get().getUserInfo().getName(), matchedUserById.get().getUserInfo().getSurname()), false, NotificationType.MATCH));

            notificationService.sendNotification(new Notification(IdGenerator.getUniqueId(), matchedUser, currentTime,
                    String.format("Новый метч с %s %s!", userById.get().getUserInfo().getName(), userById.get().getUserInfo().getSurname()), false, NotificationType.MATCH));
            Chat chat = new Chat(IdGenerator.getUniqueId(), List.of(userId, matchedUser), new ArrayList<>());
            chatService.addChat(chat);
            System.out.println("Matched. Send notifications");

            return new Response<>(true, ResponseStatus.CREATED, "Match created");
        }

        return new Response<>(true, ResponseStatus.OK, "");
    }

    @Override
    public Response<List<User>> available(Long id) {
        List<User> allUsers = userService.getAllUsers();
        List<User> collect = allUsers.stream()
                .filter(user -> !user.getId().equals(id))
                .collect(Collectors.toList());
        return new Response<>(collect, ResponseStatus.OK, "");
    }

    @Override
    public Response<List<Notification>> getUserNotifications(Long userId) {
        return null;
    }

    @Override
    public Response<Boolean> setShown(Long userId) {
        return null;
    }

    @Override
    public Response<Chat> getChatById(Long chatId) {
        Optional<Chat> chatById = chatService.getChatById(chatId);
        if (chatById.isPresent()) {
            return new Response<>(chatById.get(), ResponseStatus.OK, "");
        }
        return new Response<>(null, ResponseStatus.NOT_FOUND, "");
    }

    @Override
    public Response<List<Chat>> getChatByParticipantId(Long participantId) {
        return new Response<>(chatService.getChatByParticipantId(participantId), ResponseStatus.OK, "");
    }

    @Override
    public Response<Chat> addMessage(ChatMessage chatMessage) {
        Optional<Chat> chat = chatService.addMessage(chatMessage);
        if (chat.isPresent()) {
            return new Response<>(chat.get(), ResponseStatus.CREATED, "");
        }
        return new Response<>(null, ResponseStatus.NOT_FOUND, "");
    }
}
