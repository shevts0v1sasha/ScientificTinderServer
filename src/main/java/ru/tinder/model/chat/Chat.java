package ru.tinder.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {

    private Long id;

    private List<Long> participants;

    private List<ChatMessage> messages;
}
