package com.plumdevs.plumjob.UI.component;

import com.plumdevs.plumjob.entity.Event;
import com.plumdevs.plumjob.repository.EventRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.transaction.annotation.Transactional;
import com.plumdevs.plumjob.UI.component.Calendar;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Calendar extends VerticalLayout {
    private final EventRepository eventRepository;
    private final AuthenticationContext authContext;
    private final DatePicker datePicker;
    private final VerticalLayout eventsLayout;
    private final Dialog addEventDialog;

    public Calendar(EventRepository eventRepository, AuthenticationContext authContext) {
        this.eventRepository = eventRepository;
        this.authContext = authContext;

        setWidthFull();
        setSpacing(true);
        setPadding(true);

        this.datePicker = new DatePicker("Select date");
        this.eventsLayout = new VerticalLayout();
        this.addEventDialog = createEventDialog();

        initCalendarSection();
    }

    private void initCalendarSection() {
        datePicker.setPrefixComponent(VaadinIcon.CALENDAR.create());
        datePicker.setWidth("300px");

        eventsLayout.setPadding(false);
        eventsLayout.setSpacing(false);

        Button addEventButton = new Button("Add Event", VaadinIcon.PLUS.create(), e -> {
            if (datePicker.getValue() == null) {
                Notification.show("Please select a date first");
                return;
            }
            addEventDialog.open();
        });

        datePicker.addValueChangeListener(e -> refreshEvents());

        HorizontalLayout calendarControls = new HorizontalLayout(datePicker, addEventButton);
        calendarControls.setAlignItems(Alignment.BASELINE);

        add(new H2("Your Calendar"), calendarControls, eventsLayout);
    }

    private Dialog createEventDialog() {
        Dialog dialog = new Dialog();
        TextField eventField = new TextField("Event description");
        TimePicker eventTimePicker = new TimePicker("Event time");
        eventTimePicker.setValue(LocalTime.NOON);

        Button saveButton = new Button("Save", e -> {
            try {
                LocalDate selectedDate = datePicker.getValue();
                LocalTime eventTime = eventTimePicker.getValue() != null ? eventTimePicker.getValue() : LocalTime.NOON;
                String description = eventField.getValue().trim();

                saveEvent(selectedDate, eventTime, description);
                dialog.close();
                Notification.show("Event saved successfully!");
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage());
            }
        });

        LocalDate selectedDate = datePicker.getValue();
        String dateDisplay = selectedDate != null ? selectedDate.toString() : "selected day";

        VerticalLayout dialogLayout = new VerticalLayout(
                new H2("Add new event for " + dateDisplay),
                new Span("Date: " + dateDisplay),
                eventField,
                eventTimePicker,
                saveButton
        );

        dialogLayout.setPadding(true);
        dialogLayout.setSpacing(true);
        dialog.add(dialogLayout);
        return dialog;
    }

    @Transactional
    private void saveEvent(LocalDate date, LocalTime time, String description) {
        String username = authContext.getPrincipalName().orElse(null);
        if (date == null || username == null || description.isEmpty()) {
            throw new IllegalArgumentException("Please provide all event details");
        }

        Event newEvent = new Event(date, time, description, username);
        eventRepository.save(newEvent);
        refreshEvents();
    }

    private void refreshEvents() {
        eventsLayout.removeAll();
        LocalDate selectedDate = datePicker.getValue();
        String username = authContext.getPrincipalName().orElse(null);

        if (selectedDate == null || username == null) {
            eventsLayout.add(new Paragraph("No date selected or user not logged in"));
            return;
        }

        List<Event> events = eventRepository.findByEventDateAndUsername(selectedDate, username);

        if (events.isEmpty()) {
            eventsLayout.add(new Paragraph("No events for selected day."));
        } else {
            eventsLayout.add(new H2("Events:"));
            events.forEach(event -> {
                HorizontalLayout eventLayout = new HorizontalLayout(
                        new Paragraph(event.getEventTime() + ": " + event.getDescription()),
                        new Button(VaadinIcon.TRASH.create(), e -> {
                            eventRepository.deleteById(event.getId());
                            refreshEvents();
                        })
                );
                eventLayout.setAlignItems(Alignment.CENTER);
                eventsLayout.add(eventLayout);
            });
        }
    }
}