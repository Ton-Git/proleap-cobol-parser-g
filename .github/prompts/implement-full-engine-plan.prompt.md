---
name: "Implement Full Engine Plan"
description: "Implement the full COBOL-to-Java conversion engine plan in this repository using the locked v1 defaults, Phase 00 first, and the canonical critical path"
argument-hint: "Optional focus, constraint, or current blocker"
agent: "agent"
---
Implement the full conversion-engine plan for this repository.

Primary sources of truth:
- [Plan overview](../../plans/README.md)
- [Implementation backlog](../../plans/implementation-backlog.md)
- [Phase 01](../../plans/phase-01-program-foundation-and-governance.md)
- [Phase 02](../../plans/phase-02-parser-hardening-and-estate-ingestion.md)
- [Phase 03](../../plans/phase-03-semantic-index-and-estate-intelligence.md)
- [Phase 04](../../plans/phase-04-normalized-ir-and-transformation-pipeline.md)
- [Phase 05](../../plans/phase-05-cobol-compatibility-runtime.md)
- [Phase 06](../../plans/phase-06-java-and-spring-code-generation.md)
- [Phase 07](../../plans/phase-07-verification-harness-and-golden-master.md)
- [Phase 08](../../plans/phase-08-pilot-wave-delivery-and-production-scaleout.md)
- [Phase 09](../../plans/phase-09-cutover-refactoring-and-decommissioning.md)

Execution contract:
- Execute Phase 00 first. Do not start broad implementation until the autonomous-execution lock-in items are satisfied in code and docs.
- Follow the canonical critical path in [Implementation backlog](../../plans/implementation-backlog.md).
- Treat the repository as a single-module Maven build for the first integrated build.
- Keep implementation inside the existing `io.proleap.cobol.engine` packages unless a plan item explicitly requires otherwise.
- Use JSON artifacts and deterministic file-backed storage under `target/engine/` for v1.
- Prefer repo-local validation paths. Do not introduce mandatory external databases, queues, dashboards, or distributed workers in the first integrated build.
- Optimize for one working end-to-end reference-program loop before broadening feature coverage.
- When semantics are unsupported, emit explicit machine-readable gap artifacts and route them through verification and remediation instead of guessing.
- Keep docs, schemas, examples, and tests aligned with code as you progress.

Implementation requirements:
- Work through the backlog in dependency order.
- Prefer additive changes over broad refactors.
- Use the smallest meaningful validation command after each completed backlog item.
- After each phase, run the canonical local validation suite for that phase.
- Update plan-adjacent docs when implementation choices become concrete.
- Preserve deterministic ownership boundaries between controller-owned artifacts, generated artifacts, and human-editable zones.
- Do not bypass a phase gate because a later phase seems easier to scaffold.

Stop conditions:
- Stop only for contradictory plan requirements, missing required fixtures, or validation failures that cannot be resolved within the touched scope.
- If blocked by unsupported COBOL semantics, emit or update the corresponding gap artifact, tests, and routing logic, then continue on the next allowed path.
- Do not silently narrow scope. If you must defer something, record it explicitly in code, docs, and artifacts.

Expected working style:
- Build the end-to-end deterministic loop first: ingestion -> indexing -> IR -> slice packaging -> runtime support -> code generation -> verification -> remediation artifacts -> qualification runner.
- Keep the generated project and evidence model reconstructable from persisted artifacts.
- Make every accepted slice and program decision traceable to parser, IR, runtime, codegen, and verification evidence.

If the user supplied additional focus or constraints, apply them without violating the execution contract above.
