# Runbook: Reference Corpus Strategy

Purpose: Define how to build and maintain the reference corpus used to implement and qualify the engine.

## Corpus composition

Include representative families of:
- batch-like programs
- request-like programs
- copybook-heavy programs
- SQL-heavy programs
- file-heavy programs
- unsupported-edge programs used to prove blocking behavior

## Rules

- every corpus member must have a purpose
- every corpus member must map to supported-v1 scope or explicit blocked scope
- fixture provenance must be recorded
- masked or synthetic data rules must be documented

## Qualification usage

The reference corpus is the source of truth for engine readiness claims until a real customer onboarding program exists.
