# No Overlapping Technician Bookings

When booking a technician, compare the proposed time slot with every existing job for that same technician. Treat each job as reserving a half-open interval from its start time through its end time plus `TRAVEL_BUFFER_MINUTES`.

Reject the booking when the proposed reserved interval intersects an existing reserved interval. For proposed `[start, end + buffer)` and existing `[existingStart, existingEnd + buffer)`, a conflict exists when:

```text
start < existingEnd + buffer
and existingStart < end + buffer
```

Use the shared `TRAVEL_BUFFER_MINUTES` constant; do not duplicate or silently change its value. A new job may start exactly when an earlier job's travel buffer expires. Keep focused regression tests for different-start overlaps, travel-buffer conflicts, and the exact buffer boundary.