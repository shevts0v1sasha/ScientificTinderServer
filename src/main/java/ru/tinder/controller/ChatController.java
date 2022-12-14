package ru.tinder.controller;

import ru.tinder.dto.chat.CreateGroupChatRequest;
import ru.tinder.model.chat.Chat;
import ru.tinder.model.chat.ChatInfo;
import ru.tinder.model.chat.ChatMessage;
import ru.tinder.model.response.Response;

import java.util.List;
import java.util.Optional;

public interface ChatController {

    Response<Chat> getChatById(Long chatId);
    Response<List<Chat>> getChatByParticipantId(Long participantId);
    Response<ChatMessage> addMessage(ChatMessage chatMessage);

    Response<List<ChatMessage>> getChatMessages(Long chatId);

    Response<ChatMessage> getLastChatMessage(Long chatId);

    Response<Chat> createGroupChat(CreateGroupChatRequest request);

    Response<ChatInfo> getChatInfo(Long chatId);

}
