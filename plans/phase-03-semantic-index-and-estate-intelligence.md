# Phase 03 Implementation Plan - Semantic Index and Conversion Intelligence

> For Hermes: This phase turns parsed files into conversion intelligence. Do not start transformation work until the whole program can be queried, ranked, and decomposed.

Goal: Build whole-program semantic intelligence on top of parser outputs so the engine can understand call relationships, data structures, external dependencies, and conversion complexity before code generation begins.

Architecture: Parse artifacts flow into indexers that populate searchable stores and graph structures. The result is a conversion intelligence layer used by planners, converters, and reviewers. It is not yet the IR; it is the queryable foundation that reveals what the entire codebase contains, how its pieces relate, and how the single large logical COBOL program should be decomposed into bounded slices.

Tech Stack: Java indexing services, PostgreSQL plus optional graph database/search index, REST or CLI query APIs, artifact readers from Phase 02.

---

## Phase objective

Produce a reliable whole-program conversion map: what source units exist, how they call each other, what they read and write, and how risky they are to automate.

## In scope

- Program inventory APIs
- Cross-file dependency indexing
- Call graph and copybook graph
- Data dictionary and shared structure index
- External interaction inventory
- Complexity and convertibility scoring
- Slice-readiness scoring for bounded agent execution
- Whole-program decomposition support
- Domain dashboards and reports

## Concrete engine outputs targeted in this phase

- searchable whole-program dependency graph
- data dictionary and duplicate-structure map
- external interaction inventory
- slice backlog with readiness, blockers, and priority
- candidate integration checkpoint groups
- automation viability score per region and whole program

## Ordered work packages

### Work Package 1: Define the conversion intelligence data model

Objective: Create the query model that downstream phases use.

Tasks:
1. Define entities for logical program, compilation unit, copybook, paragraph, section, data structure, external dependency, and diagnostic summary.
2. Define edge types for CALL, COPY, data reference, file use, SQL use, and ownership.
3. Define identity rules across duplicate names and multi-environment sources.
4. Version the model.
5. Create sample queries needed by later IR and codegen phases.

### Work Package 2: Build cross-file dependency indexing

Objective: Connect parsed units into one coherent codebase-wide graph.

Tasks:
1. Populate static CALL relationships and candidate dynamic CALL relationships.
2. Track COPY dependencies and copybook fan-out.
3. Track shared data structure usage across the whole codebase.
4. Track file names, table names, transaction references, and external system markers.
5. Flag unresolved edges explicitly.

### Work Package 3: Build a canonical data dictionary

Objective: Understand the program’s data shapes before generation.

Tasks:
1. Extract data descriptions, level structures, redefines, occurs, and condition names.
2. Cluster structurally similar copybooks and data records.
3. Identify duplicate business records with inconsistent field naming.
4. Identify high-risk structures such as deep REDEFINES and packed decimal heavy records.
5. Publish a canonical data dictionary report.

### Work Package 4: Classify regions by migration pattern

Objective: Stop treating all COBOL regions the same.

Tasks:
1. Define classification features such as batch-like, online-like, DB-heavy, file-heavy, utility, reporting, screen-driven, and transaction-oriented.
2. Implement rule-based classification initially.
3. Record confidence and reasons for each classification.
4. Suggest internal target roles inside the one Spring Boot microservice such as domain service, integration adapter, data-access component, API boundary, internal job flow, or manual redesign.
5. Validate classifications against reference corpus expectations and engineering review.

### Work Package 5: Score complexity and automation viability

Objective: Separate automatable work from manual redesign early.

Tasks:
1. Define risk signals: dynamic CALLs, ALTER, GO TO density, unsupported EXEC usage, unresolved references, copybook ambiguity, heavy redefines.
2. Compute a complexity score and an automation viability score per region and per whole program.
3. Create dashboards ranking high-value regions by automation potential.
4. Add manual-review queues for low-confidence or high-risk regions.
5. Feed these scores into engine implementation priorities and qualification order.
6. Add slice-readiness indicators that predict whether a region can enter the alternating deterministic plus human coding-agent loop directly or must first wait for runtime, IR, or parser improvements.

### Work Package 6: Build the conversion-slice backlog model

Objective: Turn conversion intelligence into a queue of bounded units that later phases can hand to a human developer safely.

Tasks:
1. Define how the full codebase is decomposed into candidate conversion slices using dependency hotspots, paragraph groups, data structures, file interactions, SQL regions, and packaging tasks.
2. Record blockers per slice such as unresolved copybooks, unsupported constructs, missing runtime support, or low classification confidence.
3. Publish a backlog view showing which slices are ready for deterministic scaffolding and which require prior platform work.
4. Add prioritization rules so high-value and high-reusability slices rise earlier in the execution queue.
5. Feed slice backlog status into engine validation planning and qualification forecasting.
6. Define grouping rules for cross-slice integration checkpoints so slice success can later converge into whole-program success.

## Phase completion gate

Proceed only when the codebase can be searched, ranked, decomposed, and segmented well enough to drive IR slicing, runtime priorities, codegen sequencing, and human handoff planning for the whole logical program.
