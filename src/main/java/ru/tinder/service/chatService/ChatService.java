package ru.tinder.service.chatService;

import org.springframework.stereotype.Service;
import ru.tinder.model.chat.Chat;
import ru.tinder.model.chat.ChatInfo;
import ru.tinder.model.chat.ChatMessage;
import ru.tinder.utils.IdGenerator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final List<Chat> chats = Collections.synchronizedList(new ArrayList<>());
    private final ConcurrentHashMap<Long, List<ChatMessage>> chatMessages = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Long, ChatInfo> chatInfos = new ConcurrentHashMap<>();

    public Optional<Chat> addChat(Chat chat) {
        boolean chatAlreadyExists = chats
                .stream()
                .anyMatch(c -> chat.getParticipants().containsAll(c.getParticipants()));
        if (!chatAlreadyExists) {
            chats.add(chat);
            return Optional.of(chat);
        }
        return Optional.empty();
    }

    public Optional<Chat> addGroupChat(Chat chat, ChatInfo chatInfo) {
        if (!chatExists(chat.getId())) {
            chats.add(chat);
            chatInfos.put(chat.getId(), chatInfo);
            return Optional.of(chat);
        }
        return Optional.empty();
    }

    public Optional<Chat> getChatById(Long chatId) {
        return chats
                .stream()
                .filter(chat -> chat.getId().equals(chatId))
                .findAny();
    }

    public List<Chat> getChatByParticipantId(Long participantId) {
        return chats
                .stream()
                .filter(chat -> chat.getParticipants().contains(participantId))
                .collect(Collectors.toList());
    }

    public Optional<ChatMessage> addMessage(ChatMessage chatMessage) {
        Optional<Chat> any = chats
                .stream()
                .filter(chat -> chat.getId().equals(chatMessage.getChatId()))
                .findAny();

        if (any.isPresent()) {
            if (!chatMessages.containsKey(chatMessage.getChatId())) {
                chatMessages.put(chatMessage.getChatId(), new ArrayList<>());
            }
            chatMessages.get(chatMessage.getChatId()).add(chatMessage);
            chatMessages.get(chatMessage.getChatId()).sort(Comparator.comparing(ChatMessage::getPosted));
            return Optional.of(chatMessage);
        }
        return Optional.empty();
    }

    public Optional<List<ChatMessage>> getChatMessages(Long chatId) {
        Optional<Chat> any = chats
                .stream()
                .filter(chat -> chat.getId().equals(chatId))
                .findAny();

        if (any.isPresent()) {
            if (chatMessages.containsKey(chatId)) {
                return Optional.of(
                        chatMessages.get(chatId)
                );
            }
        }
        return Optional.empty();
    }

    private boolean chatExists(Long chatId) {
        return chats
                .stream()
                .anyMatch(chat -> chat.getId().equals(chatId));
    }

    public Optional<ChatMessage> getChatLastMessage(Long chatId) {
        Optional<Chat> any = chats
                .stream()
                .filter(chat -> chat.getId().equals(chatId))
                .findAny();

        if (any.isPresent()) {
            if (chatMessages.containsKey(chatId)) {
                List<ChatMessage> messages = chatMessages.get(chatId);
                return Optional.of(
                        messages.get(messages.size() - 1)
                );
            }
        }
        return Optional.empty();
    }

    public Optional<ChatInfo> addChatInfo(ChatInfo chatInfo) {
        if (chatExists(chatInfo.getChatId())) {
            chatInfo.setId(IdGenerator.getUniqueId());
            chatInfos.put(chatInfo.getChatId(), chatInfo);
            return Optional.of(chatInfo);
        }
        return Optional.empty();
    }

    public Optional<ChatInfo> getChatInfo(Long chatId) {
        if (chatExists(chatId)) {
            if (chatInfos.containsKey(chatId)) {
                return Optional.of(chatInfos.get(chatId));
            }
        }
        return Optional.empty();
    }

}
