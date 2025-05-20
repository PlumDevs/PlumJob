package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.service.TagService;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import com.plumdevs.plumjob.UI.component.StickyAdBar;
import com.plumdevs.plumjob.service.TagService;
import com.vaadin.flow.spring.security.AuthenticationContext;

import java.util.List;

@PermitAll
@PageTitle("Plum Job - User Profile")
@Route(value = "profile", layout = MainLayout.class)
public class UserProfileView extends VerticalLayout {

    private final TagService tagService;
    private final AuthenticationContext authContext;

    public UserProfileView(TagService tagService, AuthenticationContext authContext) {
        this.tagService = tagService;
        this.authContext = authContext;

        setWidthFull();
        setSpacing(true);
        setPadding(true);

        H2 title = new H2("Your Profile");
        add(title);

        ComboBox<String> industryComboBox = new ComboBox<>("Interested Industry");
        List<String> industries = List.of(
                "Software Development",
                "Data Science",
                "Cybersecurity",
                "Product Management",
                "UI/UX Design",
                "Cloud Engineering",
                "Project Management",
                "QA / Testing"
        );
        industryComboBox.setItems(industries);

        ComboBox<String> experienceComboBox = new ComboBox<>("Experience Level");
        experienceComboBox.setItems("Student", "Junior", "Mid-Level", "Senior", "Lead", "Manager");

        Span currentPreferencesLabel = new Span();
        currentPreferencesLabel.getStyle().set("margin-top", "20px");

        String username = authContext.getPrincipalName().orElse(null);
        if (username != null) {
            String savedIndustry = tagService.getTagValueForType(username, "industry");
            String savedExperience = tagService.getTagValueForType(username, "experience");

            if (savedIndustry != null) {
                industryComboBox.setValue(savedIndustry);
            }

            if (savedExperience != null) {
                experienceComboBox.setValue(savedExperience);
            }

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

        add(industryComboBox, experienceComboBox, saveButton, currentPreferencesLabel);

        StickyAdBar adBar = new StickyAdBar(tagService, authContext);
        add(adBar);
    }
}
