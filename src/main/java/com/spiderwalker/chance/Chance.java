package com.spiderwalker.chance;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("casing", "");
        options = initOptions(options, defaults);


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
                pool += Constants.SYMBOLS;
            }
            if (pool.isEmpty()) {
                pool = letters + Constants.NUMBERS + Constants.SYMBOLS;
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
     * @throws {RangeError} Can only specify fixed or precision, not both. Also
     *                      min cannot be greater than max
     * @returns {Number} a single floating point number
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
     * <p>
     * NOTE the max and min are INCLUDED in the range.
     *
     * @param {Object} [options={}] can specify a min and/or max
     * @throws {RangeError} min cannot be greater than max nor negative
     * @returns {Number} a single random prime number
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

        List<Float> primes = new ArrayList<>((List<Float>) data.get("primes"));

        Integer lastPrime = (int) Math.round(10.0);

        if (max > lastPrime) {
            for (int i = lastPrime + 2; i <= max; ++i) {
                if (isPrime(i)) {
                    primes.add((float) i);
                }
            }
        }

        return Math.round(Float.parseFloat(String.valueOf(pickone(primes))));
    }

    ;

    public String letter(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("casing", "lower");
        String pool = "abcdefghijklmnopqrstuvwxyz";

        options = initOptions(options, defaults);
        Map<String, Object> defaultsCharacter = new HashMap<>();
        defaultsCharacter.put("pool", pool);
        defaultsCharacter.put("casing", "lower");
        String letter = String.valueOf(character(defaultsCharacter));
        if (options.get("casing") == "upper") {
            letter = letter.toUpperCase();
        }
        return letter;
    }

    /**
     * Return a random hex number as string
     * <p>
     * NOTE the max and min are INCLUDED in the range. So:
     * chance.hex({min: '9', max: 'B'});
     * would return either '9', 'A' or 'B'.
     *
     * @param {Object} [options={}] can specify a min and/or max and/or casing
     * @throws {RangeError} min cannot be greater than max
     * @returns {String} a single random string hex number
     */
    public String hex(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", Integer.MAX_VALUE);
        defaults.put("casing", "lower");
        options = initOptions(options, defaults);
        testRange((int) options.get("min") < 0, "Chance: Min cannot be less than zero.");
        int integer = natural(options);
        if (options.get("casing") == "upper") {
            return Integer.toUnsignedString(integer, 16).toUpperCase();
        }
        return Integer.toUnsignedString(integer, 16);
    }

    ;

    /**
     * Return a random string
     *
     * @param {Object} [options={}] can specify a length or min and max
     * @throws {RangeError} length cannot be less than zero
     * @returns {String} a string of random length
     */
    public String string(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 5);
        defaults.put("max", 20);
        options = initOptions(options, defaults);

        if (options.get("length") == null) {
            options.put("length", random((int) options.get("min"), (int) options.get("max")));
        }

        testRange((int) options.get("length") < 0, "Chance: Length cannot be less than zero.");

        Supplier<Object> func = () -> character(new HashMap<>());

        int length = (int) options.get("length");
        List text = n(func, length);
        StringBuilder sb = new StringBuilder();
        for (Object s : text) {
            sb.append(s);
        }

        return sb.toString();
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
    }

    ;

    public String letter() {
        return null;
    }

    /**
     * Return a random natural
     * <p>
     * NOTE the max and min are INCLUDED in the range. So:
     * chance.natural({min: 1, max: 3});
     * would return either 1, 2, or 3.
     *
     * @param {Object} [options={}] can specify a min and/or max or a numerals
     *                 count.
     * @throws {RangeError} min cannot be greater than max
     * @returns {Number} a single random integer number
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
    }

    ;

    // Given an list, returns a single random element
    public Object pickone(List<?> list) {
        testRange(list.size() == 0, "Chance: Cannot pickone() from an empty array");
        Map<String, Object> options = new HashMap<>();
        options.put("max", list.size() - 1);
        return list.get(natural(options));
    }

    public int random(int min, int max) {
        return rand.nextInt(max - min) + min;
    }

    public long random(long min, long max) {
        return rand.nextInt((int) (max - min)) + min;
    }

    /**
     * Return a random integer
     * <p>
     * NOTE the max and min are INCLUDED in the range. So:
     * chance.integer({min: 1, max: 3});
     * would return either 1, 2, or 3.
     *
     * @param {Object} [options={}] can specify a min and/or max
     * @throws {RangeError} min cannot be greater than max
     * @returns {Number} a single random integer number
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
     * <p>
     * There can be more parameters after these. All additional parameters
     * are provided to the given function
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

    public String pad(String number, int width, String pad) {
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
            Object[] items = {pickone(Arrays.asList(arr))};
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
    }

    ;

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

        Integer[] numbers = n(naturalFn, 8).toArray(Integer[]::new);

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

    public String israelId() {
        Map<String, Object> options = new HashMap<>();
        options.put("pool", "0123456789");
        options.put("length", 8);
        String x = string(options);
        String y = "0";
        for (int i = 0; i < x.length(); i++) {
            String thisDigit = String.valueOf(x.charAt(i) * (i / 2 == Integer.parseInt(String.valueOf(i / 2)) ? 1 : 2));
            thisDigit = pad(thisDigit, 2, "0").toString();
            thisDigit = thisDigit.charAt(0) + "" + thisDigit.charAt(0);
            y = y + thisDigit;
        }
        String z = String.valueOf(10 - Long.parseLong(y.substring(0, y.length() - 1)));
        x = x + z.substring(0, z.length() - 1);
        return x;
    }


    public int age(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("type", "");
        options = initOptions(options, defaults);
        int ageRange = 0;
        String type = (String) options.get("type");
        switch (type) {
            case "child":
                ageRange = random(0, 12);
                break;
            case "teen":
                ageRange = random(13, 19);
                break;
            case "adult":
                ageRange = random(18, 65);
                break;
            case "senior":
                ageRange = random(65, 100);
                break;
            case "all":
                ageRange = random(0, 100);
                break;
            default:
                ageRange = random(18, 65);
                break;
        }

        return (ageRange);
    }

    ;

    public Object month(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 1);
        defaults.put("max", 2);
        options = initOptions(options, defaults);

        testRange((int) options.get("min") < 1, "Chance: Min cannot be less than 1.");
        testRange((int) options.get("max") > 12, "Chance: Max cannot be greater than 12.");
        testRange((int) options.get("min") > (int) options.get("max"), "Chance: Min cannot be greater than Max.");

        Map<String, Object> month = (Map) pickone(months().subList((int) options.get("min") - 1, (int) options.get("max")));
        return options.get("raw") != null ? month : (String) month.get("name");
    }

    public ArrayList<HashMap<String, Object>> months() {
        return (ArrayList<HashMap<String, Object>>) data.get("months");
    }

    public int second() {
        return random(0, 59);
    }

    ;

    public int minute(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", 0);
        defaults.put("max", 59);
        options = initOptions(options, defaults);
        int min = (int) options.get("min");
        int max = (int) options.get("min");
        testRange(min < 1, "Chance: Min cannot be less than 1.");
        testRange(max > 12, "Chance: Max cannot be greater than 12.");
        testRange(min > max, "Chance: Min cannot be greater than Max.");

        return random(min, max);
    }

    // Guid
    public String guid() {
        return UUID.randomUUID().toString();
    }

    // Coin - Flip, flip, flipadelphia
    public String coin() {
        return bool(new HashMap<>()) ? "heads" : "tails";
    }

    public String capitalize(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }
    public String capitalizeEach(String words) {
        return Stream.of(words.trim().split("\\s"))
                .filter(word -> word.length() > 0)
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1))
                .collect(Collectors.joining(" "));
    }


    // Could get smarter about this than generating random words and
    // chaining them together. Such as: http://vq.io/1a5ceOh
    public String sentence(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", random(3, 7));
        defaults.put("words",random(12,18));
        defaults.put("punctuation",random(12,18));
        options = initOptions(options,defaults);

        int count = (int) options.get("words");
       boolean punctuation = (boolean) options.get("punctuation");
        Map<String, Object> finalOptions = options;
        Supplier<String> wordFn = () -> word(finalOptions);
       String text= null;
        List<String>   word_array = (List<String>) n(wordFn, count);

        text = String.join(" ",word_array);
        String punctuate = "";

        // Capitalize first letter of sentence
        text = capitalize(text);
        Pattern pattern = Pattern.compile("^[.?;!:]$");

        // Search above pattern in "softwareTestingHelp.com"
        Matcher m = pattern.matcher("softwareTestingHelp.com");

        // Make sure punctuation has a usable value
        if (punctuation != false && m.find()){
            punctuate = ".";
        }

        // Add punctuation mark
        if (punctuation) {
            text += punctuate;
        }

        return text;
    }
    public String word (Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
         options = initOptions(options,defaults);

         testRange(
                 options.get("syllables")!=null && options.get("length")!=null,
             "Chance: Cannot specify both syllables AND length."
         );

         int syllables = options.syllables || random(1, 3),
             text = '';

         if (options.length) {
             // Either bound word by length
             do {
                 text += this.syllable();
             } while (text.length < options.length);
             text = text.substring(0, options.length);
         } else {
             // Or by number of syllables
             for (var i = 0; i < syllables; i++) {
                 text += this.syllable();
             }
         }

         if (options.capitalize) {
             text = this.capitalize(text);
         }

         return text;
     }


    public String paragraph(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("sentences", random(3, 7));
        defaults.put("linebreak",true);
        options = initOptions(options, defaults);

        int count = (int) options.get("sentences");
        Map<String, Object> finalOptions = options;
        Supplier<String> sentenceFn = () -> sentence(finalOptions);

        List<String> sentence_array = (List<String>) n(sentenceFn, count);
                String separator = (boolean) options.get("linebreak")  ? "\n" : " ";

        return String.join(separator, sentence_array);
    }

    public int hour(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        boolean twentyfour = options.get("twentyfour") != null && (boolean) options.get("twentyfour");
        defaults.put("min", twentyfour ? 0 : 1);
        defaults.put("max", twentyfour ? 23 : 12);
        options = initOptions(options, defaults);
        int min = (int) options.get("min");
        int max = (int) options.get("min");

        testRange(min < 0, "Chance: Min cannot be less than 0.");
        testRange(twentyfour && max > 23, "Chance: Max cannot be greater than 23 for twentyfour option.");
        testRange(!twentyfour && max > 12, "Chance: Max cannot be greater than 12.");
        testRange(min > max, "Chance: Min cannot be greater than Max.");

        return random(min, max);
    }

    public long timestamp(Map<String, Object> options) {
        int min = (int) options.get("min");
        int max = (int) (new Date().getTime() / 1000);
        return random(min, max);
    }

    public String weekday(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("weekday_only", false);
        options = initOptions(options, defaults);
        List weekdays = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        if (!(boolean) options.get("month")) {
            weekdays.add("Saturday");
            weekdays.add("Sunday");
        }
        return (String) pickone(weekdays);
    }

    public String year(Map<String, Object> options) {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("min", LocalDateTime.now().getYear());
        // Default to current year as min if none specified
        options = initOptions(options, defaults);

        // Default to one century after current year as max if none specified
        int max = options.get("month") != null ? (int) options.get("max") : (int) options.get("min") + 100;

        return String.valueOf(random((int) options.get("min"), max));
    }

    ;

    public Object date(Map<String, Object> options) {
        String date_string = "";
        LocalDateTime date;
        Map<String, Object> defaults = new HashMap<>();
        // If interval is specified we ignore preset
        if (options != null && (options.get("min") != null || options.get("max") != null)) {
            defaults.put("american", true);
            defaults.put("string", false);
            options = initOptions(options, defaults);
            long min = options.get("min") != null ? ((Date) options.get("min")).getTime() : 1;
            long max = options.get("max") != null ? ((Date) options.get("min")).getTime() : 8640000000000000l;

            date = LocalDateTime.ofInstant(Instant.ofEpochSecond(random(min, max)), ZoneOffset.UTC);
        } else {
            defaults.put("raw", true);
            Map<String, Object> m = (Map) month(defaults);
            int daysInMonth = Integer.parseInt(String.valueOf(m.get("days")));

            if (options != null && options.get("month") != null) {
                // Mod 12 to allow months outside range of 0-11 (not encouraged, but also not prevented).
                m = months().get((((int) options.get("month") % 12) + 12) % 12);
                daysInMonth = Integer.parseInt(String.valueOf(m.get("days")));
            }
            defaults.put("year", Integer.parseInt(year(options), 10));
            defaults.put("month", Integer.parseInt(String.valueOf(m.get("numeric"))) - 1);
            defaults.put("day", random(1, daysInMonth));
            Map<String, Object> hourOptions = new HashMap<>();
            hourOptions.put("twentyfour", true);
            defaults.put("hour", hour(hourOptions));
            defaults.put("minute", minute(hourOptions));
            defaults.put("second", random(1, daysInMonth));
            defaults.put("millisecond", random(1, daysInMonth));
            defaults.put("american", true);
            defaults.put("string", false);

            options = initOptions(options, defaults);

            date = LocalDateTime.of(
                    (int) options.get("year"),
                    (int) options.get("month"),
                    (int) options.get("day"),
                    (int) options.get("hour"),
                    (int) options.get("minute"),
                    (int) options.get("second"),
                    (int) options.get("millisecond")
            );
        }

        if (options.get("american") != null) {
            // Adding 1 to the month is necessary because Date() 0-indexes
            // months but not day for some odd reason.
            date_string = (date.getMonthValue() + 1) + "/" + date.getDayOfMonth() + "/" + date.getYear();
        } else {
            date_string = date.getDayOfMonth() + "/" + (date.getDayOfMonth() + 1) + "/" + date.getYear();
        }

        return options.get("string") != null ? date_string : date;
    }

    ;


//    public OffsetDateTime birthday  (Map<String, Object> options) {
//         var age = age(options);
//         int currentYear = OffsetDateTime.now().getYear();
//        String type = (String) options.get("type");
//        Map<String, Object> defaults= new HashMap<>();
//        if (type!=null) {
//             var min = new Date();
//             var max = new Date();
//             min.setYear(currentYear - age - 1);
//             max.setYear(currentYear - age);
//             defaults.put("min",min);
//             defaults.put("max",max);
//
//
//             options = initOptions(options, defaults);
//         } else {
//            defaults.put("year",currentYear - age);
//             options = initOptions(options,defaults);
//         }
//
//         return date(options);
//     }
//    public String mrz(Map<String, Object> options) {
//        Function<String, Integer> checkDigit = input -> {
//
//            String[] alpha = "<ABCDEFGHIJKLMNOPQRSTUVWXYXZ".split("");
//            int[] multipliers = {7, 3, 1};
//            int runningTotal = 0;
//            int character = 0;
//            int idx = 0;
//            for (String in : input.split("")) {
//                int pos = Arrays.binarySearch(alpha, in);
//                System.out.println(pos);
//
//                if (pos != -1) {
//                    character = pos == 0 ? 0 : pos + 9;
//                } else {
//                    character = Character.getNumericValue(Integer.parseInt(in));
//                }
//                character *= multipliers[idx % multipliers.length];
//                runningTotal += character;
//                idx++;
//            }
//
//            return runningTotal % 10;
//        };
//
//
//        Function<Map<String, Object>, String> generate = (opts) -> {
//
//            Function<Integer, String> pad = (length) -> String.join("", String.valueOf(length + 1), "<");
//
//            String number = String.join("", "P<",
//                    opts.get("issuer"),
//                    opts.get("last").toString().toUpperCase(),
//                    "<<",
//                    opts.get("first").toString().toUpperCase(),
//                    pad.apply(39 - (opts.get("last").toString().length() + opts.get("first").length() + 2)),
//                    opts.get("passportNumber"),
//                    String.valueOf(checkDigit.apply(opts.get("passportNumber").toString())),
//                    opts.get("nationality"),
//                    opts.get("dob"),
//                    String.valueOf(checkDigit.apply(opts.get("dob").toString())),
//                    opts.get("gender"),
//                    opts.get("expiry"),
//                    String.valueOf(checkDigit.apply(opts.get("expiry").toString())),
//                    pad.apply(14),
//                    String.valueOf(checkDigit.apply(pad.apply(14))));
//            return Integer.valueOf(number +
//                    (checkDigit.apply(number.substring(44, 10) +
//                            number.substring(57, 7) +
//                            number.substring(65, 7))));
//        };
//        Supplier dob =  ()-> {
//            Map<String, Object> birthdayOptions= new HashMap<>();
//            birthdayOptions.put("type","adult");
//            OffsetDateTime date = birthday(birthdayOptions);
//            return String.join("",String.valueOf(date.getYear()).substring(0,2),
//                     pad(date.getMonthValue() + 1, 2,"0"),
//                    pad(date.toString(), 2,"0"));
//        };
//
//        Supplier expiry =  ()-> {
//            OffsetDateTime date = OffsetDateTime.now();
//            return String.join("",
//                    String.valueOf(date.getYear() + 5).substring(0,2),
//            pad(date.getMonthValue()+ 1, 2,""),
//                    pad(date.toString(), 2,"0"));
//        };
//         var that = this;
//         Map<String, Object> defaults= new HashMap<>();
//         defaults.put("first", first());
//                 defaults.put("last",  last());
//                         defaults.put("passportNumber", random(100000000, 999999999));
//                                 defaults.put("dob", dob.get());
//                                         defaults.put("expiry",expiry.get ());
//        defaults.put("gender", gender() =="Female" ? "F": "M");
//        defaults.put("issuer", "GBR");
//                defaults.put("nationality", "GBR");
//
//         options = initOptions(options,defaults);
//         return generate .apply(options);
//    }

//     public String name (Map<String, Object> options) {
//         options = initOptions(options);

//         var first = this.first(options),
//             last = this.last(options),
//             name;

//         if (options.middle) {
//             name = first + ' ' + this.first(options) + ' ' + last;
//         } else if (options.middle_initial) {
//             name = first + ' ' + this.character({alpha: true, casing: 'upper'}) + '. ' + last;
//         } else {
//             name = first + ' ' + last;
//         }

//         if (options.prefix) {
//             name = this.prefix(options) + ' ' + name;
//         }

//         if (options.suffix) {
//             name = name + ' ' + this.suffix(options);
//         }

//         return name;
//     };

//     // Return the list of available name prefixes based on supplied gender.
//     // @todo introduce internationalization
//     public String name_prefixes = function (gender) {
//         gender = gender || "all";
//         gender = gender.toLowerCase();

//         var prefixes = [
//             { name: 'Doctor', abbreviation: 'Dr.' }
//         ];

//         if (gender === "male" || gender === "all") {
//             prefixes.push({ name: 'Mister', abbreviation: 'Mr.' });
//         }

//         if (gender === "female" || gender === "all") {
//             prefixes.push({ name: 'Miss', abbreviation: 'Miss' });
//             prefixes.push({ name: 'Misses', abbreviation: 'Mrs.' });
//         }

//         return prefixes;
//     };

//     // Alias for name_prefix
//     public String prefix (Map<String, Object> options) {
//         return this.name_prefix(options);
//     };

//     public String name_prefix (Map<String, Object> options) {
//         options = initOptions(options, { gender: "all" });
//         return options.full ?
//             this.pick(this.name_prefixes(options.gender)).name :
//             this.pick(this.name_prefixes(options.gender)).abbreviation;
//     };
//     //Hungarian ID number
//     public String HIDN= function(){
//      //Hungarian ID nuber structure: XXXXXXYY (X=number,Y=Capital Latin letter)
//       var idn_pool="0123456789";
//       var idn_chrs="ABCDEFGHIJKLMNOPQRSTUVWXYXZ";
//       var idn="";
//         idn+=this.string({pool:idn_pool,length:6});
//         idn+=this.string({pool:idn_chrs,length:2});
//         return idn;
//     };


//     public String ssn (Map<String, Object> options) {
//         options = initOptions(options, {ssnFour: false, dashes: true});
//         var ssn_pool = "1234567890",
//             ssn,
//             dash = options.dashes ? '-' : '';

//         if(!options.ssnFour) {
//             ssn = this.string({pool: ssn_pool, length: 3}) + dash +
//             this.string({pool: ssn_pool, length: 2}) + dash +
//             this.string({pool: ssn_pool, length: 4});
//         } else {
//             ssn = this.string({pool: ssn_pool, length: 4});
//         }
//         return ssn;
//     };

//     // Aadhar is similar to ssn, used in India to uniquely identify a person
//     public String aadhar (Map<String, Object> options) {
//         options = initOptions(options, {onlyLastFour: false, separatedByWhiteSpace: true});
//         var aadhar_pool = "1234567890",
//             aadhar,
//             whiteSpace = options.separatedByWhiteSpace ? ' ' : '';

//         if(!options.onlyLastFour) {
//             aadhar = this.string({pool: aadhar_pool, length: 4}) + whiteSpace +
//             this.string({pool: aadhar_pool, length: 4}) + whiteSpace +
//             this.string({pool: aadhar_pool, length: 4});
//         } else {
//             aadhar = this.string({pool: aadhar_pool, length: 4});
//         }
//         return aadhar;
//     };

//     // Return the list of available name suffixes
//     // @todo introduce internationalization
//     public String name_suffixes = function () {
//         var suffixes = [
//             { name: 'Doctor of Osteopathic Medicine', abbreviation: 'D.O.' },
//             { name: 'Doctor of Philosophy', abbreviation: 'Ph.D.' },
//             { name: 'Esquire', abbreviation: 'Esq.' },
//             { name: 'Junior', abbreviation: 'Jr.' },
//             { name: 'Juris Doctor', abbreviation: 'J.D.' },
//             { name: 'Master of Arts', abbreviation: 'M.A.' },
//             { name: 'Master of Business Administration', abbreviation: 'M.B.A.' },
//             { name: 'Master of Science', abbreviation: 'M.S.' },
//             { name: 'Medical Doctor', abbreviation: 'M.D.' },
//             { name: 'Senior', abbreviation: 'Sr.' },
//             { name: 'The Third', abbreviation: 'III' },
//             { name: 'The Fourth', abbreviation: 'IV' },
//             { name: 'Bachelor of Engineering', abbreviation: 'B.E' },
//             { name: 'Bachelor of Technology', abbreviation: 'B.TECH' }
//         ];
//         return suffixes;
//     };

//     // Alias for name_suffix
//     public String suffix (Map<String, Object> options) {
//         return this.name_suffix(options);
//     };

//     public String name_suffix (Map<String, Object> options) {
//         options = initOptions(options);
//         return options.full ?
//             this.pick(this.name_suffixes()).name :
//             this.pick(this.name_suffixes()).abbreviation;
//     };

//     public String nationalities = function () {
//         return this.get("nationalities");
//     };

//     // Generate random nationality based on json list
//     public String nationality = function () {
//         var nationality = this.pick(this.nationalities());
//         return nationality.name;
//     };

//     // -- End Person --

//     // -- Mobile --
//     // Android GCM Registration ID
//     public String android_id = function () {
//         return "APA91" + this.string({ pool: "0123456789abcefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_", length: 178 });
//     };

//     // Apple Push Token
//     public String apple_token = function () {
//         return this.string({ pool: "abcdef1234567890", length: 64 });
//     };

//     // Windows Phone 8 ANID2
//     public String wp8_anid2 = function () {
//         return base64( this.hash( { length : 32 } ) );
//     };

//     // Windows Phone 7 ANID
//     public String wp7_anid = function () {
//         return 'A=' + this.guid().replace(/-/g, '').toUpperCase() + '&E=' + this.hash({ length:3 }) + '&W=' + this.integer({ min:0, max:9 });
//     };

//     // BlackBerry Device PIN
//     public String bb_pin = function () {
//         return this.hash({ length: 8 });
//     };

//     // -- End Mobile --

//     // -- Web --
//     public String avatar (Map<String, Object> options) {
//         var url = null;
//         var URL_BASE = '//www.gravatar.com/avatar/';
//         var PROTOCOLS = {
//             http: 'http',
//             https: 'https'
//         };
//         var FILE_TYPES = {
//             bmp: 'bmp',
//             gif: 'gif',
//             jpg: 'jpg',
//             png: 'png'
//         };
//         var FALLBACKS = {
//             '404': '404', // Return 404 if not found
//             mm: 'mm', // Mystery man
//             identicon: 'identicon', // Geometric pattern based on hash
//             monsterid: 'monsterid', // A generated monster icon
//             wavatar: 'wavatar', // A generated face
//             retro: 'retro', // 8-bit icon
//             blank: 'blank' // A transparent png
//         };
//         var RATINGS = {
//             g: 'g',
//             pg: 'pg',
//             r: 'r',
//             x: 'x'
//         };
//         var opts = {
//             protocol: null,
//             email: null,
//             fileExtension: null,
//             size: null,
//             fallback: null,
//             rating: null
//         };

//         if (!options) {
//             // Set to a random email
//             opts.email = this.email();
//             options = {};
//         }
//         else if (typeof options === 'string') {
//             opts.email = options;
//             options = {};
//         }
//         else if (typeof options !== 'object') {
//             return null;
//         }
//         else if (options.constructor === 'Array') {
//             return null;
//         }

//         opts = initOptions(options, opts);

//         if (!opts.email) {
//             // Set to a random email
//             opts.email = this.email();
//         }

//         // Safe checking for params
//         opts.protocol = PROTOCOLS[opts.protocol] ? opts.protocol + ':' : '';
//         opts.size = parseInt(opts.size, 0) ? opts.size : '';
//         opts.rating = RATINGS[opts.rating] ? opts.rating : '';
//         opts.fallback = FALLBACKS[opts.fallback] ? opts.fallback : '';
//         opts.fileExtension = FILE_TYPES[opts.fileExtension] ? opts.fileExtension : '';

//         url =
//             opts.protocol +
//             URL_BASE +
//             this.bimd5.md5(opts.email) +
//             (opts.fileExtension ? '.' + opts.fileExtension : '') +
//             (opts.size || opts.rating || opts.fallback ? '?' : '') +
//             (opts.size ? '&s=' + opts.size.toString() : '') +
//             (opts.rating ? '&r=' + opts.rating : '') +
//             (opts.fallback ? '&d=' + opts.fallback : '')
//             ;

//         return url;
//     };

//     /**
//      * #Description:
//      * ===============================================
//      * Generate random color value base on color type:
//      * -> hex
//      * -> rgb
//      * -> rgba
//      * -> 0x
//      * -> named color
//      *
//      * #Examples:
//      * ===============================================
//      * * Geerate random hex color
//      * chance.color() => '#79c157' / 'rgb(110,52,164)' / '0x67ae0b' / '#e2e2e2' / '#29CFA7'
//      *
//      * * Generate Hex based color value
//      * chance.color({format: 'hex'})    => '#d67118'
//      *
//      * * Generate simple rgb value
//      * chance.color({format: 'rgb'})    => 'rgb(110,52,164)'
//      *
//      * * Generate Ox based color value
//      * chance.color({format: '0x'})     => '0x67ae0b'
//      *
//      * * Generate graiscale based value
//      * chance.color({grayscale: true})  => '#e2e2e2'
//      *
//      * * Return valide color name
//      * chance.color({format: 'name'})   => 'red'
//      *
//      * * Make color uppercase
//      * chance.color({casing: 'upper'})  => '#29CFA7'
//      *
//      * * Min Max values for RGBA
//      * var light_red = chance.color({format: 'hex', min_red: 200, max_red: 255, max_green: 0, max_blue: 0, min_alpha: .2, max_alpha: .3});
//      *
//      * @param  [object] options
//      * @return [string] color value
//      */
//     public String color (Map<String, Object> options) {
//         function gray(value, delimiter) {
//             return [value, value, value].join(delimiter || '');
//         }

//         function rgb(hasAlpha) {
//             var rgbValue     = (hasAlpha)    ? 'rgba' : 'rgb';
//             var alphaChannel = (hasAlpha)    ? (',' + this.floating({min:min_alpha, max:max_alpha})) : "";
//             var colorValue   = (isGrayscale) ? (gray(this.natural({min: min_rgb, max: max_rgb}), ',')) : (this.natural({min: min_green, max: max_green}) + ',' + this.natural({min: min_blue, max: max_blue}) + ',' + this.natural({max: 255}));
//             return rgbValue + '(' + colorValue + alphaChannel + ')';
//         }

//         function hex(start, end, withHash) {
//             var symbol = (withHash) ? "#" : "";
//             var hexstring = "";

//             if (isGrayscale) {
//                 hexstring = gray(this.pad(this.hex({min: min_rgb, max: max_rgb}), 2));
//                 if (options.format === "shorthex") {
//                     hexstring = gray(this.hex({min: 0, max: 15}));
//                 }
//             }
//             else {
//                 if (options.format === "shorthex") {
//                     hexstring = this.pad(this.hex({min: Math.floor(min_red / 16), max: Math.floor(max_red / 16)}), 1) + this.pad(this.hex({min: Math.floor(min_green / 16), max: Math.floor(max_green / 16)}), 1) + this.pad(this.hex({min: Math.floor(min_blue / 16), max: Math.floor(max_blue / 16)}), 1);
//                 }
//                 else if (min_red !== undefined || max_red !== undefined || min_green !== undefined || max_green !== undefined || min_blue !== undefined || max_blue !== undefined) {
//                     hexstring = this.pad(this.hex({min: min_red, max: max_red}), 2) + this.pad(this.hex({min: min_green, max: max_green}), 2) + this.pad(this.hex({min: min_blue, max: max_blue}), 2);
//                 }
//                 else {
//                     hexstring = this.pad(this.hex({min: min_rgb, max: max_rgb}), 2) + this.pad(this.hex({min: min_rgb, max: max_rgb}), 2) + this.pad(this.hex({min: min_rgb, max: max_rgb}), 2);
//                 }
//             }

//             return symbol + hexstring;
//         }

//         options = initOptions(options, {
//             format: this.pick(['hex', 'shorthex', 'rgb', 'rgba', '0x', 'name']),
//             grayscale: false,
//             casing: 'lower',
//             min: 0,
//             max: 255,
//             min_red: undefined,
//             max_red: undefined,
//             min_green: undefined,
//             max_green: undefined,
//             min_blue: undefined,
//             max_blue: undefined,
//             min_alpha: 0,
//             max_alpha: 1
//         });

//         var isGrayscale = options.grayscale;
//         var min_rgb = options.min;
//         var max_rgb = options.max;
//         var min_red = options.min_red;
//         var max_red = options.max_red;
//         var min_green = options.min_green;
//         var max_green = options.max_green;
//         var min_blue = options.min_blue;
//         var max_blue = options.max_blue;
//         var min_alpha = options.min_alpha;
//         var max_alpha = options.max_alpha;
//         if (options.min_red === undefined) { min_red = min_rgb; }
//         if (options.max_red === undefined) { max_red = max_rgb; }
//         if (options.min_green === undefined) { min_green = min_rgb; }
//         if (options.max_green === undefined) { max_green = max_rgb; }
//         if (options.min_blue === undefined) { min_blue = min_rgb; }
//         if (options.max_blue === undefined) { max_blue = max_rgb; }
//         if (options.min_alpha === undefined) { min_alpha = 0; }
//         if (options.max_alpha === undefined) { max_alpha = 1; }
//         if (isGrayscale && min_rgb === 0 && max_rgb === 255 && min_red !== undefined && max_red !== undefined) {
//             min_rgb = ((min_red + min_green + min_blue) / 3);
//             max_rgb = ((max_red + max_green + max_blue) / 3);
//         }
//         var colorValue;

//         if (options.format === 'hex') {
//             colorValue = hex.call(this, 2, 6, true);
//         }
//         else if (options.format === 'shorthex') {
//             colorValue = hex.call(this, 1, 3, true);
//         }
//         else if (options.format === 'rgb') {
//             colorValue = rgb.call(this, false);
//         }
//         else if (options.format === 'rgba') {
//             colorValue = rgb.call(this, true);
//         }
//         else if (options.format === '0x') {
//             colorValue = '0x' + hex.call(this, 2, 6);
//         }
//         else if(options.format === 'name') {
//             return this.pick(this.get("colorNames"));
//         }
//         else {
//             throw new RangeError('Invalid format provided. Please provide one of "hex", "shorthex", "rgb", "rgba", "0x" or "name".');
//         }

//         if (options.casing === 'upper' ) {
//             colorValue = colorValue.toUpperCase();
//         }

//         return colorValue;
//     };

//     public String domain (Map<String, Object> options) {
//         options = initOptions(options);
//         return this.word() + '.' + (options.tld || this.tld());
//     };

//     public String email (Map<String, Object> options) {
//         options = initOptions(options);
//         return this.word({length: options.length}) + '@' + (options.domain || this.domain());
//     };

//     /**
//      * #Description:
//      * ===============================================
//      * Generate a random Facebook id, aka fbid.
//      *
//      * NOTE: At the moment (Sep 2017), Facebook ids are
//      * "numeric strings" of length 16.
//      * However, Facebook Graph API documentation states that
//      * "it is extremely likely to change over time".
//      * @see https://developers.facebook.com/docs/graph-api/overview/
//      *
//      * #Examples:
//      * ===============================================
//      * chance.fbid() => '1000035231661304'
//      *
//      * @return [string] facebook id
//      */
//     public String fbid = function () {
//         return '10000' + this.string({pool: "1234567890", length: 11});
//     };

//     public String google_analytics = function () {
//         var account = this.pad(this.natural({max: 999999}), 6);
//         var property = this.pad(this.natural({max: 99}), 2);

//         return 'UA-' + account + '-' + property;
//     };

//     public String hashtag = function () {
//         return '#' + this.word();
//     };

//     public String ip = function () {
//         // Todo: This could return some reserved IPs. See http://vq.io/137dgYy
//         // this should probably be updated to account for that rare as it may be
//         return this.natural({min: 1, max: 254}) + '.' +
//                this.natural({max: 255}) + '.' +
//                this.natural({max: 255}) + '.' +
//                this.natural({min: 1, max: 254});
//     };

//     public String ipv6 = function () {
//         var ip_addr = this.n(this.hash, 8, {length: 4});

//         return ip_addr.join(":");
//     };

//     public String klout = function () {
//         return this.natural({min: 1, max: 99});
//     };

//     public String mac (Map<String, Object> options) {
//         // Todo: This could also be extended to EUI-64 based MACs
//         // (https://www.iana.org/assignments/ethernet-numbers/ethernet-numbers.xhtml#ethernet-numbers-4)
//         // Todo: This can return some reserved MACs (similar to IP function)
//         // this should probably be updated to account for that rare as it may be
//         options = initOptions(options, { delimiter: ':' });
//         return this.pad(this.natural({max: 255}).toString(16),2) + options.delimiter +
//                this.pad(this.natural({max: 255}).toString(16),2) + options.delimiter +
//                this.pad(this.natural({max: 255}).toString(16),2) + options.delimiter +
//                this.pad(this.natural({max: 255}).toString(16),2) + options.delimiter +
//                this.pad(this.natural({max: 255}).toString(16),2) + options.delimiter +
//                this.pad(this.natural({max: 255}).toString(16),2);
//     };

//     public String semver (Map<String, Object> options) {
//         options = initOptions(options, { include_prerelease: true });

//         var range = this.pickone(["^", "~", "<", ">", "<=", ">=", "="]);
//         if (options.range) {
//             range = options.range;
//         }

//         var prerelease = "";
//         if (options.include_prerelease) {
//             prerelease = this.weighted(["", "-dev", "-beta", "-alpha"], [50, 10, 5, 1]);
//         }
//         return range + this.rpg('3d10').join('.') + prerelease;
//     };

//     public String tlds = function () {
//         return ['com', 'org', 'edu', 'gov', 'co.uk', 'net', 'io', 'ac', 'ad', 'ae', 'af', 'ag', 'ai', 'al', 'am', 'ao', 'aq', 'ar', 'as', 'at', 'au', 'aw', 'ax', 'az', 'ba', 'bb', 'bd', 'be', 'bf', 'bg', 'bh', 'bi', 'bj', 'bm', 'bn', 'bo', 'br', 'bs', 'bt', 'bv', 'bw', 'by', 'bz', 'ca', 'cc', 'cd', 'cf', 'cg', 'ch', 'ci', 'ck', 'cl', 'cm', 'cn', 'co', 'cr', 'cu', 'cv', 'cw', 'cx', 'cy', 'cz', 'de', 'dj', 'dk', 'dm', 'do', 'dz', 'ec', 'ee', 'eg', 'eh', 'er', 'es', 'et', 'eu', 'fi', 'fj', 'fk', 'fm', 'fo', 'fr', 'ga', 'gb', 'gd', 'ge', 'gf', 'gg', 'gh', 'gi', 'gl', 'gm', 'gn', 'gp', 'gq', 'gr', 'gs', 'gt', 'gu', 'gw', 'gy', 'hk', 'hm', 'hn', 'hr', 'ht', 'hu', 'id', 'ie', 'il', 'im', 'in', 'io', 'iq', 'ir', 'is', 'it', 'je', 'jm', 'jo', 'jp', 'ke', 'kg', 'kh', 'ki', 'km', 'kn', 'kp', 'kr', 'kw', 'ky', 'kz', 'la', 'lb', 'lc', 'li', 'lk', 'lr', 'ls', 'lt', 'lu', 'lv', 'ly', 'ma', 'mc', 'md', 'me', 'mg', 'mh', 'mk', 'ml', 'mm', 'mn', 'mo', 'mp', 'mq', 'mr', 'ms', 'mt', 'mu', 'mv', 'mw', 'mx', 'my', 'mz', 'na', 'nc', 'ne', 'nf', 'ng', 'ni', 'nl', 'no', 'np', 'nr', 'nu', 'nz', 'om', 'pa', 'pe', 'pf', 'pg', 'ph', 'pk', 'pl', 'pm', 'pn', 'pr', 'ps', 'pt', 'pw', 'py', 'qa', 're', 'ro', 'rs', 'ru', 'rw', 'sa', 'sb', 'sc', 'sd', 'se', 'sg', 'sh', 'si', 'sj', 'sk', 'sl', 'sm', 'sn', 'so', 'sr', 'ss', 'st', 'su', 'sv', 'sx', 'sy', 'sz', 'tc', 'td', 'tf', 'tg', 'th', 'tj', 'tk', 'tl', 'tm', 'tn', 'to', 'tp', 'tr', 'tt', 'tv', 'tw', 'tz', 'ua', 'ug', 'uk', 'us', 'uy', 'uz', 'va', 'vc', 've', 'vg', 'vi', 'vn', 'vu', 'wf', 'ws', 'ye', 'yt', 'za', 'zm', 'zw'];
//     };

//     public String tld = function () {
//         return this.pick(this.tlds());
//     };

//     public String twitter = function () {
//         return '@' + this.word();
//     };

//     public String url (Map<String, Object> options) {
//         options = initOptions(options, { protocol: "http", domain: this.domain(options), domain_prefix: "", path: this.word(), extensions: []});

//         var extension = options.extensions.length > 0 ? "." + this.pick(options.extensions) : "";
//         var domain = options.domain_prefix ? options.domain_prefix + "." + options.domain : options.domain;

//         return options.protocol + "://" + domain + "/" + options.path + extension;
//     };

//     public String port = function() {
//         return this.integer({min: 0, max: 65535});
//     };

//     public String locale (Map<String, Object> options) {
//         options = initOptions(options);
//         if (options.region){
//           return this.pick(this.get("locale_regions"));
//         } else {
//           return this.pick(this.get("locale_languages"));
//         }
//     };

//     public String locales (Map<String, Object> options) {
//       options = initOptions(options);
//       if (options.region){
//         return this.get("locale_regions");
//       } else {
//         return this.get("locale_languages");
//       }
//     };

//     public String loremPicsum (Map<String, Object> options) {
//         options = initOptions(options, { width: 500, height: 500, greyscale: false, blurred: false });

//         var greyscale = options.greyscale ? 'g/' : '';
//         var query = options.blurred ? '/?blur' : '/?random';

//         return 'https://picsum.photos/' + greyscale + options.width + '/' + options.height + query;
//     }

//     // -- End Web --

//     // -- Location --

//     public String address (Map<String, Object> options) {
//         options = initOptions(options);
//         return this.natural({min: 5, max: 2000}) + ' ' + this.street(options);
//     };

//     public String altitude (Map<String, Object> options) {
//         options = initOptions(options, {fixed: 5, min: 0, max: 8848});
//         return this.floating({
//             min: options.min,
//             max: options.max,
//             fixed: options.fixed
//         });
//     };

//     public String areacode (Map<String, Object> options) {
//         options = initOptions(options, {parens : true});
//         // Don't want area codes to start with 1, or have a 9 as the second digit
//         var areacode = options.exampleNumber ?
//         "555" :
//         this.natural({min: 2, max: 9}).toString() +
//                 this.natural({min: 0, max: 8}).toString() +
//                 this.natural({min: 0, max: 9}).toString();

//         return options.parens ? '(' + areacode + ')' : areacode;
//     };

//     public String city = function () {
//         return this.capitalize(this.word({syllables: 3}));
//     };

//     public String coordinates (Map<String, Object> options) {
//         return this.latitude(options) + ', ' + this.longitude(options);
//     };

//     public String countries = function () {
//         return this.get("countries");
//     };

//     public String country (Map<String, Object> options) {
//         options = initOptions(options);
//         var country = this.pick(this.countries());
//         return options.raw ? country : options.full ? country.name : country.abbreviation;
//     };

//     public String depth (Map<String, Object> options) {
//         options = initOptions(options, {fixed: 5, min: -10994, max: 0});
//         return this.floating({
//             min: options.min,
//             max: options.max,
//             fixed: options.fixed
//         });
//     };

//     public String geohash (Map<String, Object> options) {
//         options = initOptions(options, { length: 7 });
//         return this.string({ length: options.length, pool: '0123456789bcdefghjkmnpqrstuvwxyz' });
//     };

//     public String geojson (Map<String, Object> options) {
//         return this.latitude(options) + ', ' + this.longitude(options) + ', ' + this.altitude(options);
//     };

//     public String latitude (Map<String, Object> options) {
//         // Constants - Formats
//         var [DDM, DMS, DD] = ['ddm', 'dms', 'dd'];

//         options = initOptions(
// options,
//             options && options.format && [DDM, DMS].includes(options.format.toLowerCase()) ?
//             {min: 0, max: 89, fixed: 4} :
//             {fixed: 5, min: -90, max: 90, format: DD}
// );

//         var format = options.format.toLowerCase();

//         if (format === DDM || format === DMS) {
//             testRange(options.min < 0 || options.min > 89, "Chance: Min specified is out of range. Should be between 0 - 89");
//             testRange(options.max < 0 || options.max > 89, "Chance: Max specified is out of range. Should be between 0 - 89");
//             testRange(options.fixed > 4, 'Chance: Fixed specified should be below or equal to 4');
//         }

//         switch (format) {
//             case DDM: {
//                 return  this.integer({min: options.min, max: options.max}) + '°' +
//                         this.floating({min: 0, max: 59, fixed: options.fixed});
//             }
//             case DMS: {
//                 return  this.integer({min: options.min, max: options.max}) + '°' +
//                         this.integer({min: 0, max: 59}) + '’' +
//                         this.floating({min: 0, max: 59, fixed: options.fixed}) + '”';
//             }
//             case DD:
//             default: {
//                 return this.floating({min: options.min, max: options.max, fixed: options.fixed});
//             }
//         }
//     };

//     public String longitude (Map<String, Object> options) {
//         // Constants - Formats
//         var [DDM, DMS, DD] = ['ddm', 'dms', 'dd'];

//         options = initOptions(
// options,
//             options && options.format && [DDM, DMS].includes(options.format.toLowerCase()) ?
//             {min: 0, max: 179, fixed: 4} :
//             {fixed: 5, min: -180, max: 180, format: DD}
// );

//         var format = options.format.toLowerCase();

//         if (format === DDM || format === DMS) {
//             testRange(options.min < 0 || options.min > 179, "Chance: Min specified is out of range. Should be between 0 - 179");
//             testRange(options.max < 0 || options.max > 179, "Chance: Max specified is out of range. Should be between 0 - 179");
//             testRange(options.fixed > 4, 'Chance: Fixed specified should be below or equal to 4');
//         }

//         switch (format) {
//             case DDM: {
//                 return  this.integer({min: options.min, max: options.max}) + '°' +
//                         this.floating({min: 0, max: 59.9999, fixed: options.fixed})
//             }
//             case DMS: {
//                 return  this.integer({min: options.min, max: options.max}) + '°' +
//                         this.integer({min: 0, max: 59}) + '’' +
//                         this.floating({min: 0, max: 59.9999, fixed: options.fixed}) + '”';
//             }
//             case DD:
//             default: {
//                 return this.floating({min: options.min, max: options.max, fixed: options.fixed});
//             }
//         }
//     };

//     public String phone (Map<String, Object> options) {
//         var self = this,
//             numPick,
//             ukNum = function (parts) {
//                 var section = [];
//                 //fills the section part of the phone number with random numbers.
//                 parts.sections.forEach(function(n) {
//                     section.push(self.string({ pool: '0123456789', length: n}));
//                 });
//                 return parts.area + section.join(' ');
//             };
//         options = initOptions(options, {
//             formatted: true,
//             country: 'us',
//             mobile: false,
//             exampleNumber: false,
//         });
//         if (!options.formatted) {
//             options.parens = false;
//         }
//         var phone;
//         switch (options.country) {
//             case 'fr':
//                 if (!options.mobile) {
//                     numPick = this.pick([
//                         // Valid zone and département codes.
//                         '01' + this.pick(['30', '34', '39', '40', '41', '42', '43', '44', '45', '46', '47', '48', '49', '53', '55', '56', '58', '60', '64', '69', '70', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83']) + self.string({ pool: '0123456789', length: 6}),
//                         '02' + this.pick(['14', '18', '22', '23', '28', '29', '30', '31', '32', '33', '34', '35', '36', '37', '38', '40', '41', '43', '44', '45', '46', '47', '48', '49', '50', '51', '52', '53', '54', '56', '57', '61', '62', '69', '72', '76', '77', '78', '85', '90', '96', '97', '98', '99']) + self.string({ pool: '0123456789', length: 6}),
//                         '03' + this.pick(['10', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '39', '44', '45', '51', '52', '54', '55', '57', '58', '59', '60', '61', '62', '63', '64', '65', '66', '67', '68', '69', '70', '71', '72', '73', '80', '81', '82', '83', '84', '85', '86', '87', '88', '89', '90']) + self.string({ pool: '0123456789', length: 6}),
//                         '04' + this.pick(['11', '13', '15', '20', '22', '26', '27', '30', '32', '34', '37', '42', '43', '44', '50', '56', '57', '63', '66', '67', '68', '69', '70', '71', '72', '73', '74', '75', '76', '77', '78', '79', '80', '81', '82', '83', '84', '85', '86', '88', '89', '90', '91', '92', '93', '94', '95', '97', '98']) + self.string({ pool: '0123456789', length: 6}),
//                         '05' + this.pick(['08', '16', '17', '19', '24', '31', '32', '33', '34', '35', '40', '45', '46', '47', '49', '53', '55', '56', '57', '58', '59', '61', '62', '63', '64', '65', '67', '79', '81', '82', '86', '87', '90', '94']) + self.string({ pool: '0123456789', length: 6}),
//                         '09' + self.string({ pool: '0123456789', length: 8}),
//                     ]);
//                     phone = options.formatted ? numPick.match(/../g).join(' ') : numPick;
//                 } else {
//                     numPick = this.pick(['06', '07']) + self.string({ pool: '0123456789', length: 8});
//                     phone = options.formatted ? numPick.match(/../g).join(' ') : numPick;
//                 }
//                 break;
//             case 'uk':
//                 if (!options.mobile) {
//                     numPick = this.pick([
//                         //valid area codes of major cities/counties followed by random numbers in required format.

//                         { area: '01' + this.character({ pool: '234569' }) + '1 ', sections: [3,4] },
//                         { area: '020 ' + this.character({ pool: '378' }), sections: [3,4] },
//                         { area: '023 ' + this.character({ pool: '89' }), sections: [3,4] },
//                         { area: '024 7', sections: [3,4] },
//                         { area: '028 ' + this.pick(['25','28','37','71','82','90','92','95']), sections: [2,4] },
//                         { area: '012' + this.pick(['04','08','54','76','97','98']) + ' ', sections: [6] },
//                         { area: '013' + this.pick(['63','64','84','86']) + ' ', sections: [6] },
//                         { area: '014' + this.pick(['04','20','60','61','80','88']) + ' ', sections: [6] },
//                         { area: '015' + this.pick(['24','27','62','66']) + ' ', sections: [6] },
//                         { area: '016' + this.pick(['06','29','35','47','59','95']) + ' ', sections: [6] },
//                         { area: '017' + this.pick(['26','44','50','68']) + ' ', sections: [6] },
//                         { area: '018' + this.pick(['27','37','84','97']) + ' ', sections: [6] },
//                         { area: '019' + this.pick(['00','05','35','46','49','63','95']) + ' ', sections: [6] }
//                     ]);
//                     phone = options.formatted ? ukNum(numPick) : ukNum(numPick).replace(' ', '', 'g');
//                 } else {
//                     numPick = this.pick([
//                         { area: '07' + this.pick(['4','5','7','8','9']), sections: [2,6] },
//                         { area: '07624 ', sections: [6] }
//                     ]);
//                     phone = options.formatted ? ukNum(numPick) : ukNum(numPick).replace(' ', '');
//                 }
//                 break;
//             case 'za':
//                 if (!options.mobile) {
//                     numPick = this.pick([
//                        '01' + this.pick(['0', '1', '2', '3', '4', '5', '6', '7', '8']) + self.string({ pool: '0123456789', length: 7}),
//                        '02' + this.pick(['1', '2', '3', '4', '7', '8']) + self.string({ pool: '0123456789', length: 7}),
//                        '03' + this.pick(['1', '2', '3', '5', '6', '9']) + self.string({ pool: '0123456789', length: 7}),
//                        '04' + this.pick(['1', '2', '3', '4', '5','6','7', '8','9']) + self.string({ pool: '0123456789', length: 7}),
//                        '05' + this.pick(['1', '3', '4', '6', '7', '8']) + self.string({ pool: '0123456789', length: 7}),
//                     ]);
//                     phone = options.formatted || numPick;
//                 } else {
//                     numPick = this.pick([
//                         '060' + this.pick(['3','4','5','6','7','8','9']) + self.string({ pool: '0123456789', length: 6}),
//                         '061' + this.pick(['0','1','2','3','4','5','8']) + self.string({ pool: '0123456789', length: 6}),
//                         '06'  + self.string({ pool: '0123456789', length: 7}),
//                         '071' + this.pick(['0','1','2','3','4','5','6','7','8','9']) + self.string({ pool: '0123456789', length: 6}),
//                         '07'  + this.pick(['2','3','4','6','7','8','9']) + self.string({ pool: '0123456789', length: 7}),
//                         '08'  + this.pick(['0','1','2','3','4','5']) + self.string({ pool: '0123456789', length: 7}),
//                     ]);
//                     phone = options.formatted || numPick;
//                 }
//                 break;
//             case 'us':
//                 var areacode = this.areacode(options).toString();
//                 var exchange = this.natural({ min: 2, max: 9 }).toString() +
//                     this.natural({ min: 0, max: 9 }).toString() +
//                     this.natural({ min: 0, max: 9 }).toString();
//                 var subscriber = this.natural({ min: 1000, max: 9999 }).toString(); // this could be random [0-9]{4}
//                 phone = options.formatted ? areacode + ' ' + exchange + '-' + subscriber : areacode + exchange + subscriber;
//                 break;
//             case 'br':
//                 var areaCode = this.pick(["11", "12", "13", "14", "15", "16", "17", "18", "19", "21", "22", "24", "27", "28", "31", "32", "33", "34", "35", "37", "38", "41", "42", "43", "44", "45", "46", "47", "48", "49", "51", "53", "54", "55", "61", "62", "63", "64", "65", "66", "67", "68", "69", "71", "73", "74", "75", "77", "79", "81", "82", "83", "84", "85", "86", "87", "88", "89", "91", "92", "93", "94", "95", "96", "97", "98", "99"]);
//                 var prefix;
//                 if (options.mobile) {
//                     // Brasilian official reference (mobile): http://www.anatel.gov.br/setorregulado/plano-de-numeracao-brasileiro?id=330
//                     prefix = '9' + self.string({ pool: '0123456789', length: 4});
//                 } else {
//                     // Brasilian official reference: http://www.anatel.gov.br/setorregulado/plano-de-numeracao-brasileiro?id=331
//                     prefix = this.natural({ min: 2000, max: 5999 }).toString();
//                 }
//                 var mcdu = self.string({ pool: '0123456789', length: 4});
//                 phone = options.formatted ? '(' + areaCode + ') ' + prefix + '-' + mcdu : areaCode + prefix + mcdu;
//                 break;
//         }
//         return phone;
//     };

//     public String postal = function () {
//         // Postal District
//         var pd = this.character({pool: "XVTSRPNKLMHJGECBA"});
//         // Forward Sortation Area (FSA)
//         var fsa = pd + this.natural({max: 9}) + this.character({alpha: true, casing: "upper"});
//         // Local Delivery Unut (LDU)
//         var ldu = this.natural({max: 9}) + this.character({alpha: true, casing: "upper"}) + this.natural({max: 9});

//         return fsa + " " + ldu;
//     };

//     public String postcode = function () {
//         // Area
//         var area = this.pick(this.get("postcodeAreas")).code;
//         // District
//         var district = this.natural({max: 9});
//         // Sub-District
//         var subDistrict = this.bool() ? this.character({alpha: true, casing: "upper"}) : "";
//         // Outward Code
//         var outward = area + district + subDistrict;
//         // Sector
//         var sector = this.natural({max: 9});
//         // Unit
//         var unit = this.character({alpha: true, casing: "upper"}) + this.character({alpha: true, casing: "upper"});
//         // Inward Code
//         var inward = sector + unit;

//         return outward + " " + inward;
//     };

//     public String counties (Map<String, Object> options) {
//         options = initOptions(options, { country: 'uk' });
//         return this.get("counties")[options.country.toLowerCase()];
//     };

//     public String county (Map<String, Object> options) {
//         return this.pick(this.counties(options)).name;
//     };

//     public String provinces (Map<String, Object> options) {
//         options = initOptions(options, { country: 'ca' });
//         return this.get("provinces")[options.country.toLowerCase()];
//     };

//     public String province (Map<String, Object> options) {
//         return (options && options.full) ?
//             this.pick(this.provinces(options)).name :
//             this.pick(this.provinces(options)).abbreviation;
//     };

//     public String state (Map<String, Object> options) {
//         return (options && options.full) ?
//             this.pick(this.states(options)).name :
//             this.pick(this.states(options)).abbreviation;
//     };

//     public String states (Map<String, Object> options) {
//         options = initOptions(options, { country: 'us', us_states_and_dc: true } );

//         var states;

//         switch (options.country.toLowerCase()) {
//             case 'us':
//                 var us_states_and_dc = this.get("us_states_and_dc"),
//                     territories = this.get("territories"),
//                     armed_forces = this.get("armed_forces");

//                 states = [];

//                 if (options.us_states_and_dc) {
//                     states = states.concat(us_states_and_dc);
//                 }
//                 if (options.territories) {
//                     states = states.concat(territories);
//                 }
//                 if (options.armed_forces) {
//                     states = states.concat(armed_forces);
//                 }
//                 break;
//             case 'it':
//             case 'mx':
//                 states = this.get("country_regions")[options.country.toLowerCase()];
//                 break;
//             case 'uk':
//                 states = this.get("counties")[options.country.toLowerCase()];
//                 break;
//         }

//         return states;
//     };

//     public String street (Map<String, Object> options) {
//         options = initOptions(options, { country: 'us', syllables: 2 });
//         var     street;

//         switch (options.country.toLowerCase()) {
//             case 'us':
//                 street = this.word({ syllables: options.syllables });
//                 street = this.capitalize(street);
//                 street += ' ';
//                 street += options.short_suffix ?
//                     this.street_suffix(options).abbreviation :
//                     this.street_suffix(options).name;
//                 break;
//             case 'it':
//                 street = this.word({ syllables: options.syllables });
//                 street = this.capitalize(street);
//                 street = (options.short_suffix ?
//                     this.street_suffix(options).abbreviation :
//                     this.street_suffix(options).name) + " " + street;
//                 break;
//         }
//         return street;
//     };

//     public String street_suffix (Map<String, Object> options) {
//         options = initOptions(options, { country: 'us' });
//         return this.pick(this.street_suffixes(options));
//     };

//     public String street_suffixes (Map<String, Object> options) {
//         options = initOptions(options, { country: 'us' });
//         // These are the most common suffixes.
//         return this.get("street_suffixes")[options.country.toLowerCase()];
//     };

//     // Note: only returning US zip codes, internationalization will be a whole
//     // other beast to tackle at some point.
//     public String zip (Map<String, Object> options) {
//         var zip = this.n(this.natural, 5, {max: 9});

//         if (options && options.plusfour === true) {
//             zip.push('-');
//             zip = zip.concat(this.n(this.natural, 4, {max: 9}));
//         }

//         return zip.join("");
//     };

//     // -- End Location --

//     // -- Time

//     public String ampm = function () {
//         return this.bool() ? 'am' : 'pm';
//     };

//     public String date (Map<String, Object> options) {
//         var date_string, date;

//         // If interval is specified we ignore preset
//         if(options && (options.min || options.max)) {
//             options = initOptions(options, {
//                 american: true,
//                 string: false
//             });
//             var min = typeof options.min !== "undefined" ? options.min.getTime() : 1;
//             // 100,000,000 days measured relative to midnight at the beginning of 01 January, 1970 UTC. http://es5.github.io/#x15.9.1.1
//             var max = typeof options.max !== "undefined" ? options.max.getTime() : 8640000000000000;

//             date = new Date(this.integer({min: min, max: max}));
//         } else {
//             var m = this.month({raw: true});
//             var daysInMonth = m.days;

//             if(options && options.month) {
//                 // Mod 12 to allow months outside range of 0-11 (not encouraged, but also not prevented).
//                 daysInMonth = this.get('months')[((options.month % 12) + 12) % 12].days;
//             }

//             options = initOptions(options, {
//                 year: parseInt(this.year(), 10),
//                 // Necessary to subtract 1 because Date() 0-indexes month but not day or year
//                 // for some reason.
//                 month: m.numeric - 1,
//                 day: this.natural({min: 1, max: daysInMonth}),
//                 hour: this.hour({twentyfour: true}),
//                 minute: this.minute(),
//                 second: this.second(),
//                 millisecond: this.millisecond(),
//                 american: true,
//                 string: false
//             });

//             date = new Date(options.year, options.month, options.day, options.hour, options.minute, options.second, options.millisecond);
//         }

//         if (options.american) {
//             // Adding 1 to the month is necessary because Date() 0-indexes
//             // months but not day for some odd reason.
//             date_string = (date.getMonth() + 1) + '/' + date.getDate() + '/' + date.getFullYear();
//         } else {
//             date_string = date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear();
//         }

//         return options.string ? date_string : date;
//     };

//     public String hammertime (Map<String, Object> options) {
//         return this.date(options).getTime();
//     };

//     public String hour (Map<String, Object> options) {
//         options = initOptions(options, {
//             min: options && options.twentyfour ? 0 : 1,
//             max: options && options.twentyfour ? 23 : 12
//         });

//         testRange(options.min < 0, "Chance: Min cannot be less than 0.");
//         testRange(options.twentyfour && options.max > 23, "Chance: Max cannot be greater than 23 for twentyfour option.");
//         testRange(!options.twentyfour && options.max > 12, "Chance: Max cannot be greater than 12.");
//         testRange(options.min > options.max, "Chance: Min cannot be greater than Max.");

//         return this.natural({min: options.min, max: options.max});
//     };

//     public String millisecond = function () {
//         return this.natural({max: 999});
//     };

//     public String minute = public String second (Map<String, Object> options) {
//         options = initOptions(options, {min: 0, max: 59});

//         testRange(options.min < 0, "Chance: Min cannot be less than 0.");
//         testRange(options.max > 59, "Chance: Max cannot be greater than 59.");
//         testRange(options.min > options.max, "Chance: Min cannot be greater than Max.");

//         return this.natural({min: options.min, max: options.max});
//     };

//     public String month (Map<String, Object> options) {
//         options = initOptions(options, {min: 1, max: 12});

//         testRange(options.min < 1, "Chance: Min cannot be less than 1.");
//         testRange(options.max > 12, "Chance: Max cannot be greater than 12.");
//         testRange(options.min > options.max, "Chance: Min cannot be greater than Max.");

//         var month = this.pick(this.months().slice(options.min - 1, options.max));
//         return options.raw ? month : month.name;
//     };

//     public String months = function () {
//         return this.get("months");
//     };

//     public String second = function () {
//         return this.natural({max: 59});
//     };

//     public String timestamp = function () {
//         return this.natural({min: 1, max: parseInt(new Date().getTime() / 1000, 10)});
//     };

//     public String weekday (Map<String, Object> options) {
//         options = initOptions(options, {weekday_only: false});
//         var weekdays = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
//         if (!options.weekday_only) {
//             weekdays.push("Saturday");
//             weekdays.push("Sunday");
//         }
//         return this.pickone(weekdays);
//     };

//     public String year (Map<String, Object> options) {
//         // Default to current year as min if none specified
//         options = initOptions(options, {min: new Date().getFullYear()});

//         // Default to one century after current year as max if none specified
//         options.max = (typeof options.max !== "undefined") ? options.max : options.min + 100;

//         return this.natural(options).toString();
//     };

//     // -- End Time

//     // -- Finance --

//     public String cc (Map<String, Object> options) {
//         options = initOptions(options);

//         var type, number, to_generate;

//         type = (options.type) ?
//                     this.cc_type({ name: options.type, raw: true }) :
//                     this.cc_type({ raw: true });

//         number = type.prefix.split("");
//         to_generate = type.length - type.prefix.length - 1;

//         // Generates n - 1 digits
//         number = number.concat(this.n(this.integer, to_generate, {min: 0, max: 9}));

//         // Generates the last digit according to Luhn algorithm
//         number.push(this.luhn_calculate(number.join("")));

//         return number.join("");
//     };

//     public String cc_types = function () {
//         // http://en.wikipedia.org/wiki/Bank_card_number#Issuer_identification_number_.28IIN.29
//         return this.get("cc_types");
//     };

//     public String cc_type (Map<String, Object> options) {
//         options = initOptions(options);
//         var types = this.cc_types(),
//             type = null;

//         if (options.name) {
//             for (var i = 0; i < types.length; i++) {
//                 // Accept either name or short_name to specify card type
//                 if (types[i].name === options.name || types[i].short_name === options.name) {
//                     type = types[i];
//                     break;
//                 }
//             }
//             if (type === null) {
//                 throw new RangeError("Chance: Credit card type '" + options.name + "' is not supported");
//             }
//         } else {
//             type = this.pick(types);
//         }

//         return options.raw ? type : type.name;
//     };

//     // return all world currency by ISO 4217
//     public String currency_types = function () {
//         return this.get("currency_types");
//     };

//     // return random world currency by ISO 4217
//     public String currency = function () {
//         return this.pick(this.currency_types());
//     };

//     // return all timezones available
//     public String timezones = function () {
//         return this.get("timezones");
//     };

//     // return random timezone
//     public String timezone = function () {
//         return this.pick(this.timezones());
//     };

//     //Return random correct currency exchange pair (e.g. EUR/USD) or array of currency code
//     public String currency_pair = function (returnAsString) {
//         var currencies = this.unique(this.currency, 2, {
//             comparator: function(arr, val) {

//                 return arr.reduce(function(acc, item) {
//                     // If a match has been found, short circuit check and just return
//                     return acc || (item.code === val.code);
//                 }, false);
//             }
//         });

//         if (returnAsString) {
//             return currencies[0].code + '/' + currencies[1].code;
//         } else {
//             return currencies;
//         }
//     };

//     public String dollar (Map<String, Object> options) {
//         // By default, a somewhat more sane max for dollar than all available numbers
//         options = initOptions(options, {max : 10000, min : 0});

//         var dollar = this.floating({min: options.min, max: options.max, fixed: 2}).toString(),
//             cents = dollar.split('.')[1];

//         if (cents === undefined) {
//             dollar += '.00';
//         } else if (cents.length < 2) {
//             dollar = dollar + '0';
//         }

//         if (dollar < 0) {
//             return '-$' + dollar.replace('-', '');
//         } else {
//             return '$' + dollar;
//         }
//     };

//     public String euro (Map<String, Object> options) {
//         return Number(this.dollar(options).replace("$", "")).toLocaleString() + "€";
//     };

//     public String exp (Map<String, Object> options) {
//         options = initOptions(options);
//         var exp = {};

//         exp.year = this.exp_year();

//         // If the year is this year, need to ensure month is greater than the
//         // current month or this expiration will not be valid
//         if (exp.year === (new Date().getFullYear()).toString()) {
//             exp.month = this.exp_month({future: true});
//         } else {
//             exp.month = this.exp_month();
//         }

//         return options.raw ? exp : exp.month + '/' + exp.year;
//     };

//     public String exp_month (Map<String, Object> options) {
//         options = initOptions(options);
//         var month, month_int,
//             // Date object months are 0 indexed
//             curMonth = new Date().getMonth() + 1;

//         if (options.future && (curMonth !== 12)) {
//             do {
//                 month = this.month({raw: true}).numeric;
//                 month_int = parseInt(month, 10);
//             } while (month_int <= curMonth);
//         } else {
//             month = this.month({raw: true}).numeric;
//         }

//         return month;
//     };

//     public String exp_year = function () {
//         var curMonth = new Date().getMonth() + 1,
//             curYear = new Date().getFullYear();

//         return this.year({min: ((curMonth === 12) ? (curYear + 1) : curYear), max: (curYear + 10)});
//     };

//     public String vat (Map<String, Object> options) {
//         options = initOptions(options, { country: 'it' });
//         switch (options.country.toLowerCase()) {
//             case 'it':
//                 return this.it_vat();
//         }
//     };

//     /**
//      * Generate a string matching IBAN pattern (https://en.wikipedia.org/wiki/International_Bank_Account_Number).
//      * No country-specific formats support (yet)
//      */
//     public String iban = function () {
//         var alpha = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
//         var alphanum = alpha + '0123456789';
//         var iban =
//             this.string({ length: 2, pool: alpha }) +
//             this.pad(this.integer({ min: 0, max: 99 }), 2) +
//             this.string({ length: 4, pool: alphanum }) +
//             this.pad(this.natural(), this.natural({ min: 6, max: 26 }));
//         return iban;
//     };

}