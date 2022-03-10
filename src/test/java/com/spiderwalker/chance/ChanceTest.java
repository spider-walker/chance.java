package com.spiderwalker.chance;


import com.spiderwalker.chance.constant.Constants;
import com.spiderwalker.chance.exception.RangeError;
import com.spiderwalker.chance.util.FileUtils;
import com.spiderwalker.chance.util.ListUtils;
import com.spiderwalker.chance.util.NumberUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

public class ChanceTest {

    @Test
    public void shouldLoadFile() {
        File file = FileUtils.readFile();
        assertTrue(file.exists());
    }

    @Test
    public void shouldReadJsonFile() {
        Map<String, ?> map = FileUtils.readJson();
        assertEquals(27, map.size());
    }

    @Test
    public void shouldReturnWeekDay() {
        Chance chance = Chance.getInstance();
        String result = chance.weekday(null);
        assertNotNull(result);

    }

    @Test
    public void shouldReturnTimestamp() {
        Chance chance = Chance.getInstance();
        String result = String.valueOf(chance.timestamp(null));
        assertNotNull(result);
    }

    @Test
    public void shouldReturnHour() {
        Chance chance = Chance.getInstance();
        String result = String.valueOf(chance.hour(null));
        assertNotNull(result);
    }

    @Test
    public void shouldReturnAmPm() {
        Chance chance = Chance.getInstance();
        String result = String.valueOf(chance.ampm());
        assertNotNull(result);
    }

    @Test
    public void shouldReturnStreet() {
        Chance chance = Chance.getInstance();
        String result = String.valueOf(chance.street(null));
        assertNotNull(result);
    }

    @Test
    public void shouldReturnColor() {
        Chance chance = Chance.getInstance();
        Map<String, Object> options = new HashMap<>();
        options.put("format", "hex");
        String result = String.valueOf(chance.color(options));
        assertEquals(7, result.length());
        options.put("format", "rgb");
        result = String.valueOf(chance.color(options));
        assertNotNull(result);
        options.put("format", "name");
        result = String.valueOf(chance.color(options));
        assertNotNull(result);
    }

    @Test
    public void shouldReturnZip() {
        Chance chance = Chance.getInstance();
        String result = String.valueOf(chance.zip(null));
        assertNotNull(result);
        Map<String, Object> options = new HashMap<>();
        options.put("plusfour", true);
        result = String.valueOf(chance.zip(options));
        assertNotNull(result);
    }

    @Test
    public void shouldGenerateRandomInteger() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
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
        Chance chance = Chance.getInstance();
        Supplier<Object> func = () -> chance.bool(options);
        assertThrows(RangeError.class, () -> ListUtils.range(func, 6));

    }

    @Test
    public void shouldAddMixins() {
        Map<String, Supplier> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        Supplier d4 = () -> chance.diceFn(1,2);
        options.put("d12", d4);
        chance.mixins(options);
        Map<String,Supplier<Object>> fns=chance.mixin;
        Supplier supp= fns.get("d12");
        assertTrue((int)supp.get()>0);

    }


    @Test
    public void shouldGiveOne() {
        Chance chance = Chance.getInstance();
        assertNotNull(chance.pickone(List.of("desert", "forest", "ocean", "zoo", "farm", "pet", "grassland")));
    }

    @Test
    public void shouldPickEmoticon() {
        Chance chance = Chance.getInstance();
        assertFalse(chance.emotion().isEmpty());
    }

    @Test
    public void shouldNItems() {
        Chance chance = Chance.getInstance();
        Supplier<Object> func = () -> chance.natural(new HashMap<>());
        List<Integer> list = chance.n(func, 10);
        assertEquals(10, list.size());

    }

    @Test
    public void shouldIDNumberFoBrazilCompanies() {
        Chance chance = Chance.getInstance();
        assertFalse(chance.cnpj().isEmpty());
    }

    @Test
    public void shouldPad() {
        Chance chance = Chance.getInstance();
        assertEquals("01234", chance.pad(1234, 5, "0"));
        assertEquals("0000001234", chance.pad(1234, 10, "0"));
        assertEquals("1234", chance.pad(1234, 4, "0"));

    }

    @Test
    public void shouldPickItemsInArray() {
        Object[] array1 = new Object[0];
        Chance chance = Chance.getInstance();
        assertEquals(0, chance.pickset(array1, 0).length);

        assertThrows(RangeError.class, () -> chance.pickset(array1, 4));
        Object[] array2 = {1, 4, 5};

        assertThrows(RangeError.class, () -> chance.pickset(array2, -4));
        Object[] array3 = {1, 4, 5};

        assertEquals(1, chance.pickset(array3, 1).length);

        Object[] array4 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 12, 13, 14};
        assertEquals(5, chance.pickset(array4, 5).length);
    }

    @Test
    public void shouldShuffleItemsInArray() {
        Object[] array1 = new Object[0];
        Chance chance = Chance.getInstance();
        assertEquals(0, chance.shuffle(array1).length);

        Object[] array3 = {1, 4, 5};

        assertEquals(3, chance.shuffle(array3).length);

        Object[] array4 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 12, 13, 14};
        assertEquals(14, chance.shuffle(array4).length);
    }

    @Test
    public void shouldReturnSingleItem() {
        Chance chance = Chance.getInstance();

        Object[] array1 = {1, 4, 5};
        int[] weights1 = {1, 4};

        assertThrows(RangeError.class, () -> chance.weighted(array1, weights1));

        Object[] array2 = {1, 4, 5, 8, 5, 6};
        int[] weights2 = {1, 1, 1, 1, 1, 2};

        chance.weighted(array2, weights2);
    }

    @Test
    public void shouldReturnSingleAnimal() {
        Chance chance = Chance.getInstance();

        Map<String, Object> options = new HashMap<>();
        options.put("type", "");
        assertThrows(RangeError.class, () -> chance.animal(options));

        options.put("type", "ocean");

        assertNotNull(chance.animal(options));
        assertNotNull(chance.animal(new HashMap<>()));
    }

    @Test
    public void shouldReturnARandomCharacter() {
        Chance chance = Chance.getInstance();

        Map<String, Object> options = new HashMap<>();
        options.put("casing", "lower");
        assertTrue((Constants.CHARS_LOWER + Constants.NUMBERS + Constants.SYMBOLS).contains(String.valueOf(chance.character(options))));
        options.put("casing", "upper");
        assertTrue((Constants.CHARS_UPPER + Constants.NUMBERS + Constants.SYMBOLS).contains(String.valueOf(chance.character(options))));
        options.put("numeric", "upper");
        assertTrue(Constants.NUMBERS.contains(String.valueOf(chance.character(options))));
    }

    @Test
    public void shouldReturnARandomFloatingPointNumber() {
        Chance chance = Chance.getInstance();

        Map<String, Object> options1 = new HashMap<>();
        options1.put("fixed", 4);
        float result = chance.floating(options1);
        String regex = "[-+]?([0-9]*\\.[0-9]+|[0-9]+)";
        assertRegex(regex, result);
    }

    @Test
    public void shouldReturnAPrimeNumber() {
        Chance chance = Chance.getInstance();

        Map<String, Object> options = new HashMap<>();
        options.put("min", -10);
        assertThrows(RangeError.class, () -> chance.prime(options));

        options.put("min", 10);
        options.put("max", 5);
        assertThrows(RangeError.class, () -> chance.prime(options));
    }

    @Test
    public void shouldReturnRandomLetter() {
        Chance chance = Chance.getInstance();

        Map<String, Object> options = new HashMap<>();
        assertNotNull(chance.letter(options));

    }

    @Test
    public void shouldReturnARandomHexNumberAsString() {
        Chance chance = Chance.getInstance();

        Map<String, Object> options = new HashMap<>();
        options.put("min", 10000);
        options.put("max", 5);
        assertThrows(RangeError.class, () -> chance.hex(options));
        options.put("max", 500000);
        assertNotNull(chance.hex(options));

    }

    @Test
    public void shouldReturnRandomString() {
        Chance chance = Chance.getInstance();

        Map<String, Object> options = new HashMap<>();
        options.put("length", -2);
        assertThrows(RangeError.class, () -> chance.string(options));
        options.put("length", 6);
        assertEquals(6, chance.string(options).length());
    }

    @Test
    public void shouldIsraelId() {
        Chance chance = Chance.getInstance();
        assertEquals(23, chance.israelId().length());
    }

    @Test
    public void shouldReturnMonths() {
        Chance chance = Chance.getInstance();
        List<Map<String, Object>> months = chance.months();
        assertEquals(12, months.size());
    }

    @Test
    public void shouldReturnAge() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
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
        Chance chance = Chance.getInstance();
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
        Map<String, Object> map = new HashMap<>();
        map.put("name", "March");
        map.put("short_name", "Mar");
        map.put("numeric", "03");
        map.put("days", 31.0);
        assertEquals(map, (chance.month(options)));
    }

    @Test
    public void shouldReturnMrz() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        try {
            assertEquals(23, chance.mrz(options).length());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldReturnDate() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertNotNull(chance.date(options));
    }

    @Test
    public void shouldReturnYear() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
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
        Chance chance = Chance.getInstance();
        String monthString = chance.month(options);
        assertNotNull(monthString);

        options.put("raw", true);
        Map<String, Object> monthMap = chance.month(options);
        assertTrue(monthMap.size() == 4);
    }

    @Test
    public void shouldCapitalize() {
        Chance chance = Chance.getInstance();
        assertEquals("Milk way", chance.capitalize("milk way"));
    }

    @Test
    public void shouldCapitalizeEach() {
        Chance chance = Chance.getInstance();
        assertEquals("Milk Way", chance.capitalizeEach("milk way"));
    }

    @Test
    public void shouldReturnString() {
        Chance chance = Chance.getInstance();
        assertNotNull(chance.string(null));
    }

    @Test
    public void shouldReturnWord() {
        Chance chance = Chance.getInstance();
        assertFalse(chance.word(null).isEmpty());
    }

    @Test
    public void shouldReturnFirst() {
        Chance chance = Chance.getInstance();
        assertFalse(chance.first(null).isEmpty());
    }

    @Test
    public void shouldReturnLast() {
        Chance chance = Chance.getInstance();
        assertFalse(chance.last(null).isEmpty());
    }

    @Test
    public void shouldReturnCodicefiscale() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        try{
            assertEquals("Milk Way", chance.cf(options));
        }catch(Exception e){
           System.out.println(e.getMessage());
        }
    }

    @Test
    public void shouldReturnBirthday() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertNotNull(chance.birthday(options));
    }

    @Test
    public void shouldReturnCpf() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertNotNull(chance.cpf(options));
    }

    @Test
    public void shouldReturnNote() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertNotNull(chance.note(options));
    }

    @Test
    public void shouldReturnDice() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        Supplier<Object> func = () -> chance.natural(options);
        Supplier<Integer> d4 = chance.d4;
        List<Integer> list = (List<Integer>) chance.n(func, d4);
        assertTrue(list.size() > 0);

        Supplier<Integer> d6 = chance.d6;
        list = (List<Integer>) chance.n(func, d6);
        assertTrue(list.size() > 0);

        Supplier<Integer> d8 = chance.d8;
        list = (List<Integer>) chance.n(func, d8);
        assertTrue(list.size() > 0);

        Supplier<Integer> d10 = chance.d10;
        list = (List<Integer>) chance.n(func, d10);
        assertTrue(list.size() > 0);

        Supplier<Integer> d20 = chance.d20;
        list = (List<Integer>) chance.n(func, d20);
        assertTrue(list.size() > 0);

        Supplier<Integer> d30 = chance.d30;
        list = (List<Integer>) chance.n(func, d30);
        assertTrue(list.size() > 0);

        Supplier<Integer> d100 = chance.d100;
        list = (List<Integer>) chance.n(func, d100);
        assertTrue(list.size() > 0);
    }


    @Test
    public void shouldReturnMidiNote() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertTrue(NumberUtils.isNumeric(chance.midi_note(options)));
    }

    @Test
    public void shouldReturnChordQuality() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertNotNull(chance.chord_quality(options));
    }

    @Test
    public void shouldReturnChord() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertNotNull(chance.chord(options));
    }

    @Test
    public void shouldReturnTempo() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertTrue(NumberUtils.isNumeric(chance.tempo(options)));
    }

    @Test
    public void shouldReturnRpg() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertThrows(RangeError.class, () -> chance.rpg(options));
        options.put("thrown", "3dx");
        assertThrows(RangeError.class, () -> chance.rpg(options));

        options.put("thrown", "5d6");
        Map<Integer, Integer> rolls = chance.rpg(options);

        assertEquals(rolls.size(), 5);

        options.put("sum", true);
        int sum = chance.rpg(options);
        assertTrue(sum > 0);
    }

    @Test
    public void shouldReturnLuhnCheck() {

        Chance chance = Chance.getInstance();
        assertFalse(chance.luhn_check(7788));
    }

    @Test
    public void shouldReturnFile() {
        Map<String, Object> options = new HashMap<>();
        Chance chance = Chance.getInstance();
        assertNotNull(chance.file(options));
    }

    @Test
    public void shouldReturnFbid() {
        Chance chance = Chance.getInstance();
        assertNotNull(chance.fbid());
    }

    void assertRegex(String regex, Object toTest) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(toTest));
        assertTrue(matcher.find());
    }
}
