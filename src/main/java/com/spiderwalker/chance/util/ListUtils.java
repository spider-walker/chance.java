package com.spiderwalker.chance.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ListUtils {
    public static List<Object> range(Supplier<Object> func, int size) {
        List<Object> s = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            s.add(func.get());
        }
        return s;
    }

}
