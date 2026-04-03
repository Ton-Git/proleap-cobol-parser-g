# Quality Gates

Purpose: Define the mandatory gates that control whether a slice, program, or qualification batch may advance.

## Gate families

1. Parse gates
   - manifest valid
   - source readable
   - copybooks resolved or explicitly diagnosed
   - parse artifact persisted
2. Intelligence gates
   - dependency graph built
   - classification present
   - slice readiness state assigned
3. IR gates
   - IR schema valid
   - unsupported nodes explicit
   - provenance links intact
4. Scaffold gates
   - allowed files declared
   - edit zones declared
   - acceptance checks attached
5. Agent execution gates
   - prompt provenance recorded
   - bounded context package recorded
   - stop conditions declared
6. Validation gates
   - allowed-file and edit-zone compliance
   - compile success
   - static analysis threshold pass
   - fixture test pass
   - golden-master pass or policy-approved normalized difference
7. Release gates for the engine
   - qualification batch pass threshold met
   - no unresolved high-severity platform defect in supported-v1 scope
   - evidence bundle complete

## Severity levels

- Blocker: no slice or program may advance
- High: may advance only by explicit override outside supported-v1 guarantees
- Medium: recorded and tracked but not automatically blocking if policy allows
- Low: informational

## Slice acceptance rule

A slice is accepted only if all blocking gates pass and any non-blocking differences are explicitly classified.

## Program acceptance rule

A program is accepted only if:
- all slices are accepted or explicitly rerouted
- integrated compile passes
- integrated verification checkpoints pass
- target module placement is assigned inside the single microservice

## Engine release rule

The engine is releasable only if:
- supported-v1 reference corpus qualification passes at target threshold
- top failing patterns are understood
- no hidden semantic gaps remain in supported-v1 claims
