package com.spiderwalker.chance;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.spiderwalker.chance.constant.Constants;
import com.spiderwalker.chance.exception.RangeError;

public class Chance {
    public String VERSION = "1.1.8";
    Map<String, ?> data = null;
    Random rand = new Random();

    public Chance() {
        data = readJson();
    }

    public File readfile() {
        File file = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            String fileName = "data.json";
            file = new File(classLoader.getResource(fileName).getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    public Map<String, ?> readJson() {
        Map<String, ?> jsonMap = null;
        try {
            // create Gson instance
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get(readfile().getAbsolutePath()));

            // convert JSON file to map
            jsonMap = gson.fromJson(reader, Map.class);
            reader.close();

        } catch (JsonParseException | IOException ex) {
            ex.printStackTrace();
        }
        return jsonMap;
    }

    public Map<String, Object> initOptions(Map<String, Object> options, Map<String, Object> defaults) {
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
        for (int i = 0; i < size; i++) {
            s.add(func.get());
        }
        return s;
    }

    public void testRange(boolean test, String errorMessage) {
        if (test) {
            throw new RangeError(errorMessage);
        }
    }

    /**
     * Return a random bool, either true or false
     *
     * @param {Object} [options={ likelihood: 50 }] alter the likelihood of
     *                 receiving a true or false value back.
     * @throws {RangeError} if the likelihood is out of bounds
     * @returns {Bool} either true or false
     */

    public boolean bool(Map<String, Object> options) {
        // likelihood of success (true)
        Map<String, Object> defaults = new HashMap<>();
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
    }

    public String animal(Map<String, Object> options) {
        // returns a random animal
        options = initOptions(options, new HashMap<>());
        Map<String, Object> animals = (Map) data.get("animals");
        List<String> animalType = Stream.of("desert", "forest", "ocean", "zoo", "farm", "pet", "grassland")
                .collect(Collectors.toList());

        if (options.get("type") != null) {
            // if user does not put in a valid animal type, user will get an error
            boolean hasType = animalType.contains(String.valueOf(options.get("type")).toLowerCase());
            testRange(
                    !hasType,
                    "Please pick from desert, ocean, grassland, forest, zoo, pets, farm.");
            List<String> animaList = (ArrayList<String>) animals.get((String) options.get("type"));
            // if user does put in valid animal type, will return a random animal of that
            // type
            return String.valueOf(pickone(animaList));
        }
        // if user does not put in any animal type, will return a random animal
        // regardless
        // var animalTypeArray =
        // ["desert","forest","ocean","zoo","farm","pet","grassland"];
        String randomType = String.valueOf(pickone(animalType));
        List<String> animaList = (ArrayList<String>) animals.get(randomType);

        return String.valueOf(pickone(animaList));
    }

    /**
     * Return a random character.
     *
     * @param {Object} [options={}] can specify a character pool or alpha,
     *                 numeric, symbols and casing (lower or upper)
     * @returns {String} a single random character
     */
    public char character(Map<String, Object> options) {
        options = initOptions(options, new HashMap<>());

        String symbols = "!@#$%^&*()[]";
        String letters;
        String pool = "";

        if (options.get("casing").toString() == "lower") {
            letters = Constants.CHARS_LOWER;
        } else if (options.get("casing") == "upper") {
            letters = Constants.CHARS_UPPER;
        } else {
            letters = Constants.CHARS_LOWER + Constants.CHARS_UPPER;
        }

        if (options.get("pool") == null) {
            if (options.get("alpha") != null) {
                pool += letters;
            }
            if (options.get("numeric") != null) {
                pool += Constants.NUMBERS;
            }
            if (options.get("symbols") != null) {
                pool += symbols;
            }
            if (pool.isEmpty()) {
                pool = letters + Constants.NUMBERS + symbols;
            }
        } else {
            pool = options.get("pool").toString();
        }

        return pool.charAt(random(0, pool.length() - 1));
    }

    // Note, wanted to use "float" or "double" but those are both JS reserved words.

    // Note, fixed means N OR LESS digits after the decimal. This because
    // It could be 14.9000 but in JavaScript, when this is cast as a number,
    // the trailing zeroes are dropped. Left to the consumer if trailing zeroes are
    // needed
    /**
     * Return a random floating point number
     *
     * @param {Object} [options={}] can specify a fixed precision, min, max
     * @returns {Number} a single floating point number
     * @throws {RangeError} Can only specify fixed or precision, not both. Also
     *                      min cannot be greater than max
     */
    public float floating(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        float max = (float) Math.pow(2, 18);
        defaults.put("fixed", 4);
        defaults.put("min", -max);
        defaults.put("max", max);
        options = initOptions(options, defaults);

        int fixed = (int) options.get("fixed");
        float min = (float) options.get("min");
        max = (float) options.get("max");

        float random = min + rand.nextFloat() * (max - min);

        return Float.parseFloat(String.format("%." + fixed + "f", random));
    }

    /**
     * Return a random prime number
     *
     * NOTE the max and min are INCLUDED in the range.
     *
     * @param {Object} [options={}] can specify a min and/or max
     * @returns {Number} a single random prime number
     * @throws {RangeError} min cannot be greater than max nor negative
     */
    public int prime(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", 10000);
        options = initOptions(options, defaults);
        int min = (int) options.get("min");
        int max = (int) options.get("max");
        testRange(min < 0, "Chance: Min cannot be less than zero.");
        testRange(min > max, "Chance: Min cannot be greater than Max.");

        List<Integer> primes = new ArrayList<>((List<Integer>) data.get("primes"));

        Integer lastPrime = (int) Math.round(10.0);

        if (max > lastPrime) {
            for (Integer i = lastPrime + 2; i <= max; ++i) {
                if (isPrime(i)) {
                    primes.add(i);
                }
            }
        }

        List<Integer> targetPrimes = primes.stream().filter(prime -> {
            System.out.println(prime);
            return prime >= min && prime <= max;
        }).collect(Collectors.toList());
        return (int) pickone(targetPrimes);
    };

     public String letter (Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("casing", "lower");
        String pool = "abcdefghijklmnopqrstuvwxyz";
        
        options = initOptions(options, defaults);
        Map<String, Object> defaultsCharacter = new HashMap<>();
        defaults.put("pool", pool);
        String letter = String.valueOf(defaultsCharacter);
        if (options.get("casing") == "upper") {
            letter = letter.toUpperCase();
        }
        return letter;
    }
    /**
     * Determine whether a given number is prime or not.
     */
    public boolean isPrime(double i2) {
        testRange(i2 <= 1, "Only positive numbers above 1 can be prime.");
        double limit = Math.sqrt(i2);

        for (int i = 2; i <= limit; i++) {
            if (i2 % i == 0) {
                return false;
            }
        }

        return true;
    };

    public String letter() {
        return null;
    }

    /**
     * Return a random natural
     *
     * NOTE the max and min are INCLUDED in the range. So:
     * chance.natural({min: 1, max: 3});
     * would return either 1, 2, or 3.
     *
     * @param {Object} [options={}] can specify a min and/or max or a numerals
     *                 count.
     * @returns {Number} a single random integer number
     * @throws {RangeError} min cannot be greater than max
     */

    boolean isNumeric(Object object) {
        return String.valueOf(object).chars().allMatch(Character::isDigit);
    }

    public int natural(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", Constants.MAX_INT);
        options = initOptions(options, defaults);
        if (options.get("numerals") != null && options.get("numerals") == "number") {
            testRange((int) options.get("numerals") < 1, "Chance: Numerals cannot be less than one.");
            options.put("min", Math.pow(10, (int) options.get("numerals") - 1));
            options.put("max", Math.pow(10, (int) options.get("numerals")) - 1);
        }
        testRange((int) options.get("min") < 0, "Chance: Min cannot be less than zero.");

        if (options.get("exclude") != null) {
            testRange(!options.get("exclude").getClass().isArray(), "Chance: exclude must be an array.");

            Object[] exclude = (Object[]) options.get("exclude");

            for (Object exclusion : exclude) {
                testRange(!isNumeric(exclusion), "Chance: exclude must be numbers.");
            }
            Map<String, Object> naturalDefaults = new HashMap<>();
            naturalDefaults.put("max", (int) options.get("max") - (int) options.get("min") - exclude.length);

            int random = (int) options.get("min") + natural(naturalDefaults);

            Integer[] sortedExclusions = Arrays.asList(exclude).toArray(new Integer[0]);

            for (int sortedExclusion : sortedExclusions) {
                if (random < sortedExclusion) {
                    break;
                }
                random++;
            }
            return random;
        }
        return integer(options);
    };

    // Given an list, returns a single random element
    public Object pickone(List<?> list) {
        testRange(list.size() == 0, "Chance: Cannot pickone() from an empty array");
        Map<String, Object> options = new HashMap<>();
        options.put("max", list.size() - 1);
        return list.get(natural(options));
    }

    public int random(int min, int max) {
        return rand.nextInt() * (max - min) + min;
    }

    /**
     * Return a random integer
     *
     * NOTE the max and min are INCLUDED in the range. So:
     * chance.integer({min: 1, max: 3});
     * would return either 1, 2, or 3.
     *
     * @param {Object} [options={}] can specify a min and/or max
     * @returns {Number} a single random integer number
     * @throws {RangeError} min cannot be greater than max
     */
    public int integer(Map<String, Object> options) {
        // 2147483647 (2^53) is the max integer number in Java
        // See: http://vq.io/132sa2j
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", Constants.MIN_INT);
        defaults.put("max", Constants.MAX_INT);
        options = initOptions(options, defaults);
        testRange((int) options.get("min") > (int) options.get("max"), "Chance: Min cannot be greater than Max.");

        return random((int) options.get("min"), (int) options.get("max"));
    }

    /**
     * Gives an array of n random terms
     *
     * @param {Function} fn the function that generates something random
     * @param {Number}   n number of terms to generate
     * @returns {Array} an array of length `n` with items generated by `fn`
     *
     *          There can be more parameters after these. All additional parameters
     *          are provided to the given function
     */
    public List<?> n(Supplier fn, int size) {
        testRange(fn == null, "Chance: The first argument must be a function.");
        // Providing a negative count should result in a noop.

        if (size < 0) {
            size = 1;
        }
        List<Object> s = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            s.add(fn.get());
        }

        return s;
    }

    // H/T to SO for this one: http://vq.io/OtUrZ5
    public String pad(int number, int width, String pad) {
        // Convert number to a string
        String word = number + "";
        if (word.length() >= width) {
            return word;
        }
        int len = width - word.length();

        StringBuilder sb = new StringBuilder();
        while (sb.length() < len) {
            sb.append(pad);
        }
        sb.append(word);

        return sb.toString();
    }

    // Given an array, returns a random set with 'count' elements
    public Object[] pickset(Object[] arr, int count) {
        if (count == 0) {
            return new Object[0];
        }

        if (arr.length == 0) {
            throw new RangeError("Chance: Cannot pickset() from an empty array");
        }
        if (count < 0) {
            throw new RangeError("Chance: Count must be a positive number");
        }
        if (count == 1) {
            Object[] items = { pickone(Arrays.asList(arr)) };
            return items;
        }
        if (count == arr.length || count > arr.length) {
            return arr;
        }
        List<Object> picked = new ArrayList<>();

        Random rand = new Random();
        List<Object> givenList = new LinkedList<>(Arrays.asList(arr));
        for (int i = 0; i < count; i++) {
            int randomIndex = rand.nextInt(givenList.size());
            Object randomElement = givenList.get(randomIndex);

            picked.add(randomElement);

            givenList.remove(randomIndex);
        }

        return picked.toArray();

    }

    public Object[] shuffle(Object[] arr) {
        List<Object> givenList = new LinkedList<>(Arrays.asList(arr));
        Collections.shuffle(givenList);
        return givenList.toArray();
    };

    // Returns a single item from an array with relative weighting of odds
    public Object weighted(Object[] arr, int[] weights, boolean trim) {
        if (arr.length != weights.length) {
            throw new RangeError("Chance: Length of array and weights must match");
        }

        // scan weights array and sum valid entries
        int sum = 0;

        for (var weightIndex = 0; weightIndex < weights.length; ++weightIndex) {
            int val = 0;
            if (!isNumeric(weights[weightIndex])) {
                throw new RangeError("Chance: All weights must be numbers");
            }
            val = weights[weightIndex];
            sum += val;
        }

        if (sum == 0) {
            throw new RangeError("Chance: No valid entries in array weights");
        }

        int selected = random(0, Integer.MAX_VALUE) * sum;

        // find array entry corresponding to selected value
        int total = 0;
        int lastGoodIdx = -1;
        int chosenIdx = -1;
        for (int weightIndex = 0; weightIndex < weights.length; ++weightIndex) {
            int val = weights[weightIndex];
            total += val;
            if (val > 0) {
                if (selected <= total) {
                    chosenIdx = weightIndex;
                    break;
                }
                lastGoodIdx = weightIndex;
            }

            // handle any possible rounding error comparison to ensure something is picked
            if (weightIndex == (weights.length - 1)) {
                chosenIdx = lastGoodIdx;
            }
        }

        int chosen = (int) arr[chosenIdx];
        return chosen;
    }

    // ID number for Brazil companies
    public String cnpj() {
        Map<String, Object> options = new HashMap<>();
        options.put("max", 9);
        Supplier<Object> naturalFn = () -> {
            return natural(options);
        };

        Integer[] numbers = ((List<Integer>) n(naturalFn, 8)).toArray(Integer[]::new);

        int d1 = 2 + numbers[7] * 6 + numbers[6] * 7 + numbers[5] * 8 + numbers[4] * 9 + numbers[3] * 2 + numbers[2] * 3
                + numbers[1] * 4 + numbers[0] * 5;
        d1 = 11 - (d1 % 11);
        if (d1 >= 10) {
            d1 = 0;
        }
        int d2 = d1 * 2 + 3 + numbers[7] * 7 + numbers[6] * 8 + numbers[5] * 9 + numbers[4] * 2 + numbers[3] * 3
                + numbers[2] * 4 + numbers[1] * 5 + numbers[0] * 6;
        d2 = 11 - (d2 % 11);
        if (d2 >= 10) {
            d2 = 0;
        }
        return "" + numbers[0] + numbers[1] + '.' + numbers[2] + numbers[3] + numbers[4] + '.' + numbers[5] + numbers[6]
                + numbers[7] + "/0001-" + d1 + d2;
    }

    public String emotion() {
        return (String) pickone((List<?>) data.get("emotions"));
    }

}