package com.example.security;

import java.util.regex.Pattern;

public class PasswordChecker {
    private static final Pattern COMPLEXITY =
            Pattern.compile("^(?=.{12,}$)(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9]).*$");

    public static boolean validate (String pw) {
        if (pw == null) return false;
        return COMPLEXITY.matcher(pw).matches();
    }

    public static String requirements(){
        return "Passwords must be atleast 12 characters, \n"
             + "include uppercase, lowercase, digit & symbols.";
    }
}
