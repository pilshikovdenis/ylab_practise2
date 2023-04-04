package com.pilshikov.io.ylab.intensive.lesson05.messagefilter;

import com.pilshikov.io.ylab.intensive.lesson05.messagefilter.text_processor.TextProcessorScheduler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class MessageFilterApp {

  public static void main(String[] args) {
    AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
    applicationContext.start();



    TextProcessorScheduler textProcessorScheduler = applicationContext.getBean(TextProcessorScheduler.class);
    textProcessorScheduler.start();


  }
}
