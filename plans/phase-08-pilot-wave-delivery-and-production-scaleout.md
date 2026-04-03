# Phase 08 Implementation Plan - End-to-End Engine Validation on Reference Corpus

> For Hermes: This phase proves the engine can deliver end-to-end value on representative reference programs. Start small, measure hard, and feed learnings back into the engine.

Goal: Validate the full conversion engine on a curated reference corpus, prove end-to-end viability for the deterministic plus human-driven coding-agent loop, and prepare the engine for later real-world adoption.

Architecture: This phase uses the end-to-end engine built in earlier phases: ingestion, indexing, IR, runtime, generation, orchestration, human handoff packets, and verification. Validation adds qualification runs, runbooks, throughput metrics, remediation queues, and repeatability checks so the platform behaves like a usable conversion engine rather than a disconnected prototype.

Tech Stack: Full engine stack from prior phases, plus qualification automation, environment provisioning for reference runs, and operations dashboards.

---

## Phase objective

Validate the engine on a carefully selected reference corpus, then harden the orchestration and evidence model for repeatable later adoption.

## In scope

- Reference corpus selection and qualification planning
- End-to-end conversion of representative reference programs
- Human-orchestrated execution workflow for qualification runs
- Engine-operability rehearsals in non-production qualification environments
- Throughput and reliability measurement
- Feedback loops into parser, IR, runtime, and codegen

## Concrete engine outputs targeted in this phase

- reference-program qualification records
- throughput and retry dashboards
- per-program execution evidence bundles
- accepted patterns and common blocker catalog
- updated slice-sizing and handoff heuristics based on real runs

## Ordered work packages

### Work Package 1: Define human-orchestrated qualification execution

Objective: Use coding agents as controlled tools during qualification runs without losing deterministic control.

Tasks:
1. Break each reference-program conversion into bounded human-executed tasks such as analysis, rule suggestion, codegen refinement, mismatch triage, and documentation.
2. Define orchestration patterns for different coding agents, including handoff artifacts and submission requirements.
3. Add a controller workflow that only advances a program when deterministic gates pass: parse, IR validation, compile, verification.
4. Define maximum task scope and context package size for each handoff packet.
5. Track productivity metrics such as accepted suggestions, rejected suggestions, retries, and time saved.
6. Define a standard per-program execution loop: classify, package bounded context, hand off to developer, run deterministic gates, persist evidence, and either accept, retry, or escalate.
7. Define when to use one developer or agent lane versus multiple specialist lanes for analysis, codegen, and verification triage.
8. Make the default engine-validation rhythm explicit: deterministic preparation, human coding-agent implementation, deterministic validation, deterministic feedback packaging, human remediation or next-slice implementation, repeated until program acceptance.

### Work Package 2: Measure end-to-end viability

Objective: Prove the loop is workable at program scale, not just in isolated unit tests.

Tasks:
1. Measure slice throughput, retry rates, checkpoint failure rates, and average time to acceptance.
2. Record the most common reasons a slice leaves the main loop for parser, IR, runtime, or manual-remediation work.
3. Compare different slice-sizing strategies and handoff bundle sizes.
4. Confirm that evidence capture remains complete under repeated retries.
5. Feed results back into earlier-phase prioritization.

## Phase completion gate

Proceed only when the engine proves it can repeatedly produce verifiable outputs on the reference corpus and human operators can run the qualification workflow confidently using bounded coding-agent passes.
