# Phase 02 Implementation Plan - Parser Hardening and Whole-Program Ingestion

> For Hermes: Do not treat parsing as a single-file batch utility. This phase must make the parser usable as the front door for one large logical COBOL program.

Goal: Harden the parser and build whole-program ingestion so the engine can reliably accept a program root containing many COBOL files, copybooks, and support artifacts.

Architecture: This phase sits directly on top of the existing parser. It adds whole-program manifests, source discovery, dialect and copybook resolution, dependency closure, parse artifact serialization, and deterministic ingestion diagnostics. Later phases depend on this phase to hand them one coherent program workspace instead of a pile of unrelated files.

Tech Stack: Java 17+, Maven, parser modules already in this repo, filesystem and archive readers, JSON schema validation, artifact storage.

---

## Phase objective

Convert the parser from a file-at-a-time tool into a deterministic ingestion front end for one large logical COBOL program.

## In scope

- Whole-program ingestion manifest
- Source discovery under a program root
- Copybook, dialect, charset, and format resolution
- Parse artifact serialization and diagnostics
- Dependency closure reporting across source units
- Ingestion quality gates and failure categories
- Parser hardening driven by representative corpus failures

## Out of scope

- Semantic indexing beyond ingestion outputs
- IR normalization
- Java code generation
- Runtime semantics implementation

## Concrete engine outputs targeted in this phase

- `program-workspace` model representing one logical COBOL program
- ingestion manifest with source-unit metadata
- parse artifact bundle for all source units in the workspace
- parser diagnostics summary with unresolved references and failure categories
- dependency summary covering COPY closure, entry units, supporting units, and unresolved edges
- whole-program readiness status for downstream IR work

## Ordered work packages

### Work Package 1: Define the whole-program ingestion contract

Objective: Establish the canonical input to the deterministic pipeline.

Tasks:
1. Define a program-root based input model instead of a single-file invocation model.
2. Define source categories such as entry source, supporting source, copybook, config input, fixture input, and unresolved artifact.
3. Capture source format, charset, origin, and classification metadata at file level.
4. Define workspace identity, manifest versioning, and repeatable discovery rules.
5. Define how later phases consume one workspace as one logical program.

### Work Package 2: Implement source discovery and dependency closure

Objective: Deterministically discover the entire program boundary before parsing.

Tasks:
1. Walk the program root and classify COBOL files, copybooks, and support assets.
2. Resolve copybook search paths and record unresolved dependencies explicitly.
3. Detect duplicate names, ambiguous ownership, and inconsistent format metadata.
4. Produce a dependency summary covering source units, copybook directories, and unresolved edges.
5. Persist the discovered workspace so later phases can replay the same ingestion set.

### Work Package 3: Harden parsing for workspace execution

Objective: Parse all source units in one workspace and keep the failure evidence usable.

Tasks:
1. Run parsing across every discovered source unit and persist per-unit artifacts.
2. Normalize diagnostics into machine-readable categories such as syntax failure, missing copybook, unsupported dialect, ambiguous dependency, and internal parser failure.
3. Add retries or fallback heuristics only where they remain deterministic and reviewable.
4. Preserve provenance from source unit to parse artifact to workspace summary.
5. Build targeted regression tests from parser failures discovered in the reference corpus.

### Work Package 4: Define ingestion quality gates

Objective: Decide when a workspace is ready to move into semantic indexing and IR generation.

Tasks:
1. Define minimum parse-completeness thresholds for a workspace.
2. Distinguish fatal blockers from tolerated warnings.
3. Define what unresolved edges can be deferred and what must stop the pipeline.
4. Emit a whole-program readiness decision with blocking reasons.
5. Publish operator-readable summaries so a human knows whether to fix ingestion, parser rules, or source metadata before attempting coding-agent work.

### Work Package 5: Productize artifact output for later handoff loops

Objective: Make parser outputs usable by deterministic and human-driven later phases.

Tasks:
1. Serialize parse artifacts in stable schemas.
2. Persist workspace-level summaries and dependency closure artifacts.
3. Make source-unit to artifact mapping explicit.
4. Expose enough context for later IR slicing, task contract generation, and verification traceability.
5. Guarantee replayability so the same workspace can be reprocessed after parser fixes without hidden state.

## Phase completion gate

Proceed only when the engine can ingest a whole program root, parse all discovered source units deterministically, emit stable workspace and dependency artifacts, and classify blockers clearly enough for downstream IR and slice generation.
