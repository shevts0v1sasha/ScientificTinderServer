package ru.tinder.rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.tinder.model.ChatMessage;
import ru.tinder.model.ChatNotification;

@Controller
public class TestSocketRestController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;


    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        System.out.println(chatMessage.getMessage());

        messagingTemplate.convertAndSendToUser(
                "0","/queue/messages",
                new ChatNotification(
                        "HELLO FROM SERVER!!"
                ));
    }
}
