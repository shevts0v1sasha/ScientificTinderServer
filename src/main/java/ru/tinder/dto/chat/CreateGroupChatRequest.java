package ru.tinder.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupChatRequest {
    private List<Long> participants;

    private String name;

    private String topic;

    private MultipartFile chatPreview;
}
