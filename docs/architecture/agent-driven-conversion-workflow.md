# Agent-Driven Conversion Workflow

Purpose: Define the bounded workflow that combines deterministic preparation with human-executed coding-agent steps.

## Flow

- generate deterministic scaffold
- generate handoff packet
- human selects and runs a coding agent
- validate deterministically
- accept, retry, or escalate

## Standard sequence

1. Parse and classify the whole logical program codebase.
2. Build the slice backlog.
3. Generate scaffold and checks for one ready slice.
4. Emit a handoff packet.
5. Human developer runs a chosen coding agent on bounded context.
6. Validate results.
7. Record evidence and move to retry, escalate, or next slice.
