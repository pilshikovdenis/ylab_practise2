package com.pilshikov.io.ylab.intensive.lesson05.messagefilter;

import org.springframework.stereotype.Component;

@Component
public class TextProcessorScheduler {
    private final TextProcessor textProcessor;

    public TextProcessorScheduler(TextProcessor textProcessor) {
        this.textProcessor = textProcessor;
    }

    public void start() {
        while (!Thread.currentThread().isInterrupted()) {
            textProcessor.processSingleMessage();
        }
    }
}
