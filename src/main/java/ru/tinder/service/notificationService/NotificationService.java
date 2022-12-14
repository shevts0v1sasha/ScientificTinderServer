package ru.tinder.service.notificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.tinder.model.notification.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<Long, List<Notification>> userNotifications = new HashMap<>();

    public void sendNotification(Notification notification) {

        if (!userNotifications.containsKey(notification.getRecipientId())) {
            userNotifications.put(notification.getRecipientId(), new ArrayList<>());
        }
        userNotifications.get(notification.getRecipientId()).add(notification);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(notification.getRecipientId()),
                "/notifications",
                notification
        );
    }

    public List<Notification> getUserNotifications(Long userId) {
        return userNotifications.get(userId);
    }

    public boolean setShown(Long userId) {
        userNotifications.get(userId).forEach(notification -> notification.setShown(true));
        return true;
    }

}
