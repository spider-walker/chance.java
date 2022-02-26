package com.spiderwalker.chance.util;

import java.util.Random;

public class NumberUtils {

    static Random rand = new Random();

    public static int random(int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    public static int random() {
        return rand.nextInt();
    }

    public static long random(long min, long max) {
        return rand.nextInt((int) (max - min)) + min;
    }
    public static float random(float min, float max) {

        return min + rand.nextFloat() * (max - min);
    }
    public static int nextRandomInt(int max){
        return rand.nextInt(max);
    }
    public static boolean isNumeric(Object object) {
        return String.valueOf(object).chars().allMatch(Character::isDigit);
    }

}
