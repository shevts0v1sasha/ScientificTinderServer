package ru.tinder.rest.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tinder.controller.ChatController;
import ru.tinder.dto.chat.CreateGroupChatRequest;
import ru.tinder.model.chat.Chat;
import ru.tinder.model.chat.ChatInfo;
import ru.tinder.model.chat.ChatMessage;
import ru.tinder.model.response.Response;
import ru.tinder.model.response.ResponseStatus;
import ru.tinder.utils.Utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatController chatController;

    @GetMapping(params = "participantId")
    public ResponseEntity<List<Chat>> getChatByParticipantId(@RequestParam("participantId") Long participantId) {
        return ResponseEntity.ok(chatController.getChatByParticipantId(participantId).getData());
    }

    @PostMapping("/messages")
    public ResponseEntity<ChatMessage> postMessage(@RequestBody ChatMessage chatMessage) {
        chatMessage.setPosted(LocalDateTime.now());
        Response<ChatMessage> res = chatController.addMessage(chatMessage);
        if (res.getResponseStatus() == ResponseStatus.CREATED) {
            return ResponseEntity.ok(res.getData());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/messages", params = "chatId")
    public ResponseEntity<List<ChatMessage>> getChatMessages(@RequestParam("chatId") Long chatId) {
        Response<List<ChatMessage>> chatMessages = chatController.getChatMessages(chatId);
        if (chatMessages.getResponseStatus() == ResponseStatus.OK) {
            return ResponseEntity.ok(chatMessages.getData());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/messages/last", params = "chatId")
    public ResponseEntity<ChatMessage> getChatLastMessage(@RequestParam("chatId") Long chatId) {
        Response<ChatMessage> lastChatMessage = chatController.getLastChatMessage(chatId);
        if (lastChatMessage.getResponseStatus() == ResponseStatus.OK) {
            return ResponseEntity.ok(lastChatMessage.getData());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Chat> createGroupChat(
            @RequestParam("participants") String participants,
            @RequestParam("name") String name,
            @RequestParam("topic") String topic,
            @RequestParam("chatPreview") MultipartFile chatPreview

    ) {
        String[] s = participants.split("_");
        List<Long> p = Arrays.stream(s).map(Long::parseLong).collect(Collectors.toList());
        final CreateGroupChatRequest request = new CreateGroupChatRequest(
                p,
                name,
                topic,
                chatPreview
        );
        Response<Chat> groupChat = chatController.createGroupChat(request);
        if (groupChat.getResponseStatus() == ResponseStatus.CREATED) {
            return new ResponseEntity<>(groupChat.getData(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/infos")
    public ResponseEntity<ChatInfo> getChatInfo(@RequestParam("chatId") Long chatId) {
        Response<ChatInfo> chatInfo = chatController.getChatInfo(chatId);
        if (chatInfo.getResponseStatus() == ResponseStatus.OK) {
            return ResponseEntity.ok(chatInfo.getData());
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
