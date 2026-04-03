# Runbook: AI-Assisted Conversion Governance

Purpose: Provide operational rules for using agents inside the engine.

## Before enabling an agent task

Confirm:
- task contract exists
- allowed files and edit zones exist
- acceptance checks exist
- runtime and template versions are pinned

## Required retention

Store for every run:
- prompt
- model and provider
- input artifact references
- output diff
- validation results
- final disposition

## Immediate stop conditions

Stop and escalate if:
- the agent edits outside declared zones
- the task requires semantic decisions outside supported-v1 policy
- the agent would need to alter runtime contracts without approval
