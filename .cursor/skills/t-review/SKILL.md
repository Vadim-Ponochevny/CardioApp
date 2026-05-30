---
name: t-code-review
description: >
  Performs automated code review based on the git diff between the current branch and origin/develop.
  Use this skill whenever the user asks to "review code", "do a code review", "check my changes",
  "review my branch", "review the diff", "что не так с кодом", "сделай ревью", "проверь изменения",
  or any similar request to analyze code changes relative to origin/develop. Always trigger when the user
  is in a project root and wants feedback on their current branch changes. The skill runs git diff,
  analyzes the output, and produces a structured review with severity levels, file names, diffs,
  problems, and recommendations.
---

# Code Review Skill

Performs a structured code review based on the diff between the current branch and `origin/develop`.

## Workflow

### Step 1 — Get the diff

Run the following command in the project root (do NOT navigate anywhere):

```bash
git diff origin/develop...HEAD
```

If that fails (e.g. `origin/develop` branch doesn't exist), try:
```bash
git diff main...HEAD
```

If the diff is empty, also check staged changes:
```bash
git diff origin/develop
```

If there are no changes at all, report this to the user.

### Step 2 — Analyze the diff

Review ALL changed files in the diff. For each issue found, create a review entry.

Focus on:
- **Bugs** — logic errors, off-by-one errors, null dereferences, race conditions
- **Security** — SQL injection, XSS, hardcoded secrets, insecure deserialization, missing auth checks
- **Performance** — N+1 queries, unnecessary re-renders, missing indexes, blocking calls
- **Code quality** — code duplication, overly complex logic, missing error handling, dead code
- **Best practices** — naming conventions, SOLID violations, missing tests for critical paths
- **Breaking changes** — API contract changes, removed exports, schema migrations without backwards compat

Do NOT flag:
- Style/formatting issues (spaces, tabs, line length) unless they indicate a real problem
- Minor naming preferences
- Changes that are clearly intentional and correct

*If the diff is large, make review file by file.*

### Step 3 — Output the review

Output review entries **sorted by severity** (Critical → High → Medium → Low).

For each issue, use this exact format:

---
**[NUMBER]**

**[SEVERITY]**

`filename.ext`

```diff
<paste only the relevant lines from the diff, not the entire file diff>
```

**Problem:** Clear description of what is wrong and why it's a problem.

**Recommendation:** Specific, actionable fix with a code example if helpful.

---

#### Severity levels:

| Level | Meaning |
|-------|---------|
| 🔴 Critical | Will cause crashes, data loss, security vulnerabilities, or broken production |
| 🟠 High | Likely bugs, significant performance issues, or security concerns |
| 🟡 Medium | Code quality issues that may cause problems under certain conditions |
| 🔵 Low | Minor improvements, best practices, or refactoring suggestions |

### Step 4 — Summary

After all issues, output a brief summary:

```
## Summary
- 🔴 Critical: N
- 🟠 High: N
- 🟡 Medium: N
- 🔵 Low: N

Total files changed: N
Overall assessment: [one sentence]
```

## Notes

- If the diff is very large (>1000 lines), prioritize Critical and High issues and note that the review is focused on the most important findings.
- Review should be in the same language the user used (Russian if they wrote in Russian, English if in English).
- When showing diff snippets, show only the relevant changed lines (with a few lines of context), not the entire file diff.
- Always include the `+`/`-` prefixes from the diff in the code block so it's clear what was added/removed.
- Numbering
