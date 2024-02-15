package com.example.bookstoredemo.service.validator;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {


    private static final String Email_Regex =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private final Pattern pattern;

    public EmailValidator(){
        pattern = Pattern.compile(Email_Regex);
    }
    @Override
    public boolean test(String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
