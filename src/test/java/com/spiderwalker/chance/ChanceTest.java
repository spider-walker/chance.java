package com.spiderwalker.chance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

public class ChanceTest {

    @Test
    public void test() {
        Map options = new HashMap<>();
        options.put("likelihood", 150);
        Chance chance = new Chance();
        Supplier<Object> func = () -> {
            return chance.bool(options);
        };

        System.out.println(chance.range(func, 6));
    }
}
