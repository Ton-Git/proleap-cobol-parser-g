# Phase 04 Implementation Plan - Normalized IR and Transformation Pipeline

> For Hermes: This is the architectural center of the engine. Keep parser internals and generated Java decoupled by building a stable IR and deterministic slice decomposition rules.

Goal: Design and implement a normalized intermediate representation (IR) and a sequence of transformation passes that convert parser outputs into a generator-friendly, semantically explicit model.

Architecture: Parsed COBOL artifacts and conversion intelligence feed a staged pipeline: normalization, symbol enrichment, control-flow restructuring, data-flow annotation, external interaction extraction, and conversion classification. The IR becomes the canonical representation consumed by runtimes, code generators, verifiers, and human handoff packets.

Tech Stack: Java core libraries, schema versioning, JSON or Protobuf serialization, graph and data-flow utilities, test fixtures from parser outputs.

---

## Phase objective

Create a stable semantic bridge between COBOL and Java generation so conversion rules are explicit, testable, and reusable across many slices.

## In scope

- IR schema and serialization
- Transformation pass framework
- Human-reviewed rule-authoring workflow for IR evolution
- Deterministic IR slicing for downstream human execution
- Control-flow and data-flow enrichment
- External interaction extraction
- Unsupported construct representation
- IR validation and snapshot testing

## Concrete engine outputs targeted in this phase

- stable IR program schema
- deterministic transform pipeline with versioned passes
- machine-readable IR slice bundle
- slice preconditions and postconditions
- unsupported-node and confidence markers that can block or shrink slices
- source-to-IR provenance needed by codegen and verification

## Ordered work packages

### Work Package 1: Define the IR schema

Objective: Model the minimum viable semantic shapes required for behavior-preserving conversion.

IR areas to cover:
- logical program metadata and identity
- entry points and callable units
- sections, paragraphs, statements, and control edges
- data definitions and memory layout
- value and condition expressions
- file interaction model
- SQL, CICS, and IMS interaction placeholders as needed
- unsupported construct placeholders
- provenance and diagnostics references

### Work Package 2: Define transform governance

Objective: Keep transforms deterministic and reviewable.

Tasks:
1. Classify transforms as deterministic, review-required, or unsupported.
2. Add a review stage where human reviewers compare proposals against parser artifacts and expected semantics.
3. Add automatic regression generation from accepted rules.
4. Require accepted proposals to be converted into versioned declarative rules or tested transform code rather than remaining one-off prompt output.
5. Version transform passes and capture compatibility with artifact schemas.

### Work Package 3: Define IR slice packaging for human handoff

Objective: Produce the bounded work units that make alternating deterministic and human coding-agent conversion passes practical.

Tasks:
1. Define slice families such as paragraph clusters, data-structure mappings, file blocks, SQL blocks, and packaging tasks.
2. Assign stable ids, provenance, dependencies, and acceptance criteria to every slice.
3. Emit machine-readable slice bundles containing only the COBOL excerpts, IR nodes, runtime contracts, and target files needed for one human-executed agent task.
4. Detect slices that are too large or too entangled and automatically decompose or route them to manual-remediation queues.
5. Attach deterministic preconditions and postconditions so later phases can tell whether a human coding-agent pass is allowed to start and whether it succeeded.
6. Define how unsupported or uncertain IR nodes shrink or block slice generation.

### Work Package 4: Define whole-program convergence semantics

Objective: Make sure slice execution can converge back into one accepted program.

Tasks:
1. Define how slice dependencies roll up into program-level readiness.
2. Mark which slices can be executed independently and which require prior checkpoint success.
3. Define integration checkpoint artifacts covering groups of slices.
4. Define the minimum IR evidence required for generated project persistence and verification traceability.
5. Ensure every slice stays connected to the same whole-program identity and target microservice model.

## Phase completion gate

Proceed only when valid IR can be deterministically decomposed into bounded conversion slices, integration checkpoint groups can be derived, and machine-readable slice bundles are stable enough for code generation and human handoff.
