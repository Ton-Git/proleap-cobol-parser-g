# Deterministic vs Agentic Transform Boundary

Purpose: Freeze which parts of the engine are deterministic and which may use bounded agents.

## Deterministic boundary

Must remain deterministic:
- parser execution
- copybook resolution
- IR lowering
- slice generation
- scaffold generation
- validation and verification
- acceptance and escalation decisions

## Agentic boundary

May use agents:
- bounded implementation inside declared edit zones
- template suggestions
- remediation for one failed slice
- triage summaries

## Boundary test

If a step changes engine truth, policy, or semantic representation, it must be deterministic or converted into deterministic assets before release.
