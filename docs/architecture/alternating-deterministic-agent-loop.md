# Alternating Deterministic-Agent Loop

Purpose: Define the standard execution loop that alternates deterministic conversion steps with human-executed coding-agent steps.

deterministic stage -> human coding-agent stage -> deterministic stage -> human remediation or next-slice stage

## Stages

Stage A: prepare and classify whole-program context
Stage B: generate deterministic scaffold
Stage C: human runs a chosen coding agent on bounded context
Stage D: validate deterministically
Stage E: accept, retry, escalate, or advance integration checkpoint
