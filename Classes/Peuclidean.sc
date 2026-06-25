// Pattern subclass that yields the Bjorklund pattern as a stream of 1s and 0s.
// Use it directly as a multiplier on \amp / \trig inside any Pbind, or wrap
// it via EuclideanPbind for the common "hit on every 1, rest on every 0"
// drum-machine pattern.
//
//   // Modulate the amp of a synth: hits are 1, rests are 0.
//   Pbind(\instrument, \saw, \dur, 0.25,
//         \amp, Peuclidean(3, 8) * 0.6).play;
//
//   // Use as a trigger to gate other patterns.
//   Pbind(\dur, 0.125, \trig, Peuclidean(5, 8, repeats: inf)).asStream;
//
// Differs from Pbjorklund (in the Bjorklund quark) in that this one accepts
// rotation and uses the same Bjorklund implementation as the rest of this
// package — no cross-quark dependency.

Peuclidean : Pattern {
    var <>pulses, <>steps, <>rotation, <>repeats;

    *new { |pulses, steps, rotation = 0, repeats = inf|
        ^super.newCopyArgs(pulses, steps, rotation, repeats)
    }

    storeArgs {
        ^[pulses, steps, rotation, repeats]
    }

    embedInStream { |inval|
        var pat = EuclideanRhythm.pattern(pulses.value, steps.value, rotation.value);
        var rep = repeats.value(inval);
        rep.do({
            pat.do({ |bit|
                inval = bit.yield;
            });
        });
        ^inval
    }
}
