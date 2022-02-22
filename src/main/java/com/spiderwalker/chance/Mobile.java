package com.spiderwalker.chance;

import java.util.*;

public class Mobile {
    Chance chance = new Chance();

    public String ukNum(String area, List<Integer> sections) {

        List section = new ArrayList<>();
        for (Integer sect : sections) {
            Map<String, Object> opts = new HashMap<>();
            opts.put("pool", "0123456789");
            opts.put("length", sect);
            section.add(chance.string(opts));
        }
        return area + String.join(" ", section);
    }

    public String phone(Map<String, Object> options) {
        String phone = null;

        String numPick;
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("formatted", true);
        defaults.put("country", "us");
        defaults.put("mobile", false);
        defaults.put("exampleNumber", false);
//
        options = chance.initOptions(options, defaults);
        if ((boolean) chance.get(options, "formatted")) {
            options.put("parens", false);
        }
        String pool = "0123456789";
        switch (options.get("country").toString()) {
            case "fr":
                if (!(boolean) options.get("mobile")) {
                    numPick = chance.pickone(Arrays.asList(
                            // Valid zone and département codes.
                            "01" + chance.pickone(Arrays.asList("30", "34", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "53", "55", "56", "58", "60", "64", "69", "70", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83")) + chance.string(pool, 6),
                            "02" + chance.pickone(Arrays.asList("14", "18", "22", "23", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "40", "41", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "56", "57", "61", "62", "69", "72", "76", "77", "78", "85", "90", "96", "97", "98", "99")) + chance.string(pool, 6),
                            "03" + chance.pickone(Arrays.asList("10", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "39", "44", "45", "51", "52", "54", "55", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90")) + chance.string(pool, 6),
                            "04" + chance.pickone(Arrays.asList("11", "13", "15", "20", "22", "26", "27", "30", "32", "34", "37", "42", "43", "44", "50", "56", "57", "63", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "88", "89", "90", "91", "92", "93", "94", "95", "97", "98")) + chance.string(pool, 6),
                            "05" + chance.pickone(Arrays.asList("08", "16", "17", "19", "24", "31", "32", "33", "34", "35", "40", "45", "46", "47", "49", "53", "55", "56", "57", "58", "59", "61", "62", "63", "64", "65", "67", "79", "81", "82", "86", "87", "90", "94")) + chance.string(pool, 6),
                            "09" + chance.string(pool, 8)
                    ));
                    phone = (boolean) options.get("formatted") ? numPick : numPick;
                } else {
                    numPick = chance.pickone(Arrays.asList("06", "07")) + chance.string(pool, 8);
                    phone = (boolean) options.get("formatted") ? numPick : numPick;
                }
                break;
            case "uk":
                if (!(boolean) chance.get(options, "mobile")) {
                    numPick = chance.pickone(
                            List.of(
                                    ukNum("01" + chance.character("234569") + "1 ", List.of(3, 4)),
                                    ukNum("020" + chance.character("378") + "1 ", List.of(3, 4)),
                                    ukNum("023" + chance.character("89") + "1 ", List.of(3, 4)),
                                    ukNum("024 7", List.of(3, 4)),
                                    ukNum("028 " + chance.pickone(List.of("25", "28", "37", "71", "82", "90", "92", "95")), List.of(2, 4)),
                                    ukNum("012" + chance.pickone(List.of("04", "08", "54", "76", "97", "98")) + " ", List.of(6)),
                                    ukNum("013" + chance.pickone(List.of("63", "64", "84", "86")) + " ", List.of(6)),
                                    ukNum("014" + chance.pickone(List.of("04", "20", "60", "61", "80", "88")) + " ", List.of(6)),
                                    ukNum("015" + chance.pickone(List.of("24", "27", "62", "66")) + " ", List.of(6)),
                                    ukNum("016" + chance.pickone(List.of("06", "29", "35", "47", "59", "95")) + " ", List.of(6)),
                                    ukNum("017" + chance.pickone(List.of("26", "44", "50", "68")) + " ", List.of(6)),
                                    ukNum("018" + chance.pickone(List.of("27", "37", "84", "97")) + " ", List.of(6)),
                                    ukNum("019" + chance.pickone(List.of("00", "05", "35", "46", "49", "63", "95")) + " ", List.of(6))
                            )
                    );
                    //valid area codes of major cities/counties followed by random numbers in required format.


                    phone = (boolean) chance.get(options, "formatted") ? ukNum(numPick, new ArrayList<>()) : ukNum(numPick, new ArrayList<>()).replace(" ", "");
                } else {
//                    numPick = chance.pickone([
//                            {area:"07" + chance.pickone(["4", "5", "7", "8", "9"]),sections: [2, 6] },
//                    {
//                        area:
//                        "07624 ", sections: [6]}
//                     ]);
//                    phone = options.formatted ? ukNum(numPick) : ukNum(numPick).replace(" ", "");
                }
                break;
            case "za":
//                if (!options.mobile) {
//                    numPick = chance.pickone([
//                            "01" + chance.pickone(["0", "1", "2", "3", "4", "5", "6", "7", "8"])+self.string({pool:
//                    "0123456789", length:7}),
//                    "02" + chance.pickone(["1", "2", "3", "4", "7", "8"])+self.string({pool:"0123456789", length:7}),
//                    "03" + chance.pickone(["1", "2", "3", "5", "6", "9"])+self.string({pool:"0123456789", length:7}),
//                    "04" + chance.pickone(["1", "2", "3", "4", "5", "6", "7", "8", "9"])+self.string({pool:
//                    "0123456789", length:7}),
//                    "05" + chance.pickone(["1", "3", "4", "6", "7", "8"])+self.string({pool:"0123456789", length:7}),
//                     ]);
//                    phone = options.formatted || numPick;
//                } else {
//                    numPick = chance.pickone([
//                            "060" + chance.pickone(["3", "4", "5", "6", "7", "8", "9"])+self.string({pool:
//                    "0123456789", length:6}),
//                    "061" + chance.pickone(["0", "1", "2", "3", "4", "5", "8"])+self.string({pool:"0123456789", length:6}),
//                    "06" + self.string({pool:"0123456789", length:7}),
//                    "071" + chance.pickone(["0", "1", "2", "3", "4", "5", "6", "7", "8", "9"])+self.string({pool:
//                    "0123456789", length:6}),
//                    "07" + chance.pickone(["2", "3", "4", "6", "7", "8", "9"])+self.string({pool:"0123456789", length:7}),
//                    "08" + chance.pickone(["0", "1", "2", "3", "4", "5"])+self.string({pool:"0123456789", length:7}),
//                     ]);
//                    phone = options.formatted || numPick;
//                }
                break;
            case "us":
//                var areacode = chance.areacode(options).toString();
//                var exchange = chance.natural({min:2, max:9 }).toString() +
//                    chance.natural({min:0, max:9 }).toString() +
//                    chance.natural({min:0, max:9 }).toString();
//                var subscriber = chance.natural({min:1000, max:9999 }).toString(); // chance could be random [0-9]{4}
//                phone = options.formatted ? areacode + " " + exchange + "-" + subscriber : areacode + exchange + subscriber;
                break;
            case "br":
//                var areaCode = chance.pickone(["11", "12", "13", "14", "15", "16", "17", "18", "19", "21", "22", "24", "27", "28", "31", "32", "33", "34", "35", "37", "38", "41", "42", "43", "44", "45", "46", "47", "48", "49", "51", "53", "54", "55", "61", "62", "63", "64", "65", "66", "67", "68", "69", "71", "73", "74", "75", "77", "79", "81", "82", "83", "84", "85", "86", "87", "88", "89", "91", "92", "93", "94", "95", "96", "97", "98", "99"]);
//                var prefix;
//                if (options.mobile) {
//                    // Brasilian official reference (mobile): http://www.anatel.gov.br/setorregulado/plano-de-numeracao-brasileiro?id=330
//                    prefix = "9" + self.string({pool:"0123456789", length:4});
//                } else {
//                    // Brasilian official reference: http://www.anatel.gov.br/setorregulado/plano-de-numeracao-brasileiro?id=331
//                    prefix = chance.natural({min:2000, max:5999 }).toString();
//                }
//                var mcdu = chance.string({pool:"0123456789", length:4});
//                phone = options.formatted ? "(" + areaCode + ") " + prefix + "-" + mcdu : areaCode + prefix + mcdu;
                break;
        }
        return phone;
    }
}

