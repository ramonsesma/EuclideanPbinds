// Run from sclang with: TestEuclideanPbinds.run

TestEuclideanPbinds : UnitTest {

    test_pattern_empty_pulses {
        this.assertEquals(
            EuclideanRhythm.pattern(0, 8),
            [0, 0, 0, 0, 0, 0, 0, 0]
        );
    }

    test_pattern_full_when_pulses_ge_steps {
        this.assertEquals(
            EuclideanRhythm.pattern(8, 8),
            [1, 1, 1, 1, 1, 1, 1, 1]
        );
    }

    test_pattern_tresillo {
        // E(3,8) — Cuban tresillo, X..X..X.
        this.assertEquals(
            EuclideanRhythm.pattern(3, 8),
            [1, 0, 0, 1, 0, 0, 1, 0]
        );
    }

    test_pattern_cinquillo {
        // E(5,8) — Cuban cinquillo, X.XX.XX.
        this.assertEquals(
            EuclideanRhythm.pattern(5, 8),
            [1, 0, 1, 1, 0, 1, 1, 0]
        );
    }

    test_pattern_rotation_shifts_first_hit {
        var base = EuclideanRhythm.pattern(3, 8);
        var rot1 = EuclideanRhythm.pattern(3, 8, 1);
        this.assertEquals(rot1[0], 0);
        this.assertEquals(rot1[1], base[0]);
    }

    test_onset_indices {
        // E(3,8) hits at 0, 3, 6.
        this.assertEquals(EuclideanRhythm.onsetIndices(3, 8), [0, 3, 6]);
    }

    test_onset_seconds_aligned_to_grid {
        // E(3,4) at 120 bpm, stepBeats = 1 (quarter note) -> hits at 0, 0.5, 1.0
        var s = EuclideanRhythm.onsetSeconds(3, 4, 120, 1);
        this.assertEquals(s.size, 3);
        this.assertFloatEquals(s[0], 0, "first");
        this.assertFloatEquals(s[1], 0.5, "second");
        this.assertFloatEquals(s[2], 1.0, "third");
    }

    test_visualise_tresillo {
        this.assertEquals(EuclideanRhythm.visualise(3, 8), "X..X..X.");
    }

    test_presets_named_lookups {
        this.assertEquals(EuclideanPresets.get(\tresillo), [3, 8, 0]);
        this.assertEquals(EuclideanPresets.get(\cinquillo), [5, 8, 0]);
        this.assert(EuclideanPresets.names.includes(\africanBell));
    }

    test_presets_unknown_returns_nil {
        this.assert(EuclideanPresets.get(\nonsense).isNil);
    }

    test_presets_pattern_helper {
        this.assertEquals(
            EuclideanPresets.pattern(\tresillo),
            [1, 0, 0, 1, 0, 0, 1, 0]
        );
    }

    test_peuclidean_yields_pattern_then_stops {
        var stream = Peuclidean(3, 8, 0, 1).asStream;
        var collected = Array.fill(8, { stream.next });
        this.assertEquals(collected, [1, 0, 0, 1, 0, 0, 1, 0]);
        // 9th call should be nil — finite repeats finished.
        this.assert(stream.next.isNil, "exhausted stream returns nil");
    }

    test_peuclidean_repeats_loop {
        var stream = Peuclidean(3, 4, 0, 3).asStream;
        var collected = Array.fill(12, { stream.next });
        // E(3,4) = [1,1,1,0] repeated 3x
        this.assertEquals(
            collected,
            [1, 1, 1, 0,  1, 1, 1, 0,  1, 1, 1, 0]
        );
    }

    test_euclidean_pbind_returns_pbind {
        var pbind = EuclideanPbind(3, 8, (instrument: \default, dur: 0.25));
        this.assert(pbind.isKindOf(Pbind), "returns a Pbind");
    }

    test_euclidean_pbind_passes_extras_through {
        // The `freq` arg isn't reserved, so it should land on the resulting
        // Pbind's pairs intact.
        var pbind = EuclideanPbind(3, 8, (instrument: \default, freq: 220));
        var pairs = pbind.patternpairs;
        var freqIndex = pairs.indexOf(\freq);
        this.assert(freqIndex.notNil, "freq passed through");
        this.assertEquals(pairs[freqIndex + 1], 220);
    }

    test_euclidean_pbind_preset_constructor {
        var pbind = EuclideanPbind.preset(\cinquillo, (instrument: \default));
        this.assert(pbind.isKindOf(Pbind));
    }

    test_euclidean_pbind_preset_unknown_throws {
        this.assertException({
            EuclideanPbind.preset(\nonsense, ());
        }, Error, "unknown preset throws");
    }
}
