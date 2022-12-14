package ru.tinder.controller;

import ru.tinder.model.chat.Chat;
import ru.tinder.model.chat.ChatMessage;
import ru.tinder.model.response.Response;

import java.util.List;
import java.util.Optional;

public interface ChatController {

    Response<Chat> getChatById(Long chatId);
    Response<List<Chat>> getChatByParticipantId(Long participantId);
    Response<Chat> addMessage(ChatMessage chatMessage);

}
