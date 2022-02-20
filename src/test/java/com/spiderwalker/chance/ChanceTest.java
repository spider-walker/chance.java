package com.spiderwalker.chance;


import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


import com.spiderwalker.chance.constant.Constants;
import com.spiderwalker.chance.exception.RangeError;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChanceTest {

    @Test
    public void shouldLoadFile() {
        Chance chance = new Chance();
        File file = chance.readfile();
        assertTrue(file.exists());

    }

    @Test
    public void shouldReadJsonFile() {
        Chance chance = new Chance();
        Map<String, ?> map = chance.readJson();
        assertEquals(27, map.size());
    }

    @Test
    public void shouldGeneratteListOfObjects() {
        Map<String, Object> options = new HashMap<>();
        options.put("likelihood", 30);
        Chance chance = new Chance();
        Supplier<Object> func = () -> chance.bool(options);
        List<Object> list = chance.range(func, 6);
        assertEquals(6, list.size());
    }

    @Test
    public void shoudlGenerateRandomInt() {
        Chance chance = new Chance();
        int upper = 12;
        int lower = 11;
        int result = chance.random(lower, upper);
        assertTrue(result >= lower && result <= upper);

    }

    @Test
    public void shoudlGenerateRandomInteger() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        int upper = Constants.MAX_INT;
        int lower = 0;
        options.put("min", lower);
        options.put("max", upper);
        int result = chance.integer(options);
        assertTrue(result >= lower && result <= upper);

    }

    @Test
    public void shouldThrowError() {
        Map<String, Object> options = new HashMap<>();
        options.put("likelihood", 130);
        Chance chance = new Chance();
        Supplier<Object> func = () -> chance.bool(options);
        assertThrows(RangeError.class, () -> {
            chance.range(func, 6);
        });

    }

    @Test
    public void shouldGiveOne() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        String[] arr = {"desert", "forest", "ocean", "zoo", "farm", "pet", "grassland"};
        assertNotNull(chance.pickone(Arrays.asList(arr)));

    }

    @Test
    public void shouldPickEmicon() {
        Chance chance = new Chance();
        assertFalse(chance.emotion().isEmpty());
    }

    @Test
    public void shouldNItems() {
        Map<String, Object> options = new HashMap<>();
        options.put("likelihood", 130);
        Chance chance = new Chance();
        Supplier<Object> func = () -> chance.natural(new HashMap<>());
        List<Integer> list = (List<Integer>) chance.n(func, 10);
        assertTrue(10 == list.size());

    }

    @Test
    public void shouldIDNumberFoBrazilCompanies() {
        Chance chance = new Chance();
        assertFalse(chance.cnpj().isEmpty());
    }

    @Test
    public void shouldPad() {
        Chance chance = new Chance();
        assertEquals("01234", chance.pad(1234, 5, "0"));
        assertEquals("0000001234", chance.pad(1234, 10, "0"));
        assertEquals("1234", chance.pad(1234, 4, "0"));

    }

    @Test
    public void shouldPickItemsInArray() {
        Object[] array1 = new Object[0];
        Chance chance = new Chance();
        assertEquals(0, chance.pickset(array1, 0).length);

        assertThrows(RangeError.class, () -> {
            chance.pickset(array1, 4);
        });
        Object[] array2 = {1, 4, 5};

        assertThrows(RangeError.class, () -> {
            chance.pickset(array2, -4);
        });
        Object[] array3 = {1, 4, 5};

        assertEquals(1, chance.pickset(array3, 1).length);

        Object[] array4 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 12, 13, 14};
        assertEquals(5, chance.pickset(array4, 5).length);
    }

    @Test
    public void shouldShuffleItemsInArray() {
        Object[] array1 = new Object[0];
        Chance chance = new Chance();
        assertEquals(0, chance.shuffle(array1).length);

        Object[] array3 = {1, 4, 5};

        assertEquals(3, chance.shuffle(array3).length);

        Object[] array4 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 12, 13, 14};
        assertEquals(14, chance.shuffle(array4).length);
    }

    @Test
    public void shouldReturnSingleItem() {
        Chance chance = new Chance();

        Object[] array1 = {1, 4, 5};
        int[] weights1 = {1, 4};

        assertThrows(RangeError.class, () -> {
            chance.weighted(array1, weights1, true);
        });

        Object[] array2 = {1, 4, 5, 8, 5, 6};
        int[] weights2 = {1, 1, 1, 1, 1, 2};

        assertNotNull(chance.weighted(array2, weights2, true));
    }

    @Test
    public void shouldReturnSingleAnimal() {
        Chance chance = new Chance();

        Map<String, Object> options = new HashMap<>();
        options.put("type", "");
        assertThrows(RangeError.class, () -> {
            chance.animal(options);
        });

        options.put("type", "ocean");

        assertNotNull(chance.animal(options));
        assertNotNull(chance.animal(new HashMap<>()));
    }

    @Test
    public void shouldReturnARandomCharacter() {
        Chance chance = new Chance();

        Map<String, Object> options = new HashMap<>();
        options.put("casing", "lower");
        assertTrue((Constants.CHARS_LOWER + Constants.NUMBERS + Constants.SYMBOLS).contains(String.valueOf(chance.character(options))));
        options.put("casing", "upper");
        assertTrue((Constants.CHARS_UPPER + Constants.NUMBERS + Constants.SYMBOLS).contains(String.valueOf(chance.character(options))));
        options.put("numeric", "upper");
        assertTrue(Constants.NUMBERS.contains(String.valueOf(chance.character(options))));
    }

    @Test
    public void shouldReturnArandomFloatingPointNumber() {
        Chance chance = new Chance();

        Map<String, Object> options1 = new HashMap<>();
        options1.put("fixed", 4);
        float result = chance.floating(options1);

    }

    @Test
    public void shouldReturnAPrimeNumber() {
        Chance chance = new Chance();

        Map<String, Object> options = new HashMap<>();
        options.put("min", -10);
        assertThrows(RangeError.class, () -> {
            chance.prime(options);
        });

        options.put("min", 10);
        options.put("max", 5);
        assertThrows(RangeError.class, () -> {
            chance.prime(options);
        });
    }

    @Test
    public void shouldReturnRandomLetter() {
        Chance chance = new Chance();

        Map<String, Object> options = new HashMap<>();
        assertNotNull(chance.letter(options));

    }

    @Test
    public void shouldReturnaRandomHexNumberAsString() {
        Chance chance = new Chance();

        Map<String, Object> options = new HashMap<>();
        options.put("min", 10000);
        options.put("max", 5);
        assertThrows(RangeError.class, () -> {
            chance.hex(options);
        });
        options.put("max", 500000);
        System.out.println(chance.hex(options));
        assertNotNull(chance.hex(options));

    }

    @Test
    public void shouldReturnRandomString() {
        Chance chance = new Chance();

        Map<String, Object> options = new HashMap<>();
        options.put("length", -2);
        assertThrows(RangeError.class, () -> {
            chance.string(options);
        });
        options.put("length", 6);
        assertEquals(6, chance.string(options).length());
    }

    @Test
    public void shouldIsraelId() {
        Chance chance = new Chance();
        assertEquals(23, chance.israelId().length());
    }

    @Test
    public void shouldReturnMonths() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        List<Map<String, Object>> months = chance.months();
        assertEquals(12, months.size());
    }

    @Test
    public void shouldReturnAge() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        int age = chance.age(options);
        assertTrue(age > 18 && age < 65);

        options.put("type", "child");
        age = chance.age(options);
        assertTrue(age >= 0 && age <= 22);

        options.put("type", "teen");
        age = chance.age(options);
        assertTrue(age >= 13 && age <= 19);
    }

    @Test
    public void shouldReturnMonth() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        assertThrows(RangeError.class, () -> {
            options.put("min", -1);
            chance.month(options);
        });
        assertThrows(RangeError.class, () -> {
            options.put("min", 6);
            options.put("max", 14);
            chance.month(options);
        });
        assertThrows(RangeError.class, () -> {
            options.put("min", 6);
            options.put("max", 3);
            chance.month(options);
        });
        options.put("min", 3);
        options.put("max", 4);
        assertEquals("March", chance.month(options));

        options.put("raw", true);
        Map<String, Object> map = new HashMap();
        map.put("name", "March");
        map.put("short_name", "Mar");
        map.put("numeric", "03");
        map.put("days", 31.0);
        assertEquals(map, (chance.month(options)));
    }

    @Test
    public void shouldReturnmrz() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        //assertEquals(23,chance.mrz(options).length());
    }

    @Test
    public void shouldReturnDate() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        assertNotNull( chance.date(options));
    }

    @Test
    public void shouldReturnYear() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        int min = LocalDateTime.now().getYear();
        int year = chance.year(options);
        assertTrue(year >= min && year < (min + 100));
        int max = min + 10;
        options.put("max", max);
        year = chance.year(options);
        assertTrue(year >= min && year < (max));
    }

    @Test
    public void shouldReturnMonthObject() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        String monthString = chance.month(options);
        assertNotNull(monthString);

        options.put("raw", true);
        Map<String, Object> monthMap = chance.month(options);

    }

    @Test
    public void shouldCapitalize() {
        Chance chance = new Chance();
        assertEquals("Milk way", chance.capitalize("milk way"));
    }
    @Test
    public void shouldReturnString() {
        Chance chance = new Chance();
        assertNotNull( chance.string(null));
    }

    @Test
    public void shouldReturnWord() {
        Chance chance = new Chance();
        assertFalse( chance.word(null).isEmpty());
    }

    @Test
    public void shouldReturnFirst() {
        Chance chance = new Chance();
        assertFalse( chance.first(null).isEmpty());
    }

    @Test
    public void shouldReturnLast() {
        Chance chance = new Chance();
        assertFalse( chance.last(null).isEmpty());
    }

    @Test
    public void shouldReturnCodicefiscale() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        assertEquals("Milk Way", chance.cf(options));
    }

    @Test
    public void shouldReturnBirthday() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = new Chance();
        assertNotNull(chance.birthday(options));
    }

}
