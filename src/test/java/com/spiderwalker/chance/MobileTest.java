package com.spiderwalker.chance;


import com.spiderwalker.chance.constant.Constants;
import com.spiderwalker.chance.exception.RangeError;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

public class MobileTest {

    @Test
    public void shouldLoadFile() {
        Map<String, Object> options = new HashMap<>();

        Mobile chance = new Mobile();
        String rs= chance.phone(options);
        assertTrue(rs.isEmpty());

    }
}
