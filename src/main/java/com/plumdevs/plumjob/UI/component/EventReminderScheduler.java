package com.plumdevs.plumjob.UI.component;

import com.plumdevs.plumjob.entity.Event;
import com.plumdevs.plumjob.repository.EventRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.plumdevs.plumjob.service.EmailService;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.time.format.DateTimeFormatter;

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
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime reminderStart = now.plusMinutes(58);
        LocalDateTime reminderEnd = now.plusMinutes(62);

        List<Event> eventsToRemind = eventRepository.findEventsToRemindByDateTime(reminderStart, reminderEnd);

        for (Event event : eventsToRemind) {
            String email = userInfoRepository.getUserEmailByUsername(event.getUsername());
            System.out.println(email);

            String formattedTime = event.getEventTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            String formattedDate = event.getEventDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

            String subject = "🔔 Upcoming Event Reminder";
            String message = String.format(
                    "Hello!\n\n" +
                            "This is a reminder that your event is starting in about an hour:\n\n" +
                            "📅 Event: \"%s\"\n" +
                            "🕐 Time: %s\n" +
                            "📍 Date: %s\n\n" +
                            "Don't forget to prepare!\n\n" +
                            "Good luck!\n" +
                            "PlumJob Team",
                    event.getDescription(),
                    formattedTime,
                    formattedDate
            );

            emailService.sendReminderEmail(email, subject, message);

            event.setReminderSent(true);
            eventRepository.save(event);
        }
    }
}
