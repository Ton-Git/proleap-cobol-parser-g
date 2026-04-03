# Phase 05 Implementation Plan - COBOL Compatibility Runtime

> For Hermes: Build the runtime before chasing beautiful Java. Behavior-preserving execution is the priority.

Goal: Implement the Java runtime libraries required to execute generated code with COBOL-compatible data, file, numeric, and procedural semantics.

Architecture: Generated Java should target stable runtime APIs instead of inlining COBOL semantics everywhere. Runtime modules encapsulate data layout, numeric operations, file behavior, condition names, and external interaction adapters. This reduces codegen complexity and centralizes semantic fixes.

Tech Stack: Java 17+, modular runtime libraries, Spring-friendly packaging, JDBC/MyBatis adapters, test fixtures and golden-master comparisons.

---

## Phase objective

Provide the execution substrate that makes first-pass generated Java correct enough to validate.

## In scope

- Data and memory semantics runtime
- Numeric and string semantics runtime
- File and record I/O runtime
- Procedure and context execution helpers
- SQL bridge abstractions
- Diagnostic tracing hooks
- Runtime gap handling for remediation loops

## Out of scope

- Final polished public Spring APIs
- Full CICS or IMS modernization strategy beyond adapter placeholders

## Concrete engine outputs targeted in this phase

- stable runtime APIs for generated code
- runtime feature-compatibility matrix
- runtime-gap bundle artifact for unsupported semantics
- trace hooks used by verification and remediation loops
- test doubles for file and SQL behavior

## Ordered work packages

### Work Package 1: Build the core execution context

Objective: Provide a stable host abstraction for generated programs.

Tasks:
1. Define execution context interfaces for working storage, linkage, environment, tracing, and configuration.
2. Model program invocation and subprogram CALL conventions.
3. Define lifecycle hooks for initialization and termination.
4. Add runtime feature flags for strict versus permissive behavior.
5. Add structured trace emission for verification.

### Work Package 2: Implement COBOL data and memory primitives

Objective: Support storage-layout-sensitive generated code.

Tasks:
1. Implement field types for alphanumeric, display numeric, packed decimal, binary numeric, and edited values as required by the supported-v1 engine scope.
2. Model group items and overlays for REDEFINES.
3. Model OCCURS and indexed access.
4. Implement 88-level condition-name evaluation helpers.
5. Implement reference modification and padding or truncation rules.

### Work Package 3: Implement numeric, comparison, and string semantics

Objective: Prevent subtle behavior drift in common business logic.

Tasks:
1. Implement arithmetic behavior matching chosen COBOL semantics and target compilers where required.
2. Implement comparison, collating, sign, rounding, and truncation behavior.
3. Implement figurative constants and blank or space handling.
4. Add exhaustive unit tests for representative edge cases.
5. Expose helper APIs with stable contracts for codegen.

### Work Package 4: Implement file and record runtime services

Objective: Support generated batch code and file-heavy logic.

Tasks:
1. Define abstractions for sequential, indexed, and relative file access as required by the supported-v1 scope.
2. Implement record layout binding to runtime data structures.
3. Implement file status and error semantics.
4. Add in-memory and filesystem-backed test doubles.
5. Add tracing of file operations for equivalence testing.

### Work Package 5: Implement SQL and external interaction bridges

Objective: Support generated code that still contains structured interaction points.

Tasks:
1. Define SQL bridge APIs with host-variable binding support.
2. Start with JDBC or MyBatis-friendly execution patterns.
3. Preserve SQL text and parameter provenance for verification.
4. Define placeholder adapter contracts for other transaction layers when needed.
5. Add logging and test doubles.

### Work Package 6: Build runtime conformance suites and gap reporting

Objective: Prove the runtime is stable enough for generator adoption and make missing semantics visible to the loop.

Tasks:
1. Curate semantic test vectors from real COBOL behavior.
2. Build unit and integration suites for each runtime module.
3. Add differential tests comparing COBOL outputs to runtime outputs where possible.
4. Add performance baselines for hot paths.
5. Document known semantic gaps.
6. Publish runtime-gap bundles that downstream deterministic validation and human remediation loops can reference when a slice fails because the platform lacks required semantic support.

## Phase completion gate

Proceed only when generated code can rely on stable runtime contracts, supported-v1 semantic gaps are documented as machine-readable artifacts, and verification can trace failures back to runtime deficiencies rather than opaque behavior drift.
