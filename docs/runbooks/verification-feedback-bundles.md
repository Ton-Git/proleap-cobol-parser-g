# Verification Feedback Bundles

Purpose: Standardize the deterministic evidence package produced when validation fails and a bounded remediation pass is needed.

## Required fields

A feedback bundle should include:
- schema_version
- feedback_bundle_id
- target_id
- target_kind
- prior_agent_run_id
- failure_category
- failure_summary
- remediation_objective
- retry_count
- recommended_escalation_target
- failing_checks

## Implemented engine behavior

The current engine skeleton creates an iteration record for each slice.

If verification passes:
- iteration state is `accepted`
- no feedback bundle is attached

If verification fails:
- iteration state is `retry_pending`
- a verification feedback bundle is attached with:
  - failure category: `golden_master_mismatch`
  - remediation objective: fix the generated slice without changing runtime APIs
  - escalation target: `codegen`
  - failing checks copied from deterministic verification mismatches

## Example

For HelloWorld-style output the bundle shape is:
- feedback_bundle_id: `helloworld-slice-1-feedback-1`
- target_id: `helloworld-slice-1`
- target_kind: `slice`
- prior_agent_run_id: `helloworld-slice-1-agent-run-1`
- retry_count: 1

## Usage in the loop

1. Deterministic generation produces bounded files and acceptance checks.
2. Deterministic verification runs.
3. On failure, a feedback bundle is written to artifacts.
4. The next bounded agent remediation pass consumes that bundle.
5. Acceptance still depends on deterministic rerun success.
