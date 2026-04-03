# Target Platform Overview

## Engine stages

1. Whole-program ingestion
2. Parsing and indexing
3. IR lowering and slicing
4. Runtime-backed generation
5. Human handoff loop
   - human developer uses a coding agent on one slice at a time
   - deterministic validation after every pass
6. Program-level convergence and packaging
