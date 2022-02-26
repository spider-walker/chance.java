package com.spiderwalker.chance;

import com.spiderwalker.chance.constant.Constants;
import com.spiderwalker.chance.exception.RangeError;
import com.spiderwalker.chance.util.*;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Chance {
    public String VERSION = "1.1.8";
    Map<String, Object> data;
    public Map<String, Supplier<Object>> mixin= new HashMap<>();

    private static Chance instance = null;

    public static Chance getInstance() {
        if (instance == null) {
            instance = new Chance();
        }
        return instance;
    }

    public Chance() {
        data = FileUtils.readJson();
    }


    /**
     * Return a random bool, either true or false
     * <p>
     * param options={ likelihood: 50 } alter the likelihood of
     * receiving a true or false value back.
     * throws {RangeError} if the likelihood is out of bounds
     * returns boolean either true or false
     */
    public boolean bool(Map<String, Object> options) {
        // likelihood of success (true)
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("likelihood", 50);
        options = MapUtils.initOptions(options, defaults);

        // Note, we could get some minor perf optimizations by checking range
        // prior to initializing defaults, but that makes code a bit messier
        // and the check more complicated as we have to check existence of
        // the object then existence of the key before checking constraints.
        // Since the options initialization should be minor computationally,
        // decision made for code cleanliness intentionally. This is mentioned
        // here as it's the first occurrence, will not be mentioned again.
        ErrorUtils.testRange(
                (int) MapUtils.get(options, "likelihood") < 0 || (int) MapUtils.get(options, "likelihood") > 100,
                "Chance: Likelihood accepts values from 0 to 100.");

        return NumberUtils.random() * 100 < (int) MapUtils.get(options, "likelihood");
    }

    public <T> T animals() {
        return (T) data.get("animals");
    }

    public <T> T animal(Map<String, Object> options) {
        // returns a random animal
        options = MapUtils.initOptions(options, new HashMap<>());
        List<String> animalType = Stream.of("desert", "forest", "ocean", "zoo", "farm", "pet", "grassland")
                .collect(Collectors.toList());
        String type = MapUtils.get(options, "type");

        if (type != null) {
            // if user does not put in a valid animal type, user will get an error
            boolean hasType = animalType.contains(StringUtils.toLowerCase(type));
            ErrorUtils.testRange(
                    !hasType,
                    "Please pick from desert, ocean, grassland, forest, zoo, pets, farm.");
            List<String> animaList = ((Map<String, List<String>>) animals()).get((String) MapUtils.get(options, "type"));
            // if user does put in valid animal type, will return a random animal of that
            // type
            return pickone(animaList);
        }
        // if user does not put in any animal type, will return a random animal
        // regardless
        // var animalTypeArray =
        // ["desert","forest","ocean","zoo","farm","pet","grassland"];
        String randomType = pickone(animalType);
        List<String> animaList = ((Map<String, List<String>>) animals()).get(randomType);

        return pickone(animaList);
    }

    /**
     * Return a random character.
     * <p>
     * param {Object} [options={}] can specify a character pool or alpha,
     * numeric, symbols and casing (lower or upper)
     * returns {String} a single random character
     */
    public char character(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("casing", "");
        options = MapUtils.initOptions(options, defaults);

        String letters;
        String pool = "";

        if (Objects.equals(MapUtils.get(options, "casing").toString(), "lower")) {
            letters = Constants.CHARS_LOWER;
        } else if (MapUtils.get(options, "casing") == "upper") {
            letters = Constants.CHARS_UPPER;
        } else {
            letters = Constants.CHARS_LOWER + Constants.CHARS_UPPER;
        }

        if (MapUtils.get(options, "pool") == null) {
            if (MapUtils.get(options, "alpha") != null) {
                pool += letters;
            }
            if (MapUtils.get(options, "numeric") != null) {
                pool += Constants.NUMBERS;
            }
            if (MapUtils.get(options, "symbols") != null) {
                pool += Constants.SYMBOLS;
            }
            if (pool.isEmpty()) {
                pool = letters + Constants.NUMBERS + Constants.SYMBOLS;
            }
        } else {
            pool = MapUtils.get(options, "pool").toString();
        }

        return pool.charAt(NumberUtils.random(0, pool.length() - 1));
    }

    public char character(String pool) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("pool", pool);

        return character(defaults);
    }

    public char character(boolean alpha, String casing) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("alpha", alpha);
        defaults.put("casing", casing);

        return character(defaults);
    }

    /**
     * Return a random floating point number
     * <p>
     * param {Object} [options={}] can specify a fixed precision, min, max
     * throws {RangeError} Can only specify fixed or precision, not both. Also
     * min cannot be greater than max
     * returns {Number} a single floating point number
     */

    public float floating(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        float max = (float) Math.pow(2, 18);
        defaults.put("fixed", 4);
        defaults.put("min", -max);
        defaults.put("max", max);
        options = MapUtils.initOptions(options, defaults);

        int fixed = MapUtils.get(options, "fixed");
        float min = MapUtils.get(options, "min");
        max = MapUtils.get(options, "max");

        float random = NumberUtils.random(min, max);

        return Float.parseFloat(String.format("%." + fixed + "f", random));
    }

    public float floating(int min, float max, int fixed) {
        float random = NumberUtils.random(min, max);

        return Float.parseFloat(String.format("%." + fixed + "f", random));
    }

    /**
     * Return a random integer
     * <p>
     * NOTE the max and min are INCLUDED in the range. So:
     * chance.integer(1, 3});
     * would return either 1, 2, or 3.
     * <p>
     * param {Object} [options={}] can specify a min and/or max
     * throws {RangeError} min cannot be greater than max
     * returns {Number} a single random integer number
     */
    public int integer(Map<String, Object> options) {
        // 2147483647 (2^53) is the max integer number in Java
        // See: http://vq.io/132sa2j
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", Constants.MIN_INT);
        defaults.put("max", Constants.MAX_INT);
        options = MapUtils.initOptions(options, defaults);
        ErrorUtils.testRange((int) MapUtils.get(options, "min") > (int) MapUtils.get(options, "max"), "Chance: Min cannot be greater than Max.");

        return NumberUtils.random(MapUtils.get(options, "min"), MapUtils.get(options, "max"));
    }

    public int integer(int min, int max) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", min);
        defaults.put("max", max);
        return integer(defaults);
    }

    /**
     * Return a random natural
     * <p>
     * NOTE the max and min are INCLUDED in the range. So:
     * chance.natural(1, 3});
     * would return either 1, 2, or 3.
     * <p>
     * param options can specify a min and/or max or a numerals
     * count.
     * throws RangeError min cannot be greater than max
     * returns Number a single random integer number
     */
    public int natural(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", Constants.MAX_INT);
        options = MapUtils.initOptions(options, defaults);
        if (MapUtils.get(options, "numerals") != null && MapUtils.get(options, "numerals") == "number") {
            ErrorUtils.testRange((int) MapUtils.get(options, "numerals") < 1, "Chance: Numerals cannot be less than one.");
            options.put("min", Math.pow(10, (int) MapUtils.get(options, "numerals") - 1));
            options.put("max", Math.pow(10, MapUtils.get(options, "numerals")) - 1);
        }
        ErrorUtils.testRange((int) MapUtils.get(options, "min") < 0, "Chance: Min cannot be less than zero.");

        if (MapUtils.get(options, "exclude") != null) {
            ErrorUtils.testRange(!MapUtils.get(options, "exclude").getClass().isArray(), "Chance: exclude must be an array.");

            Object[] exclude = MapUtils.get(options, "exclude");

            for (Object exclusion : exclude) {
                ErrorUtils.testRange(NumberUtils.isNumeric(exclusion), "Chance: exclude must be numbers.");
            }
            Map<String, Object> naturalDefaults = new HashMap<>();
            naturalDefaults.put("max", (int) MapUtils.get(options, "max") - (int) MapUtils.get(options, "min") - exclude.length);

            int random = (int) MapUtils.get(options, "min") + natural(naturalDefaults);

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
    }

    public int natural(int max) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("max", max);
        return natural(defaults);
    }

    public int natural(int min, int max) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", min);
        defaults.put("max", max);
        return natural(defaults);
    }

    /**
     * Determine whether a given number is prime or not.
     */
    public boolean isPrime(double i2) {
        ErrorUtils.testRange(i2 <= 1, "Only positive numbers above 1 can be prime.");
        double limit = Math.sqrt(i2);

        for (int i = 2; i <= limit; i++) {
            if (i2 % i == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * Return a random prime number
     * <p>
     * NOTE the max and min are INCLUDED in the range.
     * <p>
     * param {Object} [options={}] can specify a min and/or max
     * throws {RangeError} min cannot be greater than max nor negative
     * returns {Number} a single random prime number
     */
    public int prime(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", 10000);
        options = MapUtils.initOptions(options, defaults);
        int min = MapUtils.get(options, "min");
        int max = MapUtils.get(options, "max");
        ErrorUtils.testRange(min < 0, "Chance: Min cannot be less than zero.");
        ErrorUtils.testRange(min > max, "Chance: Min cannot be greater than Max.");

        List<Float> primes = new ArrayList<>((List<Float>) data.get("primes"));

        int lastPrime = (int) Math.round(10.0);

        if (max > lastPrime) {
            for (int i = lastPrime + 2; i <= max; ++i) {
                if (isPrime(i)) {
                    primes.add((float) i);
                }
            }
        }

        return Math.round(Float.parseFloat(String.valueOf(pickone(primes))));
    }

    /**
     * Return a random hex number as string
     * <p>
     * NOTE the max and min are INCLUDED in the range. So:
     * chance.hex('9', 'B'});
     * would return either '9', 'A' or 'B'.
     * <p>
     * param {Object} [options={}] can specify a min and/or max and/or casing
     * throws {RangeError} min cannot be greater than max
     * returns {String} a single random string hex number
     */
    public String hex(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", Integer.MAX_VALUE);
        defaults.put("casing", "lower");
        options = MapUtils.initOptions(options, defaults);
        ErrorUtils.testRange((int) MapUtils.get(options, "min") < 0, "Chance: Min cannot be less than zero.");
        int integer = natural(options);
        if (MapUtils.get(options, "casing") == "upper") {
            return Integer.toUnsignedString(integer, 16).toUpperCase();
        }
        return Integer.toUnsignedString(integer, 16);
    }

    public String letter(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("casing", "lower");
        options = MapUtils.initOptions(options, defaults);

        defaults.put("pool", "abcdefghijklmnopqrstuvwxyz");
        String letter = String.valueOf(character(options));
        if (MapUtils.get(options, "casing") == "upper") {
            letter = letter.toUpperCase();
        }
        return letter;
    }

    /**
     * Return a random string
     * <p>
     * param {Object} [options={}] can specify a length or min and max
     * throws {RangeError} length cannot be less than zero
     * returns {String} a string of random length
     */
    public String string(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 5);
        defaults.put("max", 20);
        options = MapUtils.initOptions(options, defaults);

        if (MapUtils.get(options, "length") == null) {
            options.put("length", NumberUtils.random(MapUtils.get(options, "min"), MapUtils.get(options, "max")));
        }

        ErrorUtils.testRange((int) MapUtils.get(options, "length") < 0, "Chance: Length cannot be less than zero.");

        Supplier<Object> func = () -> character(new HashMap<>());

        int length = MapUtils.get(options, "length");
        List text = n(func, length);
        StringBuilder sb = new StringBuilder();
        for (Object s : text) {
            sb.append(s);
        }

        return sb.toString();
    }

    public String string(String pool, int len) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("pool", pool);
        defaults.put("length", len);

        return string(defaults);
    }

    /**
     * Return a random buffer
     * <p>
     * param {Object} [options={}] can specify a length
     * throws {RangeError} length cannot be less than zero
     * returns {Buffer} a buffer of random length
     */
    public Object buffer(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 5);
        defaults.put("max", 20);
        defaults.put("length", NumberUtils.random(5, 20));

        options = MapUtils.initOptions(options, defaults);
        int length = MapUtils.get(options, "length");
        ErrorUtils.testRange(length < 0, "Chance: Length cannot be less than zero.");
        Map<String, Object> finalOptions = options;
        Supplier characterFn = () -> character(finalOptions);

        return n(characterFn, length);
    }
    // -- End Basics --

    // -- Helpers --
    public String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public String capitalizeEach(String words) {
        return Stream.of(words.trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }

    public void mixins(Map<String, Supplier> fns) {
        for (String key : fns.keySet()) {
//            if (fns.get(key) == null) {
                mixin.put(key, (Supplier<Object>) fns.get(key));
//            }
        }
    }

    /**
     * Given a function that generates something random and a number of items to generate,
     * return an array of items where none repeat.
     * <p>
     * param {Function} fn the function that generates something random
     * param {Number}   num number of terms to generate
     * param {Object}   options any options to pass on to the generator function
     * returns {Array} an array of length `num` with every item generated by `fn` and unique
     * <p>
     * There can be more parameters after these. All additional parameters are provided to the given function
     */
    // ToDo
    public List<?> unique(Supplier fn, int num, Map<String, Object> options) {
        ErrorUtils.testRange(fn == null,
                "Chance: The first argument must be a function."
        );

//        Supplier comparator = (arr, val)-> {
//            List<Object> list = ()arr;
//            return arr.contains(val);
//        };
//
//        if (options!=null && get(options,"comparator")!=null) {
//            comparator = (Supplier) get(options,"comparator");
//        }
//
        //        int count = 0;
//                result,
//                        int MAX_DUPLICATES = num * 50,
//                params = slice.call(arguments, 2);
//
//        while (arr.length < num) {
//            var clonedParams = JSON.parse(JSON.stringify(params));
//            Object result = fn.get(this, clonedParams);
//            if (!comparator(arr, result)) {
//                arr.add(result);
//                // reset count when unique found
//                count = 0;
//            }
//
//            ErrorUtils.testRange(                ++count > MAX_DUPLICATES,
//                    "Chance: num is likely too large for sample set"
//            );
//        }
        return new ArrayList<>();
    }

    /**
     * Gives an array of n random terms
     * <p>
     * param {Function} fn the function that generates something random
     * param {Number}   n number of terms to generate
     * returns {Array} an array of length `n` with items generated by `fn`
     * <p>
     * There can be more parameters after these. All additional parameters
     * are provided to the given function
     */
    public List<?> n(Supplier fn, Supplier d4) {
        int size = (int) d4.get();
        return n(fn, size);
    }

    public <T> T n(Supplier fn, int size) {
        ErrorUtils.testRange(fn == null, "Chance: The first argument must be a function.");
        // Providing a negative count should result in a noop.

        if (size < 0) {
            size = 1;
        }
        List<T> s = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            s.add((T) fn.get());
        }

        return (T) s;
    }

    // H/T to SO for this one: http://vq.io/OtUrZ5
    public String pad(Object number, int width, String pad) {
        // Convert number to a string
        String word = number + "";
        if (word.length() >= width) {
            return word;
        }
        if (pad == null || pad.isBlank()) {
            pad = "0";
        }
        int len = width - word.length();

        StringBuilder sb = new StringBuilder();
        while (sb.length() < len) {
            sb.append(pad);
        }
        sb.append(word);

        return sb.toString();
    }

    // Given an list, returns a single random element
    public <T> T pickone(List<?> list) {
        ErrorUtils.testRange(list.size() == 0, "Chance: Cannot pickone() from an empty array");
        Map<String, Object> options = new HashMap<>();
        options.put("max", list.size() - 1);
        return (T) list.get(natural(options));
    }

    // Given an array, returns a random set with 'count' elements
    public Object[] pickset(Object[] arr, int count) {
        if (count == 0) {
            return new Object[0];
        }

        ErrorUtils.testRange(arr.length == 0, "Chance: Cannot pickset() from an empty array");
        ErrorUtils.testRange(count < 0, "Chance: Count must be a positive number");
        if (count == 1) {
            return new Object[]{pickone(Arrays.asList(arr))};
        }
        if (count == arr.length || count > arr.length) {
            return arr;
        }
        List<Object> picked = new ArrayList<>();

        List<Object> givenList = new LinkedList<>(Arrays.asList(arr));
        for (int i = 0; i < count; i++) {
            int randomIndex = NumberUtils.nextRandomInt(givenList.size());
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
    }

    // Returns a single item from an array with relative weighting of odds
    public int weighted(Object[] arr, int[] weights, boolean trim) {
        if (arr.length != weights.length) {
            throw new RangeError("Chance: Length of array and weights must match");
        }

        // scan weights array and sum valid entries
        int sum = 0;

        for (int weight : weights) {
            int val;
            if (!NumberUtils.isNumeric(weight)) {
                throw new RangeError("Chance: All weights must be numbers");
            }
            val = weight;
            sum += val;
        }

        if (sum == 0) {
            throw new RangeError("Chance: No valid entries in array weights");
        }

        int selected = NumberUtils.random(0, Integer.MAX_VALUE) * sum;

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

        return (int) arr[chosenIdx];
    }
    // -- End Helpers --

    // -- Text --
    public String paragraph(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("sentences", NumberUtils.random(3, 7));
        defaults.put("linebreak", true);
        options = MapUtils.initOptions(options, defaults);

        int count = MapUtils.get(options, "sentences");
        Map<String, Object> finalOptions = options;
        Supplier<String> sentenceFn = () -> sentence(finalOptions);

        List<String> sentence_array = n(sentenceFn, count);
        String separator = (boolean) MapUtils.get(options, "linebreak") ? "\n" : " ";

        return String.join(separator, sentence_array);
    }

    // Could get smarter about this than generating random words and
    // chaining them together. Such as: http://vq.io/1a5ceOh
    public String sentence(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", NumberUtils.random(3, 7));
        defaults.put("words", NumberUtils.random(12, 18));
        defaults.put("punctuation", NumberUtils.random(12, 18));
        options = MapUtils.initOptions(options, defaults);

        int count = MapUtils.get(options, "words");
        boolean punctuation = MapUtils.get(options, "punctuation");
        Map<String, Object> finalOptions = options;
        Supplier<String> wordFn = () -> word(finalOptions);
        String text;
        List<String> word_array = n(wordFn, count);

        text = String.join(" ", word_array);
        String punctuate = "";

        // Capitalize first letter of sentence
        text = capitalize(text);
        Pattern pattern = Pattern.compile("^[.?;!:]$");

        // Search above pattern in "softwareTestingHelp.com"
        Matcher m = pattern.matcher("softwareTestingHelp.com");

        // Make sure punctuation has a usable value
        if (punctuation && m.find()) {
            punctuate = ".";
        }

        // Add punctuation mark
        if (punctuation) {
            text += punctuate;
        }

        return text;
    }

    public String syllable(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("syllables", NumberUtils.random(1, 3));
        defaults.put("length", NumberUtils.random(2, 3));
        defaults.put("capitalize", false);
        options = MapUtils.initOptions(options, defaults);

        int length = MapUtils.get(options, "length");
        String consonants = "bcdfghjklmnprstvwz"; // consonants except hard to speak ones
        // vowels
        StringBuilder text = new StringBuilder();
        String chr = null;

        // I'm sure there's a more elegant way to do this, but this works
        // decently well.
        Map<String, Object> characterOptions = new HashMap<>();
        for (var i = 0; i < length; i++) {
            if (i == 0) {
                // First character can be anything
                characterOptions.put("pool", "all");


            } else if (!consonants.contains(chr)) {
                // Last character was a vowel, now we want a consonant
                characterOptions.put("pool", "consonants");
            } else {
                // Last character was a consonant, now we want a vowel
                characterOptions.put("pool", "vowels");

            }
            chr = String.valueOf(character(characterOptions));

            text.append(chr);
        }

        if ((boolean) MapUtils.get(options, "capitalize")) {
            text = new StringBuilder(capitalize(text.toString()));
        }

        return text.toString();
    }

    public String word(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("syllables", NumberUtils.random(1, 3));
        defaults.put("capitalize", false);
        options = MapUtils.initOptions(options, defaults);

        ErrorUtils.testRange(
                MapUtils.get(options, "syllables") != null && MapUtils.get(options, "length") != null,
                "Chance: Cannot specify both syllables AND length."
        );

        int syllables = MapUtils.get(options, "syllables");
        int length = MapUtils.get(options, "length") == null ? syllables + 1 : (int) MapUtils.get(options, "length");
        StringBuilder text = new StringBuilder();

        if (length > 0) {
            // Either bound word by length
            do {
                text.append(syllable(options));
            } while (text.length() < length);
            text = new StringBuilder(text.substring(0, length));
        } else {
            // Or by number of syllables
            for (var i = 0; i < syllables; i++) {
                text.append(syllable(options));
            }
        }

        if ((boolean) MapUtils.get(options, "capitalize")) {
            text = Optional.ofNullable(capitalize(text.toString())).map(StringBuilder::new).orElse(null);
        }

        return text == null ? null : text.toString();
    }

    public String word() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("syllables", NumberUtils.random(1, 3));
        defaults.put("capitalize", false);
        return word(defaults);
    }
    // -- End Text --

    // -- Person --
    public int age(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("type", "");
        options = MapUtils.initOptions(options, defaults);
        int ageRange;
        String type = MapUtils.get(options, "type");
        switch (type) {
            case "child":
                ageRange = NumberUtils.random(0, 12);
                break;
            case "teen":
                ageRange = NumberUtils.random(13, 19);
                break;
            case "senior":
                ageRange = NumberUtils.random(65, 100);
                break;
            case "all":
                ageRange = NumberUtils.random(0, 100);
                break;
            default:
                ageRange = NumberUtils.random(18, 65);
                break;
        }

        return (ageRange);
    }

    public LocalDateTime birthday(Map<String, Object> options) {
        var age = age(options);
        LocalDateTime currentYear = LocalDateTime.now();
        String type = MapUtils.get(options, "type");
        Map<String, Object> defaults = new HashMap<>();
        if (type != null) {
            defaults.put("minDate", currentYear.minusYears((age + 1)));
            defaults.put("maxDate", currentYear.minusYears((age)));


            options = MapUtils.initOptions(options, defaults);
        } else {
            defaults.put("year", currentYear.getYear() - age);
            options = MapUtils.initOptions(options, defaults);
        }

        return date(options);
    }

    // CPF; ID to identify taxpayers in Brazil
    public String cpf(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("formatted", true);
        options = MapUtils.initOptions(options, defaults);
        options.put("max", 9);
        Map<String, Object> finalOptions = options;
        Supplier fn = () -> natural(finalOptions);

        List<Integer> n = n(fn, 9);
        var d1 = n.get(8) * 2 + n.get(7) * 3 + n.get(6) * 4 + n.get(5) * 5 + n.get(4) * 6 +
                n.get(3) * 7 + n.get(2) * 8 + n.get(1) * 9 + n.get(0) * 10;
        d1 = 11 - (d1 % 11);
        if (d1 >= 10) {
            d1 = 0;
        }
        int d2 = d1 * 2 + n.get(8) * 3 + n.get(7) * 4 + n.get(6) * 5 + n.get(5) * 6 + n.get(4) * 7
                + n.get(3) * 8 + n.get(2) * 9 + n.get(1) * 10 + n.get(0) * 11;
        d2 = 11 - (d2 % 11);
        if (d2 >= 10) {
            d2 = 0;
        }
        String cpf = "" + n.get(0) + n.get(1) + n.get(2) + "." + n.get(3)
                + n.get(4) + n.get(5) + '.' + n.get(6) + n.get(7) + n.get(8) + "-" + d1 + d2;
        return (boolean) MapUtils.get(options, "formatted") ? cpf : cpf.replace("\\D", "");
    }

    // ID number for Brazil companies
    public String cnpj() {
        Map<String, Object> options = new HashMap<>();
        options.put("max", 9);
        Supplier<Integer> naturalFn = () -> natural(options);
        List<Integer> list = n(naturalFn, 8);
        Integer[] numbers = list.toArray(Integer[]::new);

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

    public String first(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("gender", gender(null));
        defaults.put("nationality", "en");
        options = MapUtils.initOptions(options, defaults);
        Map<String, Object> firstNames = (Map<String, Object>) data.get("firstNames");
        Map<String, Object> firstNamesGender =
                (Map<String, Object>) firstNames.get(((String) MapUtils.get(options, "gender")).toLowerCase());
        List<String> firstNamesNationality = (List<String>) firstNamesGender.get(MapUtils.get(options, "nationality"));
        return pickone(firstNamesNationality);
    }

    public List<Map<String, String>> professions() {
        return (List<Map<String, String>>) data.get("profession");
    }

    public String profession(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("rank", "false");
        options = MapUtils.initOptions(options, defaults);
        String rank = "";
        if ((boolean) MapUtils.get(options, "rank")) {
            rank = pickone(Arrays.asList("Apprentice", "Junior", "Senior", "Lead")) + " ";
        }
        return rank + pickone(professions());
    }

    public List<Map<String, String>> companys() {
        return (List<Map<String, String>>) data.get("company");
    }

    public String company() {
        return pickone(companys());
    }

    public String gender(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("extraGenders", new ArrayList<>());
        options = MapUtils.initOptions(options, defaults);
        List<String> genders = Arrays.asList("Male", "Female");
        genders.addAll(MapUtils.get(options, "extraGenders"));
        return pickone(genders);
    }

    public String last(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("nationality", "*");
        options = MapUtils.initOptions(options, defaults);
        Map<String, Object> lastNames = (Map<String, Object>) data.get("lastNames");
        if (MapUtils.get(options, "nationality") == "*") {
            List<String> allLastNames = new ArrayList<>();
            lastNames.forEach((key, k) -> allLastNames.addAll((Collection<? extends String>) lastNames.get(key)));
            return pickone(allLastNames);
        } else {

            List<String> lastNamesNationality = (List<String>) lastNames
                    .get(MapUtils.get(options, "nationality"));
            return pickone(lastNamesNationality);
        }

    }

    public String israelId() {
        Map<String, Object> options = new HashMap<>();
        options.put("pool", "0123456789");
        options.put("length", 8);
        String x = string(options);
        StringBuilder y = new StringBuilder("0");
        for (int i = 0; i < x.length(); i++) {
            String thisDigit = String.valueOf(x.charAt(i) * (i / 2 == Integer.parseInt(String.valueOf(i / 2)) ? 1 : 2));
            thisDigit = pad(thisDigit, 2, "0");
            thisDigit = thisDigit.charAt(0) + "" + thisDigit.charAt(0);
            y.append(thisDigit);
        }
        String z = String.valueOf(10 - Long.parseLong(y.substring(0, y.length() - 1)));
        x = x + z.substring(0, z.length() - 1);
        return x;
    }

    public String mrz(Map<String, Object> options) {
        Function<String, Integer> checkDigit = input -> {

            String[] alpha = "<ABCDEFGHIJKLMNOPQRSTUVWXYXZ".split("");
            int[] multipliers = {7, 3, 1};
            int runningTotal = 0;
            int character = 0;
            int idx = 0;
            for (String in : input.split("")) {
                int pos = Arrays.binarySearch(alpha, in);

                if (pos != -1) {
                    character = pos == 0 ? 0 : pos + 9;
                } else if(NumberUtils.isNumeric(in)){
                    character = Character.getNumericValue(Integer.parseInt(in));
                }
                character *= multipliers[idx % multipliers.length];
                runningTotal += character;
                idx++;
            }

            return runningTotal % 10;
        };


        Function<Map<String, Object>, String> generate = (opts) -> {

            Function<Integer, String> pad = (length) -> String.join("", String.valueOf(length + 1), "<");

            String number = String.join("", "P<",
                    opts.get("issuer") + "",
                    opts.get("last").toString().toUpperCase(),
                    "<<",
                    opts.get("first").toString().toUpperCase(),
                    pad.apply(39 - (opts.get("last").toString().length() + opts.get("first").toString().length() + 2)),
                    opts.get("passportNumber") + "",
                    String.valueOf(checkDigit.apply(opts.get("passportNumber").toString())),
                    opts.get("nationality") + "",
                    opts.get("dob") + "",
                    String.valueOf(checkDigit.apply(opts.get("dob").toString())),
                    opts.get("gender") + "",
                    opts.get("expiry") + "",
                    String.valueOf(checkDigit.apply(opts.get("expiry").toString())),
                    pad.apply(14),
                    String.valueOf(checkDigit.apply(pad.apply(14))));
            return String.valueOf(Integer.valueOf(number +
                    (checkDigit.apply(number.substring(44, 10) +
                            number.substring(57, 7) +
                            number.substring(65, 7)))));
        };
        Supplier dob = () -> {
            Map<String, Object> birthdayOptions = new HashMap<>();
            birthdayOptions.put("type", "adult");
            LocalDateTime date = birthday(birthdayOptions);
            return String.join("", String.valueOf(date.getYear()).substring(0, 2),
                    pad(String.valueOf(date.getMonthValue() + 1), 2, "0"),
                    pad(date.toString(), 2, "0"));
        };

        Supplier expiry = () -> {
            OffsetDateTime date = OffsetDateTime.now();
            return String.join("",
                    String.valueOf(date.getYear() + 5).substring(0, 2),
                    pad(String.valueOf(date.getMonthValue() + 1), 2, ""),
                    pad(date.toString(), 2, "0"));
        };

        Map<String, Object> defaults = new HashMap<>();
        defaults.put("first", first(null));
        defaults.put("last", last(null));
        defaults.put("passportNumber", NumberUtils.random(100000000, 999999999));
        defaults.put("dob", dob.get());
        defaults.put("expiry", expiry.get());
        defaults.put("gender", gender(null).equals("Female") ? "F" : "M");
        defaults.put("issuer", "GBR");
        defaults.put("nationality", "GBR");

        options = MapUtils.initOptions(options, defaults);
        return generate.apply(options);
    }

    public String name(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        options = MapUtils.initOptions(options, defaults);

        String first = first(options);
        String last = last(options);
        String name;

        if (MapUtils.get(options, "middle") != null) {
            name = first + ' ' + first(options) + ' ' + last;
        } else if (MapUtils.get(options, "middle_initial") != null) {
            Map<String, Object> charDefaults = new HashMap<>();
            charDefaults.put("alpha", true);
            charDefaults.put("casing", "upper");
            name = first + " " + character(charDefaults) + ". " + last;
        } else {
            name = first + " " + last;
        }

        if (MapUtils.get(options, "prefix") != null) {
            name = prefix(String.valueOf(options)) + " " + name;
        }

        if (MapUtils.get(options, "suffix") != null) {
            name = name + " " + suffix(options);
        }

        return name;
    }

    // Return the list of available name prefixes based on supplied gender.
    // @todo introduce internationalization
    public List<Map<String, String>> name_prefixes(String givenGender) {
        String gender = givenGender != null ? givenGender : "all";
        gender = gender.toLowerCase();
        Map<String, String> options = new HashMap<>();
        options.put("name", "Doctor");
        options.put("abbreviation.", "Dr.");


        List<Map<String, String>> prefixes = new ArrayList<>();
        prefixes.add(options);

        if (gender.equals("male") || gender.equals("all")) {
            options = new HashMap<>();
            options.put("name", "Mister");
            options.put("abbreviation.", "Mr.");
        }

        if (gender.equals("female") || gender.equals("all")) {
            options = new HashMap<>();
            options.put("name", "Miss");
            options.put("abbreviation.", "Miss.");

            options = new HashMap<>();
            options.put("name", "Misses");
            options.put("abbreviation.", "Mrs.");

        }
        return prefixes;
    }


    // Alias for name_prefix
    public List<Map<String, String>> prefix(String options) {
        return name_prefixes(options);
    }

    public String name_prefix(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("gender", "all");
        defaults.put("full", false);
        options = MapUtils.initOptions(options, defaults);
        Map<String, String> map = pickone(name_prefixes(MapUtils.get(options, "gender")));
        return (boolean) MapUtils.get(options, "full") ?
                map.get("name") :
                map.get("abbreviation");
    }

    //Hungarian ID number
    public String HIDN() {
        //Hungarian ID nuber structure: XXXXXXYY (X=number,Y=Capital Latin letter)
        String idn_pool = "0123456789";
        String idn_chrs = "ABCDEFGHIJKLMNOPQRSTUVWXYXZ";
        Map<String, Object> options = new HashMap<>();
        options.put("pool", idn_pool);
        options.put("length", 6);
        String idn = "";
        idn += string(options);
        options.put("pool", idn_chrs);
        options.put("length", 2);
        idn += string(options);
        return idn;
    }

    public String ssn(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("dashes", true);
        defaults.put("ssnFour", false);
        defaults.put("pool", "1234567890");
        options = MapUtils.initOptions(options, defaults);
        String ssn;
        String dash = (boolean) MapUtils.get(options, "dashes") ? "-" : "";
        options.put("length", 4);
        if (!(boolean) MapUtils.get(options, "ssnFour")) {
            options.put("length", 3);
            ssn = string(options) + dash;
            options.put("length", 2);
            ssn += string(options) + dash;
            options.put("length", 4);
            ssn += string(options);
        } else {
            ssn = string(options);
        }
        return ssn;
    }


    // Aadhar is similar to ssn, used in India to uniquely identify a person
    public String aadhar(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("separatedByWhiteSpace", true);
        defaults.put("onlyLastFour", false);
        defaults.put("aadhar_pool", "1234567890");
        options = MapUtils.initOptions(options, defaults);
        String aadhar;
        String whiteSpace = (boolean) MapUtils.get(options, "separatedByWhiteSpace") ? " " : "";
        options.put("length", 4);
        if (!(boolean) MapUtils.get(options, "onlyLastFour")) {
            aadhar = string(options) + whiteSpace;
            aadhar += string(options) + whiteSpace;
            aadhar += string(options);
        } else {
            aadhar = string(options);
        }
        return aadhar;
    }

    // Return the list of available name suffixes
    // @todo introduce internationalization
    public List<Map<String, String>> name_suffixes() {
        return  (List<Map<String, String>>) data.get("suffixes");
    }

    // Alias for name_suffix
    public String suffix(Map<String, Object> options) {
        return name_suffix(options);
    }

    public String name_suffix(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        options = MapUtils.initOptions(options, defaults);
        Map<String, String> map = pickone(name_suffixes());
        return (boolean) MapUtils.get(options, "full") ?
                map.get("name") :
                map.get("abbreviation");
    }

    public List<Map<String, String>> nationalities() {
        return (List<Map<String, String>>) data.get("nationalities");
    }

    // Generate random nationality based on json list
    public String nationality() {
        Map<String, String> nationality = pickone(nationalities());
        return nationality.get("name");
    }

    // -- End Person --
    // -- Mobile --
    // Android GCM Registration ID
    public String android_id() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("pool", "0123456789abcefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_");
        defaults.put("length", 178);
        String id = string(defaults);
        return "APA91" + id;
    }


    // Apple Push Token
    public String apple_token() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("pool", "abcdef1234567890");
        defaults.put("length", 64);
        return string(defaults);
    }

    public String base64(String hash) {
        return Base64.getEncoder().encodeToString(hash.getBytes());
    }

    // Windows Phone 8 ANID2
    public String wp8_anid2() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("length", 32);
        return base64(hash(defaults));
    }

    //     // Windows Phone 7 ANID
    public String wp7_anid() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("length", 3);
        return "A=" + guid().replace("-", "").toUpperCase() + "&E="
                + hash(defaults)
                + "&W=" + NumberUtils.random(0, 9);
    }

    //     // BlackBerry Device PIN
    public String bb_pin() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("length", 8);
        return hash(defaults);
    }

    // -- End Mobile --
    // -- Web --
    boolean arrayContains(String[] arr, Object searchString) {
        return Arrays.stream(arr)
                .anyMatch(x -> x.equals(String.valueOf(searchString)));
    }


    public String md5(String plaintext) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(plaintext.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public String domain(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        options = MapUtils.initOptions(options, defaults);

        return word(options) + "." + (MapUtils.get(options, "tld") != null ? MapUtils.get(options, "tld") : tld());
    }

    public String email(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        options = MapUtils.initOptions(options, defaults);
        return word(options) + "@" + (MapUtils.get(options, "domain") != null ? MapUtils.get(options, "domain") : domain(options));
    }

    public String email() {
        Map<String, Object> options = new HashMap<>();
        return word(options) + "@" + (MapUtils.get(options, "domain") != null ? MapUtils.get(options, "domain") : domain(options));
    }

    public String avatar(Map<String, Object> options) {
        String URL_BASE = "//www.gravatar.com/avatar/";
        String[] PROTOCOLS = {"http", "https"};
        String[] FILE_TYPES = {"bmp", "gif", "jpg", "png"};
        String[] FALLBACKS = {
                "404", // Return 404 if not found
                "mm", // Mystery man
                "identicon", // Geometric pattern based on hash
                "monsterid", // A generated monster icon
                "wavatar", // A generated face
                "retro", // 8-bit icon
                "blank" // A transparent png
        };
        String[] RATINGS = {
                "g",
                "pg",
                "r",
                "x"
        };
        Map<String, Object> opts = new HashMap<>();
        opts.put("protocol", null);
        opts.put("email", null);
        opts.put("fileExtension", null);
        opts.put("size", null);
        opts.put("fallback", null);
        opts.put("rating", null);


        if (options != null) {
            // Set to a random email
            opts.put("email", email());
            options = new HashMap<>();
        }

        opts = MapUtils.initOptions(options, opts);

        if (opts.get("email") != null) {
            // Set to a random email
            opts.put("email", email());
        }

        // Safe checking for params

        opts.put("protocol", arrayContains(PROTOCOLS, opts.get("protocol")) ? opts.get("protocol") + ":" : "");
        opts.put("size", opts.get("size") != null ? opts.get("size") : "");
        opts.put("rating", arrayContains(RATINGS, opts.get("rating")) ? opts.get("rating") : "");
        opts.put("fallback", arrayContains(FALLBACKS, opts.get("fallback")) ? opts.get("fallback") : "");
        opts.put("fileExtension", arrayContains(FILE_TYPES, opts.get("fileExtension")) ? opts.get("fileExtension") : "");

        return opts.get("protocol") +
                URL_BASE +
                md5((String) opts.get("email")) +
                (opts.get("fileExtension") != null ? "." + opts.get("fileExtension") : "") +
                (opts.get("size") != null || opts.get("rating") != null || opts.get("fallback") != null ? "?" : "") +
                (opts.get("size") != null ? "&s=" + opts.get("size").toString() : "") +
                (opts.get("rating") != null ? "&r=" + opts.get("rating") : "") +
                (opts.get("fallback") != null ? "&d=" + opts.get("fallback") : "");
    }

    /**
     * #Description:
     * ===============================================
     * Generate random color value base on color type:
     * -> hex
     * -> rgb
     * -> rgba
     * -> 0x
     * -> named color
     * <p>
     * #Examples:
     * ===============================================
     * * Geerate random hex color
     * chance.color() => '#79c157' / 'rgb(110,52,164)' / '0x67ae0b' / '#e2e2e2' / '#29CFA7'
     * <p>
     * * Generate Hex based color value
     * chance.color({format: 'hex'})    => '#d67118'
     * <p>
     * * Generate simple rgb value
     * chance.color({format: 'rgb'})    => 'rgb(110,52,164)'
     * <p>
     * * Generate Ox based color value
     * chance.color({format: '0x'})     => '0x67ae0b'
     * <p>
     * * Generate graiscale based value
     * chance.color({grayscale: true})  => '#e2e2e2'
     * <p>
     * * Return valide color name
     * chance.color({format: 'name'})   => 'red'
     * <p>
     * * Make color uppercase
     * chance.color({casing: 'upper'})  => '#29CFA7'
     * <p>
     * * Min Max values for RGBA
     * var light_red = chance.color({format: 'hex', min_red: 200, max_red: 255, max_green: 0, max_blue: 0, min_alpha: .2, max_alpha: .3});
     * <p>
     * param options
     *
     * @return [string] color value
     */
    public String color(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("format", "hex");
        defaults.put("min_red", "hex");
        defaults.put("min_green", "hex");
        options = MapUtils.initOptions(options, defaults);
        int red = NumberUtils.nextRandomInt(256);
        int green = NumberUtils.nextRandomInt(256);
        int blue = NumberUtils.nextRandomInt(256);
        Color randomColour = new Color(red, green, blue);

        String format = MapUtils.get(options, "format");
        if (Objects.equals(format, "hex")) {
            return "#" + Integer.toHexString(randomColour.getRGB()).substring(2);
        }
        return randomColour.toString();
    }

    public String color() {

        int red = NumberUtils.nextRandomInt(256);
        int green = NumberUtils.nextRandomInt(256);
        int blue = NumberUtils.nextRandomInt(256);

        Color randomColour = new Color(red, green, blue);
        String hex = "#" + Integer.toHexString(randomColour.getRGB()).substring(2);
        return hex.toUpperCase();
    }

    /**
     * #Description:
     * ===============================================
     * Generate a random Facebook id, aka fbid.
     * <p>
     * NOTE: At the moment (Sep 2017), Facebook ids are
     * "numeric strings" of length 16.
     * However, Facebook Graph API documentation states that
     * "it is extremely likely to change over time".
     *
     * @return [string] facebook id
     * <p>
     * #Examples:
     * ===============================================
     * chance.fbid() => '1000035231661304'
     */
    public String fbid() {
        Map<String, Object> options = new HashMap<>();
        options.put("pool", "1234567890");
        options.put("length", 11);
        return "10000" + string(options);
    }


    public String google_analytics() {
        Map<String, Object> options = new HashMap<>();
        options.put("max", "999999");
        options.put("length", 11);
        String account = pad(NumberUtils.random(111111, 999999), 6, "0");
        String property = pad(NumberUtils.random(11, 99), 2, "0");

        return "UA-" + account + '-' + property;
    }

    public String hashtag() {
        return "#" + word(null);
    }

    public String ip() {
        // Todo: This could return some reserved IPs. See http://vq.io/137dgYy
        // this should probably be updated to account for that rare as it may be
        return String.join(".",
                NumberUtils.random(1, 254) + "",
                NumberUtils.random(0, 255) + "",
                NumberUtils.random(0, 255) + "",
                NumberUtils.random(1, 254) + ""
        );
    }

    // Hash
    public String hash(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("length", 40);
        defaults.put("casing", "lower");
        options = MapUtils.initOptions(options, defaults);
        String pool = Constants.HEX_POOL;
        if (MapUtils.get(options, "casing") == "upper") {
            pool = Constants.HEX_POOL.toUpperCase();
        }

        options.put("pool", pool);
        return string(options);
    }

    public String ipv6() {
        Map<String, Object> options = new HashMap<>();
        options.put("length", 4);
        Supplier<String> hashFn = () -> hash(options);
        List<String> ip_addr = n(hashFn, 8);

        return String.join(":", ip_addr);
    }

    public String klout() {
        return String.valueOf(NumberUtils.random(1, 99));
    }

    public String mac(Map<String, Object> options) {
        // Todo: This could also be extended to EUI-64 based MACs
        // (https://www.iana.org/assignments/ethernet-numbers/ethernet-numbers.xhtml#ethernet-numbers-4)
        // Todo: This can return some reserved MACs (similar to IP function)
        // this should probably be updated to account for that rare as it may be
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("delimiter", ":");
        options = MapUtils.initOptions(options, defaults);
        String delimiter = MapUtils.get(options, "delimiter");
        return pad(NumberUtils.random(0, 255), 2, "") + delimiter +
                pad(NumberUtils.random(0, 255), 2, "") + delimiter +
                pad(NumberUtils.random(0, 255), 2, "") + delimiter +
                pad(NumberUtils.random(0, 255), 2, "") + delimiter +
                pad(NumberUtils.random(0, 255), 2, "") + delimiter +
                pad(NumberUtils.random(0, 255), 2, "") + "";
    }

    public String semver(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("include_prerelease", true);
        options = MapUtils.initOptions(options, defaults);

        var range = pickone(Arrays.asList("^", "~", "<", ">", "<=", ">=", "="));
        if (MapUtils.get(options, "range") != null) {
            range = MapUtils.get(options, "range");
        }

        var prerelease = "";
        if (MapUtils.get(options, "include_prerelease") != null) {
            Object[] arr = new Object[]{"", "-dev", "-beta", "-alpha"};
            int[] weights = {50, 10, 5, 1};
            prerelease = String.valueOf(weighted(arr, weights, false));
        }

        return range + String.join(".") + prerelease;
    }

    private String rpg(String s) {
        return null;
    }

    public List<String> tlds() {
        return (List<String>) data.get("tlds");
    }

    public String tld() {
        return pickone(tlds());
    }

    public String twitter() {
        return "@" + word();
    }

    public String url(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("protocol", "http");
        defaults.put("domain", domain(options));
        defaults.put("domain_prefix", "");
        defaults.put("path", word());
        defaults.put("extensions", new ArrayList<>());

        options = MapUtils.initOptions(options, defaults);

        List<String> extensions = MapUtils.get(options, "extensions");
        String domain_prefix = MapUtils.get(options, "domain_prefix");
        String givenDomain = MapUtils.get(options, "domain");
        String protocol = MapUtils.get(options, "protocol");
        String path = MapUtils.get(options, "path");
//
        var extension = extensions.size() > 0 ? "." + pickone(extensions) : "";
        var domain = !domain_prefix.isBlank() ? domain_prefix + "."
                + givenDomain : givenDomain;
//
        return protocol + "://" + domain + "/" + path + extension;
    }

    public int port() {
        return NumberUtils.random(80, 65535);
    }

    public String locale(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("region", false);
        options = MapUtils.initOptions(options, defaults);
        if ((boolean) MapUtils.get(options, "region")) {
            return pickone((List<?>) data.get("locale_regions"));
        } else {
            return pickone((List<?>) data.get("locale_languages"));
        }
    }

    public List<String> locales(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("region", false);
        options = MapUtils.initOptions(options, defaults);
        if ((boolean) MapUtils.get(options, "region")) {
            return (List<String>) data.get("locale_regions");
        } else {
            return (List<String>) data.get("locale_languages");
        }
    }

    public String loremPicsum(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("width", 500);
        defaults.put("height", 500);
        defaults.put("greyscale", false);
        defaults.put("blurred", false);
        options = MapUtils.initOptions(options, defaults);

        var greyscale = (boolean) MapUtils.get(options, "greyscale") ? "g/" : "";
        var query = (boolean) MapUtils.get(options, "blurred") ? "/?blur" : "/?random";

        return "https://picsum.photos/" + greyscale + MapUtils.get(options, "width") + "/" + MapUtils.get(options, "height") + query;
    }

    // -- End Web --
    // -- Location --

    public String address(Map<String, Object> options) {
        options = MapUtils.initOptions(options, new HashMap<>());
        return NumberUtils.random(5, 2000) + " " + street(options);
    }

    public float altitude(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("fixed", 5);
        defaults.put("min", 0);
        defaults.put("max", 8848);
        options = MapUtils.initOptions(options, defaults);
        return floating(options);
    }

    public String areacode(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("parens", true);
        options = MapUtils.initOptions(options, defaults);
        // Don't want area codes to start with 1, or have a 9 as the second digit
        String areacode;
        MapUtils.get(options, "exampleNumber");
        areacode = "555";

        return (boolean) MapUtils.get(options, "parens") ? '(' + areacode + ')' : areacode;
    }

    public String city() {
        Map<String, Object> options = new HashMap<>();
        options.put("syllables", 3);
        return capitalize(word(options));
    }

    public String coordinates(Map<String, Object> options) {
        return latitude(options) + ",  " + longitude(options);
    }

    public List<Map<String, String>> countries() {
        return (List<Map<String, String>>) data.get("countries");
    }


    public Object country(Map<String, Object> options) {
        options = MapUtils.initOptions(options, new HashMap<>());
        Map<String, String> country = pickone(countries());
        return (boolean) MapUtils.get(options, "raw") ?
                country : (boolean) MapUtils.get(options, "full") ? country.get("name")
                : country.get("abbreviation");
    }


    public float depth(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("fixed", 5);
        defaults.put("min", -10994);
        defaults.put("max", 0);
        options = MapUtils.initOptions(options, defaults);
        return floating(options);
    }


    public String geohash(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("length", 7);
        defaults.put("pool", "'0123456789bcdefghjkmnpqrstuvwxyz'");
        options = MapUtils.initOptions(options, defaults);
        return string(options);
    }


    public String geojson(Map<String, Object> options) {
        return latitude(options) + ", " + longitude(options) + ", " + altitude(options);
    }

    public String latitude(Map<String, Object> options) {
        // Constants - Formats
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", 89);
        defaults.put("fixed", 4);

        if (MapUtils.get(options, "format") != null) {
            defaults.put("format", "");
        }

        String format = MapUtils.get(options, "format");
        if (!format.isEmpty() && (format.contains("ddm") || format.contains("dms"))) {
            defaults.put("min", 0);
            defaults.put("max", 89);
            defaults.put("fixed", 4);

        } else {
            defaults.put("min", -90);
            defaults.put("max", 90);
            defaults.put("fixed", 5);
            defaults.put("format", "dd");

        }

        options = MapUtils.initOptions(options, defaults);

        format = MapUtils.get(options, "format");
        int min = MapUtils.get(options, "min");
        int max = MapUtils.get(options, "max");
        int fixed = MapUtils.get(options, "fixed");


        if (Objects.equals(format, "ddm") || Objects.equals(format, "dms")) {
            ErrorUtils.testRange(min < 0 || min > 89, "Chance: Min specified is out of range. Should be between 0 - 89");
            ErrorUtils.testRange(max < 0 || max > 89, "Chance: Max specified is out of range. Should be between 0 - 89");
            ErrorUtils.testRange(fixed > 4, "Chance: Fixed specified should be below or equal to 4");
        }

        switch (format) {
            case "ddm": {
                return NumberUtils.random(min, max) + "°" + floating(options);
            }
            case "dms": {
                return NumberUtils.random(min, max) + "°" +
                        NumberUtils.random(0, 59) + "’" +
                        floating(0, 59, fixed) + "\"";
            }
            case "dd":
            default: {
                return floating(min, max, fixed) + "";
            }
        }
    }

    public String longitude(Map<String, Object> options) {
        // Constants - Formats
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", -180);
        defaults.put("max", 180);
        defaults.put("fixed", 5);
        defaults.put("format", "dd");

        if (MapUtils.get(options, "format") != null) {
            defaults.put("format", "");
        }
        String format = MapUtils.get(options, "format");
        if (!format.isEmpty() && (format.contains("ddm") || format.contains("dms"))) {
            defaults.put("min", 0);
            defaults.put("max", 179);
            defaults.put("fixed", 4);
        }
        options = MapUtils.initOptions(options, defaults);


        format = format.toLowerCase();
        int min = MapUtils.get(options, "min");
        int max = MapUtils.get(options, "max");
        int fixed = MapUtils.get(options, "fixed");

        if (format.equals("ddm") || format.equals("dms")) {
            ErrorUtils.testRange(min < 0 || min > 179, "Chance: Min specified is out of range. Should be between 0 - 179");
            ErrorUtils.testRange(max < 0 || max > 179, "Chance: Max specified is out of range. Should be between 0 - 179");
            ErrorUtils.testRange(fixed > 4, "Chance: Fixed specified should be below or equal to 4");
        }

        switch (format) {
            case "ddm": {
                return NumberUtils.random(min, max) + "°" +
                        floating(0, 59.9999f, fixed);
            }
            case "dms": {
                return NumberUtils.random(min, max) + "°" +
                        NumberUtils.random(0, 59) + "’" +
                        floating(0, 59.9999f, fixed) + "”";
            }
            case "dd":
            default: {
                return floating(min, max, fixed) + "";
            }
        }
    }

    public String postal() {
        // Postal District
        char pd = character("XVTSRPNKLMHJGECBA");
        // Forward Sortation Area (FSA)
        String fsa = pd + "" + natural(9) + character(true, "upper");
        // Local Delivery Unut (LDU)
        String ldu = natural(9) + "" + character(true, "upper") + natural(9);

        return fsa + " " + ldu;
    }

    public ArrayList<Map<String, Object>> postcodeAreas() {
        return (ArrayList<Map<String, Object>>) data.get("postcodeAreas");
    }

    public String postcode() {
        // Area
        Map<String, Object> area = pickone(postcodeAreas());
        var areaCode = area.get("code");
        // District
        var district = natural(9) + "";
        // Sub-District
        var subDistrict = bool(null) ?
                character(true, "upper") : "";
        // Outward Code
        var outward = areaCode + district + subDistrict;
        // Sector
        var sector = natural(9) + "";
        // Unit
        var unit = character(true, "upper") + character(true, "upper");
        // Inward Code
        var inward = sector + unit;

        return outward + " " + inward;
    }

    public <T> T counties() {
        return (T) data.get("counties");
    }

    public List<Map<String, Object>> counties(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("country", "uk");
        options = MapUtils.initOptions(options, defaults);
        Map<String, List<Map<String, Object>>> picked = counties();
        return picked.get(MapUtils.get(options, "country"));
    }

    public <T> T provinces() {
        return (T) data.get("provinces");
    }

    public String county(Map<String, Object> options) {
        Map<String, String> picked = pickone(counties(options));
        return picked.get("name");
    }

    public List<Map<String, Object>> provinces(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("country", "ca");
        options = MapUtils.initOptions(options, defaults);
        Map<String, List<Map<String, Object>>> picked = provinces();
        return picked.get(MapUtils.get(options, "country"));
    }

    public String province(Map<String, Object> options) {
        Map<String, String> picked = pickone(provinces(options));
        return (options != null && MapUtils.get(options, "full") != null) ?
                picked.get("name") :
                picked.get("abbreviation");
    }

    public String state(Map<String, Object> options) {
        Map<String, String> picked = pickone(states(options));
        return (options != null && MapUtils.get(options, "full") != null) ?
                picked.get("name") :
                picked.get("abbreviation");
    }

    public <T> T us_states_and_dc() {
        return (T) data.get("us_states_and_dc");
    }

    public <T> T armed_forces() {
        return (T) data.get("armed_forces");
    }

    public <T> T territories() {
        return (T) data.get("territories");
    }

    public <T> T country_regions() {
        return (T) data.get("territories");
    }

    public List<Map<String, Object>> states(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("country", "us");
        defaults.put("us_states_and_dc", true);
        options = MapUtils.initOptions(options, defaults);

        List<Map<String, Object>> states = new ArrayList<>();

        switch (MapUtils.get(options, "country").toString().toLowerCase()) {
            case "us":
                states = new ArrayList<>();

                if ((boolean) MapUtils.get(options, "us_states_and_dc")) {
                    states.add(us_states_and_dc());
                }
                if (MapUtils.get(options, "territories") != null) {
                    states.add(territories());
                }
                if (MapUtils.get(options, "armed_forces") != null) {
                    states.add(armed_forces());
                }
                break;
            case "it":
            case "mx":
                Map<String, List<Map<String, Object>>> cr = country_regions();
                states.add((Map<String, Object>) cr.get(MapUtils.get(options, "country")));
                break;
            case "uk":
                states = counties(null);
                break;
        }

        return states;
    }

    public String street(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("country", "us");
        defaults.put("syllables", 2);
        defaults.put("short_suffix", false);
        options = MapUtils.initOptions(options, defaults);
        String streetName = capitalize(word(options));
        Map<String, Object> streetCountry = (Map<String, Object>) street_suffix(options);
        String suffix = (String) streetCountry.get("name");
        if ((boolean) MapUtils.get(options, "short_suffix")) {
            suffix = (String) streetCountry.get("abbreviation");
        }
        return suffix + " " + streetName;
    }

    public Object street_suffix(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("country", "us");
        options = MapUtils.initOptions(options, defaults);
        Map<String, List<String>> map = street_suffixes();
        List<String> street_suffixes = map.get(MapUtils.get(options, "country"));
        return pickone(street_suffixes);
    }

    public <T> T street_suffixes() {
        return (T) data.get("street_suffixes");
    }

    public <T> T months() {
        return (T) data.get("months");
    }


    // Note: only returning US zip codes, internationalization will be a whole
    // other beast to tackle at some point.
    public String zip(Map<String, Object> options) {
        Supplier fn = () -> natural(9);
        List<String> zip = ((List<Integer>) n(fn, 5))
                .stream().map(String::valueOf).collect(Collectors.toList());

        if (options != null && (boolean) MapUtils.get(options, "plusfour")) {
            zip.add("-");
            List<String> plusfour = ((List<Integer>) n(fn, 4))
                    .stream().map(String::valueOf).collect(Collectors.toList());
            zip.addAll(plusfour);
        }

        return String.join("", zip);
    }

    // -- End Location --

    // -- Time
    public String ampm() {
        return bool(null) ? "am" : "pm";
    }

    public <T> T date(Map<String, Object> options) {
        String date_string;
        LocalDateTime date;
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("string", false);
        options = MapUtils.initOptions(options, defaults);
        // If interval is specified we ignore preset
        if (MapUtils.get(options, "min") != null || MapUtils.get(options, "max") != null) {
            defaults.put("american", true);
            defaults.put("string", false);
            options = MapUtils.initOptions(options, defaults);
            long min = MapUtils.get(options, "min") != null ? ((Date) MapUtils.get(options, "minDate")).getTime() : 1;
            long max = MapUtils.get(options, "max") != null ? ((Date) MapUtils.get(options, "maxDate")).getTime() : 8640000000000000L;

            date = LocalDateTime.ofInstant(Instant.ofEpochSecond(NumberUtils.random(min, max)), ZoneOffset.UTC);
        } else {
            options.put("raw", true);
            Map<String, Object> m = month(options);

            int daysInMonth = (int) (double) m.get("days");

            if (MapUtils.get(options, "month") != null) {
                // Mod 12 to allow months outside range of 0-11 (not encouraged, but also not prevented).
                Map map = months();
                m = (Map<String, Object>) map.get((((int) MapUtils.get(options, "month") % 12) + 12) % 12);
                daysInMonth = Integer.parseInt(String.valueOf(m.get("days")));
            }
            defaults.put("year", year(null));
            defaults.put("month", Integer.parseInt(String.valueOf(m.get("numeric"))));
            defaults.put("day", NumberUtils.random(1, daysInMonth));
            Map<String, Object> hourOptions = new HashMap<>();
            hourOptions.put("twentyfour", true);
            defaults.put("hour", hour(hourOptions));
            defaults.put("minute", minute());
            defaults.put("second", NumberUtils.random(1, daysInMonth));
            defaults.put("millisecond", NumberUtils.random(1, daysInMonth));
            defaults.put("american", true);
            defaults.put("string", false);

            options = MapUtils.initOptions(options, defaults);

            date = LocalDateTime.of(
                    MapUtils.get(options, "year"),
                    (int) MapUtils.get(options, "month"),
                    MapUtils.get(options, "day"),
                    MapUtils.get(options, "hour"),
                    MapUtils.get(options, "minute"),
                    MapUtils.get(options, "second"),
                    MapUtils.get(options, "millisecond")
            );
        }

        if (MapUtils.get(options, "american") != null) {
            // Adding 1 to the month is necessary because Date() 0-indexes
            // months but not day for some odd reason.
            date_string = (date.getMonthValue() + 1) + "/" + date.getDayOfMonth() + "/" + date.getYear();
        } else {
            date_string = date.getDayOfMonth() + "/" + (date.getDayOfMonth() + 1) + "/" + date.getYear();
        }

        return (boolean) MapUtils.get(options, "string") ? (T) date_string : (T) date;
    }

    public int hour(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        boolean twentyfour = false;
        if (MapUtils.isExist(defaults, "twentyfour")) {
            twentyfour = MapUtils.get(options, "twentyfour");
        }
        defaults.put("min", twentyfour ? 0 : 1);
        defaults.put("max", twentyfour ? 23 : 12);
        options = MapUtils.initOptions(options, defaults);
        int min = MapUtils.get(options, "min");
        int max = MapUtils.get(options, "max");

        ErrorUtils.testRange(min < 0, "Chance: Min cannot be less than 0.");
        ErrorUtils.testRange(twentyfour && max > 23, "Chance: Max cannot be greater than 23 for twentyfour option.");
        ErrorUtils.testRange(!twentyfour && max > 12, "Chance: Max cannot be greater than 12.");
        ErrorUtils.testRange(min > max, "Chance: Min cannot be greater than Max.");

        return NumberUtils.random(min, max);
    }

    public long timestamp(Map<String, Object> options) {
        int max = (int) (new Date().getTime() / 1000);
        return natural(1, max);
    }

    public String weekday(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("weekday_only", false);
        options = MapUtils.initOptions(options, defaults);
        String[] days = new String[]{"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        List<String> weekdays = new ArrayList(List.of(days));
        if (!(boolean) MapUtils.get(options, "weekday_only")) {
            weekdays.add("Saturday");
            weekdays.add("Sunday");
        }
        return pickone(weekdays);
    }

    public int year(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", LocalDateTime.now().getYear());
        // Default to current year as min if none specified
        options = MapUtils.initOptions(options, defaults);
        int min = MapUtils.get(options, "min");
        // Default to one century after current year as max if none specified
        int max = min + 100;
        if (MapUtils.get(options, "max") != null) {
            max = MapUtils.get(options, "max");
        }

        return natural(min, max);
    }

    public int second() {
        return NumberUtils.random(0, 59);
    }

    public int minute(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", 59);
        options = MapUtils.initOptions(options, defaults);
        int min = MapUtils.get(options, "min");
        int max = MapUtils.get(options, "min");
        ErrorUtils.testRange(min < 1, "Chance: Min cannot be less than 1.");
        ErrorUtils.testRange(max > 12, "Chance: Max cannot be greater than 12.");
        ErrorUtils.testRange(min > max, "Chance: Min cannot be greater than Max.");

        return NumberUtils.random(min, max);
    }

    public int minute() {
        return NumberUtils.random(0, 59);
    }

    public int millisecond() {
        return NumberUtils.random(0, 999);
    }

    public <T> T month(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 1);
        defaults.put("max", 12);
        defaults.put("raw", false);
        options = MapUtils.initOptions(options, defaults);
        int min = MapUtils.get(options, "min");
        int max = MapUtils.get(options, "max");

        ErrorUtils.testRange(min < 1, "Chance: Min cannot be less than 1.");
        ErrorUtils.testRange(max > 12, "Chance: Max cannot be greater than 12.");
        ErrorUtils.testRange(min > max, "Chance: Min cannot be greater than Max.");
        List<Map<String, String>> months = months();
        Map<String, Object> month = pickone(months.subList(min - 1, max));
        return (boolean) MapUtils.get(options, "raw") ? (T) month : (T) month.get("name");
    }

    long hammertime(Map<String, Object> options) {
        LocalDateTime localDateTime = date(options);
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
    // -- End Time

    //     // -- Finance --

    public String cc(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        options = MapUtils.initOptions(options, defaults);

        Map<String, Object> type;
        int to_generate;

        type = (MapUtils.get(options, "type") != null) ?
                cc_type((String) MapUtils.get(options, "type"), true) :
                cc_type(null, true);

        List<String> number = new ArrayList<>(Arrays.asList(String.valueOf(type.get("prefix")).split("")));
        to_generate = (int) type.get("length")
                - String.valueOf(type.get("prefix")).length() - 1;

        // Generates n - 1 digits
        number.addAll(Arrays.asList(String.valueOf(type.get("prefix")).split("")));
        Supplier fn = () -> integer(0, 9);
        number.addAll(n(fn, to_generate));


        // Generates the last digit according to Luhn algorithm
        number.add(luhn_calculate(String.join("", number)));

        return String.join("", number);
    }

    public <T> List<T> reverseList(List<T> list) {
        List<T> reverse = new ArrayList<>(list);
        Collections.reverse(reverse);
        return reverse;
    }

    public String luhn_calculate(String num) {
        List<String> digits = reverseList(Arrays.asList(num.split("")));
        var sum = 0;
        int digit;

        for (int i = 0, l = digits.size(); l > i; ++i) {
            digit = Integer.parseInt(digits.get(i));
            if (i % 2 == 0) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }
        return ((sum * 9) % 10) + "";
    }

    public <T> T cc_types() {
        // http://en.wikipedia.org/wiki/Bank_card_number#Issuer_identification_number_.28IIN.29
        return (T) data.get("cc_types");
    }

    public <T> T cc_type(String name, boolean raw) {
        List<Map<String, Object>> types = cc_types();
        Map<String, Object> type = null;

        if (name != null) {
            for (Map<String, Object> stringObjectMap : types) {
                // Accept either name or short_name to specify card type
                if (stringObjectMap.get("name") == name
                        || stringObjectMap.get("short_name") == name) {
                    type = stringObjectMap;
                    break;
                }
            }
            ErrorUtils.testRange(type == null, "Chance: Credit card type '" + name + "' is not supported");

        } else {
            type = pickone(types);
        }

        return raw ? (T) type : (T) type.get("name");
    }

    //     // return all world currency by ISO 4217
    public <T> T currency_types() {
        return (T) data.get("currency_types");
    }

    //     // return random world currency by ISO 4217
    public Map<String, Object> currency() {
        return pickone(currency_types());
    }

    // return all timezones available
    public <T> T timezones() {
        return (T) data.get("timezones");
    }

    // return random timezone
    public Map<String, Object> timezone() {
        return pickone(timezones());
    }

    //Return random correct currency exchange pair (e.g. EUR/USD) or array of currency code
    //ToDO
    public String currency_pair(String returnAsString) {
//         var currencies = unique(currency, 2, {
//             comparator: function(arr, val) {
//
//                 return arr.reduce(function(acc, item) {
//                     // If a match has been found, short circuit check and just return
//                     return acc || (item.code === val.code);
//                 }, false);
//             }
//         });
//
//         if (returnAsString) {
//             return currencies[0].code + '/' + currencies[1].code;
//         } else {
//             return currencies;
//         }
        return null;
    }
     public String dollar(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("max", 10000);
        defaults.put("min", 0);
        // By default, a somewhat more sane max for dollar than all available numbers
        options = MapUtils.initOptions(options, defaults);

        String dollar = String.valueOf(floating(MapUtils.get(options, "min"), MapUtils.get(options, "max"), 2));


        if (dollar.contains("\\.")) {
            dollar += ".00";
        }
        String cents = dollar.split("\\.")[1];
        if (cents.length() < 2) {
            dollar = dollar + '0';
        }

        if (Float.parseFloat(dollar) < 0) {
            return "-$" + dollar.replace("-", "");
        } else {
            return "$" + dollar;
        }
    }

    public String euro(Map<String, Object> options) {
        return (dollar(options).replace("$", "")) + "€";
    }

    public <T> T exp(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("exp", LocalDateTime.now().getYear()+5);
        options = MapUtils.initOptions(options, defaults);
        Map<String, Object> exp = new HashMap<>();

        int exp_year = exp_year();
        int exp_month = exp_month(false);

        // If the year is this year, need to ensure month is greater than the
        // current month or this expiration will not be valid
        if (Objects.equals(exp.get("year"),LocalDateTime.now().getYear())) {
            exp_month = exp_month(true);
        }
        defaults.put("month", exp_month);
        defaults.put("year", exp_year);

        return (boolean) MapUtils.get(options, "raw") ? (T) exp : (T) (exp_month + "/" + exp_year);
    }

    public int exp_month(boolean future) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("future", future);
        return exp_month(defaults);
    }

    public int exp_month(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        options = MapUtils.initOptions(options, defaults);
        int month, month_int;
        // Date object months are 0 indexed
        int curMonth = LocalDateTime.now().getMonthValue() + 1;

        if ((boolean) MapUtils.get(options, "future") && (curMonth != 12)) {
            do {
                month = Integer.parseInt(((Map<String, String>) month(options)).get("numeric"));
                month_int = month;
            } while (month_int <= curMonth);
        } else {
            options.put("raw", true);

            month = Integer.parseInt(((Map<String, String>) month(options)).get("numeric"));
        }

        return month;
    }

    public int exp_year() {
        int curMonth = LocalDateTime.now().getMonthValue() + 1;
        int curYear = LocalDateTime.now().getYear();
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", ((curMonth == 12) ? (curYear + 1) : curYear));
        defaults.put("max", (curYear + 10));

        return year(defaults);
    }

    public String vat(Map<String, Object> options) {
        if ("it".equalsIgnoreCase(MapUtils.get(options, "country").toString())) {
            return it_vat();
        }
        return null;
    }

    private String it_vat() {
        var it_vat = natural(1, 1800000);
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("country", "it");
        Map<String, Object> p = pickone(provinces(defaults));

        it_vat = Integer.parseInt(pad(it_vat, 7, "") + pad(p.get("code"), 3, ""));
        return it_vat + luhn_calculate(it_vat + "");
    }

    /**
     * Generate a string matching IBAN pattern (https://en.wikipedia.org/wiki/International_Bank_Account_Number).
     * No country-specific formats support (yet)
     */
    public String iban() {
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String alphanum = alpha + "0123456789";
        return string(alpha, 2)
                + pad(integer(0, 99), 2, "")
                + string(alphanum, 4)
                + pad(natural(9), natural(6, 26), "");
    }
    // -- End Finance

    // Guid
    public String guid() {
        return UUID.randomUUID().toString();
    }

    // Coin - Flip, flip, flipadelphia
    public String coin() {
        return bool(new HashMap<>()) ? "heads" : "tails";
    }

    public String emotion() {
        return pickone((List<?>) data.get("emotions"));
    }

    public int diceFn(int min, int max) {
        return natural(min, max);
    }

    public Supplier d4 = () -> diceFn(1, 4);
    public Supplier d6 = () -> diceFn(1, 6);
    public Supplier d8 = () -> diceFn(1, 8);
    public Supplier d10 = () -> diceFn(1, 10);
    public Supplier d20 = () -> diceFn(1, 20);
    public Supplier d30 = () -> diceFn(1, 30);
    public Supplier d100 = () -> diceFn(1, 100);


    /*
     * this generator is written following the official algorithm
     * all data can be passed explicitely or randomized by calling chance.cf() without options
     * the code does not check that the input data is valid (it goes beyond the scope of the generator)
     *
     * param  [Object] options = { first: first name,
     *                              last: last name,
     *                              gender: female|male,
                                    birthday: JavaScript date object,
                                    city: string(4), 1 letter + 3 numbers
                                   }
     * @return [string] codice fiscale
     *
    */
    public String cf(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();

        options = MapUtils.initOptions(options, defaults);
        String gender = MapUtils.get(options, "gender") != null ? (String) MapUtils.get(options, "gender") : gender(options);
        options.put("gender", gender);
        options.put("nationality", "it");
        String first = MapUtils.get(options, "first") != null ? (String) MapUtils.get(options, "first") : first(options);
        String last = MapUtils.get(options, "last") != null ? (String) MapUtils.get(options, "last") : last(options);
        LocalDateTime birthday = MapUtils.get(options, "birthday") != null ? (LocalDateTime) MapUtils.get(options, "birthday") : birthday(options);
        String city = MapUtils.get(options, "city") != null ? (String) MapUtils.get(options, "city") : pickone(Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'L', 'M', 'Z'))
                + pad(natural(999), 3, "");
        List<String> cf = new ArrayList<>();


        cf.add(name_generator(last, true));
        cf.add(name_generator(first, false));
        cf.add(date_generator(birthday, gender));
        cf.addAll(List.of(city.toUpperCase().split("")));
        String rs = String.join("", cf);
        rs += checkdigit_generator(rs.toUpperCase());

        return rs.toUpperCase();
    }

    String name_generator(String name, boolean isLast) {
        String temp;
        List<String> return_value = new ArrayList<>();

        if (name.length() < 3) {
            return_value.addAll(List.of(name.split("")));
            return_value.addAll(List.of("XXX".split("")));
            return_value = return_value.subList(0, 3);
        } else {
            temp = Arrays.stream(name.toUpperCase().split(""))
                    .map((c) -> ("BCDFGHJKLMNPRSTVWZ".contains(c)) ? c : null)
                    .collect(Collectors.joining(""));
            if (temp.length() > 3) {
                if (isLast) {
                    temp = temp.substring(0, 3);
                } else {
                    temp = temp.charAt(0) + temp.substring(2, 2);
                }
            }
            if (temp.length() < 3) {
                return_value = Collections.singletonList(temp);
                temp = Arrays.stream(name.toUpperCase().split(""))
                        .map((c) -> ("AEIOU".contains(c)) ? c : null)
                        .collect(Collectors.joining("")).substring(0, 3 - return_value.size());
            }
            return_value = Collections.singletonList(return_value + temp);
        }

        return String.join("", return_value);
    }

    String date_generator(LocalDateTime birthday, String gender) {
        var lettermonths = new char[]{'A', 'B', 'C', 'D', 'E', 'H', 'L', 'M', 'P', 'R', 'S', 'T'};

        return String.valueOf(birthday.getYear()).substring(2) +
                lettermonths[birthday.getMonthValue()] +
                pad(birthday.getDayOfMonth() + ((gender.toLowerCase().equals("female")) ? 40 : 0), 2, "");
    }

    String checkdigit_generator(String cf) {
        String range1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String[] range2 = "ABCDEFGHIJABCDEFGHIJKLMNOPQRSTUVWXYZ".split("");
        String evens = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String odds = "BAKPLCQDREVOSFTGUHMINJWZYX";
        int digit = 0;
        for (var i = 0; i < 15; i++) {
            if (i % 2 != 0) {
                digit += evens.indexOf(range2[range1.indexOf(cf.charAt(i))]);
            } else {
                digit += odds.indexOf(range2[range1.indexOf(cf.charAt(i))]);
            }
        }
        return evens.split("")[digit % 26];
    }

    public String pl_pesel() {
        int number = natural(1, 999999999);
        int[] arr = Stream.of(pad(number, 10, "").split(""))
                .mapToInt(Integer::parseInt)
                .toArray();

        int controlNumber = (1 * arr[0] + 3 * arr[1] + 7 * arr[2] + 9 * arr[3] + 1 * arr[4] + 3 * arr[5] + 7 * arr[6] + 9 * arr[7] + 1 * arr[8] + 3 * arr[9]) % 10;
        if (controlNumber != 0) {
            controlNumber = 10 - controlNumber;
        }

        return Arrays.stream(arr)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("")) + controlNumber;
    }

    public String pl_nip() {
        int number = natural(1, 999999999);
        int[] arr = Stream.of(pad(number, 9, "").split(""))
                .mapToInt(Integer::parseInt)
                .toArray();

        int controlNumber = (6 * arr[0] + 5 * arr[1] + 7 * arr[2] + 2 * arr[3] + 3 * arr[4] + 4 * arr[5] + 5 * arr[6] + 6 * arr[7] + 7 * arr[8]) % 11;
        if (controlNumber == 10) {
            return this.pl_nip();
        }

        return Arrays.stream(arr)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("")) + controlNumber;
    }

    public String pl_regon() {
        int number = natural(1, 999999999);
        int[] arr = Stream.of(pad(number, 8, "").split(""))
                .mapToInt(Integer::parseInt)
                .toArray();


        int controlNumber = (8 * arr[0] + 9 * arr[1] + 2 * arr[2] + 3 * arr[3] + 4 * arr[4] + 5 * arr[5] + 6 * arr[6] + 7 * arr[7]) % 11;
        if (controlNumber == 10) {
            controlNumber = 0;
        }

        return Arrays.stream(arr)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("")) + controlNumber;
    }

    // -- End Regional

    // -- Music --

    public String note(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("notes", "flatKey");
        // choices for 'notes' option:
        // flatKey - chromatic scale with flat notes (default)
        // sharpKey - chromatic scale with sharp notes
        // flats - just flat notes
        // sharps - just sharp notes
        // naturals - just natural notes
        // all - naturals, sharps and flats
        options = MapUtils.initOptions(options, defaults);
        Map<String, Object> scales = new HashMap<>();
        List<String> naturals = List.of("C", "D", "E", "F", "G", "A", "B");
        List<String> flats = List.of("D♭", "E♭", "G♭", "A♭", "B♭");
        List<String> sharps = List.of("C♯", "D♯", "F♯", "G♯", "A♯");

        List<String> all = Stream.of(naturals, flats, sharps)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<String> flatKey = Stream.of(naturals, flats)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        List<String> sharpKey = Stream.of(naturals, sharps)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        scales.put("all", all);
        scales.put("flatKey", flatKey);
        scales.put("sharpKey", sharpKey);
        List<String> notes = (List<String>) scales.get(MapUtils.get(options, "notes"));
        return pickone(notes);
    }


    public int midi_note(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", 127);

        options = MapUtils.initOptions(options, defaults);
        int min = MapUtils.get(options, "min");
        int max = MapUtils.get(options, "max");
        return integer(min, max);
    }

    public String chord_quality(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("jazz", true);
        options = MapUtils.initOptions(options, defaults);
        List<String> chord_qualities = List.of("maj", "min", "aug", "dim");
        if ((boolean) MapUtils.get(options, "jazz")) {
            chord_qualities = List.of(
                    "maj7",
                    "min7",
                    "7",
                    "sus",
                    "dim",
                    "ø"
            );
        }
        return pickone(chord_qualities);
    }

    public String chord(Map<String, Object> options) {
        options = MapUtils.initOptions(options, new HashMap<>());
        return note(options) + chord_quality(options);
    }

    public int tempo(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 40);
        defaults.put("max", 320);

        options = MapUtils.initOptions(options, defaults);
        int min = MapUtils.get(options, "min");
        int max = MapUtils.get(options, "max");
        return integer(min, max);
    }
    // -- End Music

    // -- Miscellaneous --


    // Dice - For all the board game geeks out there, myself included ;)


    public <T> T rpg(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        options = MapUtils.initOptions(options, defaults);
        ErrorUtils.testRange(MapUtils.get(options, "thrown") == null, "Chance: A type of die roll must be included");
        String[] bits = ((String) MapUtils.get(options, "thrown")).toLowerCase().split("d");
        Map<Integer, Integer> rolls = new HashMap<>();
//
        ErrorUtils.testRange(bits.length != 2 || !NumberUtils.isNumeric(bits[0]) || !NumberUtils.isNumeric(bits[1]), "Chance: Invalid format provided. Please provide #d# where the first # is the number of dice to roll, the second # is the max of each die");
        for (int i = Integer.parseInt(bits[0]); i > 0; i--) {
            rolls.put(i - 1, natural(1, Integer.parseInt(bits[1])));
        }
        if (MapUtils.get(options, "sum") != null && (boolean) MapUtils.get(options, "sum")) {
            return (T) rolls.values().parallelStream().reduce(0, Integer::sum);
        }
        return (T) rolls;
    }

    public boolean luhn_check(int num) {
        String str = num + "";
        var checkDigit = str.substring(str.length() - 1);
        return checkDigit.equals(luhn_calculate(str.substring(0, str.length() - 1)));
    }

    public <T> T fileExtension() {
        return (T) data.get("fileExtension");
    }

    public boolean checkArray(Object abc) {
        return abc.getClass().isArray();
    }

    public String file(Map<String, Object> options) {
        Map<String, Object> fileOptions = MapUtils.initOptions(options, new HashMap<>());
        Map<String, List<String>> fileExtensions = fileExtension();
        Set<String> typeRange = fileExtensions.keySet();//['raster', 'vector', '3d', 'document'];

        String fileExtension;
//        // Generate random file name
        String fileName = word(fileOptions);
//
//        // Generate file by specific extension provided by the user
        if (MapUtils.isExist(options, "extension")) {//
            fileExtension = MapUtils.get(options, "extension");
            return (fileName + '.' + fileExtension);
        }

        // Generate file by specific extension collection
        if (MapUtils.isExist(options, "extension")) {
            if (checkArray(MapUtils.get(options, "extension"))) {

                fileExtension = pickone(List.of(MapUtils.get(options, "extension")));
                return (fileName + '.' + fileExtension);
            } else if (MapUtils.get(options, "extension") instanceof String) {

                Map<String, List<String>> extensionObjectCollection = MapUtils.get(options, "extension");
                List<String> keys = new ArrayList<>(extensionObjectCollection.keySet());
                fileExtension = pickone(extensionObjectCollection.get(pickone(keys)));
                return (fileName + '.' + fileExtension);
            }
            ErrorUtils.testRange(true, "Chance: Extensions must be an Array or Object");

        }

        // Generate file extension based on specific file type
        if (MapUtils.isExist(options, "fileType")) {

            String fileType = MapUtils.get(options, "fileType");
            if (new ArrayList<>(typeRange).contains(fileType)) {

                fileExtension = pickone(fileExtensions.get(fileType));
                return (fileName + '.' + fileExtension);
            }

            ErrorUtils.testRange(true, "Chance: Expect file type value to be 'raster', 'vector', '3d' or 'document'");
        }

        // Generate random file name if no extension options are passed
        fileExtension = pickone(fileExtensions.get(pickone(new ArrayList<>(typeRange))));
        return (fileName + '.' + fileExtension);
    }


    // Mac Address
    public String mac_address(Map<String, Object> options) {
        // typically mac addresses are separated by ":"
        // however they can also be separated by "-"
        // the network variant uses a dot every fourth byte
        Map<String, Object> defaults = new HashMap<>();
        options = MapUtils.initOptions(options, defaults);
        String separator = "";
        if (!MapUtils.isExist(options, "separator")) {
            separator = MapUtils.get(options, "networkVersion") ? "." : ":";
        }

        String mac_pool = "ABCDEF1234567890";
        String mac = "";
        if (!MapUtils.isExist(options, "networkVersion")) {
            Supplier fn = () -> string(mac_pool, 2);
            List<String> macs = n(fn, 6);
            mac = String.join(separator, macs);
        } else {
            Supplier fn = () -> string(mac_pool, 4);
            List<String> macs = n(fn, 3);
            mac = String.join(separator, macs);
        }

        return mac;
    }



    public int normal(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        options.put("mean", 0);
        options.put("dev", 1);
        options.put("pool", new ArrayList<>());
        options = MapUtils.initOptions(options, defaults);

        ErrorUtils.testRange(
                MapUtils.isExist(options, "pool"),
                "Chance: The pool option must be a valid array."
        );
        ErrorUtils.testRange(
                NumberUtils.isNumeric(MapUtils.get(options, "mean")),
                "Chance: Mean (mean) must be a number"
        );
        ErrorUtils.testRange(
                NumberUtils.isNumeric(MapUtils.get(options, "dev")),
                "Chance: Standard deviation (dev) must be a number"
        );

        // If a pool has been passed, then we are returning an item from that pool,
        // using the normal distribution settings that were passed in
        String pool = MapUtils.get(options, "pool");
        if (pool.length() > 0) {
            return normal_pool(options);
        }

        // The Marsaglia Polar method
        int s, u, v, norm;
        int mean = MapUtils.get(options, "mean");
        int dev = MapUtils.get(options, "dev");

        do {
            // U and V are from the uniform distribution on (-1, 1)
            u = NumberUtils.random() * 2 - 1;
            v = NumberUtils.random() * 2 - 1;

            s = u * u + v * v;
        } while (s >= 1);

        // Compute the standard normal variate
        norm = (int) (u * Math.sqrt(-2 * Math.log(s) / s));

        // Shape and scale
        return dev * norm + mean;
    }

    public int normal_pool(Map<String, Object> options) {
        int performanceCounter = 0;
        Map<String, Object> defaults = new HashMap<>();
        options.put("mean", MapUtils.get(options, "mean"));
        options.put("dev", MapUtils.get(options, "dev"));
        do {
            var idx = Math.round(normal(options));

            String pool = MapUtils.get(options, "pool");
            int len = pool.length();
            if (idx < len && idx >= 0) {
                return pool.charAt(idx);
            } else {
                performanceCounter++;
            }
        } while (performanceCounter < 100);

        ErrorUtils.testRange(true, "Chance: Your pool is too small for the given mean and standard deviation. Please adjust.");
        return 0;
    }

    public String radio(Map<String, Object> options) {
        // Initial Letter (Typically Designated by Side of Mississippi River)
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("side", "?");
        options = MapUtils.initOptions(options, defaults);
        String fl;
        switch (((String) MapUtils.get(options, "side")).toLowerCase()) {
            case "east":
            case "e":
                fl = "W";
                break;
            case "west":
            case "w":
                fl = "K";
                break;
            default:
                fl = String.valueOf(character("KW"));
                break;
        }
        options.put("alpha", true);
        options.put("casing", "upper");
        return fl + character(options) +
                character(options) +
                character(options);
    }

    // Set the data as key and data or the data map
    public void set(String name, Object values) {
        if (name instanceof String) {
            data.put(name, values);
        }
//        else {
//            data = copyObject(name, data);
//        }
    }

    public String tv(Map<String, Object> options) {
        return radio(options);
    }

}
