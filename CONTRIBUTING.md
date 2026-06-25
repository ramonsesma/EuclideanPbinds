# Contributing

Thanks for helping improve `EuclideanPbinds`.

## Scope

This repository is a standalone public SuperCollider Quark. Changes should stay focused on:

- rhythm generation, presets, and pattern behavior
- Pbind and stream integration
- tests, schelp, README, and release metadata

## Local Development

Run the UnitTests from the repository root:

```powershell
& 'C:\Program Files\SuperCollider-3.14.1\sclang.exe' -D -r -s --include-path 'Classes' --include-path 'tests' 'tests\RunEuclideanPbinds.scd'
```
