package com.spiderwalker.chance;



import com.spiderwalker.chance.util.MapUtils;

import java.util.*;
import java.util.stream.IntStream;

public class Mobile {

    Random gen = new Random();

    public String phone(Map<String, Object> options) {
        Chance chance = Chance.getInstance();
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("formatted", true);
        defaults.put("country", "us");
        defaults.put("mobile", false);
        defaults.put("exampleNumber", false);
//
        options = MapUtils.initOptions(options, defaults);
        if ((boolean) MapUtils.get(options, "formatted")) {
            options.put("parens", false);
        }
//        if()

        return randomTelNo();
    }

    public String phone() {
        return randomTelNo();
    }

    private IntStream octals() {
        return IntStream.generate(() -> gen.ints(0, 8).limit(3).reduce(0, (t, n) -> t * 10 + n));
    }

    public String randomTelNo() {
        Random gen = new Random();
        return String.format("%d-%d%d", randomOctal(), random100To742(), gen.nextInt(10));
    }

    public int randomOctal() {
        return octals()
                .filter(n -> n >= 100 && n < 800)
                .findFirst()
                .getAsInt();
    }

    private static int random100To742() {
        Random gen = new Random();
        return 100 + gen.nextInt(643);
    }
}

