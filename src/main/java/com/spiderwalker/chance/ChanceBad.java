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



//     /**
//      * Determine whether a given number is prime or not.
//      */
//     Chance.prototype.is_prime = function (n) {
//         if (n % 1 || n < 2) {
//             return false;
//         }
//         if (n % 2 === 0) {
//             return n === 2;
//         }
//         if (n % 3 === 0) {
//             return n === 3;
//         }
//         var m = Math.sqrt(n);
//         for (var i = 5; i <= m; i += 6) {
//             if (n % i === 0 || n % (i + 2) === 0) {
//                 return false;
//             }
//         }
//         return true;
//     };

//     /**
//      *  Return a random hex number as string
//      *
//      *  NOTE the max and min are INCLUDED in the range. So:
//      *  chance.hex({min: '9', max: 'B'});
//      *  would return either '9', 'A' or 'B'.
//      *
//      *  @param {Object} [options={}] can specify a min and/or max and/or casing
//      *  @returns {String} a single random string hex number
//      *  @throws {RangeError} min cannot be greater than max
//      */
//     Chance.prototype.hex = function (options) {
//         options = initOptions(options, {min: 0, max: MAX_INT, casing: 'lower'});
//         testRange(options.min < 0, "Chance: Min cannot be less than zero.");
// 		var integer = this.natural({min: options.min, max: options.max});
// 		if (options.casing === 'upper') {
// 			return integer.toString(16).toUpperCase();
// 		}
// 		return integer.toString(16);
//     };

//     Chance.prototype.letter = function(options) {
//         options = initOptions(options, {casing: 'lower'});
//         var pool = "abcdefghijklmnopqrstuvwxyz";
//         var letter = this.character({pool: pool});
//         if (options.casing === 'upper') {
//             letter = letter.toUpperCase();
//         }
//         return letter;
//     }

//     /**
//      *  Return a random string
//      *
//      *  @param {Object} [options={}] can specify a length or min and max
//      *  @returns {String} a string of random length
//      *  @throws {RangeError} length cannot be less than zero
//      */
//     Chance.prototype.string = function (options) {
//         options = initOptions(options, { min: 5, max: 20 });

//         if (!options.length) {
//             options.length = this.natural({ min: options.min, max: options.max })
//         }

//         testRange(options.length < 0, "Chance: Length cannot be less than zero.");
//         var length = options.length,
//             text = this.n(this.character, length, options);

//         return text.join("");
//     };

//     function CopyToken(c) {
//         this.c = c
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

//     Chance.prototype.israelId=function(){
//         var x=this.string({pool: '0123456789',length:8});
//         var y=0;
//         for (var i=0;i<x.length;i++){
//             var thisDigit=  x[i] *  (i/2===parseInt(i/2) ? 1 : 2);
//             thisDigit=this.pad(thisDigit,2).toString();
//             thisDigit=parseInt(thisDigit[0]) + parseInt(thisDigit[1]);
//             y=y+thisDigit;
//         }
//         x=x+(10-parseInt(y.toString().slice(-1))).toString().slice(-1);
//         return x;
//     };

//     Chance.prototype.mrz = function (options) {
//         var checkDigit = function (input) {
//             var alpha = "<ABCDEFGHIJKLMNOPQRSTUVWXYXZ".split(''),
//                 multipliers = [ 7, 3, 1 ],
//                 runningTotal = 0;

//             if (typeof input !== 'string') {
//                 input = input.toString();
//             }

//             input.split('').forEach(function(character, idx) {
//                 var pos = alpha.indexOf(character);

//                 if(pos !== -1) {
//                     character = pos === 0 ? 0 : pos + 9;
//                 } else {
//                     character = parseInt(character, 10);
//                 }
//                 character *= multipliers[idx % multipliers.length];
//                 runningTotal += character;
//             });
//             return runningTotal % 10;
//         };
//         var generate = function (opts) {
//             var pad = function (length) {
//                 return new Array(length + 1).join('<');
//             };
//             var number = [ 'P<',
//                            opts.issuer,
//                            opts.last.toUpperCase(),
//                            '<<',
//                            opts.first.toUpperCase(),
//                            pad(39 - (opts.last.length + opts.first.length + 2)),
//                            opts.passportNumber,
//                            checkDigit(opts.passportNumber),
//                            opts.nationality,
//                            opts.dob,
//                            checkDigit(opts.dob),
//                            opts.gender,
//                            opts.expiry,
//                            checkDigit(opts.expiry),
//                            pad(14),
//                            checkDigit(pad(14)) ].join('');

//             return number +
//                 (checkDigit(number.substr(44, 10) +
//                             number.substr(57, 7) +
//                             number.substr(65, 7)));
//         };

//         var that = this;

//         options = initOptions(options, {
//             first: this.first(),
//             last: this.last(),
//             passportNumber: this.integer({min: 100000000, max: 999999999}),
//             dob: (function () {
//                 var date = that.birthday({type: 'adult'});
//                 return [date.getFullYear().toString().substr(2),
//                         that.pad(date.getMonth() + 1, 2),
//                         that.pad(date.getDate(), 2)].join('');
//             }()),
//             expiry: (function () {
//                 var date = new Date();
//                 return [(date.getFullYear() + 5).toString().substr(2),
//                         that.pad(date.getMonth() + 1, 2),
//                         that.pad(date.getDate(), 2)].join('');
//             }()),
//             gender: this.gender() === 'Female' ? 'F': 'M',
//             issuer: 'GBR',
//             nationality: 'GBR'
//         });
//         return generate (options);
//     };

//     Chance.prototype.name = function (options) {
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
//     Chance.prototype.name_prefixes = function (gender) {
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
//     Chance.prototype.prefix = function (options) {
//         return this.name_prefix(options);
//     };

//     Chance.prototype.name_prefix = function (options) {
//         options = initOptions(options, { gender: "all" });
//         return options.full ?
//             this.pick(this.name_prefixes(options.gender)).name :
//             this.pick(this.name_prefixes(options.gender)).abbreviation;
//     };
//     //Hungarian ID number
//     Chance.prototype.HIDN= function(){
//      //Hungarian ID nuber structure: XXXXXXYY (X=number,Y=Capital Latin letter)
//       var idn_pool="0123456789";
//       var idn_chrs="ABCDEFGHIJKLMNOPQRSTUVWXYXZ";
//       var idn="";
//         idn+=this.string({pool:idn_pool,length:6});
//         idn+=this.string({pool:idn_chrs,length:2});
//         return idn;
//     };


//     Chance.prototype.ssn = function (options) {
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
//     Chance.prototype.aadhar = function (options) {
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
//     Chance.prototype.name_suffixes = function () {
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
//     Chance.prototype.suffix = function (options) {
//         return this.name_suffix(options);
//     };

//     Chance.prototype.name_suffix = function (options) {
//         options = initOptions(options);
//         return options.full ?
//             this.pick(this.name_suffixes()).name :
//             this.pick(this.name_suffixes()).abbreviation;
//     };

//     Chance.prototype.nationalities = function () {
//         return this.get("nationalities");
//     };

//     // Generate random nationality based on json list
//     Chance.prototype.nationality = function () {
//         var nationality = this.pick(this.nationalities());
//         return nationality.name;
//     };

//     // -- End Person --

//     // -- Mobile --
//     // Android GCM Registration ID
//     Chance.prototype.android_id = function () {
//         return "APA91" + this.string({ pool: "0123456789abcefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_", length: 178 });
//     };

//     // Apple Push Token
//     Chance.prototype.apple_token = function () {
//         return this.string({ pool: "abcdef1234567890", length: 64 });
//     };

//     // Windows Phone 8 ANID2
//     Chance.prototype.wp8_anid2 = function () {
//         return base64( this.hash( { length : 32 } ) );
//     };

//     // Windows Phone 7 ANID
//     Chance.prototype.wp7_anid = function () {
//         return 'A=' + this.guid().replace(/-/g, '').toUpperCase() + '&E=' + this.hash({ length:3 }) + '&W=' + this.integer({ min:0, max:9 });
//     };

//     // BlackBerry Device PIN
//     Chance.prototype.bb_pin = function () {
//         return this.hash({ length: 8 });
//     };

//     // -- End Mobile --

//     // -- Web --
//     Chance.prototype.avatar = function (options) {
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
//     Chance.prototype.color = function (options) {
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

//     Chance.prototype.domain = function (options) {
//         options = initOptions(options);
//         return this.word() + '.' + (options.tld || this.tld());
//     };

//     Chance.prototype.email = function (options) {
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
//     Chance.prototype.fbid = function () {
//         return '10000' + this.string({pool: "1234567890", length: 11});
//     };

//     Chance.prototype.google_analytics = function () {
//         var account = this.pad(this.natural({max: 999999}), 6);
//         var property = this.pad(this.natural({max: 99}), 2);

//         return 'UA-' + account + '-' + property;
//     };

//     Chance.prototype.hashtag = function () {
//         return '#' + this.word();
//     };

//     Chance.prototype.ip = function () {
//         // Todo: This could return some reserved IPs. See http://vq.io/137dgYy
//         // this should probably be updated to account for that rare as it may be
//         return this.natural({min: 1, max: 254}) + '.' +
//                this.natural({max: 255}) + '.' +
//                this.natural({max: 255}) + '.' +
//                this.natural({min: 1, max: 254});
//     };

//     Chance.prototype.ipv6 = function () {
//         var ip_addr = this.n(this.hash, 8, {length: 4});

//         return ip_addr.join(":");
//     };

//     Chance.prototype.klout = function () {
//         return this.natural({min: 1, max: 99});
//     };

//     Chance.prototype.mac = function (options) {
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

//     Chance.prototype.semver = function (options) {
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

//     Chance.prototype.tlds = function () {
//         return ['com', 'org', 'edu', 'gov', 'co.uk', 'net', 'io', 'ac', 'ad', 'ae', 'af', 'ag', 'ai', 'al', 'am', 'ao', 'aq', 'ar', 'as', 'at', 'au', 'aw', 'ax', 'az', 'ba', 'bb', 'bd', 'be', 'bf', 'bg', 'bh', 'bi', 'bj', 'bm', 'bn', 'bo', 'br', 'bs', 'bt', 'bv', 'bw', 'by', 'bz', 'ca', 'cc', 'cd', 'cf', 'cg', 'ch', 'ci', 'ck', 'cl', 'cm', 'cn', 'co', 'cr', 'cu', 'cv', 'cw', 'cx', 'cy', 'cz', 'de', 'dj', 'dk', 'dm', 'do', 'dz', 'ec', 'ee', 'eg', 'eh', 'er', 'es', 'et', 'eu', 'fi', 'fj', 'fk', 'fm', 'fo', 'fr', 'ga', 'gb', 'gd', 'ge', 'gf', 'gg', 'gh', 'gi', 'gl', 'gm', 'gn', 'gp', 'gq', 'gr', 'gs', 'gt', 'gu', 'gw', 'gy', 'hk', 'hm', 'hn', 'hr', 'ht', 'hu', 'id', 'ie', 'il', 'im', 'in', 'io', 'iq', 'ir', 'is', 'it', 'je', 'jm', 'jo', 'jp', 'ke', 'kg', 'kh', 'ki', 'km', 'kn', 'kp', 'kr', 'kw', 'ky', 'kz', 'la', 'lb', 'lc', 'li', 'lk', 'lr', 'ls', 'lt', 'lu', 'lv', 'ly', 'ma', 'mc', 'md', 'me', 'mg', 'mh', 'mk', 'ml', 'mm', 'mn', 'mo', 'mp', 'mq', 'mr', 'ms', 'mt', 'mu', 'mv', 'mw', 'mx', 'my', 'mz', 'na', 'nc', 'ne', 'nf', 'ng', 'ni', 'nl', 'no', 'np', 'nr', 'nu', 'nz', 'om', 'pa', 'pe', 'pf', 'pg', 'ph', 'pk', 'pl', 'pm', 'pn', 'pr', 'ps', 'pt', 'pw', 'py', 'qa', 're', 'ro', 'rs', 'ru', 'rw', 'sa', 'sb', 'sc', 'sd', 'se', 'sg', 'sh', 'si', 'sj', 'sk', 'sl', 'sm', 'sn', 'so', 'sr', 'ss', 'st', 'su', 'sv', 'sx', 'sy', 'sz', 'tc', 'td', 'tf', 'tg', 'th', 'tj', 'tk', 'tl', 'tm', 'tn', 'to', 'tp', 'tr', 'tt', 'tv', 'tw', 'tz', 'ua', 'ug', 'uk', 'us', 'uy', 'uz', 'va', 'vc', 've', 'vg', 'vi', 'vn', 'vu', 'wf', 'ws', 'ye', 'yt', 'za', 'zm', 'zw'];
//     };

//     Chance.prototype.tld = function () {
//         return this.pick(this.tlds());
//     };

//     Chance.prototype.twitter = function () {
//         return '@' + this.word();
//     };

//     Chance.prototype.url = function (options) {
//         options = initOptions(options, { protocol: "http", domain: this.domain(options), domain_prefix: "", path: this.word(), extensions: []});

//         var extension = options.extensions.length > 0 ? "." + this.pick(options.extensions) : "";
//         var domain = options.domain_prefix ? options.domain_prefix + "." + options.domain : options.domain;

//         return options.protocol + "://" + domain + "/" + options.path + extension;
//     };

//     Chance.prototype.port = function() {
//         return this.integer({min: 0, max: 65535});
//     };

//     Chance.prototype.locale = function (options) {
//         options = initOptions(options);
//         if (options.region){
//           return this.pick(this.get("locale_regions"));
//         } else {
//           return this.pick(this.get("locale_languages"));
//         }
//     };

//     Chance.prototype.locales = function (options) {
//       options = initOptions(options);
//       if (options.region){
//         return this.get("locale_regions");
//       } else {
//         return this.get("locale_languages");
//       }
//     };

//     Chance.prototype.loremPicsum = function (options) {
//         options = initOptions(options, { width: 500, height: 500, greyscale: false, blurred: false });

//         var greyscale = options.greyscale ? 'g/' : '';
//         var query = options.blurred ? '/?blur' : '/?random';

//         return 'https://picsum.photos/' + greyscale + options.width + '/' + options.height + query;
//     }

//     // -- End Web --

//     // -- Location --

//     Chance.prototype.address = function (options) {
//         options = initOptions(options);
//         return this.natural({min: 5, max: 2000}) + ' ' + this.street(options);
//     };

//     Chance.prototype.altitude = function (options) {
//         options = initOptions(options, {fixed: 5, min: 0, max: 8848});
//         return this.floating({
//             min: options.min,
//             max: options.max,
//             fixed: options.fixed
//         });
//     };

//     Chance.prototype.areacode = function (options) {
//         options = initOptions(options, {parens : true});
//         // Don't want area codes to start with 1, or have a 9 as the second digit
//         var areacode = options.exampleNumber ?
//         "555" :
//         this.natural({min: 2, max: 9}).toString() +
//                 this.natural({min: 0, max: 8}).toString() +
//                 this.natural({min: 0, max: 9}).toString();

//         return options.parens ? '(' + areacode + ')' : areacode;
//     };

//     Chance.prototype.city = function () {
//         return this.capitalize(this.word({syllables: 3}));
//     };

//     Chance.prototype.coordinates = function (options) {
//         return this.latitude(options) + ', ' + this.longitude(options);
//     };

//     Chance.prototype.countries = function () {
//         return this.get("countries");
//     };

//     Chance.prototype.country = function (options) {
//         options = initOptions(options);
//         var country = this.pick(this.countries());
//         return options.raw ? country : options.full ? country.name : country.abbreviation;
//     };

//     Chance.prototype.depth = function (options) {
//         options = initOptions(options, {fixed: 5, min: -10994, max: 0});
//         return this.floating({
//             min: options.min,
//             max: options.max,
//             fixed: options.fixed
//         });
//     };

//     Chance.prototype.geohash = function (options) {
//         options = initOptions(options, { length: 7 });
//         return this.string({ length: options.length, pool: '0123456789bcdefghjkmnpqrstuvwxyz' });
//     };

//     Chance.prototype.geojson = function (options) {
//         return this.latitude(options) + ', ' + this.longitude(options) + ', ' + this.altitude(options);
//     };

//     Chance.prototype.latitude = function (options) {
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

//     Chance.prototype.longitude = function (options) {
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

//     Chance.prototype.phone = function (options) {
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

//     Chance.prototype.postal = function () {
//         // Postal District
//         var pd = this.character({pool: "XVTSRPNKLMHJGECBA"});
//         // Forward Sortation Area (FSA)
//         var fsa = pd + this.natural({max: 9}) + this.character({alpha: true, casing: "upper"});
//         // Local Delivery Unut (LDU)
//         var ldu = this.natural({max: 9}) + this.character({alpha: true, casing: "upper"}) + this.natural({max: 9});

//         return fsa + " " + ldu;
//     };

//     Chance.prototype.postcode = function () {
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

//     Chance.prototype.counties = function (options) {
//         options = initOptions(options, { country: 'uk' });
//         return this.get("counties")[options.country.toLowerCase()];
//     };

//     Chance.prototype.county = function (options) {
//         return this.pick(this.counties(options)).name;
//     };

//     Chance.prototype.provinces = function (options) {
//         options = initOptions(options, { country: 'ca' });
//         return this.get("provinces")[options.country.toLowerCase()];
//     };

//     Chance.prototype.province = function (options) {
//         return (options && options.full) ?
//             this.pick(this.provinces(options)).name :
//             this.pick(this.provinces(options)).abbreviation;
//     };

//     Chance.prototype.state = function (options) {
//         return (options && options.full) ?
//             this.pick(this.states(options)).name :
//             this.pick(this.states(options)).abbreviation;
//     };

//     Chance.prototype.states = function (options) {
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

//     Chance.prototype.street = function (options) {
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

//     Chance.prototype.street_suffix = function (options) {
//         options = initOptions(options, { country: 'us' });
//         return this.pick(this.street_suffixes(options));
//     };

//     Chance.prototype.street_suffixes = function (options) {
//         options = initOptions(options, { country: 'us' });
//         // These are the most common suffixes.
//         return this.get("street_suffixes")[options.country.toLowerCase()];
//     };

//     // Note: only returning US zip codes, internationalization will be a whole
//     // other beast to tackle at some point.
//     Chance.prototype.zip = function (options) {
//         var zip = this.n(this.natural, 5, {max: 9});

//         if (options && options.plusfour === true) {
//             zip.push('-');
//             zip = zip.concat(this.n(this.natural, 4, {max: 9}));
//         }

//         return zip.join("");
//     };

//     // -- End Location --

//     // -- Time

//     Chance.prototype.ampm = function () {
//         return this.bool() ? 'am' : 'pm';
//     };

//     Chance.prototype.date = function (options) {
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

//     Chance.prototype.hammertime = function (options) {
//         return this.date(options).getTime();
//     };

//     Chance.prototype.hour = function (options) {
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

//     Chance.prototype.millisecond = function () {
//         return this.natural({max: 999});
//     };

//     Chance.prototype.minute = Chance.prototype.second = function (options) {
//         options = initOptions(options, {min: 0, max: 59});

//         testRange(options.min < 0, "Chance: Min cannot be less than 0.");
//         testRange(options.max > 59, "Chance: Max cannot be greater than 59.");
//         testRange(options.min > options.max, "Chance: Min cannot be greater than Max.");

//         return this.natural({min: options.min, max: options.max});
//     };

//     Chance.prototype.month = function (options) {
//         options = initOptions(options, {min: 1, max: 12});

//         testRange(options.min < 1, "Chance: Min cannot be less than 1.");
//         testRange(options.max > 12, "Chance: Max cannot be greater than 12.");
//         testRange(options.min > options.max, "Chance: Min cannot be greater than Max.");

//         var month = this.pick(this.months().slice(options.min - 1, options.max));
//         return options.raw ? month : month.name;
//     };

//     Chance.prototype.months = function () {
//         return this.get("months");
//     };

//     Chance.prototype.second = function () {
//         return this.natural({max: 59});
//     };

//     Chance.prototype.timestamp = function () {
//         return this.natural({min: 1, max: parseInt(new Date().getTime() / 1000, 10)});
//     };

//     Chance.prototype.weekday = function (options) {
//         options = initOptions(options, {weekday_only: false});
//         var weekdays = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];
//         if (!options.weekday_only) {
//             weekdays.push("Saturday");
//             weekdays.push("Sunday");
//         }
//         return this.pickone(weekdays);
//     };

//     Chance.prototype.year = function (options) {
//         // Default to current year as min if none specified
//         options = initOptions(options, {min: new Date().getFullYear()});

//         // Default to one century after current year as max if none specified
//         options.max = (typeof options.max !== "undefined") ? options.max : options.min + 100;

//         return this.natural(options).toString();
//     };

//     // -- End Time

//     // -- Finance --

//     Chance.prototype.cc = function (options) {
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

//     Chance.prototype.cc_types = function () {
//         // http://en.wikipedia.org/wiki/Bank_card_number#Issuer_identification_number_.28IIN.29
//         return this.get("cc_types");
//     };

//     Chance.prototype.cc_type = function (options) {
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
//     Chance.prototype.currency_types = function () {
//         return this.get("currency_types");
//     };

//     // return random world currency by ISO 4217
//     Chance.prototype.currency = function () {
//         return this.pick(this.currency_types());
//     };

//     // return all timezones available
//     Chance.prototype.timezones = function () {
//         return this.get("timezones");
//     };

//     // return random timezone
//     Chance.prototype.timezone = function () {
//         return this.pick(this.timezones());
//     };

//     //Return random correct currency exchange pair (e.g. EUR/USD) or array of currency code
//     Chance.prototype.currency_pair = function (returnAsString) {
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

//     Chance.prototype.dollar = function (options) {
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

//     Chance.prototype.euro = function (options) {
//         return Number(this.dollar(options).replace("$", "")).toLocaleString() + "€";
//     };

//     Chance.prototype.exp = function (options) {
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

//     Chance.prototype.exp_month = function (options) {
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

//     Chance.prototype.exp_year = function () {
//         var curMonth = new Date().getMonth() + 1,
//             curYear = new Date().getFullYear();

//         return this.year({min: ((curMonth === 12) ? (curYear + 1) : curYear), max: (curYear + 10)});
//     };

//     Chance.prototype.vat = function (options) {
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
//     Chance.prototype.iban = function () {
//         var alpha = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
//         var alphanum = alpha + '0123456789';
//         var iban =
//             this.string({ length: 2, pool: alpha }) +
//             this.pad(this.integer({ min: 0, max: 99 }), 2) +
//             this.string({ length: 4, pool: alphanum }) +
//             this.pad(this.natural(), this.natural({ min: 6, max: 26 }));
//         return iban;
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
