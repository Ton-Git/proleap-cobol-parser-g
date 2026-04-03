# Runbook: Golden Master Execution

Purpose: Define how to execute COBOL baseline runs and generated Java runs for qualification.

## Procedure

1. select a reference program and fixture set
2. run COBOL baseline capture
3. run generated Java against identical inputs
4. capture outputs, files, DB effects, and traces
5. apply normalization rules
6. produce comparison report
7. classify result as exact, normalized acceptable, manual review, or fail

## Required retained artifacts

- baseline run id
- generated run id
- fixture id
- comparison report
- normalization rules applied
- final disposition
