package com.spiderwalker.chance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import com.spiderwalker.chance.exception.RangeError;

public class Chance {
    public String VERSION = "1.1.8";

    public Map<String, ?> initOptions(Map<String, Object> options, Map<String, ?> defaults) {
        if (options == null) {
            options = new HashMap<>();
        }
        if (defaults != null) {
            for (String key : defaults.keySet()) {
                if (options.get(key) == null) {
                    options.put(key, defaults.get(key));
                }
            }
        }

        return options;
    }

    public List<Object> range(Supplier func, int size) {
        List<Object> s = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            s.add(func.get());
        }
        return s;
    }

    public void testRange(boolean test, String errorMessage) {
        if (test) {
             new RangeError(errorMessage);
        }
    }

    public boolean bool(Map options) {
        // likelihood of success (true)
        Map defaults = new HashMap<>();
        defaults.put("likelihood", 50);
        options = initOptions(options, defaults);

        // Note, we could get some minor perf optimizations by checking range
        // prior to initializing defaults, but that makes code a bit messier
        // and the check more complicated as we have to check existence of
        // the object then existence of the key before checking constraints.
        // Since the options initialization should be minor computationally,
        // decision made for code cleanliness intentionally. This is mentioned
        // here as it's the first occurrence, will not be mentioned again.
        testRange(
                (int) options.get("likelihood") < 0 || (int) options.get("likelihood") > 100,
                "Chance: Likelihood accepts values from 0 to 100.");

        return Math.random() * 100 < (int) options.get("likelihood");
    };
}