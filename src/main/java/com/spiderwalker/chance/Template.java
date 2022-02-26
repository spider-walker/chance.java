package com.spiderwalker.chance;

public class Template {

//
//
//    function CopyToken(c) {
//        this.c = c
//    }
//
//    CopyToken.prototype = {
//        substitute: function () {
//            return this.c
//        }
//    }
//
//    function EscapeToken(c) {
//        this.c = c
//    }
//
//    EscapeToken.prototype = {
//        substitute: function () {
//            if (!/[{}\\]/.test(this.c)) {
//                throw new Error('Invalid escape sequence: "\\' + this.c + '".')
//            }
//            return this.c
//        }
//    }
//
//    function ReplaceToken(c) {
//        this.c = c
//    }
//
//    ReplaceToken.prototype = {
//        replacers: {
//            '#': function (chance) { return chance.character({ pool: NUMBERS }) },
//            'A': function (chance) { return chance.character({ pool: CHARS_UPPER }) },
//            'a': function (chance) { return chance.character({ pool: CHARS_LOWER }) },
//        },
//
//        substitute: function (chance) {
//            var replacer = this.replacers[this.c]
//            if (!replacer) {
//                throw new Error('Invalid replacement character: "' + this.c + '".')
//            }
//            return replacer(chance)
//        }
//    }
//
//    function parseTemplate(template) {
//        var tokens = []
//        var mode = 'identity'
//        for (var i = 0; i<template.length; i++) {
//            var c = template[i]
//            switch (mode) {
//                case 'escape':
//                    tokens.push(new EscapeToken(c))
//                    mode = 'identity'
//                    break
//                case 'identity':
//                    if (c === '{') {
//                        mode = 'replace'
//                    } else if (c === '\\') {
//                        mode = 'escape'
//                    } else {
//                        tokens.push(new CopyToken(c))
//                    }
//                    break
//                case 'replace':
//                    if (c === '}') {
//                        mode = 'identity'
//                    } else {
//                        tokens.push(new ReplaceToken(c))
//                    }
//                    break
//            }
//        }
//        return tokens
//    }
//
//    /**
//     *  Return a random string matching the given template.
//     *
//     *  The template consists of any number of "character replacement" and
//     *  "character literal" sequences. A "character replacement" sequence
//     *  starts with a left brace, has any number of special replacement
//     *  characters, and ends with a right brace. A character literal can be any
//     *  character except a brace or a backslash. A literal brace or backslash
//     *  character can be included in the output by escaping with a backslash.
//     *
//     *  The following replacement characters can be used in a replacement
//     *  sequence:
//     *
//     *      "#": a random digit
//     *      "a": a random lower case letter
//     *      "A": a random upper case letter
//     *
//     *  Example: chance.template('{AA###}-{##}')
//     *
//     *  @param {String} template string.
//     *  @returns {String} a random string matching the template.
//     */
//    Chance.prototype.template = function (template) {
//        if (!template) {
//            throw new Error('Template string is required')
//        }
//        var self = this
//        return parseTemplate(template)
//                .map(function (token) { return token.substitute(self) })
//            .join('');
//    };
}
