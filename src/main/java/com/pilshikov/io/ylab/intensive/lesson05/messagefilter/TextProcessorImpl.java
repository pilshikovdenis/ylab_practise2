package com.pilshikov.io.ylab.intensive.lesson05.messagefilter;

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

    private List<String> cropWords(String text) {
        String[] words = text.split(" ");

        List<String> result = new ArrayList<>();
        for (String word : words) {
            String wordWithoutSymbols = word.replace(",", "")
                    .replace("?", "")
                    .replace(".", "")
                    .replace(";", "")
                    .replace(",", "")
                    .replace("!", "")
                    .replace("\\s", "");
            if (!wordWithoutSymbols.isEmpty() && !wordWithoutSymbols.isBlank()) {
                result.add(wordWithoutSymbols);
            }

        }
        return (result.isEmpty()) ? null : result;
    }

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
            String outputMessage = inputMessage;
            List<String> words = cropWords(inputMessage);

            for (String w : words) {
                if (!dbClient.isWordValid(w)) {
                    outputMessage = outputMessage.replace(w, addCensorship(w));
                }
            }

            rabbitMQClient.sendOutputMessage(outputMessage);
            System.out.println("Отправлено :" + outputMessage);
        } catch (IOException | TimeoutException | SQLException e) {
            throw new RuntimeException(e);
        }


    }
}
