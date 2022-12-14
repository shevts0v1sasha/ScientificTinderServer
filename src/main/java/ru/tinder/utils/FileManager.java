package ru.tinder.utils;

import org.springframework.web.multipart.MultipartFile;
import ru.tinder.constants.Constants;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager {

    public static String[] saveChatPreview(MultipartFile multipartFile, String filePrefix) throws IOException {
        if (multipartFile != null && !multipartFile.isEmpty()) {
            byte[] bytes = multipartFile.getBytes();
            String fileName = (filePrefix != null) ? filePrefix + "_" + multipartFile.getOriginalFilename() : multipartFile.getOriginalFilename();
            String savePath = Constants.CHAT_PREVIEWS_FOLDER + fileName;
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(savePath));
            stream.write(bytes);
            stream.flush();
            stream.close();
            return new String[] {Constants.API + "/chat-previews/" + fileName, savePath};
        }
        return new String[] {};
    }
}
