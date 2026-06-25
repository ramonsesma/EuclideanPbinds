// Convenience constructor: euclidean rhythm → ready-to-play Pbind. The hits
// of the Bjorklund pattern become \note events, the rests become \rest, so
// any voice / sample SynthDef plugs in unchanged.
//
//   EuclideanPbind(3, 8, (instrument: \default, dur: 0.25, amp: 0.4, freq: 220)).play;
//   EuclideanPbind(*EuclideanPresets.get(\tresillo).copyRange(0, 1),
//                  (instrument: \kick, dur: 0.125)).play;
//
// Reserved keys: \pulses \steps \rotation \repeats \instrument \dur \amp.
// Everything else in `args` (freq, degree, scale, midinote, ...) passes
// straight through to the Pbind so the standard pitch / event model is
// available without ceremony.

EuclideanPbind {
    classvar <reservedKeys;

    *initClass {
        reservedKeys = IdentitySet[
            \pulses, \steps, \rotation, \repeats,
            \instrument, \dur, \amp
        ];
    }

    *new { |pulses, steps, args|
        var bits, types, finalArgs, repeats;
        args = args ?? ();
        repeats = args[\repeats] ? inf;
        bits = EuclideanRhythm.pattern(pulses, steps, args[\rotation] ? 0);
        types = bits.collect({ |b| if (b == 1) { \note } { \rest } });

        finalArgs = [
            \instrument, args[\instrument] ? \default,
            \type,       Pseq(types, repeats),
            \dur,        args[\dur] ? 0.25,
            \amp,        args[\amp] ? 0.5
        ];

        // Pass through everything that isn't already handled. Lets the caller
        // set \freq, \degree, \scale, \midinote, \pan, …
        args.keysValuesDo({ |k, v|
            if (reservedKeys.includes(k).not) {
                finalArgs = finalArgs ++ [k, v];
            };
        });

        ^Pbind(*finalArgs)
    }

    // Same as new but takes a preset name instead of pulses/steps.
    *preset { |name, args|
        var spec = EuclideanPresets.get(name);
        var mergedArgs;
        if (spec.isNil) {
            Error("Unknown euclidean preset:" + name).throw;
        };
        mergedArgs = args ?? ();
        mergedArgs = mergedArgs.copy;
        if (mergedArgs[\rotation].isNil) { mergedArgs[\rotation] = spec[2] };
        ^this.new(spec[0], spec[1], mergedArgs)
    }
}
