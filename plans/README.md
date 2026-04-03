# COBOL to Java Spring Boot Conversion Engine Implementation Plan

> For Hermes: Use subagent-driven-development for execution once individual phase scopes are approved and resourced.

Goal: Turn this repository from a COBOL parser into the front end of a deterministic plus human-driven coding-agent conversion engine capable of converting a very large COBOL codebase that together implements one large program into one Spring Boot microservice.

Architecture: The parser remains the COBOL front end. Around it, we build whole-program ingestion, copybook and dialect resolution, semantic indexing, a normalized intermediate representation (IR), a COBOL compatibility runtime, Java/Spring code generators, a deterministic orchestration controller, human handoff packets for bounded coding-agent work, and a golden-master verification harness. The immediate objective is to build the engine itself and qualify it on representative reference programs, not to run a real customer cutover yet.

Tech Stack: Java 17+, Maven multi-module build, ANTLR4, Spring Boot, optional internal job/orchestration components, JDBC/MyBatis, PostgreSQL or graph/search store for indexes, JSON/Parquet/Protobuf artifacts, Docker, GitHub Actions, OpenTelemetry/Prometheus/Grafana.

---

## Feasibility verdict

Yes, your idea is doable.

The key constraint is that the coding agent must not be asked to convert the whole COBOL program in one free-form pass. The practical model is:

1. deterministic preparation narrows the problem
2. deterministic scaffolding creates bounded editable output
3. a human developer uses a coding agent such as Claude Code on one bounded slice
4. deterministic validation accepts or rejects the result
5. deterministic feedback packaging narrows the next retry
6. the human developer uses the coding agent again only on the narrowed retry or on the next ready slice

In short: the human plus coding agent is the worker, but the deterministic pipeline is the judge.

## Current scope: build the conversion engine, not run the migration program

In scope now:

- implement whole-program ingestion for one large COBOL program composed of many files and copybooks
- implement the deterministic conversion pipeline
- implement the human-driven bounded coding-agent loop
- implement runtime, codegen, and verification machinery
- implement operator tooling, schemas, handoff packets, and evidence handling
- qualify the engine on reference corpora, synthetic fixtures, and sample conversions targeting one Spring Boot microservice

Explicitly out of scope for now:

- real customer estate onboarding and execution
- production cutover planning
- production hypercare
- decommissioning legacy runtime dependencies in customer environments
- business UAT and rollout governance

## Non-negotiable operating assumptions

- The target is one Spring Boot microservice, not many generated microservices.
- The large COBOL estate is one logical program spread across many files.
- A human developer chooses and drives the coding agent manually.
- Claude Code is a valid default example, but the engine must remain agent-neutral.
- No agent-produced change bypasses deterministic gates.
- The unit of execution is a bounded conversion slice, never an unconstrained whole-program prompt.

## Default per-program conversion loop

### Step 1: Deterministic whole-program preparation

The engine must:

- ingest the full program root and resolve copybooks, formats, and charsets
- build a whole-program manifest
- parse and serialize source artifacts with diagnostics
- build dependency closure across source units, copybooks, external interactions, and shared data structures
- lower the parsed result into validated IR
- decompose the program into bounded conversion slices with stable ids and readiness states

### Step 2: Deterministic slice baseline generation

For one ready slice, the engine must:

- generate the baseline Java/Spring scaffold for that slice
- attach runtime bindings and source maps
- mark editable zones explicitly
- generate slice-level acceptance tests and integration checkpoints
- produce a task contract that says what the human and coding agent may edit

### Step 3: Human-driven coding-agent pass

The engine prepares a handoff packet. A human developer then chooses a coding agent, for example Claude Code, and asks it to work only inside the allowed zones for that one slice.

The developer must record:

- chosen coding agent and model
- prompt summary
- assumptions introduced during the pass
- resulting diff or patch set
- unresolved questions or blockers

### Step 4: Deterministic validation pass

The engine reruns:

- schema and artifact validation
- generator-consistency checks
- compile and static analysis
- fixture and unit tests
- replay and golden-master checks
- slice-level and program-level integration checks as required

### Step 5: Deterministic feedback packaging

If validation fails, the engine must emit a remediation bundle containing:

- compiler errors
- failing tests
- mismatch summaries
- runtime traces
- editable-zone references
- narrowed hypotheses
- unchanged stop conditions and retry budget

### Step 6: Human remediation or next-slice pass

The human developer either:

- reruns the chosen coding agent on the remediation bundle for the same slice, or
- moves to the next ready slice once the current slice passes

### Step 7: Program convergence and acceptance

The whole program is accepted only when:

- all required slices pass their gates
- all integration checkpoints pass
- unresolved blockers are cleared or explicitly classified as manual redesign
- the full evidence bundle is stored in a versioned record

## Engine artifacts that make the workflow real

The plans below should converge on a controller that emits and consumes artifacts such as:

- whole-program ingestion manifest
- parse artifact bundle
- dependency summary
- semantic index snapshot
- IR slice bundle
- agent task contract
- human handoff packet
- generated project skeleton
- verification feedback bundle
- integration checkpoint record
- program conversion record

These artifacts are the contract between deterministic steps and human-driven coding-agent steps.

## Phase map

1. Phase 01 establishes governance, roles, schemas, and the deterministic-versus-human authority model.
2. Phase 02 hardens whole-program ingestion, parser execution, and dependency closure.
3. Phase 03 builds the semantic index and slice backlog model.
4. Phase 04 creates normalized IR, deterministic decomposition, and machine-readable slice bundles.
5. Phase 05 builds the COBOL compatibility runtime and runtime-gap reporting.
6. Phase 06 productizes Java/Spring generation and the alternating slice handoff loop.
7. Phase 07 builds the verification and remediation evidence pipeline.
8. Phase 08 proves end-to-end viability on a reference corpus.
9. Phase 09 hardens the engine into a sustainable operator-facing product.

## Ordered list of phase documents

1. [Phase 01 - Engine Foundation and Governance](./phase-01-program-foundation-and-governance.md)
2. [Phase 02 - Parser Hardening and Whole-Program Ingestion](./phase-02-parser-hardening-and-estate-ingestion.md)
3. [Phase 03 - Semantic Index and Conversion Intelligence](./phase-03-semantic-index-and-estate-intelligence.md)
4. [Phase 04 - Normalized IR and Transformation Pipeline](./phase-04-normalized-ir-and-transformation-pipeline.md)
5. [Phase 05 - COBOL Compatibility Runtime](./phase-05-cobol-compatibility-runtime.md)
6. [Phase 06 - Java and Spring Code Generation](./phase-06-java-and-spring-code-generation.md)
7. [Phase 07 - Verification Harness and Engine Qualification](./phase-07-verification-harness-and-golden-master.md)
8. [Phase 08 - End-to-End Engine Validation on Reference Corpus](./phase-08-pilot-wave-delivery-and-production-scaleout.md)
9. [Phase 09 - Engine Hardening, Extensibility, and Operator Handoff](./phase-09-cutover-refactoring-and-decommissioning.md)

## Concrete execution backlog

Use the issue-sized backlog here to turn the phase plans into implementation work:

- [Concrete Implementation Backlog](./implementation-backlog.md)
