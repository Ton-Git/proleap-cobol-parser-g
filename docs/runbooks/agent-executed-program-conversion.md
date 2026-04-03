# Runbook: Human-Executed Program Conversion

Purpose: Describe the operator-facing sequence for running one program conversion with a human developer using any bounded coding agent.

## Preconditions

- qualification batch approved
- logical program assigned to the batch
- slice backlog exists
- deterministic scaffolding is available
- verification assets are available

## Standard execution checklist

- logical program classification exists
- target role exists per slice
- selected slices are ready_for_human_agent
- runtime and template versions are pinned
- output directories are isolated from unrelated work
- handoff packets are generated and versioned

## Required record fields

- program_id
- domain_id
- qualification_batch_id
- target_shape
- selected slices
- controller_version
- operator_id
- chosen_agent
- prompt_summary
- resulting_diff_ref

## Completion rule

A program conversion closes only when the converted logic is accepted and linked into its target module, adapter, job flow, or shared package inside the single Spring Boot microservice with full evidence.
