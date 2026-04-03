# Human-Guided Coding-Agent Conversion Loop Architecture

Purpose: Define the bounded, repeatable execution loop that uses a human-selected coding agent inside the deterministic conversion engine.

A coding agent may help implement code.
The engine decides whether the code is accepted.

## Loop

1. Deterministic preparation
2. Deterministic scaffold generation
   - attach acceptance checks
3. Human coding-agent implementation pass
   - send one bounded slice bundle to a human developer
4. Deterministic validation
5. Deterministic remediation bundle
6. Human remediation or next-slice pass

This loop is working when a reference program can move from validated IR to accepted Java through multiple bounded deterministic and human-guided coding-agent passes with complete evidence.
