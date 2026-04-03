# Generation Strategy

Purpose: Define how the engine turns validated IR into Java and Spring artifacts for one target microservice.

## Two-layer generation model

Layer 1: executable Java generation
- state holders
- procedural logic classes
- runtime calls
- source provenance markers

Layer 2: single-microservice assembly
- domain service placement
- job/orchestration placement
- API adapter placement
- configuration and dependency wiring

## Generation priorities

1. correctness and traceability
2. stable runtime binding
3. deterministic regeneration
4. clear internal boundaries inside the single microservice

## Regeneration rule

Generated files must be regenerable from persisted artifacts.
Manual logic is permitted only in explicit extension zones.
