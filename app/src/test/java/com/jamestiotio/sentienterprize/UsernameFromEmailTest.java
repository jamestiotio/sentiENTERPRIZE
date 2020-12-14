package com.jamestiotio.sentienterprize;

import org.junit.Test;

import static org.junit.Assert.*;

import static com.jamestiotio.sentienterprize.utils.GetUsernameFromEmail.usernameFromEmail;

public class UsernameFromEmailTest {
    @Test
    public void checkEmptyEmail() {
        String email = "";
        String result = "";

        assertEquals(result, usernameFromEmail(email));
    }

    @Test
    public void checkNoAtSign() {
        String email = "example";
        String result = "example";

        assertEquals(result, usernameFromEmail(email));
    }

    @Test
    public void checkHasOneAtSign() {
        String email = "test@example.com";
        String result = "test";

        assertEquals(result, usernameFromEmail(email));
    }

    @Test
    public void checkHasTwoAtSigns() {
        String email = "invalid@example@email.com";
        String result = "invalid";

        assertEquals(result, usernameFromEmail(email));
    }

    @Test
    public void checkHasSevenAtSigns() {
        String email = "long@invalid@example@email@with@multiple@at@signs.com";
        String result = "long";

        assertEquals(result, usernameFromEmail(email));
    }

    @Test
    public void checkStartWithAtSign() {
        String email = "@email.com";
        String result = "";

        assertEquals(result, usernameFromEmail(email));
    }

    @Test
    public void checkEndWithAtSign() {
        String email = "example_email.com@";
        String result = "example_email.com";

        assertEquals(result, usernameFromEmail(email));
    }

    @Test
    public void checkStartAndEndWithAtSign() {
        String email = "@example_email.com@";
        String result = "";

        assertEquals(result, usernameFromEmail(email));
    }

    @Test
    public void checkOnlyOneAtSign() {
        String email = "@";
        String result = "";

        assertEquals(result, usernameFromEmail(email));
    }

    @Test
    public void checkOnlyMultipleAtSigns() {
        String email = "@@@@@@@@@@@@@@@@@";
        String result = "";

        assertEquals(result, usernameFromEmail(email));
    }
}