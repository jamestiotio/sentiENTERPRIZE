package com.jamestiotio.sentienterprize;

import org.junit.Test;

import static org.junit.Assert.*;

import com.jamestiotio.sentienterprize.models.Comment;

import java.util.Objects;
import java.util.stream.Stream;

public class InventoryCommentTest {
    @Test
    public void checkUninitializedDataFields() {
        Comment comment = new Comment();

        assertTrue(Stream.of(comment.uid, comment.author, comment.text).allMatch(Objects::isNull));
    }

    @Test
    public void checkEmptyUIDAuthorAndText() {
        Comment comment = new Comment("", "", "");

        assertTrue(Stream.of(comment.uid, comment.author, comment.text).allMatch(String::isEmpty));
    }

    @Test
    public void checkValidUID() {
        Comment comment = new Comment("42069definitely_a-random|user~id", "", "");
        String result = "42069definitely_a-random|user~id";

        assertEquals(result, comment.uid);
    }

    @Test
    public void checkValidAuthor() {
        Comment comment = new Comment("", "James Raphael Tiovalen", "");
        String result = "James Raphael Tiovalen";

        assertEquals(result, comment.author);
    }

    @Test
    public void checkValidText() {
        Comment comment = new Comment("", "", "This comment is made with love. <3");
        String result = "This comment is made with love. <3";

        assertEquals(result, comment.text);
    }

    @Test
    public void checkSampleUIDAuthorAndText() {
        Comment comment = new Comment("4540d93f-522c-4360-af3b-51d60961ca65", "jamestiotio", "Please restock this item ASAP!");
        String uidResult = "4540d93f-522c-4360-af3b-51d60961ca65";
        String authorResult = "jamestiotio";
        String textResult = "Please restock this item ASAP!";

        assertEquals(uidResult, comment.uid);
        assertEquals(authorResult, comment.author);
        assertEquals(textResult, comment.text);
    }
}