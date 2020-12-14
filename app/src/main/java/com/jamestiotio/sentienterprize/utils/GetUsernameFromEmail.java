package com.jamestiotio.sentienterprize.utils;

public class GetUsernameFromEmail {
    public static String usernameFromEmail(String email) {
        if (email.matches("[@]+")) {
            return "";
        } else if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
}