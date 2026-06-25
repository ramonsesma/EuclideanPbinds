// Bjorklund's algorithm (Toussaint 2005) — distributes `pulses` hits across
// `steps` slots as evenly as integer arithmetic allows. The resulting
// 1s-and-0s array is what makes E(3,8) the Cuban tresillo, E(5,8) the
// cinquillo, E(7,12) the West-African bell, and so on.
//
// The Bjorklund quark by redFrik already ships the algorithm as Bjorklund.scd
// / Pbjorklund.sc. EuclideanRhythm restates it here so this quark has no
// hard dependency on Bjorklund, and exposes the result in a couple of forms
// (raw array, indexed onset positions, beat-aligned seconds) that the
// Pattern + Pbind wrappers in this package consume directly.

EuclideanRhythm {

    // Returns an Array of 1s and 0s, length = steps.
    *pattern { |pulses, steps, rotation = 0|
        var p, n, groups, pulseCount, restCount, moves, next, newPulseLen, flat, len, offset;
        p = pulses.floor.max(0);
        n = steps.floor.max(0);
        if (n == 0) { ^[] };
        if (p == 0) { ^Array.fill(n, { 0 }) };
        if (p >= n) { ^Array.fill(n, { 1 }) };

        groups = Array.newClear(n);
        p.do({ |i| groups[i] = [1] });
        (n - p).do({ |i| groups[p + i] = [0] });
        pulseCount = p;
        restCount = n - p;

        while ({ restCount > 1 }, {
            moves = min(pulseCount, restCount);
            next = Array.new(groups.size);
            moves.do({ |i|
                next = next.add(groups[i] ++ groups[groups.size - moves + i]);
            });
            (groups.size - (moves * 2)).do({ |i|
                next = next.add(groups[i + moves]);
            });
            groups = next;
            newPulseLen = groups[0].size;
            pulseCount = groups.count({ |g| g.size == newPulseLen });
            restCount = groups.size - pulseCount;
        });

        flat = groups.flatten(1);
        len = flat.size;
        offset = ((rotation.floor % len) + len) % len;
        ^(flat.copyRange(len - offset, len - 1) ++ flat.copyRange(0, len - offset - 1))
    }

    // Just the indices where the pattern is 1. Useful for scheduling
    // discrete hits without iterating over every rest.
    *onsetIndices { |pulses, steps, rotation = 0|
        var pat = this.pattern(pulses, steps, rotation);
        var out = List.new;
        pat.do({ |bit, i| if (bit > 0) { out.add(i) } });
        ^out.asArray
    }

    // Onset times in seconds given a tempo and a step length in beats
    // (default 0.25 = 1/16 grid).
    *onsetSeconds { |pulses, steps, bpm, stepBeats = 0.25, rotation = 0|
        var indices = this.onsetIndices(pulses, steps, rotation);
        var stepSec = (60 / bpm) * stepBeats;
        ^indices.collect({ |i| i * stepSec })
    }

    // String visualisation: "X..X..X." for E(3,8). Handy in the post window
    // when iterating on a rhythm.
    *visualise { |pulses, steps, rotation = 0|
        var pat = this.pattern(pulses, steps, rotation);
        ^pat.collect({ |b| if (b > 0) { $X } { $. } }).join
    }
}
