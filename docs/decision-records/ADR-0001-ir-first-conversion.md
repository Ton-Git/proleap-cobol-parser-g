# ADR-0001: IR-First Conversion Strategy

Status: Accepted
Date: 2026-04-03

## Context

Direct parser-to-codegen coupling would make semantic changes opaque and hard to verify.

## Decision

The engine will normalize parser outputs into a versioned IR before code generation or agent packaging.

## Consequences

- codegen and verification depend on stable IR, not parser internals
- unsupported semantics stay explicit
