# Session A2 Reflection

## Manual-First Evidence

**Hand-written change:** PR #2, branch `homework/session-a2-manual`, commit `505f83c` ("Fix overlapping technician bookings and enforce travel buffer").

**Reproduction before the fix:** I found that the existing logic only checked whether two bookings had the same start time. For example, if a technician already had a job from 09:00 to 11:00, the system could still accept another job from 10:00 to 12:00. I added the `rejectsOverlappingJobsWithDifferentStartTimes` test to cover this issue.

**Verification after the fix:** I ran the full backend Maven test suite against my manual change on 2026-11-17, and it passed. I also covered bookings for different technicians, overlapping time ranges, the 45-minute travel-buffer boundaries, and whether a rejected booking leaves the job list unchanged.

**Implementation comparison:** Both my fix and the agent's fix compare the time intervals instead of just checking start times. Both also include the 45-minute travel buffer. In my code, I used `plusMinutes`, `isBefore`, and `isAfter` directly in the condition. The agent stored the reserved end times in variables first, which made its condition a little easier to read. The error messages were slightly different too: mine was generic, while the agent's included the proposed start time.

**Test comparison:** I used parameterized tests to check several overlap and boundary cases, including bookings for different technicians. The agent used separate tests for overlapping jobs, travel-buffer conflicts on either side of a job, and a booking that starts exactly when the buffer ends. Both approaches cover the main requirement, but I found the parameterized tests useful for checking multiple boundaries together.

## Reflection

**Exact doing-to-deciding moment:** When I fixed the bug manually, I spent most of my time working out the interval condition and writing the tests. When I reviewed the agent's fix, I was looking at the problem differently. Instead of writing the condition again, I checked whether the proposed logic would reject overlapping bookings and still allow a booking exactly at the end of the 45-minute buffer. That was the point where I moved from doing the implementation to deciding whether the implementation was correct.

**What did not change:** I was still responsible for making sure the fix met the requirement. Even though the agent wrote the code, I needed to review the logic, check the edge cases, and make sure the tests passed. I would not accept the fix just because the agent produced it.

**Fix comparison:** My fix and the agent's fix use the same basic interval-overlap approach. The main difference is how the condition is written. The agent's named variables make it easier to follow, while my inline version is more compact. My tests cover multiple cases through parameters, and the agent's tests explain each scenario separately. I don't think one approach is automatically better in every case; readability and coverage both matter.

**Edit to defend at Show & Tell:** I would defend replacing the exact-start-time check with an interval comparison that includes the 45-minute travel buffer. The original check missed bookings that overlapped but started at different times. The new logic handles that case and allows a booking once the buffer has ended. I also added boundary tests so this behavior is checked rather than assumed.

**What I would check first next time:** I would start with the Definition of Done and the decision log to understand the expected behavior. Then I would write a test that reproduces the bug before making changes. If I use an agent, I would give it those requirements and test cases, then review its fix against them instead of relying only on the generated code.

---

The manual-fix evidence refers to the learner-authored PR commit. The agent-directed implementation is on the separate `homework/session-a2` branch.
