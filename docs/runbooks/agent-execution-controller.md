# Agent Execution Controller

Purpose: Describe how the implemented engine skeleton drives deterministic preparation, bounded task definition, human handoff, validation, and evidence writing.

## Current implemented direction

1. Build an `IngestionManifest` from many `SourceUnit` entries representing the codebase for one logical COBOL program.
2. Parse source units with `ParseArtifactService`.
3. Lower parsed output into `IrProgram` using `IrProgramBuilder`.
4. Package bounded slices through `IrSlicePackager`.
5. Generate Java project artifacts with `JavaSpringCodeGenerator`.
6. Run deterministic checks with `VerificationHarness`.
7. Build `AgentTaskContract` metadata for each slice.
8. Build `AgentIterationRecord` evidence.
9. Persist artifacts with `EngineArtifactWriter` or through the CLI.

## Human-driven execution model

The controller does not need to invoke a coding agent automatically.
Its responsibility is to prepare and persist the handoff artifacts that a human developer uses.

The human workflow is:
- choose the next ready slice
- open the handoff packet
- choose any coding agent
- apply edits only in allowed zones
- capture prompt/model/assumptions externally in the evidence package
- resubmit the result to deterministic validation

## Required outputs per program

The controller should emit at minimum:
- `program-conversion-record.json`
- `ir-slice-bundle.json`
- `agent-task-contracts.json`
- `verification-feedback-bundles.json`
- a human-facing handoff packet per ready slice

## Retry semantics

The engine skeleton does not invoke a real coding agent.
It models the handoff protocol:
- accepted iteration with no feedback bundle on success
- retry_pending iteration with a verification feedback bundle on failure
- escalated/manual states when retry budgets or platform blockers are reached

## CLI direction

The CLI should evolve toward:
- `io.proleap.cobol.engine.cli.MigrationCli <program-root> <output-dir>`

Where `<program-root>` identifies the codebase root for one logical COBOL program rather than one source file.
