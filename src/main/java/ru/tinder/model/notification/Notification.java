package ru.tinder.model.notification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinder.model.TDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    private Long id;
    private Long recipientId;
    private TDate created;
    private String message;
    private boolean shown;
    private NotificationType type;


}
