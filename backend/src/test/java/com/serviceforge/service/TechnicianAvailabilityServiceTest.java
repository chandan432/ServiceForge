package com.serviceforge.service;

import com.serviceforge.data.MockDataStore;
import com.serviceforge.model.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TechnicianAvailabilityServiceTest {

    private MockDataStore dataStore;
    private TechnicianAvailabilityService service;

    @BeforeEach
    void setUp() {
        dataStore = new MockDataStore();
        dataStore.seed();
        service = new TechnicianAvailabilityService(dataStore);
    }

    @Test
    void booksAJobForAnAvailableTechnician() {
        LocalDateTime start = LocalDateTime.now().withHour(17).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusHours(1);

        Job job = service.bookJob(1L, "New Customer", start, end);

        assertEquals("New Customer", job.getCustomerName());
        assertEquals(1L, job.getTechnicianId());
    }

    @Test
    void rejectsABookingAtAnIdenticalStartTimeAsAnExistingJob() {
        LocalDateTime start = LocalDateTime.now().withHour(9).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusHours(1);
        // Technician 1 already has a seeded job starting at 09:00 (see MockDataStore.seed()).

        assertThrows(IllegalStateException.class, () -> service.bookJob(1L, "Conflicting Customer", start, end));
    }

    @Test
    void rejectsOverlappingJobsWithDifferentStartTimes() {
        LocalDateTime start = LocalDateTime.of(2030, 1, 15, 9, 0);
        service.bookJob(1L, "First Customer", start, start.plusHours(2));
        int jobCount = dataStore.getAllJobs().size();

        assertThrows(IllegalStateException.class,
                () -> service.bookJob(1L, "Second Customer", start.plusHours(1), start.plusHours(3)));

        assertEquals(jobCount, dataStore.getAllJobs().size());
    }

    @Test
    void allowsOverlappingJobsForDifferentTechnicians() {
        LocalDateTime start = LocalDateTime.of(2030, 1, 15, 9, 0);
        service.bookJob(1L, "First Customer", start, start.plusHours(2));

        Job job = service.bookJob(2L, "Second Customer", start.plusHours(1), start.plusHours(3));

        assertEquals(2L, job.getTechnicianId());
    }

    @ParameterizedTest
    @CsvSource({
            "8, 0, 10, 0",
            "9, 30, 10, 30",
            "8, 0, 12, 0",
            "11, 0, 12, 0",
            "11, 44, 12, 44",
            "7, 0, 8, 16"
    })
    void rejectsOverlapsAndInsufficientTravelTime(int startHour, int startMinute, int endHour, int endMinute) {
        LocalDateTime existingStart = LocalDateTime.of(2030, 1, 15, 9, 0);
        service.bookJob(1L, "First Customer", existingStart, existingStart.plusHours(2));
        LocalDateTime start = existingStart.withHour(startHour).withMinute(startMinute);
        LocalDateTime end = existingStart.withHour(endHour).withMinute(endMinute);

        assertThrows(IllegalStateException.class, () -> service.bookJob(1L, "Second Customer", start, end));
    }

    @ParameterizedTest
    @CsvSource({"11, 45, 12, 45", "7, 0, 8, 15"})
    void allowsExactlyTheTravelBufferBetweenJobs(int startHour, int startMinute, int endHour, int endMinute) {
        LocalDateTime existingStart = LocalDateTime.of(2030, 1, 15, 9, 0);
        service.bookJob(1L, "First Customer", existingStart, existingStart.plusHours(2));
        LocalDateTime start = existingStart.withHour(startHour).withMinute(startMinute);
        LocalDateTime end = existingStart.withHour(endHour).withMinute(endMinute);

        Job job = service.bookJob(1L, "Second Customer", start, end);

        assertEquals(start, job.getStartTime());
        assertEquals(end, job.getEndTime());
    }

    @Test
    void throwsForAnUnknownTechnician() {
        LocalDateTime start = LocalDateTime.now().plusDays(1);
        LocalDateTime end = start.plusHours(1);

        assertThrows(IllegalArgumentException.class, () -> service.bookJob(999L, "Nobody", start, end));
    }
}
