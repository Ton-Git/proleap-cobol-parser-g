# Runbook: Per-Program Agentic Conversion

Purpose: Convert one large logical COBOL program spread across many files through the deterministic plus human-driven coding-agent loop.

## Preconditions

- whole-program ingestion manifest exists
- copybook resolution is complete enough for the current ready slices
- IR validation passed for the current conversion scope
- target role inside the single microservice is assigned for each slice
- fixture data exists for at least one meaningful checkpoint
- runtime and template versions are pinned

## Procedure

1. confirm whole-program readiness and blocked states
2. choose the next ready slice from the program backlog
3. generate deterministic scaffold
4. prepare slice bundle, task contract, and human handoff packet
5. human developer runs a chosen coding agent on the bounded slice
6. run deterministic validation
7. accept, retry, or escalate
8. repeat until the slice backlog is exhausted or blocked
9. run cross-slice integration verification checkpoints
10. when all required slices are accepted, run whole-program verification and attach the result to its assigned module set inside the single microservice

## Definition of done

The program is done when:
- all required slices are accepted or explicitly rerouted
- integration checkpoints pass
- program-level compile passes
- required golden-master checkpoints pass
- target module packaging is assigned and generated
- evidence is complete enough for qualification review
