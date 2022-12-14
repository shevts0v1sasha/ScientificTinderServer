package ru.tinder.service.chatService;

import org.springframework.stereotype.Service;
import ru.tinder.model.chat.Chat;
import ru.tinder.model.chat.ChatMessage;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ConcurrentHashMap<Long, Chat> chats = new ConcurrentHashMap<>();

    public Chat addChat(Chat chat) {
        chats.put(chat.getId(), chat);
        return chat;
    }

    public Optional<Chat> getChatById(Long chatId) {
        return Optional.ofNullable(chats.get(chatId));
    }

    public List<Chat> getChatByParticipantId(Long participantId) {
        return chats.values()
                .stream()
                .filter(chat -> chat.getParticipants().contains(participantId))
                .collect(Collectors.toList());
    }

    public Optional<Chat> addMessage(ChatMessage chatMessage) {
        if (chats.containsKey(chatMessage.getChatId())) {
            Chat chat = chats.get(chatMessage.getChatId());
            chat.getMessages().add(chatMessage);
            return Optional.of(chat);
        }
        return Optional.empty();
    }

}
