package com.example.kartik.service;

import org.springframework.stereotype.Service;

@Service
public class Base62Encoder {
    private static final String ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int BASE = ALLOWED_CHARACTERS.length();

    public static String encode(Long id) {
        if (id == 0) return String.valueOf(ALLOWED_CHARACTERS.charAt(0));

        StringBuilder sb = new StringBuilder();
        while (id > 0) {
            sb.append(ALLOWED_CHARACTERS.charAt((int) (id % BASE)));
            id /= BASE;
        }
        return sb.reverse().toString();
    }
}