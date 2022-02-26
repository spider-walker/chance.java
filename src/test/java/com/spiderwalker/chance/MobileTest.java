package com.spiderwalker.chance;


import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MobileTest {

    @Test
    public void shouldReturnRandomPhoneNumberWithCountryCode() {
        Map<String, Object> options = new HashMap<>();

        Mobile chance = new Mobile();
        String rs= chance.phone(options);
        assertFalse(rs.isEmpty());

    }
    @Test
    void shouldReturnRandomPhoneNumber(){
        Map<String, Object> options = new HashMap<>();
        Mobile chance = new Mobile();
        String rs= chance.phone(options);
        assertFalse(rs.isEmpty());
    }
}
