package com.spiderwalker.chance.util;

import com.spiderwalker.chance.exception.RangeError;

public class ErrorUtils {
    public static void testRange(boolean test, String errorMessage) {
        if (test) {
            throw new RangeError(errorMessage);
        }
    }
}
