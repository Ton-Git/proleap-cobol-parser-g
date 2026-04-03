# Transformation Pipeline

Purpose: Define the deterministic lowering path from parser artifacts to codegen-ready IR and slice bundles.

## Pipeline stages

1. Parse artifact import
2. Symbol normalization
3. Data structure normalization
4. Control-flow extraction
5. Data-flow annotation
6. External interaction extraction
7. Pattern classification
8. Unsupported-node marking
9. Slice generation

## Stage outputs

Each stage must emit:
- versioned checkpoint identifier
- diagnostics
- timings
- deterministic success or failure status

## Failure policy

- do not silently skip unsupported semantics
- fail a stage with structured diagnostics when policy requires
- allow downstream manual-remediation routing where safe

## Replay rule

Given the same input artifacts and versions, the pipeline must produce the same IR and slice bundle outputs.
