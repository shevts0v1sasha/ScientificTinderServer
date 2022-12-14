package ru.tinder.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinder.dto.auth.LoginRequest;
import ru.tinder.dto.chat.CreateGroupChatRequest;
import ru.tinder.model.TDate;
import ru.tinder.model.chat.Chat;
import ru.tinder.model.chat.ChatInfo;
import ru.tinder.model.chat.ChatMessage;
import ru.tinder.model.match.Match;
import ru.tinder.model.notification.Notification;
import ru.tinder.model.notification.NotificationType;
import ru.tinder.model.response.Response;
import ru.tinder.model.response.ResponseStatus;
import ru.tinder.model.user.User;
import ru.tinder.service.chatService.ChatService;
import ru.tinder.service.matchService.MatchService;
import ru.tinder.service.notificationService.NotificationService;
import ru.tinder.service.userService.UserService;
import ru.tinder.utils.FileManager;
import ru.tinder.utils.IdGenerator;
import ru.tinder.utils.Utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class Controller implements AuthController, UserController, MatchController, NotificationLogic, ChatController {

    private final UserService userService;
    private final NotificationService notificationService;
    private final ChatService chatService;
    private final MatchService matchService;

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
    public Response<List<User>> getLikedUsers(Long userId) {
        List<Chat> chatByParticipantId = chatService.getChatByParticipantId(userId);
        final List<Long> res = chatByParticipantId.stream().flatMap(chat -> chat.getParticipants().stream()).distinct().filter(id -> !id.equals(userId)).collect(Collectors.toList());
//        List<Long> res = matchService.getLikedIds(userId);
        List<User> likedUsers = new ArrayList<>(res.size());
        for (Long id : res) {
            Optional<User> userById = userService.findUserById(id);
            if (userById.isPresent()) {
                likedUsers.add(userById.get());
            }
        }
        return new Response<>(likedUsers, ResponseStatus.OK, "");
    }

    private void onMutualMatch(Match match) {
        final TDate currentTime = Utils.getCurrentTime();
        Long whoId = match.getWhoId();
        Long whomId = match.getWhomId();
            Optional<User> whoById = userService.findUserById(whoId);
            Optional<User> whomById = userService.findUserById(whomId);
            if (whoById.isPresent() && whomById.isPresent()) {
                User who = whoById.get();
                User whom = whomById.get();
                Chat chat = new Chat(IdGenerator.getUniqueId(), List.of(whoId, whomId));
                Optional<Chat> optionalChat = chatService.addChat(chat);
                if (optionalChat.isPresent()) {
                    notificationService.sendNotification(new Notification(IdGenerator.getUniqueId(), whoId, currentTime,
                            String.format("Новый метч с %s %s!", whom.getUserInfo().getName(), whom.getUserInfo().getSurname()), false, NotificationType.MATCH));

                    notificationService.sendNotification(new Notification(IdGenerator.getUniqueId(), whomId, currentTime,
                            String.format("Новый метч с %s %s!", who.getUserInfo().getName(), who.getUserInfo().getSurname()), false, NotificationType.MATCH));

                }
            }
    }

    @Override
    public Response<Match> match(Match match) {
        Long userId = match.getWhoId();
        Long matchedUser = match.getWhomId();
        Optional<Match> optionalMatch = matchService.addMatch(match, this::onMutualMatch);
        if (optionalMatch.isPresent()) {
            return new Response<>(optionalMatch.get(), ResponseStatus.CREATED, "");
        }
        String errorMessage = String.format("Match with users [%d, %d] already exists!", userId, matchedUser);
        log.error(errorMessage);
        return new Response<>(null, ResponseStatus.CONFLICT, errorMessage);
    }

    @Override
    public Response<List<User>> available(Long id) {
        List<User> allUsers = userService.getAllUsers();
        List<Long> matchedUserIds = matchService.getMatchedUserIds(id);
        allUsers.removeIf(user -> user.getId().equals(id) || matchedUserIds.contains(user.getId()));
        return new Response<>(allUsers, ResponseStatus.OK, "");
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
    public Response<ChatMessage> addMessage(ChatMessage chatMessage) {
        Optional<ChatMessage> chatMessageOptional = chatService.addMessage(chatMessage);
        Optional<Chat> chatById = chatService.getChatById(chatMessage.getChatId());
        if (chatMessageOptional.isPresent() && chatById.isPresent()) {
            List<Long> participants = chatById.get().getParticipants();
            for (Long participantId : participants) {
                notificationService.sendNotification(
                        new Notification(
                                IdGenerator.getUniqueId(),
                                participantId,
                                Utils.getCurrentTime(),
                                "У вас новое сообщение!",
                                false,
                                NotificationType.MESSAGE
                        )
                );
            }
            return new Response<>(chatMessageOptional.get(), ResponseStatus.CREATED, "");
        }
        return new Response<>(null, ResponseStatus.NOT_FOUND, "");
    }

    @Override
    public Response<List<ChatMessage>> getChatMessages(Long chatId) {
        Optional<List<ChatMessage>> chatMessages = chatService.getChatMessages(chatId);
        if (chatMessages.isPresent()) {
            return new Response<>(chatMessages.get(), ResponseStatus.OK, "");
        }
        return new Response<>(null, ResponseStatus.NOT_FOUND, "");
    }

    @Override
    public Response<ChatMessage> getLastChatMessage(Long chatId) {
        Optional<ChatMessage> chatLastMessage = chatService.getChatLastMessage(chatId);
        if (chatLastMessage.isPresent()) {
            return new Response<>(chatLastMessage.get(), ResponseStatus.OK, "");
        }
        return new Response<>(null, ResponseStatus.NOT_FOUND, "");
    }

    @Override
    public Response<Chat> createGroupChat(CreateGroupChatRequest request) {
        Chat chat = new Chat(
                IdGenerator.getUniqueId(),
                request.getParticipants()
        );
        ChatInfo chatInfo = new ChatInfo(
                IdGenerator.getUniqueId(),
                chat.getId(),
                request.getName(),
                request.getTopic(),
                ""
        );

        try {
            String[] paths = FileManager.saveChatPreview(request.getChatPreview(), String.format("chatId_%d", chat.getId()));
            chatInfo.setPreviewPath(paths[0]);
            Optional<Chat> optionalChat = chatService.addGroupChat(chat, chatInfo);
            if (optionalChat.isPresent()) {
                for (Long participantId : request.getParticipants()) {
                    notificationService.sendNotification(
                            new Notification(
                                    IdGenerator.getUniqueId(),
                                    participantId,
                                    Utils.getCurrentTime(),
                                    "С вашим участием был создан чат " + request.getName(),
                                    false,
                                    NotificationType.MESSAGE
                            )
                    );
                }
                return new Response<>(optionalChat.get(), ResponseStatus.CREATED, "");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Response<>(null, ResponseStatus.CONFLICT, "");
    }

    @Override
    public Response<ChatInfo> getChatInfo(Long chatId) {
        Optional<ChatInfo> chatInfo = chatService.getChatInfo(chatId);
        if (chatInfo.isPresent()) {
            return new Response<>(chatInfo.get(), ResponseStatus.OK, "");
        }
        return new Response<>(null, ResponseStatus.NOT_FOUND, "");
    }
}
