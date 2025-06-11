package com.plumdevs.plumjob.entity;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate eventDate;
    private LocalTime eventTime;
    private String description;
    private String username;
    private boolean reminderSent = false;

    public boolean isReminderSent() {
        return reminderSent;
    }

    public void setReminderSent(boolean reminderSent) {
        this.reminderSent = reminderSent;
    }

    public LocalDateTime getEventDateTime() {
        return LocalDateTime.of(this.eventDate, this.eventTime);
    }


    public Event() {}

    public Event(LocalDate eventDate, LocalTime eventTime, String description, String username,boolean reminderSent) {
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.description = description;
        this.username = username;
        this.reminderSent = reminderSent;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public LocalTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
