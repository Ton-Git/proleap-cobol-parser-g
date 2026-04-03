# Concrete Implementation Backlog

> For Hermes: Use subagent-driven-development to execute one issue at a time. Treat each issue below as one PR-sized unit of work with tests and docs included.

Goal: Turn the phase plans into a concrete, issue-sized backlog for building the deterministic plus human-driven coding-agent COBOL conversion engine.

Architecture: This backlog follows the same factory model as the phase plans: deterministic whole-program preparation, deterministic scaffolding, human-driven coding-agent execution on bounded slices, deterministic verification, deterministic feedback packaging, and whole-program convergence. The backlog is ordered by dependency, but many issues inside a phase can run in parallel once prerequisite artifacts exist.

Tech Stack: Java 17, Maven, existing `io.proleap.cobol.engine` packages, new `docs/` architecture and runbook docs, JSON schemas and artifact examples, JUnit-based tests, GitHub issue or PR workflow.

---

## How to use this backlog

- One issue = one branch, one PR, one reviewable outcome.
- Every issue should produce or update tests.
- Every issue should update docs when it changes artifacts, schemas, or operator workflow.
- Prefer additive, vertical slices over broad refactors.
- If an issue grows too large, split it before implementation.

## Definition of “issue-sized” for this backlog

An issue should usually fit within:

- 1 focused PR
- 1 to 3 source packages touched
- 1 test class added or updated
- 1 clearly demonstrable acceptance result

## Current baseline already present in the repo

The repo already contains an initial engine skeleton in:

- `src/main/java/io/proleap/cobol/engine/ingest`
- `src/main/java/io/proleap/cobol/engine/parse`
- `src/main/java/io/proleap/cobol/engine/ir`
- `src/main/java/io/proleap/cobol/engine/slice`
- `src/main/java/io/proleap/cobol/engine/codegen`
- `src/main/java/io/proleap/cobol/engine/verification`
- `src/main/java/io/proleap/cobol/engine/orchestration`
- `src/main/java/io/proleap/cobol/engine/serialization`
- `src/main/java/io/proleap/cobol/engine/cli`

The backlog below assumes that skeleton exists and now needs to be productized phase by phase.

---

## Phase 01 backlog - Engine foundation and governance

Outcome: establish the operating rules, schema ownership, architecture docs, and governance needed for later implementation.

### P1-01: Write target platform overview and scope ADR
- Purpose: make the one-program, one-microservice target explicit.
- Primary files:
  - `docs/architecture/target-platform-overview.md`
  - `docs/decision-records/ADR-0001-engine-scope.md`
- Depends on: none
- Done when:
  - the docs state that many COBOL files form one logical program
  - the docs state that the output target is one Spring Boot microservice
  - out-of-scope items exclude cutover and customer rollout execution
- Suggested validation:
  - review for consistency with `plans/README.md`

### P1-02: Define module layout and ownership boundaries
- Purpose: map parser, ingest, IR, runtime, codegen, orchestration, verification, and docs into a stable repo structure.
- Primary files:
  - `docs/architecture/module-layout.md`
  - optional updates to `pom.xml` if module decomposition starts now
- Depends on: P1-01
- Done when:
  - module responsibilities are documented
  - controller-owned artifacts versus human-editable outputs are documented
  - package ownership is assigned at a high level
- Suggested validation:
  - architecture review with no unresolved overlap between runtime, codegen, and verification responsibilities

### P1-03: Define quality gates and engine definition of done
- Purpose: make “accepted slice” and “accepted program” objective.
- Primary files:
  - `docs/architecture/quality-gates.md`
  - `docs/architecture/engine-definition-of-done.md`
- Depends on: P1-01
- Done when:
  - parse, IR, codegen, verification, and convergence gates are listed
  - slice-level and whole-program acceptance criteria are distinct
  - retry, escalation, and blocker states are named
- Suggested validation:
  - doc review against `ProgramConversionRecord` fields already in code

### P1-04: Define AI agent operating model and human handoff policy
- Purpose: formalize manual use of Claude Code or any other coding agent.
- Primary files:
  - `docs/architecture/ai-agent-operating-model.md`
  - `docs/runbooks/human-agent-handoff.md`
  - `docs/runbooks/ai-assisted-conversion-governance.md`
- Depends on: P1-03
- Done when:
  - the deterministic controller and human developer responsibilities are separated
  - coding-agent metadata capture requirements are documented
  - bounded slice restrictions and stop conditions are documented
- Suggested validation:
  - docs explicitly prohibit unconstrained whole-program prompts

### P1-05: Define task-contract and handoff-packet ownership docs
- Purpose: make the controller-produced artifacts formally governed before deeper implementation.
- Primary files:
  - `docs/architecture/agent-task-contracts.md`
  - `docs/schemas/agent-task-contract.schema.json`
  - `docs/runbooks/whole-program-conversion-workflow.md`
- Depends on: P1-04
- Done when:
  - task contract fields are documented and schematized
  - handoff packet contents are documented
  - slice lifecycle states are named and owned
- Suggested validation:
  - artifact fields line up with `AgentTaskContract` and `HandoffPacket`

---

## Phase 02 backlog - Parser hardening and whole-program ingestion

Outcome: ingest one whole program root deterministically and emit stable parse and dependency artifacts.

### P2-01: Harden the program-root CLI contract
- Purpose: make whole-program execution the default entrypoint.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/cli/MigrationCli.java`
  - `src/test/java/io/proleap/cobol/engine/MigrationCliTest.java`
- Depends on: P1-03
- Done when:
  - CLI clearly accepts program-root oriented input
  - usage text explains output artifacts and workspace behavior
  - tests cover valid and invalid invocations
- Suggested validation:
  - `mvn -q -Dtest=io.proleap.cobol.engine.MigrationCliTest test`

### P2-02: Implement deterministic source discovery under a program root
- Purpose: discover COBOL files, copybooks, and support files reliably.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/ingest/IngestionManifest.java`
  - `src/main/java/io/proleap/cobol/engine/ingest/ProgramWorkspace.java`
  - `src/main/java/io/proleap/cobol/engine/ingest/SourceUnit.java`
  - `src/test/java/io/proleap/cobol/engine/EngineManifestAndParserTest.java`
- Depends on: P2-01
- Done when:
  - source discovery works from a directory root
  - source categories and metadata are persisted in the manifest
  - tests cover multi-file workspace discovery
- Suggested validation:
  - `mvn -q -Dtest=io.proleap.cobol.engine.EngineManifestAndParserTest test`

### P2-03: Normalize parse diagnostics into stable failure categories
- Purpose: make parser failures actionable for later automation.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/parse/ParseArtifact.java`
  - `src/main/java/io/proleap/cobol/engine/parse/ParseArtifactService.java`
  - `src/test/java/io/proleap/cobol/engine/EngineManifestAndParserTest.java`
- Depends on: P2-02
- Done when:
  - parse errors are categorized rather than stored as opaque strings
  - provenance from source file to parse artifact is preserved
  - tests cover at least one failure path and one success path
- Suggested validation:
  - focused parser tests pass

### P2-04: Emit workspace dependency summaries and readiness decisions
- Purpose: tell downstream phases whether a workspace can move forward.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/orchestration/DependencySummary.java`
  - `src/main/java/io/proleap/cobol/engine/orchestration/ConversionEngine.java`
  - `src/main/java/io/proleap/cobol/engine/serialization/EngineArtifactWriter.java`
  - `src/test/java/io/proleap/cobol/engine/EngineExtendedWorkflowArtifactsTest.java`
- Depends on: P2-03
- Done when:
  - dependency summary includes source-unit and copybook closure information
  - a readiness decision can be derived from parse outcomes
  - artifact output contains a stable dependency summary file
- Suggested validation:
  - `mvn -q -Dtest=io.proleap.cobol.engine.EngineExtendedWorkflowArtifactsTest test`

### P2-05: Build ingestion regression fixtures for representative failures
- Purpose: prevent parser regressions while hardening whole-program ingestion.
- Primary files:
  - `src/test/resources/...` new fixtures as needed
  - `src/test/java/io/proleap/cobol/engine/EngineManifestAndParserTest.java`
- Depends on: P2-03
- Done when:
  - at least three representative workspace failure cases are captured
  - regression tests explain why each case blocks or warns
- Suggested validation:
  - parser and manifest tests pass with new fixtures

---

## Phase 03 backlog - Semantic index and conversion intelligence

Outcome: compute the whole-program dependency and slice-readiness model that drives later work.

### P3-01: Define semantic index domain objects and serialization format
- Purpose: make the index output stable before building advanced analysis.
- Primary files:
  - new package `src/main/java/io/proleap/cobol/engine/index/`
  - `docs/architecture/conversion-tiering.md`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P2-04
- Done when:
  - logical program, compilation unit, dependency edge, and diagnostic summary objects exist
  - index serialization format is documented
  - snapshot-style tests cover serialization shape
- Suggested validation:
  - targeted semantic-index tests pass

### P3-02: Build cross-file dependency graph extraction
- Purpose: connect CALL, COPY, file-use, and SQL-use relationships across the workspace.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/index/...`
  - `src/main/java/io/proleap/cobol/engine/parse/...`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P3-01
- Done when:
  - whole-program dependency edges are emitted
  - unresolved edges are explicit
  - at least one multi-source fixture demonstrates graph output
- Suggested validation:
  - semantic-index tests plus existing pipeline tests

### P3-03: Extract canonical data dictionary and shared-record clusters
- Purpose: understand record structures before codegen.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/index/...`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P3-02
- Done when:
  - data structures can be exported in a canonical index view
  - duplicate or structurally similar records can be grouped
  - test fixtures prove grouping behavior
- Suggested validation:
  - targeted JUnit suite for record extraction passes

### P3-04: Implement slice-readiness scoring and blocker classification
- Purpose: decide what can enter the deterministic-plus-agent loop.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/index/...`
  - `docs/architecture/conversion-tiering.md`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P3-02
- Done when:
  - readiness scores exist per slice candidate
  - blockers such as unresolved copybooks or unsupported constructs are classified
  - output can distinguish ready, blocked, and manual-remediation candidates
- Suggested validation:
  - targeted slice-readiness tests pass

### P3-05: Publish the conversion-slice backlog artifact
- Purpose: turn intelligence into an execution queue.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/index/...`
  - `src/main/java/io/proleap/cobol/engine/serialization/EngineArtifactWriter.java`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P3-04
- Done when:
  - a machine-readable backlog artifact exists with readiness, blockers, and priority
  - candidate integration checkpoint groups are included
  - artifact writer persists the backlog output
- Suggested validation:
  - end-to-end artifact test includes backlog output

---

## Phase 04 backlog - Normalized IR and transformation pipeline

Outcome: produce a stable IR and bounded slice bundles with provenance and convergence semantics.

### P4-01: Version the IR schema explicitly
- Purpose: lock down the minimum stable semantic model before adding more transforms.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/ir/IrProgram.java`
  - `src/main/java/io/proleap/cobol/engine/ir/IrStatement.java`
  - `docs/architecture/ir-schema-overview.md`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P3-01
- Done when:
  - IR versioning rules are explicit in code and docs
  - schema evolution expectations are documented
  - serialization tests pin the IR shape
- Suggested validation:
  - IR serialization tests pass

### P4-02: Split IR building into named transform stages
- Purpose: replace opaque lowering with reviewable transform passes.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/ir/IrProgramBuilder.java`
  - new transform classes under `src/main/java/io/proleap/cobol/engine/ir/`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P4-01
- Done when:
  - normalization, enrichment, and unsupported-node marking are separated
  - each stage can be unit tested independently
- Suggested validation:
  - IR builder tests and pipeline tests pass

### P4-03: Add provenance and unsupported-node markers to IR
- Purpose: preserve traceability from COBOL to generated outputs and retries.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/ir/...`
  - `src/main/java/io/proleap/cobol/engine/verification/...`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P4-02
- Done when:
  - IR nodes carry source provenance references
  - unsupported or uncertain nodes are explicit and test-covered
- Suggested validation:
  - focused provenance tests pass

### P4-04: Upgrade slice packaging to emit richer IR slice bundles
- Purpose: make slice bundles sufficient for human handoff without extra manual context gathering.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/slice/IrSlicePackager.java`
  - `src/main/java/io/proleap/cobol/engine/slice/SliceBundle.java`
  - `src/main/java/io/proleap/cobol/engine/serialization/EngineArtifactWriter.java`
  - `src/test/java/io/proleap/cobol/engine/EngineSerializationTest.java`
- Depends on: P4-03, P3-05
- Done when:
  - slice bundles include provenance, dependencies, acceptance criteria, and target-file hints
  - JSON artifact tests pin the bundle structure
- Suggested validation:
  - `mvn -q -Dtest=io.proleap.cobol.engine.EngineSerializationTest test`

### P4-05: Derive integration checkpoint groups from slice dependencies
- Purpose: let slice-level progress converge back into whole-program acceptance.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/orchestration/IntegrationCheckpoint.java`
  - `src/main/java/io/proleap/cobol/engine/orchestration/ConversionEngine.java`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P4-04
- Done when:
  - checkpoints are dependency-driven instead of placeholder-only
  - checkpoint state can be computed from grouped slices
  - tests cover single-slice and multi-slice behavior
- Suggested validation:
  - `mvn -q -Dtest=io.proleap.cobol.engine.EngineExtendedWorkflowArtifactsTest test`

---

## Phase 05 backlog - COBOL compatibility runtime

Outcome: provide stable runtime semantics and gap reporting for supported generated code.

### P5-01: Implement explicit runtime execution context contracts
- Purpose: give generated code a stable host model.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/runtime/CobolRuntime.java`
  - new runtime support classes under `src/main/java/io/proleap/cobol/engine/runtime/`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P4-01
- Done when:
  - execution context, environment, and trace hooks are modeled cleanly
  - unit tests cover context creation and lifecycle behavior
- Suggested validation:
  - targeted runtime tests pass

### P5-02: Add supported-v1 data and memory primitives
- Purpose: support the minimal set of COBOL data semantics needed by early codegen.
- Primary files:
  - new runtime data classes under `src/main/java/io/proleap/cobol/engine/runtime/`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P5-01
- Done when:
  - group items, scalar values, and basic overlay behavior are represented
  - padding, truncation, and simple condition handling are test-covered
- Suggested validation:
  - runtime unit tests pass

### P5-03: Add numeric and string semantics coverage for generated code
- Purpose: reduce business-logic drift in early conversions.
- Primary files:
  - runtime numeric and string helper classes
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P5-02
- Done when:
  - arithmetic, comparison, and figurative constant behavior are covered for supported-v1 scope
  - risky edge cases are represented in tests
- Suggested validation:
  - numeric and string runtime suites pass

### P5-04: Add file and SQL test doubles with trace hooks
- Purpose: allow generated code to be verified without full external infrastructure.
- Primary files:
  - runtime file and SQL helper classes
  - verification support classes
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P5-01
- Done when:
  - in-memory file and SQL doubles exist
  - operations emit traces consumable by verification
- Suggested validation:
  - integration-style runtime tests pass

### P5-05: Emit runtime-gap bundles for unsupported semantics
- Purpose: route slice failures to runtime backlog instead of blind agent retries.
- Primary files:
  - runtime gap artifact classes
  - `src/main/java/io/proleap/cobol/engine/verification/...`
  - `src/main/java/io/proleap/cobol/engine/serialization/EngineArtifactWriter.java`
- Depends on: P5-02, P5-03
- Done when:
  - unsupported runtime behavior can be emitted as a machine-readable gap bundle
  - gap bundles can be referenced from verification feedback
- Suggested validation:
  - targeted verification artifact tests pass

---

## Phase 06 backlog - Java and Spring code generation

Outcome: generate a stable project skeleton, bounded editable zones, and artifact-driven retry loops.

### P6-01: Stabilize generated project layout and bootstrap output
- Purpose: make generated project structure deterministic and reviewable.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/codegen/JavaSpringCodeGenerator.java`
  - `src/main/java/io/proleap/cobol/engine/codegen/GeneratedProject.java`
  - `src/test/java/io/proleap/cobol/engine/EngineArtifactsTest.java`
- Depends on: P4-04, P5-01
- Done when:
  - generated project always contains a stable build file and application bootstrap
  - package naming and module layout are deterministic
- Suggested validation:
  - `mvn -q -Dtest=io.proleap.cobol.engine.EngineArtifactsTest test`

### P6-02: Add explicit editable zones and source maps to generated files
- Purpose: constrain human and coding-agent edits safely.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/codegen/GeneratedFile.java`
  - `src/main/java/io/proleap/cobol/engine/codegen/JavaSpringCodeGenerator.java`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P6-01
- Done when:
  - generated files declare editable and controller-owned zones
  - source maps tie IR nodes to generated locations
  - tests assert zone and source-map emission
- Suggested validation:
  - targeted codegen tests pass

### P6-03: Persist generated project state between slice passes
- Purpose: make multi-slice program conversion incremental rather than stateless.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/codegen/GeneratedProject.java`
  - `src/main/java/io/proleap/cobol/engine/serialization/EngineArtifactWriter.java`
  - `src/test/java/io/proleap/cobol/engine/EngineExtendedWorkflowArtifactsTest.java`
- Depends on: P6-01
- Done when:
  - generated project can be serialized and reconstructed
  - artifact output persists full project state between slice runs
- Suggested validation:
  - `mvn -q -Dtest=io.proleap.cobol.engine.EngineExtendedWorkflowArtifactsTest test`

### P6-04: Expand handoff packet output into a complete slice packet
- Purpose: make the human handoff operational with minimal manual assembly.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/agent/HandoffPacket.java`
  - `src/main/java/io/proleap/cobol/engine/orchestration/ConversionEngine.java`
  - `src/main/java/io/proleap/cobol/engine/serialization/EngineArtifactWriter.java`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P4-04, P6-02
- Done when:
  - handoff packet directories include instructions, task contract, dependency summary refs, and checklist content
  - slice packet contents match docs and schema expectations
- Suggested validation:
  - artifact tests pass and emitted packet directories are readable

### P6-05: Enrich program conversion records with retry and convergence metadata
- Purpose: make the loop auditable across repeated slice attempts.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/orchestration/ProgramConversionRecord.java`
  - `src/main/java/io/proleap/cobol/engine/orchestration/AgentIterationRecord.java`
  - `src/main/java/io/proleap/cobol/engine/orchestration/ConversionEngine.java`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P6-03, P4-05
- Done when:
  - program conversion record stores retry history, checkpoint state, and convergence details
  - tests cover accepted, retry-pending, and blocked outcomes
- Suggested validation:
  - `mvn -q -Dtest=io.proleap.cobol.engine.EngineWholeProgramWorkflowTest,io.proleap.cobol.engine.EnginePipelineIntegrationTest test`

---

## Phase 07 backlog - Verification harness and engine qualification

Outcome: make verification the authoritative accept-or-reject gate with actionable remediation artifacts.

### P7-01: Standardize fixture packaging for dual execution
- Purpose: let COBOL and generated Java consume the same reference inputs.
- Primary files:
  - new verification support classes under `src/main/java/io/proleap/cobol/engine/verification/`
  - tests under `src/test/java/io/proleap/cobol/engine/`
  - `docs/runbooks/reference-corpus-strategy.md`
- Depends on: P2-05, P5-04
- Done when:
  - fixtures can be packaged once and addressed by slice, checkpoint, and program
  - verification tests reuse the same fixture contract
- Suggested validation:
  - focused verification fixture tests pass

### P7-02: Emit structured slice-level verification reports
- Purpose: make every slice decision machine-readable.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/verification/VerificationReport.java`
  - `src/main/java/io/proleap/cobol/engine/verification/VerificationHarness.java`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P7-01
- Done when:
  - verification reports separate compile, static-analysis, fixture, and golden-master results
  - reports can be tied back to slice ids and project versions
- Suggested validation:
  - verification unit tests and pipeline tests pass

### P7-03: Add integration checkpoint verification support
- Purpose: verify grouped slices before whole-program acceptance.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/verification/...`
  - `src/main/java/io/proleap/cobol/engine/orchestration/IntegrationCheckpoint.java`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P4-05, P7-02
- Done when:
  - checkpoint verification is distinct from slice verification
  - checkpoint failures can block downstream work cleanly
- Suggested validation:
  - targeted checkpoint verification tests pass

### P7-04: Stabilize remediation feedback bundle schemas and examples
- Purpose: make the next human retry package deterministic and portable.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/verification/VerificationFeedbackBundle.java`
  - `docs/schemas/verification-feedback-bundle.schema.json`
  - `docs/examples/verification-feedback-bundle.example.json`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P7-02
- Done when:
  - feedback bundle fields are documented, serialized, and example-backed
  - emitted bundles include editable-zone references and retry context
- Suggested validation:
  - serialization tests plus docs review

### P7-05: Implement acceptance policy and escalation routing
- Purpose: stop blind retries and route failures to the right backlog.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/verification/...`
  - `src/main/java/io/proleap/cobol/engine/orchestration/ConversionEngine.java`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P7-03, P7-04
- Done when:
  - failure categories map to parser, IR, runtime, codegen, fixture, or manual-remediation routes
  - retry budgets interact with verification outcomes deterministically
- Suggested validation:
  - pipeline integration tests cover multiple escalation paths

---

## Phase 08 backlog - End-to-end engine validation on reference corpus

Outcome: prove the factory loop works on representative programs and measure its behavior.

### P8-01: Curate the reference corpus and qualification tags
- Purpose: define what programs prove the engine is viable.
- Primary files:
  - `docs/runbooks/reference-corpus-strategy.md`
  - new fixture indexes under `docs/` or `src/test/resources/`
- Depends on: P2-05, P7-01
- Done when:
  - reference programs are categorized by difficulty and coverage purpose
  - qualification tags exist for parser, IR, runtime, codegen, and verification coverage
- Suggested validation:
  - documentation review and fixture discoverability check

### P8-02: Build a qualification runner for reference-program workflows
- Purpose: execute a whole-program validation flow repeatably.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/cli/MigrationCli.java`
  - orchestration and verification classes as needed
  - `src/test/java/io/proleap/cobol/engine/EnginePipelineIntegrationTest.java`
- Depends on: P7-05, P6-05
- Done when:
  - one command or workflow can execute a full qualification run
  - outputs are grouped per program with evidence artifacts
- Suggested validation:
  - targeted pipeline integration test passes

### P8-03: Add throughput and retry metrics reporting
- Purpose: measure whether the slice loop is operationally useful.
- Primary files:
  - orchestration reporting classes
  - `docs/runbooks/agent-execution-controller.md`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P8-02
- Done when:
  - retry counts, checkpoint failures, and acceptance latency are reportable
  - metrics can be derived from conversion records and verification output
- Suggested validation:
  - reporting tests verify metrics on synthetic multi-slice runs

### P8-04: Compare single-lane versus specialist-lane qualification flows
- Purpose: learn whether one developer lane or multiple specialist lanes fit the workflow better.
- Primary files:
  - `docs/runbooks/per-program-agentic-conversion.md`
  - `docs/runbooks/agent-executed-program-conversion.md`
- Depends on: P8-03
- Done when:
  - runbooks describe when to use one lane versus multiple lanes
  - evidence from at least one reference run is summarized into the guidance
- Suggested validation:
  - documentation review against real qualification artifacts

### P8-05: Feed qualification learnings back into slice-sizing heuristics
- Purpose: turn reference-run lessons into deterministic improvements.
- Primary files:
  - slice packaging and backlog scoring classes
  - `docs/architecture/alternating-deterministic-agent-loop.md`
  - tests under `src/test/java/io/proleap/cobol/engine/`
- Depends on: P8-03
- Done when:
  - slice-sizing heuristics are updated from observed retry and failure patterns
  - regression tests pin the new heuristics where possible
- Suggested validation:
  - end-to-end and serialization tests still pass after heuristic updates

---

## Phase 09 backlog - Engine hardening, extensibility, and operator handoff

Outcome: convert the qualified engine into a maintainable product with release rules and operator onboarding.

### P9-01: Write engine release playbook and readiness checklist
- Purpose: make qualification candidate and approved engine releases repeatable.
- Primary files:
  - `docs/runbooks/engine-release-playbook.md`
  - `docs/architecture/supported-v1-engine-scope.md`
- Depends on: P8-02
- Done when:
  - release modes and readiness evidence are documented
  - rollback and freeze rules are documented
- Suggested validation:
  - playbook review against actual engine artifacts

### P9-02: Add support reporting and operator-facing runbooks
- Purpose: prepare the engine for sustained use beyond the first qualification cycles.
- Primary files:
  - `docs/runbooks/engine-support-playbook.md`
  - `docs/runbooks/generated-code-review.md`
  - `docs/runbooks/human-agent-handoff.md`
- Depends on: P9-01
- Done when:
  - support responsibilities and operator checklists are documented
  - common failure-handling paths are easy to follow
- Suggested validation:
  - runbook walkthrough using an existing artifact folder

### P9-03: Refactor orchestration and codegen hotspots with regression protection
- Purpose: improve maintainability without changing accepted behavior.
- Primary files:
  - `src/main/java/io/proleap/cobol/engine/orchestration/...`
  - `src/main/java/io/proleap/cobol/engine/codegen/...`
  - `src/test/java/io/proleap/cobol/engine/...`
- Depends on: P8-02
- Done when:
  - duplicated or tangled orchestration or codegen logic is reduced
  - targeted regression tests prove no workflow drift
- Suggested validation:
  - targeted suites plus broader Maven test run

### P9-04: Define extension checklist and schema compatibility policy
- Purpose: make future construct support additions safe.
- Primary files:
  - `docs/architecture/engine-extension-checklist.md`
  - `docs/architecture/engine-refactoring-policy.md`
  - schema docs under `docs/schemas/`
- Depends on: P9-01
- Done when:
  - new parser, IR, runtime, codegen, and verification additions have a documented path
  - schema compatibility expectations are explicit
- Suggested validation:
  - docs review against existing artifact versions

### P9-05: Package operator onboarding and training materials
- Purpose: ensure humans can actually run the loop confidently.
- Primary files:
  - `docs/runbooks/per-program-agentic-conversion.md`
  - `docs/runbooks/agent-executed-program-conversion.md`
  - `docs/runbooks/whole-program-conversion-workflow.md`
- Depends on: P9-02
- Done when:
  - onboarding materials show how to choose an agent, review diffs, resubmit slices, and interpret evidence
  - the materials use current artifact names and workflow states
- Suggested validation:
  - perform a dry-run walkthrough using a generated artifact directory

---

## Recommended first execution wave

If you want the fastest path to visible progress, start with this sequence:

1. P1-04 define the AI-agent and human handoff policy
2. P1-05 define task-contract and handoff-packet ownership docs
3. P2-04 emit workspace dependency summaries and readiness decisions
4. P3-05 publish the conversion-slice backlog artifact
5. P4-04 upgrade slice packaging to emit richer IR slice bundles
6. P6-02 add explicit editable zones and source maps to generated files
7. P6-04 expand handoff packet output into a complete slice packet
8. P7-02 emit structured slice-level verification reports
9. P7-04 stabilize remediation feedback bundle schemas and examples
10. P8-02 build a qualification runner for reference-program workflows

That wave would make the deterministic -> human coding-agent -> deterministic loop concrete very quickly.
