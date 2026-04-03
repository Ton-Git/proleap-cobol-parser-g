# Runbook: Generated Code Review

Purpose: Standardize review of generated and agent-assisted code.

## Review checklist

Confirm:
- source provenance is preserved
- only allowed files and edit zones were touched
- runtime APIs are used as intended
- unsupported semantics are explicit
- no accidental microservice splitting occurred
- generated placement matches target role inside the single service

## Reject immediately if

- compile or verification still fails
- target role changed without architecture approval
- hidden manual logic was inserted into generated-only regions
