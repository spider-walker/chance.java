package com.spiderwalker.chance.exception;

public class UnsupportedError extends Exception{
    public String message="This feature is not supported on this platform";

    public UnsupportedError(String message) {
        this.message = message;
    }
    
}
