# Agent Task Contracts

Purpose: Define the contract for a single bounded human-executed coding-agent task so the controller, developer, generator, and verifier speak the same language.

## Required fields

Every task contract must define:
- schema_version
- task_id
- task_type
- slice_id
- program_id
- target_role
- purpose statement
- allowed target files
- acceptance checks
- stop conditions
- retry budget
- human handoff instructions or references

## Approved task types

- human-agent-bounded-implementation
- remediate_slice
- author_template_change
- review_generated_output
- investigate_verification_failure

## Human workflow

The contract does not assume automatic agent invocation.
It exists so a human developer can:
- understand the slice boundary
- choose any coding agent
- constrain edits to approved files and zones
- return the result to deterministic validation

## Output requirements

The human-submitted output must include:
- code changes limited to approved zones
- explicit assumptions
- unresolved issues if any
- prompt/model summary in the evidence package
- no hidden file creation outside the contract
