package ru.tinder.rest.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tinder.controller.ChatController;
import ru.tinder.model.chat.Chat;
import ru.tinder.model.response.Response;
import ru.tinder.model.response.ResponseStatus;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatController chatController;

    @GetMapping(params = "participantId")
    public ResponseEntity<List<Chat>> getChatByParticipantId(@RequestParam("participantId") Long participantId) {
        return ResponseEntity.ok(chatController.getChatByParticipantId(participantId).getData());
    }

}
