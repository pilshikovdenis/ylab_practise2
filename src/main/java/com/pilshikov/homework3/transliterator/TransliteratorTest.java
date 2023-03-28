package com.pilshikov.homework3.transliterator;

public class TransliteratorTest {
    public static void main(String[] args) {
        Transliterator transliterator = new TransliteratorImpl();
        String translatedString = transliterator.transliterate("HELLO! ПРИВЕТ! Go, boy!");
        System.out.println(translatedString);
    }
}
