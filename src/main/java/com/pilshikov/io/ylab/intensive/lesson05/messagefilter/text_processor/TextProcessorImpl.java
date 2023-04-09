package com.pilshikov.io.ylab.intensive.lesson05.messagefilter.text_processor;

import com.pilshikov.io.ylab.intensive.lesson05.messagefilter.DBClient;
import com.pilshikov.io.ylab.intensive.lesson05.messagefilter.RabbitMQClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
public class TextProcessorImpl implements TextProcessor {

    private RabbitMQClient rabbitMQClient;
    private DBClient dbClient;

    public TextProcessorImpl(RabbitMQClient rabbitMQClient, DBClient dbClient) {
        this.rabbitMQClient = rabbitMQClient;
        this.dbClient = dbClient;
    }

    // Метод проверяет является ли char в указанной позиции одним из разделяющих символов
    private boolean charIsSymbol(String str, int i) {
        char[] symbols = {'.', ' ', ',', ';', '?', '!', '\n'};
        char n = str.charAt(i);
        for (char ch : symbols) {
            if (ch == n) {
                return true;
            }
        }
        return false;
    }

    // Метод проверяет, есть ли в строке после указанной позиции еще буквы
    private boolean letterExistsAfterIndex(String str, int index) {
        for (int i = index; i < str.length(); i++) {
            if (!charIsSymbol(str, i)) {
                return true;
            }
        }
        return false;
    }

    // Метод разрезает строку, возвращает список с массивами формата [начало_слова, конец_слова]
    private List<int[]> cropWords(String text) {
        if (text.isEmpty()) return null;

        List<int[]> words = new ArrayList<>();

        int beginFindIndex = 0;
        int first = -1;
        int second = -1;

        while (letterExistsAfterIndex(text, beginFindIndex)) {
            for (int i = beginFindIndex; i < text.length(); i++) {
                if (!charIsSymbol(text, i)) {
                    first = i;
                    break;
                }
            }
            for (int j = first; j < text.length(); j++) {
                if (j == text.length() - 1) {
                    second = j;
                    break;
                }
                if (charIsSymbol(text, j + 1)) {
                    second = j;
                    break;
                }
            }
            beginFindIndex = second + 1;

            words.add(new int[]{first, second});

        }
        return words;
    }

    // Метод добавляет цензуру в указанное слово
    private String addCensorship(String word) {
        if (word.length() == 1) {
            return word;
        }

        String substr = word.substring(1, word.length()-1);
        return word.replace(substr, "*".repeat(substr.length()));
    }


    @Override
    public void processSingleMessage() {
        try {
            String inputMessage = rabbitMQClient.getInputMessage();
            if(inputMessage == null) {
                return;
            }

            System.out.println("Получено: " + inputMessage);
            StringBuilder builder = new StringBuilder(inputMessage);

            List<int[]> wordPositions = cropWords(inputMessage);
            for (int[] a : wordPositions) {
                String word = inputMessage.substring(a[0], a[1]+1);

                if (!dbClient.isWordValid(word)) {
                    String wordWithCensor = addCensorship(word);
                    builder.replace(a[0], a[1]+1, wordWithCensor);
                }
            }

            rabbitMQClient.sendOutputMessage(builder.toString());
            System.out.println("Отправлено :" + builder.toString());
        } catch (IOException | TimeoutException | SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
