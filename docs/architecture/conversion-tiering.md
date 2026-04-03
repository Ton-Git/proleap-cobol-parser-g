# Conversion Tiering

Purpose: Define the conversion support levels used by the engine.

Tier 1: deterministic automatic
- parser, IR, runtime, and generator already support the pattern
- deterministic scaffold exists
- deterministic gates remain authoritative

Tier 2: deterministic plus human-guided agent assistance
- deterministic scaffold exists
- a human may use any coding agent to fill edit zones or packaging logic
- deterministic gates remain authoritative

Tier 3: manual remediation
- deterministic scaffold is incomplete or blocked
- human engineering work is required outside bounded supported patterns
