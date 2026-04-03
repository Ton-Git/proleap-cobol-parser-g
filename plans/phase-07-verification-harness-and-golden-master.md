# Phase 07 Implementation Plan - Verification Harness and Engine Qualification

> For Hermes: Equivalence is the real product. Treat the verification harness as a first-class platform, not a side utility.

Goal: Build the test harnesses, fixtures, trace collectors, and comparison tools required to prove that generated Java behaves like the original COBOL on representative reference programs and fixture suites.

Architecture: Verification sits beside parsing and generation as an independent evidence pipeline. It executes COBOL baselines and generated Java against the same reference inputs, captures outputs and traces, and computes equivalence reports. It supports replay, delta analysis, and engine qualification evidence rather than production release evidence. It also acts as the authoritative accept or reject stage after every human-guided coding-agent pass.

Tech Stack: Test orchestration tools, containers or controlled runtime environments, structured traces, diff and comparison engines, artifact storage, dashboards.

---

## Phase objective

Create a repeatable equivalence-testing system that can qualify the engine against representative workloads before any real migration program begins.

## In scope

- Reference and synthetic golden-master fixture curation
- Dual-execution harness
- Output, file, DB, and trace comparison
- Human-assisted mismatch triage workflow
- Tolerance and policy rules for acceptable differences
- Evidence packaging for engine qualification runs
- Standard feedback bundles for human remediation passes

## Concrete engine outputs targeted in this phase

- slice-level verification report
- integration checkpoint verification report
- whole-program equivalence report
- remediation feedback bundle for failed slices
- evidence model that ties validation back to task contract and generated project version

## Ordered work packages

### Work Package 1: Build the dual-execution harness

Objective: Execute COBOL and generated Java against the same reference inputs and collect comparable evidence.

Tasks:
1. Standardize input fixture packaging for both COBOL and Java execution.
2. Capture outputs, files, DB effects, and traces in structured formats.
3. Normalize environment differences that are irrelevant to equivalence.
4. Version verification runs and keep them reproducible.
5. Make reports addressable by slice, integration checkpoint, and whole program.

### Work Package 2: Standardize verification-to-human handoff

Objective: Make verification output directly usable by a developer running a coding agent manually.

Tasks:
1. Standardize the failed-slice feedback bundle used for the next human remediation pass so verification becomes an explicit part of the alternating conversion loop.
2. Include compiler output, failing checks, narrowed hypotheses, editable-zone references, and unchanged stop conditions.
3. Define the minimum evidence a human must attach when resubmitting a slice after agent-assisted remediation.
4. Distinguish slice-level failures from cross-slice integration failures and whole-program convergence failures.
5. Feed accepted triage patterns back into orchestration rules so future handoff packets use smaller and more diagnostic context bundles.

### Work Package 3: Define acceptance policy and escalation routing

Objective: Keep verification authoritative and actionable.

Tasks:
1. Define which mismatches are fatal, tolerable, or review-required.
2. Route failures to parser, IR, runtime, codegen, fixture, or manual-remediation owners instead of repeating blind retries.
3. Define retry budget interaction with slice state and task contracts.
4. Define how checkpoint failures block downstream slice execution.
5. Make whole-program acceptance contingent on completed verification evidence.

## Phase completion gate

Proceed only when verification can repeatedly accept or reject slice-level, integration-level, and whole-program outputs with machine-readable evidence that directly drives the next human action.
