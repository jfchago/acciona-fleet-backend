## PR / Branch Security Review

**Branch**: `feature/GH-4-carfleet-requests-lifecycle`
**Base**: `main`
**Review scope**: Changes introduced while resolving PR #6 review findings.

### Risk Assessment: LOW

### Security Findings in This Diff

No critical or high findings remain open. The mutation authorization path now requires an action-specific authority; PATCH input is typed and Bean Validation rejects malformed values before the application service; persistence queries remain parameterized native queries; and ETags are checked before writes.

### Security-Sensitive Changes

- `CarFleetRequestAuthorizationAdapter`: `Access` is limited to READ and configured per-action authorities remain supported.
- `CarFleetRequestController` / `CarFleetRequestPatch`: typed allow-listed request body, field limits, and strict quoted `If-Match` parsing.
- `CarFleetRequestJpaAdapter` / legacy repositories: resource lookup and optimistic concurrency happen before mutation, with parameterized filters.

### Approved Changes

- Domain validation for legacy retirement states and exactly four card digits.
- Normalized 400/422 error handling without exposing stack traces.
- OpenAPI documents security scopes, ETag concurrency, warnings, and problem responses.

### Summary

The reviewed changes are security-neutral or improve authorization and input handling. Residual integration risk is limited to confirming that production legacy views expose the `sysdate` column used as the shared version source.
