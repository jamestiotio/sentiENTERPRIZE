package com.jamestiotio.sentienterprize;

import org.junit.Test;

import static org.junit.Assert.*;

import com.jamestiotio.sentienterprize.models.User;

import java.util.Objects;
import java.util.stream.Stream;

public class InventoryUserTest {
    @Test
    public void checkUninitializedDataFields() {
        User user = new User();

        assertTrue(Stream.of(user.username, user.email).allMatch(Objects::isNull));
    }

    @Test
    public void checkEmptyUsernameAndEmail() {
        User user = new User("", "");

        assertTrue(Stream.of(user.username, user.email).allMatch(String::isEmpty));
    }

    @Test
    public void checkValidUsername() {
        User user = new User("username", "");
        String result = "username";

        assertEquals(result, user.username);
    }

    @Test
    public void checkValidEmail() {
        User user = new User("", "email");
        String result = "email";

        assertEquals(result, user.email);
    }

    @Test
    public void checkSampleUsernameAndEmail() {
        User user = new User("test", "test@example.com");
        String usernameResult = "test";
        String emailResult = "test@example.com";

        assertEquals(usernameResult, user.username);
        assertEquals(emailResult, user.email);
    }
}