package ru.tinder.model.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatInfo {

    private Long id;

    private Long chatId;

    private String name;

    private String topic;

    private String previewPath;
}
