# Runbook: Human Agent Handoff

Purpose: Standardize how the engine hands a bounded slice to a human developer who may use any coding agent.

## Handoff packet contents

Each packet should contain:
- slice id and program id
- slice purpose and target role
- COBOL excerpts and provenance
- relevant IR fragment
- dependency summary for the slice
- generated Java scaffold
- allowed target files and editable zones
- acceptance checks
- stop conditions
- retry budget
- submission checklist

## Human steps

1. Read the slice objective and stop conditions.
2. Review the scaffold, runtime constraints, and tests.
3. Choose the coding agent to use.
4. Prompt the agent only with the bounded packet contents.
5. Apply the resulting edit only within allowed zones.
6. Record prompt summary, chosen model, assumptions, and unresolved issues.
7. Submit the edited result for deterministic validation.

## Submission checklist

- edits stayed within allowed files/zones
- assumptions were recorded
- unresolved items were recorded
- no runtime API changes were introduced unless explicitly allowed
- validation rerun was requested
