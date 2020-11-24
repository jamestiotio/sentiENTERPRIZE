package com.example.posystem2;

import java.util.HashMap;

public class InsecureAuth {

    private static HashMap<String, String> hardcodedAuthIsInsecure;

    InsecureAuth() {
        hardcodedAuthIsInsecure = new HashMap<>();

        // THIS IS VERY BAD PRACTICE
        hardcodedAuthIsInsecure.put("hi@ragulbalaji.com", "p@ssword");
        hardcodedAuthIsInsecure.put("bob@gmail.com", "thebuilder");
    }

    public boolean isLegitUser(String email, String password) {
        if (hardcodedAuthIsInsecure.containsKey(email)) {
            return hardcodedAuthIsInsecure.get(email).compareTo(password) == 0;
        }

        return false;
    }
}
