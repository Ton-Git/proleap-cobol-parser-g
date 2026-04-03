# AI-Assisted Rule Authoring

Purpose: Define how agents may help author IR and generator rules without leaving opaque logic in the engine.

## Allowed workflow

1. Provide a bounded set of fixture cases.
2. Ask an agent to propose a rule or mapping.
3. Require the proposal to cite affected construct families.
4. Convert the accepted proposal into versioned code or declarative rules.
5. Add regression fixtures before release.

## Required evidence

- prompt and model metadata
- fixture references
- proposed rule text
- affected tests
- reviewer disposition

## Disallowed workflow

- shipping raw prompt output as the final implementation
- accepting rule changes with no regression coverage
