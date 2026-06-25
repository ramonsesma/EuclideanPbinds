// Named euclidean rhythms drawn from the literature (Toussaint 2013,
// "The Geometry of Musical Rhythm"). Each entry is [pulses, steps, rotation]
// — feed straight to EuclideanRhythm.pattern / Peuclidean / EuclideanPbind.
//
//   EuclideanPresets.get(\tresillo)      // -> [3, 8, 0]
//   EuclideanPresets.names               // -> all preset Symbols

EuclideanPresets {
    classvar <table;

    *initClass {
        table = IdentityDictionary[
            // Afro-Cuban
            \tresillo     -> [3, 8, 0],     // X..X..X.
            \cinquillo    -> [5, 8, 0],     // X.XX.XX.
            \sonClave     -> [3, 16, 0],
            \rumbaClave   -> [5, 16, 0],
            // West African + Middle East
            \africanBell  -> [7, 12, 0],
            \cumbia       -> [3, 4, 0],
            \shiko        -> [4, 16, 0],
            // 4/4 staples
            \fourOnFloor  -> [4, 16, 0],
            \offbeats     -> [4, 16, 2],
            \hatChop      -> [8, 16, 0],
            // Polyrhythmic / odd
            \trio23       -> [2, 3, 0],
            \quintolet    -> [5, 12, 0],
            \bossa        -> [5, 16, 0]
        ];
    }

    *get { |name|
        ^table[name.asSymbol]
    }

    *names {
        ^table.keys.asArray.sort
    }

    *pattern { |name|
        var spec = this.get(name);
        if (spec.isNil) { ^nil };
        ^EuclideanRhythm.pattern(spec[0], spec[1], spec[2])
    }
}
