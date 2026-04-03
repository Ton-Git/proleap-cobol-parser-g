# IR Schema Overview

Purpose: Describe the minimum semantic areas the normalized IR must represent.

## Required IR domains

- program identity and provenance
- entry points and callable units
- sections, paragraphs, statements, and control edges
- data definitions and storage layout
- expressions and conditions
- file interaction model
- SQL interaction placeholders and host variables
- unsupported and uncertain semantics
- verification-relevant side effects

## Required properties of every IR node

- stable id
- node type
- source provenance reference
- parent linkage where applicable
- diagnostics or uncertainty markers where applicable

## Key schema rules

- provenance is mandatory for executable semantics
- unsupported semantics must be represented explicitly
- uncertainty must be representable without breaking schema validity
- nodes used for slice generation must preserve dependency relationships
