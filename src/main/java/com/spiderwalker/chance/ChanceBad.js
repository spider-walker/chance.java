//  Chance.js 1.1.8


    // -- End Regional

    // -- Music --

    Chance.prototype.note = function(options) {
      // choices for 'notes' option:
      // flatKey - chromatic scale with flat notes (default)
      // sharpKey - chromatic scale with sharp notes
      // flats - just flat notes
      // sharps - just sharp notes
      // naturals - just natural notes
      // all - naturals, sharps and flats
      options = initOptions(options, { notes : 'flatKey'});
      var scales = {
        naturals: ['C', 'D', 'E', 'F', 'G', 'A', 'B'],
        flats: ['D♭', 'E♭', 'G♭', 'A♭', 'B♭'],
        sharps: ['C♯', 'D♯', 'F♯', 'G♯', 'A♯']
      };
      scales.all = scales.naturals.concat(scales.flats.concat(scales.sharps))
      scales.flatKey = scales.naturals.concat(scales.flats)
      scales.sharpKey = scales.naturals.concat(scales.sharps)
      return this.pickone(scales[options.notes]);
    }

    Chance.prototype.midi_note = function(options) {
      var min = 0;
      var max = 127;
      options = initOptions(options, { min : min, max : max });
      return this.integer({min: options.min, max: options.max});
    }

    Chance.prototype.chord_quality = function(options) {
      options = initOptions(options, { jazz: true });
      var chord_qualities = ['maj', 'min', 'aug', 'dim'];
      if (options.jazz){
        chord_qualities = [
          'maj7',
          'min7',
          '7',
          'sus',
          'dim',
          'ø'
        ];
      }
      return this.pickone(chord_qualities);
    }

    Chance.prototype.chord = function (options) {
      options = initOptions(options);
      return this.note(options) + this.chord_quality(options);
    }

    Chance.prototype.tempo = function (options) {
      var min = 40;
      var max = 320;
      options = initOptions(options, {min: min, max: max});
      return this.integer({min: options.min, max: options.max});
    }

    // -- End Music

    // -- Miscellaneous --


    // Dice - For all the board game geeks out there, myself included ;)


    Chance.prototype.rpg = function (thrown, options) {
        options = initOptions(options);
        if (!thrown) {
            throw new RangeError("Chance: A type of die roll must be included");
        } else {
            var bits = thrown.toLowerCase().split("d"),
                rolls = [];

            if (bits.length !== 2 || !parseInt(bits[0], 10) || !parseInt(bits[1], 10)) {
                throw new Error("Chance: Invalid format provided. Please provide #d# where the first # is the number of dice to roll, the second # is the max of each die");
            }
            for (var i = bits[0]; i > 0; i--) {
                rolls[i - 1] = this.natural({min: 1, max: bits[1]});
            }
            return (typeof options.sum !== 'undefined' && options.sum) ? rolls.reduce(function (p, c) { return p + c; }) : rolls;
        }
    };

    Chance.prototype.luhn_check = function (num) {
        var str = num.toString();
        var checkDigit = +str.substring(str.length - 1);
        return checkDigit === this.luhn_calculate(+str.substring(0, str.length - 1));
    };



    /**
     * #Description:
     * =====================================================
     * Generate random file name with extension
     *
     * The argument provide extension type
     * -> raster
     * -> vector
     * -> 3d
     * -> document
     *
     * If nothing is provided the function return random file name with random
     * extension type of any kind
     *
     * The user can validate the file name length range
     * If nothing provided the generated file name is random
     *
     * #Extension Pool :
     * * Currently the supported extensions are
     *  -> some of the most popular raster image extensions
     *  -> some of the most popular vector image extensions
     *  -> some of the most popular 3d image extensions
     *  -> some of the most popular document extensions
     *
     * #Examples :
     * =====================================================
     *
     * Return random file name with random extension. The file extension
     * is provided by a predefined collection of extensions. More about the extension
     * pool can be found in #Extension Pool section
     *
     * chance.file()
     * => dsfsdhjf.xml
     *
     * In order to generate a file name with specific length, specify the
     * length property and integer value. The extension is going to be random
     *
     * chance.file({length : 10})
     * => asrtineqos.pdf
     *
     * In order to generate file with extension from some of the predefined groups
     * of the extension pool just specify the extension pool category in fileType property
     *
     * chance.file({fileType : 'raster'})
     * => dshgssds.psd
     *
     * You can provide specific extension for your files
     * chance.file({extension : 'html'})
     * => djfsd.html
     *
     * Or you could pass custom collection of extensions by array or by object
     * chance.file({extensions : [...]})
     * => dhgsdsd.psd
     *
     * chance.file({extensions : { key : [...], key : [...]}})
     * => djsfksdjsd.xml
     *
     * @param  [collection] options
     * @return [string]
     *
     */
    Chance.prototype.file = function(options) {

        var fileOptions = options || {};
        var poolCollectionKey = "fileExtension";
        var typeRange   = Object.keys(this.get("fileExtension"));//['raster', 'vector', '3d', 'document'];
        var fileName;
        var fileExtension;

        // Generate random file name
        fileName = this.word({length : fileOptions.length});

        // Generate file by specific extension provided by the user
        if(fileOptions.extension) {

            fileExtension = fileOptions.extension;
            return (fileName + '.' + fileExtension);
        }

        // Generate file by specific extension collection
        if(fileOptions.extensions) {

            if(Array.isArray(fileOptions.extensions)) {

                fileExtension = this.pickone(fileOptions.extensions);
                return (fileName + '.' + fileExtension);
            }
            else if(fileOptions.extensions.constructor === Object) {

                var extensionObjectCollection = fileOptions.extensions;
                var keys = Object.keys(extensionObjectCollection);

                fileExtension = this.pickone(extensionObjectCollection[this.pickone(keys)]);
                return (fileName + '.' + fileExtension);
            }

            throw new Error("Chance: Extensions must be an Array or Object");
        }

        // Generate file extension based on specific file type
        if(fileOptions.fileType) {

            var fileType = fileOptions.fileType;
            if(typeRange.indexOf(fileType) !== -1) {

                fileExtension = this.pickone(this.get(poolCollectionKey)[fileType]);
                return (fileName + '.' + fileExtension);
            }

            throw new RangeError("Chance: Expect file type value to be 'raster', 'vector', '3d' or 'document'");
        }

        // Generate random file name if no extension options are passed
        fileExtension = this.pickone(this.get(poolCollectionKey)[this.pickone(typeRange)]);
        return (fileName + '.' + fileExtension);
    };


    var o_hasOwnProperty = Object.prototype.hasOwnProperty;
    var o_keys = (Object.keys || function(obj) {
      var result = [];
      for (var key in obj) {
        if (o_hasOwnProperty.call(obj, key)) {
          result.push(key);
        }
      }

      return result;
    });


    function _copyObject(source, target) {
      var keys = o_keys(source);
      var key;

      for (var i = 0, l = keys.length; i < l; i++) {
        key = keys[i];
        target[key] = source[key] || target[key];
      }
    }

    function _copyArray(source, target) {
      for (var i = 0, l = source.length; i < l; i++) {
        target[i] = source[i];
      }
    }

    function copyObject(source, _target) {
        var isArray = Array.isArray(source);
        var target = _target || (isArray ? new Array(source.length) : {});

        if (isArray) {
          _copyArray(source, target);
        } else {
          _copyObject(source, target);
        }

        return target;
    }

    /** Get the data based on key**/
    Chance.prototype.get = function (name) {
        return copyObject(data[name]);
    };

    // Mac Address
    Chance.prototype.mac_address = function(options){
        // typically mac addresses are separated by ":"
        // however they can also be separated by "-"
        // the network variant uses a dot every fourth byte

        options = initOptions(options);
        if(!options.separator) {
            options.separator =  options.networkVersion ? "." : ":";
        }

        var mac_pool="ABCDEF1234567890",
            mac = "";
        if(!options.networkVersion) {
            mac = this.n(this.string, 6, { pool: mac_pool, length:2 }).join(options.separator);
        } else {
            mac = this.n(this.string, 3, { pool: mac_pool, length:4 }).join(options.separator);
        }

        return mac;
    };

    Chance.prototype.normal = function (options) {
        options = initOptions(options, {mean : 0, dev : 1, pool : []});

        testRange(
            options.pool.constructor !== Array,
            "Chance: The pool option must be a valid array."
        );
        testRange(
            typeof options.mean !== 'number',
            "Chance: Mean (mean) must be a number"
        );
        testRange(
            typeof options.dev !== 'number',
            "Chance: Standard deviation (dev) must be a number"
        );

        // If a pool has been passed, then we are returning an item from that pool,
        // using the normal distribution settings that were passed in
        if (options.pool.length > 0) {
            return this.normal_pool(options);
        }

        // The Marsaglia Polar method
        var s, u, v, norm,
            mean = options.mean,
            dev = options.dev;

        do {
            // U and V are from the uniform distribution on (-1, 1)
            u = this.random() * 2 - 1;
            v = this.random() * 2 - 1;

            s = u * u + v * v;
        } while (s >= 1);

        // Compute the standard normal variate
        norm = u * Math.sqrt(-2 * Math.log(s) / s);

        // Shape and scale
        return dev * norm + mean;
    };

    Chance.prototype.normal_pool = function(options) {
        var performanceCounter = 0;
        do {
            var idx = Math.round(this.normal({ mean: options.mean, dev: options.dev }));
            if (idx < options.pool.length && idx >= 0) {
                return options.pool[idx];
            } else {
                performanceCounter++;
            }
        } while(performanceCounter < 100);

        throw new RangeError("Chance: Your pool is too small for the given mean and standard deviation. Please adjust.");
    };

    Chance.prototype.radio = function (options) {
        // Initial Letter (Typically Designated by Side of Mississippi River)
        options = initOptions(options, {side : "?"});
        var fl = "";
        switch (options.side.toLowerCase()) {
        case "east":
        case "e":
            fl = "W";
            break;
        case "west":
        case "w":
            fl = "K";
            break;
        default:
            fl = this.character({pool: "KW"});
            break;
        }

        return fl + this.character({alpha: true, casing: "upper"}) +
                this.character({alpha: true, casing: "upper"}) +
                this.character({alpha: true, casing: "upper"});
    };

    // Set the data as key and data or the data map
    Chance.prototype.set = function (name, values) {
        if (typeof name === "string") {
            data[name] = values;
        } else {
            data = copyObject(name, data);
        }
    };

    Chance.prototype.tv = function (options) {
        return this.radio(options);
    };




    // -- End Miscellaneous --

})();
