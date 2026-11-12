# Feature 1 — Decision Log

Decisions made while building Feature 1 that later features may need to retrieve. This file exists so a later feature's memory-retrieval step has something concrete to pull a single fact from, instead of needing the entire build history in context.

## Decision: standard travel buffer per job is 45 minutes

**What was decided:** every booked job reserves not just its own start/end time on a technician's calendar, but an additional 45-minute travel buffer, to account for the technician getting to the next job. This is encoded as `TRAVEL_BUFFER_MINUTES = 45` in `TechnicianAvailabilityService`.

**Why it matters beyond Feature 1:** this buffer is what actually limits how many jobs — and, later, how many parts-reservations tied to those jobs — can realistically be scheduled for one technician in a day. Anything built on top of the calendar that reasons about "how many jobs can fit today" needs this number, not a guess.

**Where it lives in code:** `backend/src/main/java/com/serviceforge/service/TechnicianAvailabilityService.java`, `TRAVEL_BUFFER_MINUTES` constant.

## Historical bug: overlap detection compared exact start times

**What happened:** the original `bookJob(...)` implementation rejected a new booking only if its start time exactly matched an existing booking's start time. It did not check whether the reserved time ranges overlapped. This was an implementation bug, not an accepted scheduling policy.

**Resolution:** Session A2 changed conflict detection to compare half-open reserved intervals, extending each job's end by the 45-minute travel buffer. A booking is rejected when those reserved intervals intersect. The exact buffer boundary is available for the next job, and the same technician restriction remains in force.

**Standing requirement:** follow `pipeline/rules/no-overlap-booking.md` and retain regression tests for interval overlap and the travel-buffer boundary.
