package com.spiderwalker.chance.exception;

public class Error extends Exception {
    public Error(String e) throws Error{
        throw new Error("No Base64 encoder available.");
    }
}
