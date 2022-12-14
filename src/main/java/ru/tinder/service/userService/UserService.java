package ru.tinder.service.userService;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.tinder.constants.Constants;
import ru.tinder.dto.auth.LoginRequest;
import ru.tinder.model.user.Role;
import ru.tinder.model.user.User;
import ru.tinder.model.user.UserInfo;
import ru.tinder.utils.IdGenerator;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private final UserInfo[] userInfos = {
            new UserInfo("Александр", "Шевцов", "Николаевич", "Урфу ИРИт ртф Прикладная информатика", "Студент",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null, Constants.API + "/user-photo/1.jpg", Constants.API + "/user-photo/1_preview.jpg"),
            new UserInfo("Николай", "Иванович", "Петрович", "Урфу ИРИт ртф Прикладная информатика", "Научный руководитель",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null, Constants.API + "/user-photo/2.jpg", Constants.API + "/user-photo/2_preview.jpg"),
            new UserInfo("Василий", "Васильев", "Васильевич", "Урфу ИРИт ртф Прикладная информатика", "Научный руководитель",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null, Constants.API + "/user-photo/3.jpg", Constants.API + "/user-photo/3_preview.jpg"),
            new UserInfo("Евгений", "Высоцкий", "Александрович", "Урфу ИРИт ртф Прикладная информатика", "Научный руководитель",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null,  Constants.API + "/user-photo/4.jpg", Constants.API + "/user-photo/4_preview.jpg"),
            new UserInfo("Иван", "Иванов", "Иванович", "Урфу ИРИт ртф Прикладная информатика", "Научный руководитель",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null, Constants.API + "/user-photo/5.jpg", Constants.API + "/user-photo/5_preview.jpg"),
            new UserInfo("Артем", "Селезнев", "Александрович", "Урфу ИРИт ртф Прикладная информатика", "Научный руководитель",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null, Constants.API + "/user-photo/6.jpg", Constants.API + "/user-photo/6_preview.jpg"),
            new UserInfo("Владимир", "Добрынин", "Владимирович", "Урфу ИРИт ртф Прикладная информатика", "Научный руководитель",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null, Constants.API + "/user-photo/7.jpg", Constants.API + "/user-photo/7_preview.jpg"),
            new UserInfo("Никита", "Печков", "Александрович", "Урфу ИРИт ртф Прикладная информатика", "Научный руководитель",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null, Constants.API + "/user-photo/8.jpg", Constants.API + "/user-photo/8_preview.jpg"),
            new UserInfo("Илья", "Гущин", "Александрович", "Урфу ИРИт ртф Прикладная информатика", "Научный руководитель",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null, Constants.API + "/user-photo/9.jpg", Constants.API + "/user-photo/9_preview.jpg"),
            new UserInfo("Андрей", "Евдокимов", "Александрович", "Урфу ИРИт ртф Прикладная информатика", "Научный руководитель",
                    "Люблю программировать по вечерам и вкусный кофе", "Бакалавр", "-", "vk.com/mntndw",
                    null, Constants.API + "/user-photo/10.jpg", Constants.API + "/user-photo/10_preview.jpg")
    };
    private final ConcurrentHashMap<String, User> userStorage = new ConcurrentHashMap<>();

    public HttpStatus registration(User user) {
        if (user.getUsername().isEmpty()) {
            return HttpStatus.BAD_REQUEST;
        }
        if (userStorage.containsKey(user.getUsername())) {
            return HttpStatus.CONFLICT;
        }
        userStorage.put(user.getUsername(), user);
        return HttpStatus.CREATED;
    }

    public Optional<User> login(LoginRequest loginRequest) {
        User user = userStorage.get(loginRequest.getUsername());
        return Optional.ofNullable(user != null ? (user.getPassword().equals(loginRequest.getPassword()) ? user : null) : null);
    }

    @PostConstruct
    public void fillUserStorage() {
        userStorage.put("alex_1", new User(IdGenerator.getUniqueId(), "alex_1", "1", userInfos[0], Role.STUDENT));
        userStorage.put("nikolay_1", new User(IdGenerator.getUniqueId(), "nikolay_1", "1", userInfos[1], Role.SCIENTIST));
        userStorage.put("vasiliy_1", new User(IdGenerator.getUniqueId(), "vasiliy_1", "1", userInfos[2], Role.SCIENTIST));
        userStorage.put("evgeniy_1", new User(IdGenerator.getUniqueId(), "evgeniy_1", "1", userInfos[3], Role.STUDENT));
        userStorage.put("ivan_1", new User(IdGenerator.getUniqueId(), "ivan_1", "1", userInfos[4], Role.ACADEMIC_SUPERVISOR));
        userStorage.put("artem_1", new User(IdGenerator.getUniqueId(), "artem_1", "1", userInfos[5], Role.STUDENT));
        userStorage.put("vladimir_1", new User(IdGenerator.getUniqueId(), "vladimir_1", "1", userInfos[6], Role.SCIENTIST));
        userStorage.put("nikita_1", new User(IdGenerator.getUniqueId(), "nikita_1", "1", userInfos[7], Role.SCIENTIST));
        userStorage.put("ilya_1", new User(IdGenerator.getUniqueId(), "ilya_1", "1", userInfos[8], Role.ACADEMIC_SUPERVISOR));
        userStorage.put("andrey_1", new User(IdGenerator.getUniqueId(), "andrey_1", "1", userInfos[9], Role.STUDENT));
    }

    // TODO: 27.10.2022 Придумать, как эффективно искать и по айдишнику, и по логину
    public Optional<User> findUserById(Long userId) {
        User user = null;
        for (Map.Entry<String, User> entry : userStorage.entrySet()) {
            if (entry.getValue().getId().equals(userId)) {
                user = entry.getValue();
                break;
            }
        }
        return Optional.ofNullable(user);
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());
    }
}
