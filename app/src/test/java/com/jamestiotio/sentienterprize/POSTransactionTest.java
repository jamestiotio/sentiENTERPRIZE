package com.jamestiotio.sentienterprize;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import com.jamestiotio.sentienterprize.POS.Item;
import com.jamestiotio.sentienterprize.POS.NewTransaction;
import com.jamestiotio.sentienterprize.POS.TransactionSingle;
import com.jamestiotio.sentienterprize.POS.TransactionClassification;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class POSTransactionTest {
    @Test
    public void checkTransactionItemInitialAttributes() {
        Item item = new Item("Love Potion", 250, 499.5999);

        assertEquals("Love Potion", item.getName());
        assertEquals(250, item.getQuantity());
        assertEquals(499.5999, item.getUnitPrice(), 0.0001);
        assertEquals("499.60", item.getStringAmount());
        assertEquals("Love Potion 250 499.5999", item.toString());
    }

    @Test
    public void checkTransactionItemModifiedAttributes() {
        Item item = new Item("Love Potion", 250, 499.5999);

        item.setName("Portal Gun");
        assertEquals("Portal Gun", item.getName());

        item.setQuantity(500);
        assertEquals(500, item.getQuantity());

        item.setUnitPrice(59.4459);
        assertEquals(59.4459, item.getUnitPrice(), 0.0001);
        assertEquals("59.45", item.getStringAmount());

        item.setName("Cloak of Invisibility");
        item.setName("Neuralyzer");
        assertEquals("Neuralyzer", item.getName());

        item.setQuantity(1000);
        item.setQuantity(42069);
        assertEquals(42069, item.getQuantity());

        item.setUnitPrice(20.5999);
        item.setUnitPrice(111.0000);
        assertEquals(111.0000, item.getUnitPrice(), 0.0001);
        assertEquals("111.00", item.getStringAmount());

        assertEquals("Neuralyzer 42069 111.0", item.toString());
    }

    @Test
    public void checkNewTransactionTotalCalculation() {
        NewTransaction newTransaction = new NewTransaction();
        ArrayList<Item> newItemList = new ArrayList<>(Arrays.asList(
                new Item("Love Potion", 250, 499.5999),
                new Item("Lightsaber", 10, 259.4599),
                new Item("The One Ring to Rule Them All", 1, 1999.99),
                new Item("Batmobile", 2, 69.69),
                new Item("Mobius Strip", 5, 420.00)
        ));
        double result = 131733.944;

        assertEquals(result, newTransaction.calculateTransTotal(newItemList), 0.0001);
    }

    // TODO: Check if transaction's date >= company's date of establishment/founding
    @Test
    public void checkTransactionSingleDefaultAttributes() {
        TransactionSingle transactionSingle = new TransactionSingle();

        assertEquals("Card", transactionSingle.getTransType());
        assertTrue(UnitTestUtils.isValidTransactionCodeFormat(transactionSingle.getCode()));
        assertTrue(transactionSingle.getTimestamp() - LocalDateTime.parse(transactionSingle.getDatetime(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                .atZone(ZoneId.of("Etc/UTC")).toInstant().toEpochMilli() < 60000L); // Difference is less than a minute
        assertTrue(UnitTestUtils.isValidDateFormat("dd/MM/yyyy HH:mm", transactionSingle.getDatetime(), Locale.ENGLISH));
        assertEquals(0.0d, transactionSingle.getTransTotal(), 0.0001);
        assertNull(transactionSingle.getItemList());
        assertTrue(UnitTestUtils.isValidDateFormat("dd/MM/yyyy HH:mm", transactionSingle.toString(), Locale.ENGLISH));
    }

    @Test
    public void checkTransactionSingleModifiedAttributes() {
        TransactionSingle transactionSingle = new TransactionSingle();
        transactionSingle.setTransType("Cash");

        ArrayList<Item> newItemList = new ArrayList<>(Arrays.asList(
                new Item("Love Potion", 250, 499.5999),
                new Item("Lightsaber", 10, 259.4599),
                new Item("The One Ring to Rule Them All", 1, 1999.99),
                new Item("Batmobile", 2, 69.69),
                new Item("Mobius Strip", 5, 420.00)
        ));
        double result = 131733.944;

        transactionSingle.setItemList(newItemList);
        transactionSingle.setTransTotal(131733.944);

        assertEquals("Cash", transactionSingle.getTransType());
        assertTrue(UnitTestUtils.isValidTransactionCodeFormat(transactionSingle.getCode()));
        assertTrue(transactionSingle.getTimestamp() - LocalDateTime.parse(transactionSingle.getDatetime(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                .atZone(ZoneId.of("Etc/UTC")).toInstant().toEpochMilli() < 60000L); // Difference is less than a minute
        assertTrue(UnitTestUtils.isValidDateFormat("dd/MM/yyyy HH:mm", transactionSingle.getDatetime(), Locale.ENGLISH));
        assertEquals(result, transactionSingle.getTransTotal(), 0.0001);
        assertEquals(newItemList, transactionSingle.getItemList());
        assertTrue(UnitTestUtils.isValidDateFormat("dd/MM/yyyy HH:mm", transactionSingle.toString(), Locale.ENGLISH));
    }

    @Test
    public void checkTransactionSingleComparison() {
        TransactionSingle firstTransaction = new TransactionSingle();
        TransactionSingle secondTransaction = new TransactionSingle();

        assertEquals(1, firstTransaction.compareTo(secondTransaction));
        assertEquals(-1, secondTransaction.compareTo(firstTransaction));
        assertEquals(0, firstTransaction.compareTo(firstTransaction));
        assertEquals(0, secondTransaction.compareTo(secondTransaction));
    }

    // TODO: Check if transactions are classified and grouped correctly (by day, week and month) by modifying the transactions' timestamp attributes accordingly
    @Test
    public void checkTransactionClassificationGetterMethods() {

    }
}
