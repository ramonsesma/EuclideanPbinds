# EuclideanPbinds

![SuperCollider Quarks cover](assets/supercollider-quarks-cover.png)

[![Release](https://img.shields.io/github/v/release/ramonsesma/EuclideanPbinds)](https://github.com/ramonsesma/EuclideanPbinds/releases)
[![Validate](https://img.shields.io/github/actions/workflow/status/ramonsesma/EuclideanPbinds/validate.yml?branch=main&label=validate)](https://github.com/ramonsesma/EuclideanPbinds/actions/workflows/validate.yml)
[![License](https://img.shields.io/github/license/ramonsesma/EuclideanPbinds)](https://github.com/ramonsesma/EuclideanPbinds/blob/main/LICENSE)
[![Quark](https://img.shields.io/badge/quark-0.1.0-blue)](https://github.com/ramonsesma/EuclideanPbinds/releases/tag/0.1.0)

A SuperCollider quark for Bjorklund euclidean rhythms â€” patterns, beats,
and a playable Pbind wrapper.

| Class | Purpose |
|---|---|
| `EuclideanRhythm` | Bjorklund pattern as Array, onset indices, beat-aligned seconds, post-window visualisation. |
| `Peuclidean` | `Pattern` subclass â€” yields the bit stream, composable with any Pbind input. |
| `EuclideanPbind` | Convenience constructor: bits â†’ `\note`/`\rest` events. Passes any extra Pbind key through. |
| `EuclideanPresets` | Named rhythms: `\tresillo`, `\cinquillo`, `\sonClave`, `\africanBell`, `\cumbia`, `\fourOnFloor`, â€¦ |

## Quick start

```supercollider
// Tresillo on the default synth, 1/16 grid
EuclideanPbind(3, 8, (instrument: \default, dur: 0.25)).play;

// Cinquillo on a saw at 220 Hz
EuclideanPbind(5, 8, (instrument: \default, dur: 0.125, freq: 220)).play;

// Preset by name
EuclideanPbind.preset(\africanBell, (instrument: \default, dur: 0.1)).play;

// Modulate amp with the pattern bits
Pbind(\instrument, \default, \dur, 0.25,
      \amp, Peuclidean(7, 12) * 0.6).play;
```

## Install

```supercollider
Quarks.install("https://github.com/ramonsesma/EuclideanPbinds");
```

Local dev: clone into `downloaded-quarks/` and add the path to
`sclang_conf.yaml`.

## Test

Run from the repository root:

```powershell
& 'C:\Program Files\SuperCollider-3.14.1\sclang.exe' -D -r -s --include-path 'Classes' --include-path 'tests' 'tests\RunEuclideanPbinds.scd'
```

Or inside sclang after loading the Quark classes:

```supercollider
TestEuclideanPbinds.run;
```

## Authoring notes

The Bjorklund implementation is identical to the one in our Studio Sesma
TypeScript bridge (`supercollider-bridge/src/services/euclidean.ts`) so
sclang and Node produce the same 1/0 sequences for any `(pulses, steps,
rotation)` triple.

License: MIT.

