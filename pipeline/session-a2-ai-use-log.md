# Session A2 AI-Use Log

## Kata 1 — Overlap-booking bug

- The assistant inspected the Feature 1 service, tests, feature spec, and decision log.
- The assistant added regression cases for a different-start overlap, the 45-minute travel buffer, and the exact buffer boundary.
- The assistant changed the booking conflict check to compare reserved intervals and updated the Feature 1 documentation and standing rule.
- The assistant added `.claude/agents/bugfix.md` as a reusable bugfix workflow.
- **Not completed by the assistant:** the hand-written-first implementation and the learner's personal comparison/reflection. Do not describe the assistant's code as a learner's manual fix.
- **Validation:** IDE diagnostics reported no Java errors. The focused `TechnicianAvailabilityServiceTest` suite and the full backend Maven test suite passed using temporary Maven and Java 17 installations under `/tmp`; project dependencies were not changed.

## Kata 2 — Feature 2 requirements

- The assistant explicitly invoked the BA agent with the intent to check stock and reserve parts for an already-scheduled job.
- The BA agent read the existing feature and decision artifacts and drafted `pipeline/features/feature-2-parts-reservation.md`.
- The assistant changed the status to a draft awaiting human review and annotated that intervention, then added labeled AI-review notes for the Feature 1 dependency, partial stock, zero stock, and duplicate requests. Unconfirmed business behavior remains marked for human review; no human review is claimed.
- The assistant updated `.claude/agents/ba-agent.md` with review-ready drafting guidance.

## Path convention note

The presentation names `pipeline/feature2/requirements.md`, while this repository requires one numbered feature spec under `pipeline/features/`. The spec is kept at the repository's canonical path to avoid maintaining duplicate requirements. Confirm the submission path with the instructor.