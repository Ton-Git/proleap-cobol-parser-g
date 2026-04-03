# AI Agent Operating Model

Purpose: Define how coding agents participate in the engine without becoming a source of uncontrolled semantic drift.

## Principle

The engine does not depend on automatic invocation of a specific coding agent.
A human developer chooses and operates an agent against bounded deterministic handoff packets.

## Roles

Human developer
- selects the next ready slice
- chooses the coding agent
- runs prompts within the bounded packet
- records prompt/model summary and assumptions
- submits the result back to deterministic validation

Deterministic engine
- prepares scaffolds and handoff packets
- defines boundaries and acceptance checks
- runs compile/test/equivalence gates
- records acceptance, retry, escalation, and evidence

## Approved usage model

- bounded human-guided implementation
- bounded remediation
- bounded rule suggestion
- bounded mismatch investigation

## Disallowed usage model

- unconstrained whole-codebase prompting
- silent edits outside approved zones
- accepting agent output without deterministic rerun
