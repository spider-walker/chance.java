package com.spiderwalker.chance.util;

import com.spiderwalker.chance.Chance;
import com.spiderwalker.chance.util.ListUtils;
import com.spiderwalker.chance.util.NumberUtils;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumberUtilsTest {
    @Test
    public void shouldGenerateRandomInt() {
        int upper = 12;
        int lower = 11;
        int result = NumberUtils.random(lower, upper);
        assertTrue(result >= lower && result <= upper);

    }

    @Test
    public void shouldGenerateListOfObjects() {
        Map<String, Object> options = new HashMap<>();
        options.put("likelihood", 30);
        Chance chance = Chance.getInstance();
        Supplier<Object> func = () -> chance.bool(options);
        List<Object> list = ListUtils.range(func, 6);
        assertEquals(6, list.size());
    }
}
