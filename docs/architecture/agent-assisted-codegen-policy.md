# Agent-Assisted Codegen Policy

Purpose: Define how agents may participate in code generation work.

## Allowed activities

- implement one declared slice
- suggest template changes from bounded fixtures
- remediate a failed generated slice
- generate packaging scaffolds in declared files

## Required preconditions

- baseline scaffold exists
- allowed files and edit zones exist
- acceptance checks exist
- runtime contracts are pinned

## Acceptance rule

Agent-authored changes count only when they are either:
- promoted into deterministic templates or rules, or
- retained in explicit manual-extension zones with rationale recorded
