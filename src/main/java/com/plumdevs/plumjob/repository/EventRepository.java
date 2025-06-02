package com.plumdevs.plumjob.repository;

import com.plumdevs.plumjob.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventDateAndUsername(LocalDate date, String username);
    void deleteByIdAndUsername(Long id, String username);
}