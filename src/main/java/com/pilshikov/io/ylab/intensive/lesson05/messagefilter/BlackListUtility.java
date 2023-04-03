package com.pilshikov.io.ylab.intensive.lesson05.messagefilter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlackListUtility {
    private final String blackListPath;

    public BlackListUtility(@Value("${blacklist.path}")String blackListPath) {
        this.blackListPath = blackListPath;
    }

    public String getBlackListPath() {
        return blackListPath;
    }


    // Метод считывает файл и возвращает список запрещенных слов
    public List<String> getBlackList() throws IOException {
        File file = new File(blackListPath);
        // считываем сразу весь файл как массив байтов
        byte[] bytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bytes);
        fis.close();

        // получаем строку из массива байт и разрезаем
        String[] parsedStrings = new String(bytes).split("\\s");
        if (parsedStrings.length == 0) {
            return null;
        }

        // заполняем массив и возвращаем
        List<String> words = new ArrayList<>();
        for (String word : parsedStrings) {
            if (!word.isEmpty()) {
                words.add(word.trim());
            }
        }


        return words.stream().distinct().collect(Collectors.toList());
    }
}
