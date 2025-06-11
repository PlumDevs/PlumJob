package com.plumdevs.plumjob.UI.component;
import com.plumdevs.plumjob.entity.Event;
import com.plumdevs.plumjob.repository.EventRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.plumdevs.plumjob.service.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.time.LocalDateTime;
@Component
public class EventReminderScheduler {
    private final EventRepository eventRepository;
    private final EmailService emailService;
    private final UserInfoRepository userInfoRepository;

    public EventReminderScheduler(EventRepository eventRepository, EmailService emailService,UserInfoRepository userInfoRepository) {
        this.eventRepository = eventRepository;
        this.emailService = emailService;
        this.userInfoRepository = userInfoRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void sendEventReminders() {

        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        LocalTime reminderStart = now.plusMinutes(58);
        LocalTime reminderEnd = now.plusMinutes(62);

        List<Event> eventsToRemind = eventRepository.findEventsToRemind(today, reminderStart, reminderEnd);
        for (Event event : eventsToRemind) {

            String email = userInfoRepository.getUserEmailByUsername(event.getUsername());
            System.out.println(email);

            emailService.sendReminderEmail(
                    email,
                    "Event Reminder",
                    "This is a reminder that you have an event: \"" + event.getDescription() + "\" at " + event.getEventTime()
            );

            event.setReminderSent(true);
            eventRepository.save(event);
        }
    }
}
