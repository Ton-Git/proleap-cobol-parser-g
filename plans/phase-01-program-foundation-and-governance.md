# Phase 01 Implementation Plan - Engine Foundation and Governance

> For Hermes: Use subagent-driven-development skill to execute this phase work package by work package.

Goal: Establish the architecture, engineering controls, target standards, and governance needed to build a deterministic plus human-driven coding-agent COBOL conversion engine safely.

Architecture: This phase defines how the engine is governed before deep implementation begins. It sets the operating model for one large logical COBOL program, one target Spring Boot microservice, bounded conversion slices, deterministic controller authority, and human-operated coding-agent execution.

Tech Stack: single-module Maven Java repo for the first integrated build, issue tracker, ADR templates, diagrams, local file-backed artifact storage under `target/engine/`, GitHub Actions and broader observability only after the local deterministic loop is stable.

---

## Phase objective

Create the non-negotiable rules that make the later deterministic-plus-agent conversion loop auditable and repeatable.

## In scope

- Engine charter and success criteria
- One-microservice target architecture principles
- Whole-program operating model
- Team topology and responsibilities
- Human-driven coding-agent operating model and governance
- Repository and module strategy
- Architecture decision process
- Data handling and environment access model
- Definition of done for parsing, conversion, and verification
- Reference corpus and fixture strategy
- Supported-v1 engine scope and explicit non-goals

## Out of scope

- Parser hardening implementation
- IR implementation
- Runtime implementation
- Java generation
- Real customer cutover or rollout execution

## Locked v1 defaults applied in this phase

- keep the repository as one Maven module during the first integrated build
- keep implementation inside the existing `io.proleap.cobol.engine` packages before any module split
- treat file-backed artifacts under `target/engine/` as the canonical engine storage model for v1
- defer CI hardening, dashboards, and operator-scale infrastructure until the local end-to-end loop is proven

## Concrete engine outputs defined in this phase

- role model for controller, human developer, reviewer, and approver
- default slice lifecycle and status model
- task contract schema ownership
- handoff packet schema ownership
- evidence retention model for accepted and rejected slice runs
- definition of whole-program acceptance and integration checkpoint policy

## Deliverables

1. `/docs/architecture/target-platform-overview.md`
2. `/docs/decision-records/` ADR template and first ADRs
3. `/docs/architecture/module-layout.md`
4. `/docs/architecture/quality-gates.md`
5. `/docs/architecture/conversion-tiering.md`
6. `/docs/architecture/engine-definition-of-done.md`
7. `/docs/architecture/supported-v1-engine-scope.md`
8. `/docs/architecture/ai-agent-operating-model.md`
9. `/docs/runbooks/ai-assisted-conversion-governance.md`
10. `/docs/architecture/agent-task-contracts.md`
11. `/docs/runbooks/agent-execution-controller.md`
12. `/docs/architecture/alternating-deterministic-agent-loop.md`
13. `/docs/runbooks/per-program-agentic-conversion.md`
14. `/docs/runbooks/reference-corpus-strategy.md`
15. `/docs/schemas/agent-task-contract.schema.json`
16. `/docs/runbooks/human-agent-handoff.md`
17. `/docs/runbooks/whole-program-conversion-workflow.md`

## Ordered work packages

### Work Package 1: Define engine charter and acceptance model

Objective: Align the team on what “engine complete enough to use” means.

Tasks:
1. Write measurable goals for parse coverage, IR coverage, code generation, bounded human-guided agent execution, and verification repeatability.
2. Define engine tiers: supported deterministically, supported with human-guided agent assistance, explicit manual-remediation.
3. Define what counts as behaviorally equivalent for engine qualification purposes.
4. Define sign-off authorities for parser accuracy, generated code, verification evidence, and engine release readiness.
5. Define explicit non-goals for this stage.

### Work Package 2: Define the whole-program operating model

Objective: Make it explicit that the engine ingests and converts one large COBOL codebase representing one logical program.

Tasks:
1. Define the boundary of a logical program spanning many COBOL files, copybooks, and support artifacts.
2. Define the whole-program ingestion manifest and source categorization model.
3. Define rules for identifying entry points, support units, shared records, and unresolved ownership.
4. Define how program-level readiness differs from per-file parse readiness.
5. Define how a whole-program acceptance decision is made from many slice-level outcomes.

### Work Package 3: Define the human-driven coding-agent control model

Objective: Make the human workflow explicit instead of assuming automatic invocation of a specific coding agent.

Tasks:
1. Define the responsibilities of the deterministic controller that packages context, records artifacts, and enforces gates.
2. Define the responsibilities of the human developer who chooses and drives a coding agent.
3. Standardize the default loop as: deterministic preparation -> human-guided coding-agent implementation pass -> deterministic validation -> deterministic feedback packaging -> human-guided remediation or next-slice pass.
4. Define the maximum editable scope per agent run and the rules for decomposing oversized slices.
5. Define required metadata capture: operator, chosen agent, prompt summary, assumptions, resulting diff, and disposition.
6. Document Claude Code as one recommended manual execution option while keeping the engine agent-neutral.

### Work Package 4: Define per-slice and whole-program governance

Objective: Ensure the loop stays bounded and auditable.

Tasks:
1. Define task contract fields and acceptance policy.
2. Define handoff packet contents for human execution.
3. Define the review requirement before validation reruns.
4. Require every accepted agent-assisted output to map back to versioned parser artifacts, IR artifacts, runtime contracts, or approved generator templates.
5. Add per-slice gates for the alternating conversion loop: scaffold generated, human-run agent output diff captured, compile/test result stored, retry or escalation decision recorded.
6. Define cross-slice integration checkpoint governance.

## Phase completion gate

Proceed only when the team has agreed on the whole-program boundary model, the human handoff protocol, the deterministic authority model, and the minimum evidence required for every slice and whole-program decision.
