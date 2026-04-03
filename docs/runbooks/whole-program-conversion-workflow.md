# Runbook: Whole-Program Conversion Workflow

Purpose: Define the end-to-end workflow for converting one large logical COBOL program composed of many source files and copybooks.

## Program model

This workflow assumes:
- the source codebase contains many COBOL files
- those files together implement one large logical program or business capability
- the target is one Spring Boot microservice with internal module boundaries

## Stages

1. Build a whole-program ingestion manifest.
2. Resolve copybooks and parse the full codebase.
3. Build dependency closure across calls, shared records, files, and SQL usage.
4. Lower to IR and detect unsupported constructs.
5. Decompose the codebase into bounded conversion slices.
6. Generate deterministic scaffolds and handoff packets for ready slices.
7. Let human developers execute bounded coding-agent steps slice by slice.
8. Re-run deterministic validation after every submission.
9. Run cross-slice integration checkpoints at defined milestones.
10. Run whole-program verification and package the accepted output into the single target microservice.

## Required convergence rules

- slice acceptance does not by itself imply whole-program acceptance
- integration checkpoints are mandatory when dependent slices converge
- whole-program acceptance requires compile, verification, and evidence completeness across the codebase scope
