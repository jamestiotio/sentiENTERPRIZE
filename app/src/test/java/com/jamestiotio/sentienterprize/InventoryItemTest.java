package com.jamestiotio.sentienterprize;

import org.junit.Test;

import static org.junit.Assert.*;

import com.jamestiotio.sentienterprize.models.Item;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Stream;

public class InventoryItemTest {
    @Test
    public void checkUninitializedDataFields() {
        Item item = new Item();

        assertTrue(Stream.of(item.uid, item.author, item.name, item.body, item.amount, item.unitPrice).allMatch(Objects::isNull));
        assertEquals(0, item.starCount);
        assertTrue(item.stars.isEmpty());
    }

    @Test
    public void checkEmptyAttributes() {
        Item item = new Item("", "", "", "", 0, new BigDecimal("0.0"));
        int amountResult = 0;
        String unitPriceResult = "0.0";
        int starCountResult = 0;

        assertTrue(Stream.of(item.uid, item.author, item.name, item.body).allMatch(String::isEmpty));
        assertEquals(amountResult, item.amount.intValue());
        assertEquals(unitPriceResult, item.unitPrice.toString());
        assertEquals(starCountResult, item.starCount);
        assertTrue(item.stars.isEmpty());
    }

    @Test
    public void checkValidAttributes() {
        Item item = new Item("8c4461f7-95fe-4947-a08b-40a22952a0d4", "Rick Sanchez (Dimension C-137)", "Love Potion", "A chemical drug designed to induce romantic and sexual hormones.", 500, new BigDecimal("449.99"));
        String uidResult = "8c4461f7-95fe-4947-a08b-40a22952a0d4";
        String authorResult = "Rick Sanchez (Dimension C-137)";
        String nameResult = "Love Potion";
        String bodyResult = "A chemical drug designed to induce romantic and sexual hormones.";
        int amountResult = 500;
        String unitPriceResult = "449.99";
        int starCountResult = 0;

        assertEquals(uidResult, item.uid);
        assertEquals(authorResult, item.author);
        assertEquals(nameResult, item.name);
        assertEquals(bodyResult, item.body);
        assertEquals(amountResult, item.amount.intValue());
        assertEquals(unitPriceResult, item.unitPrice.toString());
        assertEquals(starCountResult, item.starCount);
        assertTrue(item.stars.isEmpty());
    }

    @Test
    public void checkValidToMapMethod() {
        Item item = new Item("8c4461f7-95fe-4947-a08b-40a22952a0d4", "Rick Sanchez (Dimension C-137)", "Love Potion", "A chemical drug designed to induce romantic and sexual hormones.", 500, new BigDecimal("449.99"));
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", "8c4461f7-95fe-4947-a08b-40a22952a0d4");
        result.put("author", "Rick Sanchez (Dimension C-137)");
        result.put("name", "Love Potion");
        result.put("body", "A chemical drug designed to induce romantic and sexual hormones.");
        result.put("amount", 500);
        result.put("unitPrice", new Double("449.99"));
        result.put("starCount", 0);
        result.put("stars", new HashMap<>());

        assertEquals(result, item.toMap());
    }
}