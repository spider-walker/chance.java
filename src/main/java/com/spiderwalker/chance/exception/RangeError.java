package com.spiderwalker.chance.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RangeError{
    public String message="This feature is not supported on this platform";
    private final Logger LOGGER = Logger.getLogger(RangeError.class.getName());
    public RangeError(String message) {
        this.message = message;
        LOGGER.log(Level.SEVERE,message);
    }
}
