package com.pilshikov.io.ylab.intensive.lesson05.eventsourcing.db;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

@Component
public class PersonMessageReceiverScheduler {
    private PersonMessageReceiver personMessageReceiver;

    public PersonMessageReceiverScheduler(PersonMessageReceiver personMessageReceiver) {
        this.personMessageReceiver = personMessageReceiver;
    }

    public void start() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                personMessageReceiver.processOneMessage();
            } catch (IOException | TimeoutException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
