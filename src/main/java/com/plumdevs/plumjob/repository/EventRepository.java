package com.plumdevs.plumjob.repository;

import com.plumdevs.plumjob.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.reminderSent = false AND e.eventDate = :date " +
            "AND e.eventTime BETWEEN :startTime AND :endTime")
    List<Event> findEventsToRemind(
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);

    @Query("SELECT e FROM Event e WHERE e.reminderSent = false " +
            "AND FUNCTION('CONCAT', e.eventDate, ' ', e.eventTime) BETWEEN :startDateTime AND :endDateTime")
    List<Event> findEventsToRemindByDateTime(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime);

    List<Event> findByEventDateAndEventTimeAndReminderSentFalse(LocalDate date, LocalTime time);
    List<Event> findByEventDateAndUsername(LocalDate date, String username);
    void deleteByIdAndUsername(Long id, String username);
}
