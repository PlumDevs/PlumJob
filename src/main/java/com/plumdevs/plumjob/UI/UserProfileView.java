package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.entity.Event;
import com.plumdevs.plumjob.repository.EventRepository;
import com.plumdevs.plumjob.service.TagService;
<<<<<<< HEAD
import com.plumdevs.plumjob.UI.component.StickyAdBar;
=======
import com.plumdevs.plumjob.service.UserService;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;

>>>>>>> 145a568b05368e2312842eaaf16e588016e86a2c
import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import com.plumdevs.plumjob.UI.component.Calendar;

@PermitAll
@PageTitle("Plum Job - User Profile")
@Route(value = "profile", layout = MainLayout.class)
public class UserProfileView extends VerticalLayout {
    private final TagService tagService;
    private final AuthenticationContext authContext;

<<<<<<< HEAD
    public UserProfileView(TagService tagService,
                           AuthenticationContext authContext,
                           EventRepository eventRepository) {
=======
    public UserProfileView(TagService tagService, AuthenticationContext authContext, UserService userService) {
>>>>>>> 145a568b05368e2312842eaaf16e588016e86a2c
        this.tagService = tagService;
        this.authContext = authContext;

        setWidthFull();
        setSpacing(true);
        setPadding(true);

        add(createProfileSection());
        add(new StickyAdBar(tagService, authContext));
        add(new Calendar(eventRepository, authContext));
    }

    private Component createProfileSection() {
        VerticalLayout profileSection = new VerticalLayout();
        profileSection.add(new H2("Your Profile"));

        ComboBox<String> industryComboBox = new ComboBox<>("Interested Industry");
        industryComboBox.setItems(
                "Software Development", "Data Science", "Cybersecurity",
                "Product Management", "UI/UX Design", "Cloud Engineering",
                "Project Management", "QA / Testing"
        );

        ComboBox<String> experienceComboBox = new ComboBox<>("Experience Level");
        experienceComboBox.setItems("Student", "Junior", "Mid-Level", "Senior", "Lead", "Manager");

        Span currentPreferencesLabel = new Span();
        currentPreferencesLabel.getStyle().set("margin-top", "20px");

        String username = userService.getUsername();//authContext.getPrincipalName().orElse(null);
        if (username != null) {
            String savedIndustry = tagService.getTagValueForType(username, "industry");
            String savedExperience = tagService.getTagValueForType(username, "experience");

            industryComboBox.setValue(savedIndustry);
            experienceComboBox.setValue(savedExperience);

            currentPreferencesLabel.setText("Your current preferences: " +
                    (savedIndustry != null ? savedIndustry : "No industry") + " / " +
                    (savedExperience != null ? savedExperience : "No experience level"));
        }

        Button saveButton = new Button("Save Profile", event -> {
            String industry = industryComboBox.getValue();
            String experience = experienceComboBox.getValue();

            if (industry == null || experience == null) {
                Notification.show("Please select both fields");
                return;
            }

            if (username == null) {
                Notification.show("User not logged in");
                return;
            }

            tagService.assignTagToUser(username, "industry:" + industry);
            tagService.assignTagToUser(username, "experience:" + experience);
            Notification.show("Preferences saved!");
            currentPreferencesLabel.setText("Your current preferences: " + industry + " / " + experience);
        });

<<<<<<< HEAD
        profileSection.add(industryComboBox, experienceComboBox, saveButton, currentPreferencesLabel);
        return profileSection;
=======
        add(industryComboBox, experienceComboBox, saveButton, currentPreferencesLabel);

        StickyAdBar adBar = new StickyAdBar(tagService, authContext, userService);
        add(adBar);
>>>>>>> 145a568b05368e2312842eaaf16e588016e86a2c
    }
}