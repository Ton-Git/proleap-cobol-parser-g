# Phase 06 Implementation Plan - Java and Spring Code Generation

> For Hermes: Generate conservative, runnable Java first. Spring packaging comes after execution is trustworthy.

Goal: Build code generators that transform the normalized IR into compilable Java and package the output into one Spring Boot microservice structure.

Architecture: Code generation is split into two layers: executable Java generation and microservice assembly. The first layer emits behavior-preserving Java against the compatibility runtime. The second layer places generated logic into the single target Spring Boot microservice using well-defined internal module boundaries such as domain services, adapters, controllers, jobs, and support libraries. Human developers may use a coding agent such as Claude Code on top of deterministic scaffolds, but deterministic validation remains authoritative.

Tech Stack: Java code generation library or AST model, formatter and linter integration, Spring Boot, Maven build templates for v1, MyBatis or JDBC where required by the supported scope; no Gradle templates, distributed orchestration, or top-level module split in the first integrated build.

---

## Phase objective

Turn validated IR into compilable, runnable, reviewable Java packages aligned to the one Spring Boot microservice target.

## In scope

- Generator core and template infrastructure
- Human-driven template and remediation authoring workflow
- Human-executed bounded conversion loops from IR bundles to generated application packages
- Java emission from IR
- Single-microservice Spring Boot packaging
- Generator diagnostics and unsupported-node gating
- Compile and test pipeline for generated outputs

## Deliverables

1. `/src/main/java/io/proleap/cobol/engine/codegen/`
2. `/src/main/java/io/proleap/cobol/engine/orchestration/`
3. `/src/main/java/io/proleap/cobol/engine/agent/`
4. `/src/test/java/io/proleap/cobol/engine/`
5. `/docs/architecture/generation-strategy.md`
6. `/docs/architecture/agent-assisted-codegen-policy.md`
7. `/docs/architecture/agent-driven-conversion-workflow.md`
8. `/docs/runbooks/generated-code-review.md`
9. `/docs/runbooks/agent-executed-program-conversion.md`
10. `/docs/architecture/alternating-deterministic-agent-loop.md`
11. `/docs/schemas/program-conversion-record.schema.json`
12. `/docs/schemas/agent-task-contract.schema.json`
13. `/docs/runbooks/human-agent-handoff.md`

## Concrete engine outputs targeted in this phase

- generated project skeleton for one whole program target
- per-slice Java scaffold with explicit editable zones
- task contracts and human handoff packets
- generated source maps and runtime bindings
- integration checkpoint definitions
- program conversion record capturing retries, evidence, and convergence state

## Default execution pattern for one logical program

1. deterministically select one ready conversion slice from the whole-program backlog
2. deterministically generate the Java baseline, editable zones, and failing tests for that slice
3. the engine emits a handoff packet and task contract
4. a human developer uses a chosen coding agent, for example Claude Code, to implement only that slice
5. deterministically compile, lint, test, and verify
6. if the slice fails, emit a remediation bundle and rerun a bounded human-guided pass
7. if the slice passes, persist the generated project state and advance to the next slice or integration checkpoint
8. accept the program only when all required slices and checkpoints pass

## Ordered work packages

### Work Package 1: Build conservative Java generation

Objective: Emit runnable Java before pursuing aggressive refactoring or beautification.

Tasks:
1. Generate Java structures that preserve control flow and data access patterns.
2. Keep generated code bound to runtime APIs rather than inlining COBOL semantics.
3. Preserve provenance from IR nodes to generated files and methods.
4. Emit diagnostics for unsupported nodes instead of silently guessing.
5. Add compile-only smoke tests for generated outputs early.

### Work Package 2: Assemble the single Spring Boot target

Objective: Place generated code into one coherent application shape.

Tasks:
1. Define the generated package and artifact layout for the single microservice without introducing new top-level build modules in v1.
2. Separate internal roles such as domain services, adapters, data access, controllers, jobs, and support libraries.
3. Generate application bootstrap, configuration, and build files.
4. Persist the generated project as a stable artifact between slice passes.
5. Define how multiple accepted slices merge into one generated project safely.

### Work Package 3: Productize the alternating deterministic plus human coding-agent loop

Objective: Turn the “deterministic step, human coding-agent step, deterministic step, human coding-agent step” idea into the standard way one large logical program gets converted.

Tasks:
1. Generate per-slice Java scaffolds with explicit editable zones, runtime bindings, source maps, and failing acceptance tests before human handoff.
2. Build the controller action that emits a handoff packet for a human developer, including slice bundle, dependency summary, editing rules, stop conditions, and validation checklist.
3. After each human-submitted pass, automatically run formatting, generator-consistency checks, compilation, static analysis, fixture tests, and golden-master checkpoints.
4. When validation fails, emit a remediation bundle containing diff summary, compiler errors, failing tests, runtime traces, and narrowed hypotheses for the next human-driven pass.
5. Limit retries per slice and define escalation rules to runtime, IR, parser, or manual-remediation owners.
6. Define program-level integration checkpoints where multiple accepted slices must compile and verify together before more slices are attempted.

### Work Package 4: Standardize acceptance artifacts

Objective: Make every slice pass and retry reproducible.

Tasks:
1. Define the generated project artifact that the next deterministic pass reads.
2. Define the program conversion record that stores slice history, retry counts, and convergence state.
3. Persist task contracts, handoff packets, integration checkpoints, and feedback bundles in stable schemas.
4. Record what files are editable versus controller-owned.
5. Make the controller capable of reconstructing the full current project state from stored artifacts.

## Phase completion gate

Proceed only when a reference logical program can move through multiple alternating deterministic and human-guided coding-agent passes with repeatable generated project artifacts, bounded editable zones, integration checkpoints, and acceptance evidence at every loop iteration.

For the first integrated build, this must work using the current single-module repo plus generated project artifacts under `target/engine/`, before any module decomposition or external orchestration is attempted.
