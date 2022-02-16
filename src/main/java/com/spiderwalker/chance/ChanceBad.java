package com.spiderwalker.chance;

import java.util.Map;

//  Chance.js 1.1.8
//  http://chancejs.com
//  (c) 2013 Victor Quinn
//  Chance may be freely distributed or modified under the MIT license.
public class ChanceBad{
  

    public String VERSION = "1.1.8";

    // Random helper functions
    

    /**
     * Encode the input string with Base64.
     */
    // var base64 = function() {
    //     throw new Error('No Base64 encoder available.');
    // };

    // Select proper Base64 encoder.
   /* (function determineBase64Encoder() {
        if (typeof btoa === 'function') {
            base64 = btoa;
        } else if (typeof Buffer === 'function') {
            base64 = function(input) {
                return new Buffer(input).toString('base64');
            };
        }
    })();
    */

    // -- Basics --

    
    

//     Chance.prototype.falsy = function (options) {
//         // return a random falsy value
//         options = initOptions(options, {pool: [false, null, 0, NaN, '', undefined]})
//         var pool = options.pool,
//             index = this.integer({min: 0, max: pool.length - 1}),
//             value = pool[index];

//         return value;
//     }






//     CopyToken.prototype = {
//         substitute: function () {
//             return this.c
//         }
//     }

//     function EscapeToken(c) {
//         this.c = c
//     }

//     EscapeToken.prototype = {
//         substitute: function () {
//             if (!/[{}\\]/.test(this.c)) {
//                 throw new Error('Invalid escape sequence: "\\' + this.c + '".')
//             }
//             return this.c
//         }
//     }

//     function ReplaceToken(c) {
//         this.c = c
//     }

//     ReplaceToken.prototype = {
//         replacers: {
//             '#': function (chance) { return chance.character({ pool: NUMBERS }) },
//             'A': function (chance) { return chance.character({ pool: CHARS_UPPER }) },
//             'a': function (chance) { return chance.character({ pool: CHARS_LOWER }) },
//         },

//         substitute: function (chance) {
//             var replacer = this.replacers[this.c]
//             if (!replacer) {
//                 throw new Error('Invalid replacement character: "' + this.c + '".')
//             }
//             return replacer(chance)
//         }
//     }

//     function parseTemplate(template) {
//         var tokens = []
//         var mode = 'identity'
//         for (var i = 0; i<template.length; i++) {
//             var c = template[i]
//             switch (mode) {
//                 case 'escape':
//                     tokens.push(new EscapeToken(c))
//                     mode = 'identity'
//                     break
//                 case 'identity':
//                     if (c === '{') {
//                         mode = 'replace'
//                     } else if (c === '\\') {
//                         mode = 'escape'
//                     } else {
//                         tokens.push(new CopyToken(c))
//                     }
//                     break
//                 case 'replace':
//                     if (c === '}') {
//                         mode = 'identity'
//                     } else {
//                         tokens.push(new ReplaceToken(c))
//                     }
//                     break
//             }
//         }
//         return tokens
//     }

//     /**
//      *  Return a random string matching the given template.
//      *
//      *  The template consists of any number of "character replacement" and
//      *  "character literal" sequences. A "character replacement" sequence
//      *  starts with a left brace, has any number of special replacement
//      *  characters, and ends with a right brace. A character literal can be any
//      *  character except a brace or a backslash. A literal brace or backslash
//      *  character can be included in the output by escaping with a backslash.
//      *
//      *  The following replacement characters can be used in a replacement
//      *  sequence:
//      *
//      *      "#": a random digit
//      *      "a": a random lower case letter
//      *      "A": a random upper case letter
//      *
//      *  Example: chance.template('{AA###}-{##}')
//      *
//      *  @param {String} template string.
//      *  @returns {String} a random string matching the template.
//      */
//     Chance.prototype.template = function (template) {
//         if (!template) {
//             throw new Error('Template string is required')
//         }
//         var self = this
//         return parseTemplate(template)
//             .map(function (token) { return token.substitute(self) })
//             .join('');
//     };


//     /**
//      *  Return a random buffer
//      *
//      *  @param {Object} [options={}] can specify a length
//      *  @returns {Buffer} a buffer of random length
//      *  @throws {RangeError} length cannot be less than zero
//      */
//     Chance.prototype.buffer = function (options) {
//         if (typeof Buffer === 'undefined') {
//             throw new UnsupportedError('Sorry, the buffer() function is not supported on your platform');
//         }
//         options = initOptions(options, { length: this.natural({min: 5, max: 20}) });
//         testRange(options.length < 0, "Chance: Length cannot be less than zero.");
//         var length = options.length;
//         var content = this.n(this.character, length, options);

//         return Buffer.from(content);
//     };

//     // -- End Basics --

//     // -- Helpers --

//     Chance.prototype.capitalize = function (word) {
//         return word.charAt(0).toUpperCase() + word.substr(1);
//     };

//     Chance.prototype.mixin = function (obj) {
//         for (var func_name in obj) {
//             Chance.prototype[func_name] = obj[func_name];
//         }
//         return this;
//     };

//     /**
//      *  Given a function that generates something random and a number of items to generate,
//      *    return an array of items where none repeat.
//      *
//      *  @param {Function} fn the function that generates something random
//      *  @param {Number} num number of terms to generate
//      *  @param {Object} options any options to pass on to the generator function
//      *  @returns {Array} an array of length `num` with every item generated by `fn` and unique
//      *
//      *  There can be more parameters after these. All additional parameters are provided to the given function
//      */
//     Chance.prototype.unique = function(fn, num, options) {
//         testRange(
//             typeof fn !== "function",
//             "Chance: The first argument must be a function."
//         );

//         var comparator = function(arr, val) { return arr.indexOf(val) !== -1; };

//         if (options) {
//             comparator = options.comparator || comparator;
//         }

//         var arr = [], count = 0, result, MAX_DUPLICATES = num * 50, params = slice.call(arguments, 2);

//         while (arr.length < num) {
//             var clonedParams = JSON.parse(JSON.stringify(params));
//             result = fn.apply(this, clonedParams);
//             if (!comparator(arr, result)) {
//                 arr.push(result);
//                 // reset count when unique found
//                 count = 0;
//             }

//             if (++count > MAX_DUPLICATES) {
//                 throw new RangeError("Chance: num is likely too large for sample set");
//             }
//         }
//         return arr;
//     };



//     // -- End Helpers --

//     // -- Text --

//     Chance.prototype.paragraph = function (options) {
//         options = initOptions(options);

//         var sentences = options.sentences || this.natural({min: 3, max: 7}),
//             sentence_array = this.n(this.sentence, sentences),
//             separator = options.linebreak === true ? '\n' : ' ';

//         return sentence_array.join(separator);
//     };

//     // Could get smarter about this than generating random words and
//     // chaining them together. Such as: http://vq.io/1a5ceOh
//     Chance.prototype.sentence = function (options) {
//         options = initOptions(options);

//         var words = options.words || this.natural({min: 12, max: 18}),
//             punctuation = options.punctuation,
//             text, word_array = this.n(this.word, words);

//         text = word_array.join(' ');

//         // Capitalize first letter of sentence
//         text = this.capitalize(text);

//         // Make sure punctuation has a usable value
//         if (punctuation !== false && !/^[.?;!:]$/.test(punctuation)) {
//             punctuation = '.';
//         }

//         // Add punctuation mark
//         if (punctuation) {
//             text += punctuation;
//         }

//         return text;
//     };

//     Chance.prototype.syllable = function (options) {
//         options = initOptions(options);

//         var length = options.length || this.natural({min: 2, max: 3}),
//             consonants = 'bcdfghjklmnprstvwz', // consonants except hard to speak ones
//             vowels = 'aeiou', // vowels
//             all = consonants + vowels, // all
//             text = '',
//             chr;

//         // I'm sure there's a more elegant way to do this, but this works
//         // decently well.
//         for (var i = 0; i < length; i++) {
//             if (i === 0) {
//                 // First character can be anything
//                 chr = this.character({pool: all});
//             } else if (consonants.indexOf(chr) === -1) {
//                 // Last character was a vowel, now we want a consonant
//                 chr = this.character({pool: consonants});
//             } else {
//                 // Last character was a consonant, now we want a vowel
//                 chr = this.character({pool: vowels});
//             }

//             text += chr;
//         }

//         if (options.capitalize) {
//             text = this.capitalize(text);
//         }

//         return text;
//     };

//     Chance.prototype.word = function (options) {
//         options = initOptions(options);

//         testRange(
//             options.syllables && options.length,
//             "Chance: Cannot specify both syllables AND length."
//         );

//         var syllables = options.syllables || this.natural({min: 1, max: 3}),
//             text = '';

//         if (options.length) {
//             // Either bound word by length
//             do {
//                 text += this.syllable();
//             } while (text.length < options.length);
//             text = text.substring(0, options.length);
//         } else {
//             // Or by number of syllables
//             for (var i = 0; i < syllables; i++) {
//                 text += this.syllable();
//             }
//         }

//         if (options.capitalize) {
//             text = this.capitalize(text);
//         }

//         return text;
//     };

//     // -- End Text --

//     // -- Person --

//     Chance.prototype.age = function (options) {
//         options = initOptions(options);
//         var ageRange;

//         switch (options.type) {
//             case 'child':
//                 ageRange = {min: 0, max: 12};
//                 break;
//             case 'teen':
//                 ageRange = {min: 13, max: 19};
//                 break;
//             case 'adult':
//                 ageRange = {min: 18, max: 65};
//                 break;
//             case 'senior':
//                 ageRange = {min: 65, max: 100};
//                 break;
//             case 'all':
//                 ageRange = {min: 0, max: 100};
//                 break;
//             default:
//                 ageRange = {min: 18, max: 65};
//                 break;
//         }

//         return this.natural(ageRange);
//     };

//     Chance.prototype.birthday = function (options) {
//         var age = this.age(options);
//         var currentYear = new Date().getFullYear();

//         if (options && options.type) {
//             var min = new Date();
//             var max = new Date();
//             min.setFullYear(currentYear - age - 1);
//             max.setFullYear(currentYear - age);

//             options = initOptions(options, {
//                 min: min,
//                 max: max
//             });
//         } else {
//             options = initOptions(options, {
//                 year: currentYear - age
//             });
//         }

//         return this.date(options);
//     };

//     // CPF; ID to identify taxpayers in Brazil
//     Chance.prototype.cpf = function (options) {
//         options = initOptions(options, {
//             formatted: true
//         });

//         var n = this.n(this.natural, 9, { max: 9 });
//         var d1 = n[8]*2+n[7]*3+n[6]*4+n[5]*5+n[4]*6+n[3]*7+n[2]*8+n[1]*9+n[0]*10;
//         d1 = 11 - (d1 % 11);
//         if (d1>=10) {
//             d1 = 0;
//         }
//         var d2 = d1*2+n[8]*3+n[7]*4+n[6]*5+n[5]*6+n[4]*7+n[3]*8+n[2]*9+n[1]*10+n[0]*11;
//         d2 = 11 - (d2 % 11);
//         if (d2>=10) {
//             d2 = 0;
//         }
//         var cpf = ''+n[0]+n[1]+n[2]+'.'+n[3]+n[4]+n[5]+'.'+n[6]+n[7]+n[8]+'-'+d1+d2;
//         return options.formatted ? cpf : cpf.replace(/\D/g,'');
//     };

//     // CNPJ: ID to identify companies in Brazil
//     Chance.prototype.cnpj = function (options) {
//         options = initOptions(options, {
//             formatted: true
//         });

//         var n = this.n(this.natural, 12, { max: 12 });
//         var d1 = n[11]*2+n[10]*3+n[9]*4+n[8]*5+n[7]*6+n[6]*7+n[5]*8+n[4]*9+n[3]*2+n[2]*3+n[1]*4+n[0]*5;
//         d1 = 11 - (d1 % 11);
//         if (d1<2) {
//             d1 = 0;
//         }
//         var d2 = d1*2+n[11]*3+n[10]*4+n[9]*5+n[8]*6+n[7]*7+n[6]*8+n[5]*9+n[4]*2+n[3]*3+n[2]*4+n[1]*5+n[0]*6;
//         d2 = 11 - (d2 % 11);
//         if (d2<2) {
//             d2 = 0;
//         }
//         var cnpj = ''+n[0]+n[1]+'.'+n[2]+n[3]+n[4]+'.'+n[5]+n[6]+n[7]+'/'+n[8]+n[9]+n[10]+n[11]+'-'+d1+d2;
//         return options.formatted ? cnpj : cnpj.replace(/\D/g,'');
//     };

//     Chance.prototype.first = function (options) {
//         options = initOptions(options, {gender: this.gender(), nationality: 'en'});
//         return this.pick(this.get("firstNames")[options.gender.toLowerCase()][options.nationality.toLowerCase()]);
//     };

//     Chance.prototype.profession = function (options) {
//         options = initOptions(options);
//         if(options.rank){
//             return this.pick(['Apprentice ', 'Junior ', 'Senior ', 'Lead ']) + this.pick(this.get("profession"));
//         } else{
//             return this.pick(this.get("profession"));
//         }
//     };

//     Chance.prototype.company = function (){
//         return this.pick(this.get("company"));
//     };

//     Chance.prototype.gender = function (options) {
//         options = initOptions(options, {extraGenders: []});
//         return this.pick(['Male', 'Female'].concat(options.extraGenders));
//     };

//     Chance.prototype.last = function (options) {
//       options = initOptions(options, {nationality: '*'});
//       if (options.nationality === "*") {
//         var allLastNames = []
//         var lastNames = this.get("lastNames")
//         Object.keys(lastNames).forEach(function(key){
//           allLastNames = allLastNames.concat(lastNames[key])
//         })
//         return this.pick(allLastNames)
//       }
//       else {
//         return this.pick(this.get("lastNames")[options.nationality.toLowerCase()]);
//       }

//     };


//     // -- End Finance

//     // -- Regional

//     Chance.prototype.it_vat = function () {
//         var it_vat = this.natural({min: 1, max: 1800000});

//         it_vat = this.pad(it_vat, 7) + this.pad(this.pick(this.provinces({ country: 'it' })).code, 3);
//         return it_vat + this.luhn_calculate(it_vat);
//     };

//     /*
//      * this generator is written following the official algorithm
//      * all data can be passed explicitely or randomized by calling chance.cf() without options
//      * the code does not check that the input data is valid (it goes beyond the scope of the generator)
//      *
//      * @param  [Object] options = { first: first name,
//      *                              last: last name,
//      *                              gender: female|male,
//                                     birthday: JavaScript date object,
//                                     city: string(4), 1 letter + 3 numbers
//                                    }
//      * @return [string] codice fiscale
//      *
//     */
//     Chance.prototype.cf = function (options) {
//         options = options || {};
//         var gender = !!options.gender ? options.gender : this.gender(),
//             first = !!options.first ? options.first : this.first( { gender: gender, nationality: 'it'} ),
//             last = !!options.last ? options.last : this.last( { nationality: 'it'} ),
//             birthday = !!options.birthday ? options.birthday : this.birthday(),
//             city = !!options.city ? options.city : this.pickone(['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'L', 'M', 'Z']) + this.pad(this.natural({max:999}), 3),
//             cf = [],
//             name_generator = function(name, isLast) {
//                 var temp,
//                     return_value = [];

//                 if (name.length < 3) {
//                     return_value = name.split("").concat("XXX".split("")).splice(0,3);
//                 }
//                 else {
//                     temp = name.toUpperCase().split('').map(function(c){
//                         return ("BCDFGHJKLMNPRSTVWZ".indexOf(c) !== -1) ? c : undefined;
//                     }).join('');
//                     if (temp.length > 3) {
//                         if (isLast) {
//                             temp = temp.substr(0,3);
//                         } else {
//                             temp = temp[0] + temp.substr(2,2);
//                         }
//                     }
//                     if (temp.length < 3) {
//                         return_value = temp;
//                         temp = name.toUpperCase().split('').map(function(c){
//                             return ("AEIOU".indexOf(c) !== -1) ? c : undefined;
//                         }).join('').substr(0, 3 - return_value.length);
//                     }
//                     return_value = return_value + temp;
//                 }

//                 return return_value;
//             },
//             date_generator = function(birthday, gender, that) {
//                 var lettermonths = ['A', 'B', 'C', 'D', 'E', 'H', 'L', 'M', 'P', 'R', 'S', 'T'];

//                 return  birthday.getFullYear().toString().substr(2) +
//                         lettermonths[birthday.getMonth()] +
//                         that.pad(birthday.getDate() + ((gender.toLowerCase() === "female") ? 40 : 0), 2);
//             },
//             checkdigit_generator = function(cf) {
//                 var range1 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ",
//                     range2 = "ABCDEFGHIJABCDEFGHIJKLMNOPQRSTUVWXYZ",
//                     evens  = "ABCDEFGHIJKLMNOPQRSTUVWXYZ",
//                     odds   = "BAKPLCQDREVOSFTGUHMINJWZYX",
//                     digit  = 0;


//                 for(var i = 0; i < 15; i++) {
//                     if (i % 2 !== 0) {
//                         digit += evens.indexOf(range2[range1.indexOf(cf[i])]);
//                     }
//                     else {
//                         digit +=  odds.indexOf(range2[range1.indexOf(cf[i])]);
//                     }
//                 }
//                 return evens[digit % 26];
//             };

//         cf = cf.concat(name_generator(last, true), name_generator(first), date_generator(birthday, gender, this), city.toUpperCase().split("")).join("");
//         cf += checkdigit_generator(cf.toUpperCase(), this);

//         return cf.toUpperCase();
//     };

//     Chance.prototype.pl_pesel = function () {
//         var number = this.natural({min: 1, max: 9999999999});
//         var arr = this.pad(number, 10).split('');
//         for (var i = 0; i < arr.length; i++) {
//             arr[i] = parseInt(arr[i]);
//         }

//         var controlNumber = (1 * arr[0] + 3 * arr[1] + 7 * arr[2] + 9 * arr[3] + 1 * arr[4] + 3 * arr[5] + 7 * arr[6] + 9 * arr[7] + 1 * arr[8] + 3 * arr[9]) % 10;
//         if(controlNumber !== 0) {
//             controlNumber = 10 - controlNumber;
//         }

//         return arr.join('') + controlNumber;
//     };

//     Chance.prototype.pl_nip = function () {
//         var number = this.natural({min: 1, max: 999999999});
//         var arr = this.pad(number, 9).split('');
//         for (var i = 0; i < arr.length; i++) {
//             arr[i] = parseInt(arr[i]);
//         }

//         var controlNumber = (6 * arr[0] + 5 * arr[1] + 7 * arr[2] + 2 * arr[3] + 3 * arr[4] + 4 * arr[5] + 5 * arr[6] + 6 * arr[7] + 7 * arr[8]) % 11;
//         if(controlNumber === 10) {
//             return this.pl_nip();
//         }

//         return arr.join('') + controlNumber;
//     };

//     Chance.prototype.pl_regon = function () {
//         var number = this.natural({min: 1, max: 99999999});
//         var arr = this.pad(number, 8).split('');
//         for (var i = 0; i < arr.length; i++) {
//             arr[i] = parseInt(arr[i]);
//         }

//         var controlNumber = (8 * arr[0] + 9 * arr[1] + 2 * arr[2] + 3 * arr[3] + 4 * arr[4] + 5 * arr[5] + 6 * arr[6] + 7 * arr[7]) % 11;
//         if(controlNumber === 10) {
//             controlNumber = 0;
//         }

//         return arr.join('') + controlNumber;
//     };

//     // -- End Regional

//     // -- Music --

//     Chance.prototype.note = function(options) {
//       // choices for 'notes' option:
//       // flatKey - chromatic scale with flat notes (default)
//       // sharpKey - chromatic scale with sharp notes
//       // flats - just flat notes
//       // sharps - just sharp notes
//       // naturals - just natural notes
//       // all - naturals, sharps and flats
//       options = initOptions(options, { notes : 'flatKey'});
//       var scales = {
//         naturals: ['C', 'D', 'E', 'F', 'G', 'A', 'B'],
//         flats: ['D♭', 'E♭', 'G♭', 'A♭', 'B♭'],
//         sharps: ['C♯', 'D♯', 'F♯', 'G♯', 'A♯']
//       };
//       scales.all = scales.naturals.concat(scales.flats.concat(scales.sharps))
//       scales.flatKey = scales.naturals.concat(scales.flats)
//       scales.sharpKey = scales.naturals.concat(scales.sharps)
//       return this.pickone(scales[options.notes]);
//     }

//     Chance.prototype.midi_note = function(options) {
//       var min = 0;
//       var max = 127;
//       options = initOptions(options, { min : min, max : max });
//       return this.integer({min: options.min, max: options.max});
//     }

//     Chance.prototype.chord_quality = function(options) {
//       options = initOptions(options, { jazz: true });
//       var chord_qualities = ['maj', 'min', 'aug', 'dim'];
//       if (options.jazz){
//         chord_qualities = [
//           'maj7',
//           'min7',
//           '7',
//           'sus',
//           'dim',
//           'ø'
//         ];
//       }
//       return this.pickone(chord_qualities);
//     }

//     Chance.prototype.chord = function (options) {
//       options = initOptions(options);
//       return this.note(options) + this.chord_quality(options);
//     }

//     Chance.prototype.tempo = function (options) {
//       var min = 40;
//       var max = 320;
//       options = initOptions(options, {min: min, max: max});
//       return this.integer({min: options.min, max: options.max});
//     }

//     // -- End Music

//     // -- Miscellaneous --

//     // Coin - Flip, flip, flipadelphia
//     Chance.prototype.coin = function() {
//       return this.bool() ? "heads" : "tails";
//     }

//     // Dice - For all the board game geeks out there, myself included ;)
//     function diceFn (range) {
//         return function () {
//             return this.natural(range);
//         };
//     }
//     Chance.prototype.d4 = diceFn({min: 1, max: 4});
//     Chance.prototype.d6 = diceFn({min: 1, max: 6});
//     Chance.prototype.d8 = diceFn({min: 1, max: 8});
//     Chance.prototype.d10 = diceFn({min: 1, max: 10});
//     Chance.prototype.d12 = diceFn({min: 1, max: 12});
//     Chance.prototype.d20 = diceFn({min: 1, max: 20});
//     Chance.prototype.d30 = diceFn({min: 1, max: 30});
//     Chance.prototype.d100 = diceFn({min: 1, max: 100});

//     Chance.prototype.rpg = function (thrown, options) {
//         options = initOptions(options);
//         if (!thrown) {
//             throw new RangeError("Chance: A type of die roll must be included");
//         } else {
//             var bits = thrown.toLowerCase().split("d"),
//                 rolls = [];

//             if (bits.length !== 2 || !parseInt(bits[0], 10) || !parseInt(bits[1], 10)) {
//                 throw new Error("Chance: Invalid format provided. Please provide #d# where the first # is the number of dice to roll, the second # is the max of each die");
//             }
//             for (var i = bits[0]; i > 0; i--) {
//                 rolls[i - 1] = this.natural({min: 1, max: bits[1]});
//             }
//             return (typeof options.sum !== 'undefined' && options.sum) ? rolls.reduce(function (p, c) { return p + c; }) : rolls;
//         }
//     };

//     // Guid
//     Chance.prototype.guid = function (options) {
//         options = initOptions(options, { version: 5 });

//         var guid_pool = "abcdef1234567890",
//             variant_pool = "ab89",
//             guid = this.string({ pool: guid_pool, length: 8 }) + '-' +
//                    this.string({ pool: guid_pool, length: 4 }) + '-' +
//                    // The Version
//                    options.version +
//                    this.string({ pool: guid_pool, length: 3 }) + '-' +
//                    // The Variant
//                    this.string({ pool: variant_pool, length: 1 }) +
//                    this.string({ pool: guid_pool, length: 3 }) + '-' +
//                    this.string({ pool: guid_pool, length: 12 });
//         return guid;
//     };

//     // Hash
//     Chance.prototype.hash = function (options) {
//         options = initOptions(options, {length : 40, casing: 'lower'});
//         var pool = options.casing === 'upper' ? HEX_POOL.toUpperCase() : HEX_POOL;
//         return this.string({pool: pool, length: options.length});
//     };

//     Chance.prototype.luhn_check = function (num) {
//         var str = num.toString();
//         var checkDigit = +str.substring(str.length - 1);
//         return checkDigit === this.luhn_calculate(+str.substring(0, str.length - 1));
//     };

//     Chance.prototype.luhn_calculate = function (num) {
//         var digits = num.toString().split("").reverse();
//         var sum = 0;
//         var digit;

//         for (var i = 0, l = digits.length; l > i; ++i) {
//             digit = +digits[i];
//             if (i % 2 === 0) {
//                 digit *= 2;
//                 if (digit > 9) {
//                     digit -= 9;
//                 }
//             }
//             sum += digit;
//         }
//         return (sum * 9) % 10;
//     };

//     // MD5 Hash
//     Chance.prototype.md5 = function(options) {
//         var opts = { str: '', key: null, raw: false };

//         if (!options) {
//             opts.str = this.string();
//             options = {};
//         }
//         else if (typeof options === 'string') {
//             opts.str = options;
//             options = {};
//         }
//         else if (typeof options !== 'object') {
//             return null;
//         }
//         else if(options.constructor === 'Array') {
//             return null;
//         }

//         opts = initOptions(options, opts);

//         if(!opts.str){
//             throw new Error('A parameter is required to return an md5 hash.');
//         }

//         return this.bimd5.md5(opts.str, opts.key, opts.raw);
//     };

//     /**
//      * #Description:
//      * =====================================================
//      * Generate random file name with extension
//      *
//      * The argument provide extension type
//      * -> raster
//      * -> vector
//      * -> 3d
//      * -> document
//      *
//      * If nothing is provided the function return random file name with random
//      * extension type of any kind
//      *
//      * The user can validate the file name length range
//      * If nothing provided the generated file name is random
//      *
//      * #Extension Pool :
//      * * Currently the supported extensions are
//      *  -> some of the most popular raster image extensions
//      *  -> some of the most popular vector image extensions
//      *  -> some of the most popular 3d image extensions
//      *  -> some of the most popular document extensions
//      *
//      * #Examples :
//      * =====================================================
//      *
//      * Return random file name with random extension. The file extension
//      * is provided by a predefined collection of extensions. More about the extension
//      * pool can be found in #Extension Pool section
//      *
//      * chance.file()
//      * => dsfsdhjf.xml
//      *
//      * In order to generate a file name with specific length, specify the
//      * length property and integer value. The extension is going to be random
//      *
//      * chance.file({length : 10})
//      * => asrtineqos.pdf
//      *
//      * In order to generate file with extension from some of the predefined groups
//      * of the extension pool just specify the extension pool category in fileType property
//      *
//      * chance.file({fileType : 'raster'})
//      * => dshgssds.psd
//      *
//      * You can provide specific extension for your files
//      * chance.file({extension : 'html'})
//      * => djfsd.html
//      *
//      * Or you could pass custom collection of extensions by array or by object
//      * chance.file({extensions : [...]})
//      * => dhgsdsd.psd
//      *
//      * chance.file({extensions : { key : [...], key : [...]}})
//      * => djsfksdjsd.xml
//      *
//      * @param  [collection] options
//      * @return [string]
//      *
//      */
//     Chance.prototype.file = function(options) {

//         var fileOptions = options || {};
//         var poolCollectionKey = "fileExtension";
//         var typeRange   = Object.keys(this.get("fileExtension"));//['raster', 'vector', '3d', 'document'];
//         var fileName;
//         var fileExtension;

//         // Generate random file name
//         fileName = this.word({length : fileOptions.length});

//         // Generate file by specific extension provided by the user
//         if(fileOptions.extension) {

//             fileExtension = fileOptions.extension;
//             return (fileName + '.' + fileExtension);
//         }

//         // Generate file by specific extension collection
//         if(fileOptions.extensions) {

//             if(Array.isArray(fileOptions.extensions)) {

//                 fileExtension = this.pickone(fileOptions.extensions);
//                 return (fileName + '.' + fileExtension);
//             }
//             else if(fileOptions.extensions.constructor === Object) {

//                 var extensionObjectCollection = fileOptions.extensions;
//                 var keys = Object.keys(extensionObjectCollection);

//                 fileExtension = this.pickone(extensionObjectCollection[this.pickone(keys)]);
//                 return (fileName + '.' + fileExtension);
//             }

//             throw new Error("Chance: Extensions must be an Array or Object");
//         }

//         // Generate file extension based on specific file type
//         if(fileOptions.fileType) {

//             var fileType = fileOptions.fileType;
//             if(typeRange.indexOf(fileType) !== -1) {

//                 fileExtension = this.pickone(this.get(poolCollectionKey)[fileType]);
//                 return (fileName + '.' + fileExtension);
//             }

//             throw new RangeError("Chance: Expect file type value to be 'raster', 'vector', '3d' or 'document'");
//         }

//         // Generate random file name if no extension options are passed
//         fileExtension = this.pickone(this.get(poolCollectionKey)[this.pickone(typeRange)]);
//         return (fileName + '.' + fileExtension);
//     };

//   

//     var o_hasOwnProperty = Object.prototype.hasOwnProperty;
//     var o_keys = (Object.keys || function(obj) {
//       var result = [];
//       for (var key in obj) {
//         if (o_hasOwnProperty.call(obj, key)) {
//           result.push(key);
//         }
//       }

//       return result;
//     });


//     function _copyObject(source, target) {
//       var keys = o_keys(source);
//       var key;

//       for (var i = 0, l = keys.length; i < l; i++) {
//         key = keys[i];
//         target[key] = source[key] || target[key];
//       }
//     }

//     function _copyArray(source, target) {
//       for (var i = 0, l = source.length; i < l; i++) {
//         target[i] = source[i];
//       }
//     }

//     function copyObject(source, _target) {
//         var isArray = Array.isArray(source);
//         var target = _target || (isArray ? new Array(source.length) : {});

//         if (isArray) {
//           _copyArray(source, target);
//         } else {
//           _copyObject(source, target);
//         }

//         return target;
//     }

//     /** Get the data based on key**/
//     Chance.prototype.get = function (name) {
//         return copyObject(data[name]);
//     };

//     // Mac Address
//     Chance.prototype.mac_address = function(options){
//         // typically mac addresses are separated by ":"
//         // however they can also be separated by "-"
//         // the network variant uses a dot every fourth byte

//         options = initOptions(options);
//         if(!options.separator) {
//             options.separator =  options.networkVersion ? "." : ":";
//         }

//         var mac_pool="ABCDEF1234567890",
//             mac = "";
//         if(!options.networkVersion) {
//             mac = this.n(this.string, 6, { pool: mac_pool, length:2 }).join(options.separator);
//         } else {
//             mac = this.n(this.string, 3, { pool: mac_pool, length:4 }).join(options.separator);
//         }

//         return mac;
//     };

//     Chance.prototype.normal = function (options) {
//         options = initOptions(options, {mean : 0, dev : 1, pool : []});

//         testRange(
//             options.pool.constructor !== Array,
//             "Chance: The pool option must be a valid array."
//         );
//         testRange(
//             typeof options.mean !== 'number',
//             "Chance: Mean (mean) must be a number"
//         );
//         testRange(
//             typeof options.dev !== 'number',
//             "Chance: Standard deviation (dev) must be a number"
//         );

//         // If a pool has been passed, then we are returning an item from that pool,
//         // using the normal distribution settings that were passed in
//         if (options.pool.length > 0) {
//             return this.normal_pool(options);
//         }

//         // The Marsaglia Polar method
//         var s, u, v, norm,
//             mean = options.mean,
//             dev = options.dev;

//         do {
//             // U and V are from the uniform distribution on (-1, 1)
//             u = this.random() * 2 - 1;
//             v = this.random() * 2 - 1;

//             s = u * u + v * v;
//         } while (s >= 1);

//         // Compute the standard normal variate
//         norm = u * Math.sqrt(-2 * Math.log(s) / s);

//         // Shape and scale
//         return dev * norm + mean;
//     };

//     Chance.prototype.normal_pool = function(options) {
//         var performanceCounter = 0;
//         do {
//             var idx = Math.round(this.normal({ mean: options.mean, dev: options.dev }));
//             if (idx < options.pool.length && idx >= 0) {
//                 return options.pool[idx];
//             } else {
//                 performanceCounter++;
//             }
//         } while(performanceCounter < 100);

//         throw new RangeError("Chance: Your pool is too small for the given mean and standard deviation. Please adjust.");
//     };

//     Chance.prototype.radio = function (options) {
//         // Initial Letter (Typically Designated by Side of Mississippi River)
//         options = initOptions(options, {side : "?"});
//         var fl = "";
//         switch (options.side.toLowerCase()) {
//         case "east":
//         case "e":
//             fl = "W";
//             break;
//         case "west":
//         case "w":
//             fl = "K";
//             break;
//         default:
//             fl = this.character({pool: "KW"});
//             break;
//         }

//         return fl + this.character({alpha: true, casing: "upper"}) +
//                 this.character({alpha: true, casing: "upper"}) +
//                 this.character({alpha: true, casing: "upper"});
//     };

//     // Set the data as key and data or the data map
//     Chance.prototype.set = function (name, values) {
//         if (typeof name === "string") {
//             data[name] = values;
//         } else {
//             data = copyObject(name, data);
//         }
//     };

//     Chance.prototype.tv = function (options) {
//         return this.radio(options);
//     };





}
