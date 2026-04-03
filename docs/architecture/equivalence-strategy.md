# Equivalence Strategy

Purpose: Define how the engine decides that generated Java preserves COBOL behavior for supported-v1 scope.

## Comparison layers

- program outputs
- files and records
- relevant DB effects
- trace checkpoints
- policy-normalized differences

## Result classes

- exact match
- normalized acceptable difference
- manual review required
- fail

## Qualification rule

Supported-v1 claims require qualification evidence from representative fixtures, not only compile success.

## Normalization examples

Examples allowed only by policy:
- timestamps
- generated sequence ids
- ordering where business semantics do not depend on order

## Non-negotiable rule

Golden-master and verification outcomes outrank agent confidence, reviewer intuition, and compile-only success.
