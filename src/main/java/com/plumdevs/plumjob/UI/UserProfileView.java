package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.service.UserService;
import com.plumdevs.plumjob.UI.component.StickyAdBar;
import com.plumdevs.plumjob.UI.component.Calendar;
import com.plumdevs.plumjob.service.TagService;
import com.plumdevs.plumjob.service.UserService;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.service.TagService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Page;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.PermitAll;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.Map.entry;
import java.io.ByteArrayInputStream;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import com.plumdevs.plumjob.repository.EventRepository;


@PermitAll
@PageTitle("Plum Job - User Profile")
@Route(value = "profile", layout = MainLayout.class)
public class UserProfileView extends VerticalLayout {

    private TagService tagService;
    private AuthenticationContext authContext;
    private Image profileImage;
    private String username;
    private UserService userService;
    private Span currentPreferencesLabel;
    private Span profileStatsSpan;
    private VerticalLayout skillsList;
    private Span compatibilityScore;
    private Div compatibilityProgressBar;
    private com.vaadin.flow.component.textfield.TextArea notesArea;
    // Dodaj to jako pole klasy
    private boolean hasNotesFlag = false;
    private final EventRepository eventRepository;




    private static final Map<String, List<String>> JOB_SKILLS_MAP = Map.ofEntries(
            entry("Full Stack Developer", List.of("Java", "JavaScript", "React", "Spring Boot", "SQL", "HTML", "CSS")),
            entry("Frontend Developer", List.of("JavaScript", "React", "Angular", "Vue.js", "HTML", "CSS", "SASS", "Bootstrap")),
            entry("Backend Developer", List.of("Java", "Python", "Spring Boot", "Node.js", "SQL", "MongoDB", "PostgreSQL")),
            entry("DevOps Engineer", List.of("Docker", "Kubernetes", "AWS", "Azure", "Jenkins", "GitLab CI", "Python")),
            entry("Data Scientist", List.of("Python", "Machine Learning", "Data Science", "TensorFlow", "PyTorch", "SQL")),
            entry("Mobile Developer", List.of("Java", "Kotlin", "JavaScript", "React", "C#")),
            entry("UI/UX Designer", List.of("UI/UX Design", "Figma", "Adobe XD", "Photoshop", "Illustrator", "HTML", "CSS")),
            entry("Project Manager", List.of("Project Management", "Agile", "Scrum", "Kanban", "Jira", "Confluence")),
            entry("Cloud Engineer", List.of("AWS", "Azure", "Google Cloud", "Docker", "Kubernetes", "Python", "Jenkins")),
            entry("Database Administrator", List.of("SQL", "MySQL", "PostgreSQL", "MongoDB", "Oracle", "SQLite", "Python")),
            entry("AI Engineer", List.of("Python", "Machine Learning", "AI", "TensorFlow", "PyTorch", "Data Science")),
            entry("Web Developer", List.of("JavaScript", "HTML", "CSS", "PHP", "Laravel", "React", "Node.js", "SQL"))
    );


    public UserProfileView(TagService tagService, AuthenticationContext authContext, UserService userService,  EventRepository eventRepository) {
        this.tagService = tagService;
        this.authContext = authContext;
        this.userService = userService;
        this.username = authContext.getPrincipalName().orElse(null);
        this.eventRepository = eventRepository;

        setWidthFull();
        setSpacing(false);
        setPadding(false);

        getStyle()
                .set("background", "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)")
                .set("min-height", "100vh");

        initializeUI();
        loadNotesFlag();
    }



    private void initializeUI() {
        createHeader();

        VerticalLayout mainContainer = new VerticalLayout();
        mainContainer.setWidthFull();
        mainContainer.setMaxWidth("1200px");
        mainContainer.getStyle().set("margin", "0 auto");
        mainContainer.setPadding(true);
        mainContainer.setSpacing(true);

        HorizontalLayout twoColumnLayout = new HorizontalLayout();
        twoColumnLayout.setWidthFull();
        twoColumnLayout.setSpacing(true);
        twoColumnLayout.setAlignItems(FlexComponent.Alignment.START);

        VerticalLayout leftColumn = createLeftColumn();
        leftColumn.setWidth("400px");

        VerticalLayout rightColumn = createRightColumn();

        twoColumnLayout.add(leftColumn, rightColumn);
        twoColumnLayout.setFlexGrow(0, leftColumn);
        twoColumnLayout.setFlexGrow(1, rightColumn);

        mainContainer.add(twoColumnLayout);
        add(mainContainer);

        add(new StickyAdBar(tagService, authContext, userService));
    }


    private void createHeader() {
        VerticalLayout headerContainer = new VerticalLayout();
        headerContainer.setWidthFull();
        headerContainer.setPadding(true);
        headerContainer.setSpacing(false);
        headerContainer.getStyle()
                .set("background", "linear-gradient(135deg, #730D3F 0%, #a91b5b 100%)")
                .set("color", "white")
                .set("margin-bottom", "0")
                .set("box-shadow", "0 4px 20px rgba(115, 13, 63, 0.3)");

        H1 title = new H1("Your Profile");
        title.getStyle()
                .set("margin", "0")
                .set("color", "white")
                .set("font-size", "2.5rem")
                .set("font-weight", "300")
                .set("text-align", "center");

        Span subtitle = new Span("Manage your preferences and personal information");
        subtitle.getStyle()
                .set("font-size", "1.1rem")
                .set("opacity", "0.9")
                .set("text-align", "center")
                .set("display", "block")
                .set("margin-top", "8px");

        headerContainer.add(title, subtitle);
        headerContainer.setAlignItems(FlexComponent.Alignment.CENTER);
        add(headerContainer);
    }

    private VerticalLayout createLeftColumn() {
        VerticalLayout leftColumn = new VerticalLayout();
        leftColumn.setSpacing(true);
        leftColumn.setPadding(false);

        VerticalLayout profileCard = createProfileCard();

        VerticalLayout preferencesCard = createPreferencesCard();

        VerticalLayout jobMatchCard = createJobMatchCard();

        leftColumn.add(profileCard, preferencesCard, jobMatchCard);
        return leftColumn;
    }

    private VerticalLayout createRightColumn() {
        VerticalLayout rightColumn = new VerticalLayout();
        rightColumn.setSpacing(true);
        rightColumn.setPadding(false);

        VerticalLayout notesCard = createNotesCard();

        VerticalLayout statsCard = createStatsCard();

        VerticalLayout skillsCard = createSkillsCard();

        VerticalLayout calendarCard = createCalendarCard();

        rightColumn.add(calendarCard, notesCard, statsCard, skillsCard);
        return rightColumn;
    }

    private VerticalLayout createCard(String title, Icon icon) {
        VerticalLayout card = new VerticalLayout();
        card.setSpacing(true);
        card.setPadding(true);
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 6px rgba(0, 0, 0, 0.1)")
                .set("border", "1px solid rgba(0, 0, 0, 0.05)");

        HorizontalLayout cardHeader = new HorizontalLayout();
        cardHeader.setWidthFull();
        cardHeader.setAlignItems(FlexComponent.Alignment.CENTER);
        cardHeader.setSpacing(true);
        cardHeader.getStyle().set("margin-bottom", "16px");

        if (icon != null) {
            icon.setColor("#730D3F");
            icon.setSize("20px");
            icon.getStyle().set("filter", "drop-shadow(0 0 8px rgba(115, 13, 63, 0.3))");
            cardHeader.add(icon);
        }

        H3 cardTitle = new H3(title);
        cardTitle.getStyle()
                .set("margin", "0")
                .set("color", "#2d3748")
                .set("font-size", "1.25rem")
                .set("font-weight", "600")
                .set("text-shadow", "0 0 10px rgba(115, 13, 63, 0.1)");

        cardHeader.add(cardTitle);
        card.add(cardHeader);

        return card;
    }

    private VerticalLayout createProfileCard() {
        VerticalLayout profileCard = createCard("Profile Information", VaadinIcon.USER.create());

        VerticalLayout profileImageContainer = setupProfileImage();
        profileCard.add(profileImageContainer);

        return profileCard;
    }

    private VerticalLayout createPreferencesCard() {
        VerticalLayout preferencesCard = createCard("Job Preferences", VaadinIcon.COG.create());

        ComboBox<String> industryComboBox = new ComboBox<>("Interested Industry");
        List<String> industries = List.of(
                "Software Development", "Data Science", "Cybersecurity", "Product Management",
                "UI/UX Design", "Cloud Engineering", "Project Management", "QA / Testing"
        );
        industryComboBox.setItems(industries);
        industryComboBox.setWidthFull();

        ComboBox<String> experienceComboBox = new ComboBox<>("Experience Level");
        experienceComboBox.setItems("Student", "Junior", "Mid-Level", "Senior", "Lead", "Manager");
        experienceComboBox.setWidthFull();

        currentPreferencesLabel = new Span();
        currentPreferencesLabel.getStyle()
                .set("margin-top", "16px")
                .set("padding", "12px")
                .set("background", "#f7fafc")
                .set("border-radius", "8px")
                .set("border-left", "4px solid #730D3F")
                .set("font-size", "0.9rem")
                .set("box-shadow", "0 0 15px rgba(115, 13, 63, 0.1)");



        String username = userService.getUsername();

        if (username != null) {
            String savedIndustry = tagService.getTagValueForType(username, "industry");
            String savedExperience = tagService.getTagValueForType(username, "experience");

            if (savedIndustry != null) industryComboBox.setValue(savedIndustry);
            if (savedExperience != null) experienceComboBox.setValue(savedExperience);

            updatePreferencesLabel(savedIndustry, savedExperience);
        }

        Button saveButton = new Button("Save Preferences", VaadinIcon.CHECK.create());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.setWidthFull();
        saveButton.getStyle().set("margin-top", "16px");

        saveButton.addClickListener(event -> {
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

            Notification.show("Preferences saved successfully!");
            updatePreferencesLabel(industry, experience);
        });

        preferencesCard.add(industryComboBox, experienceComboBox, saveButton, currentPreferencesLabel);
        return preferencesCard;
    }

    private VerticalLayout createJobMatchCard() {
        VerticalLayout jobMatchCard = createCard("Job Compatibility", VaadinIcon.HEART.create());

        HorizontalLayout scoreLayout = new HorizontalLayout();
        scoreLayout.setWidthFull();
        scoreLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        scoreLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);

        Span compatibilityLabel = new Span("Profile Match Score");
        compatibilityLabel.getStyle()
                .set("font-weight", "600")
                .set("color", "#2d3748");

        compatibilityScore = new Span("0%");
        compatibilityScore.getStyle()
                .set("font-size", "1.5rem")
                .set("font-weight", "700")
                .set("color", "#730D3F")
                .set("text-shadow", "0 0 10px rgba(115, 13, 63, 0.3)");

        scoreLayout.add(compatibilityLabel, compatibilityScore);

        Div progressContainer = new Div();
        progressContainer.getStyle()
                .set("background", "#e2e8f0")
                .set("height", "8px")
                .set("border-radius", "4px")
                .set("overflow", "hidden")
                .set("margin", "12px 0");

        compatibilityProgressBar = new Div();
        compatibilityProgressBar.getStyle()
                .set("background", "linear-gradient(90deg, #730D3F, #a91b5b)")
                .set("height", "100%")
                .set("width", "0%")
                .set("border-radius", "4px")
                .set("box-shadow", "0 0 15px rgba(115, 13, 63, 0.4)")
                .set("transition", "all 0.3s ease");

        progressContainer.add(compatibilityProgressBar);

        VerticalLayout recommendedJobs = new VerticalLayout();
        recommendedJobs.setSpacing(true);
        recommendedJobs.setPadding(false);
        recommendedJobs.getStyle().set("margin-top", "16px");

        Span recommendedTitle = new Span("Recommended Jobs");
        recommendedTitle.getStyle()
                .set("font-weight", "600")
                .set("color", "#2d3748")
                .set("margin-bottom", "8px")
                .set("display", "block");

        VerticalLayout jobRecommendationsContainer = new VerticalLayout();
        jobRecommendationsContainer.setSpacing(true);
        jobRecommendationsContainer.setPadding(false);

        jobMatchCard.add(scoreLayout, progressContainer, recommendedTitle, jobRecommendationsContainer);

        this.jobRecommendationsContainer = jobRecommendationsContainer;

        updateJobCompatibility();

        return jobMatchCard;
    }

    private VerticalLayout jobRecommendationsContainer;

    private Span compatibilityLabel = new Span("Job Compatibility: 0%");

    private Map<String, Integer> parseSkillsJson(String skillsJson) {
        Map<String, Integer> skills = new HashMap<>();
        try {
            String clean = skillsJson.replace("[", "").replace("]", "").replace("\"", "");
            String[] entries = clean.split(",");
            for (String entry : entries) {
                String[] parts = entry.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int level = Integer.parseInt(parts[1].trim());
                    skills.put(name, level);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing skills: " + e.getMessage());
        }
        return skills;
    }

    private Span jobCompatibilityLabel = new Span();

    private String getSkillsStorageKey() {
        return "skills_default";
    }



    private void updateJobCompatibility() {
        String userEmail = "guest";
        if (username != null) {
            String email = userService.getUserEmail(username);
            if (email != null && !email.isEmpty()) {
                userEmail = email;
            }
        }

        final String skillsStorageKey = "plumjob_user_skills_" + userEmail;

        UI.getCurrent().getPage().executeJs("return localStorage.getItem($0);", skillsStorageKey)
                .then(jsonValue -> {
                    String skillsJson = jsonValue == null ? null : jsonValue.asString();

                    if (skillsJson == null || skillsJson.isEmpty()) {
                        if (username != null) {
                            List<String> userTags = tagService.getUserTags(username);
                            StringBuilder skillsBuilder = new StringBuilder("[");
                            boolean first = true;

                            for (String tag : userTags) {
                                if (tag.startsWith("skill:")) {
                                    String skillData = tag.substring(6);
                                    String[] parts = skillData.split(":");
                                    if (parts.length == 2) {
                                        if (!first) skillsBuilder.append(",");
                                        skillsBuilder.append("\"").append(parts[0]).append(":").append(parts[1]).append("\"");
                                        first = false;
                                    }
                                }
                            }
                            skillsBuilder.append("]");
                            skillsJson = skillsBuilder.toString();

                            if (!skillsJson.equals("[]")) {
                                UI.getCurrent().getPage().executeJs(
                                        "localStorage.setItem($0, $1)", skillsStorageKey, skillsJson);
                            }
                        }
                    }

                    processSkillsAndUpdateUI(skillsJson);
                });
    }

    private void processSkillsAndUpdateUI(String skillsJson) {
        if (skillsJson == null || skillsJson.isEmpty() || skillsJson.equals("[]")) {
            compatibilityScore.setText("0%");
            compatibilityProgressBar.getStyle().set("width", "0%");
            jobRecommendationsContainer.removeAll();
            Span noSkillsMessage = new Span("Add skills to see job recommendations");
            noSkillsMessage.getStyle()
                    .set("font-style", "italic")
                    .set("text-align", "center")
                    .set("color", "#718096")
                    .set("padding", "20px");
            jobRecommendationsContainer.add(noSkillsMessage);
            return;
        }

        Map<String, Integer> userSkills = parseSkillsJson(skillsJson);
        if (userSkills.isEmpty()) {
            compatibilityScore.setText("0%");
            compatibilityProgressBar.getStyle().set("width", "0%");
            return;
        }

        JobMatch bestMatch = null;
        List<JobMatch> jobMatches = new ArrayList<>();

        for (Map.Entry<String, List<String>> jobEntry : JOB_SKILLS_MAP.entrySet()) {
            String jobTitle = jobEntry.getKey();
            List<String> requiredSkills = jobEntry.getValue();

            int matchingSkills = 0;
            int totalSkillLevel = 0;

            for (String requiredSkill : requiredSkills) {
                if (userSkills.containsKey(requiredSkill)) {
                    matchingSkills++;
                    totalSkillLevel += userSkills.get(requiredSkill);
                }
            }

            if (matchingSkills > 0) {
                int matchScore = (int) ((matchingSkills / (double) requiredSkills.size()) * 100);
                int avgSkillLevel = totalSkillLevel / matchingSkills;
                int finalMatch = Math.min(100, matchScore + Math.min(avgSkillLevel * 2, 30));

                JobMatch match = new JobMatch(jobTitle, finalMatch, matchingSkills, requiredSkills.size());
                jobMatches.add(match);

                if (bestMatch == null || match.matchPercentage > bestMatch.matchPercentage) {
                    bestMatch = match;
                }
            }
        }

        if (bestMatch != null) {
            int compatibility = bestMatch.matchPercentage;
            compatibilityScore.setText(compatibility + "%");
            compatibilityProgressBar.getStyle().set("width", compatibility + "%");

            jobRecommendationsContainer.removeAll();
            jobMatches.sort((a, b) -> Integer.compare(b.matchPercentage, a.matchPercentage));
            List<JobMatch> top3Jobs = jobMatches.stream().limit(3).collect(Collectors.toList());

            for (JobMatch jobMatch : top3Jobs) {
                addJobRecommendationCard(jobMatch);
            }
        } else {
            compatibilityScore.setText("0%");
            compatibilityProgressBar.getStyle().set("width", "0%");
            jobRecommendationsContainer.removeAll();
            Span noMatchMessage = new Span("No strong matches yet, but keep adding skills!");
            noMatchMessage.getStyle()
                    .set("font-style", "italic")
                    .set("text-align", "center")
                    .set("color", "#718096")
                    .set("padding", "20px");
            jobRecommendationsContainer.add(noMatchMessage);
        }
    }



    private void updateJobRecommendations(Map<String, Integer> userSkills) {
        if (jobRecommendationsContainer == null || userSkills.isEmpty()) {
            if (jobRecommendationsContainer != null) {
                Span noSkillsMessage = new Span("Add skills to see job recommendations");
                noSkillsMessage.getStyle()
                        .set("font-style", "italic")
                        .set("text-align", "center")
                        .set("color", "#718096")
                        .set("padding", "20px");
                jobRecommendationsContainer.add(noSkillsMessage);
            }
            return;
        }

        List<JobMatch> jobMatches = new ArrayList<>();

        for (Map.Entry<String, List<String>> jobEntry : JOB_SKILLS_MAP.entrySet()) {
            String jobTitle = jobEntry.getKey();
            List<String> requiredSkills = jobEntry.getValue();

            int matchingSkills = 0;
            int totalSkillLevel = 0;
            int skillCount = 0;

            for (String requiredSkill : requiredSkills) {
                if (userSkills.containsKey(requiredSkill)) {
                    matchingSkills++;
                    totalSkillLevel += userSkills.get(requiredSkill);
                    skillCount++;
                }
            }

            if (matchingSkills > 0) {
                double skillMatchPercentage = (double) matchingSkills / requiredSkills.size();
                double averageSkillLevel = skillCount > 0 ? (double) totalSkillLevel / skillCount : 0;

                int finalMatch = (int) Math.min((skillMatchPercentage * 60) + (averageSkillLevel * 0.4), 100);

                if (finalMatch >= 30) {
                    jobMatches.add(new JobMatch(jobTitle, finalMatch, matchingSkills, requiredSkills.size()));
                }
            }
        }

        jobMatches.sort((a, b) -> Integer.compare(b.matchPercentage, a.matchPercentage));
        List<JobMatch> top3Jobs = jobMatches.stream().limit(3).collect(Collectors.toList());

        if (top3Jobs.isEmpty()) {
            Span noMatchMessage = new Span("No job matches found. Try adding more skills!");
            noMatchMessage.getStyle()
                    .set("font-style", "italic")
                    .set("text-align", "center")
                    .set("color", "#718096")
                    .set("padding", "20px");
            jobRecommendationsContainer.add(noMatchMessage);
        } else {
            for (JobMatch jobMatch : top3Jobs) {
                addJobRecommendationCard(jobMatch);
            }
        }
    }

    private void updateJobRecommendationsFromLocalStorage(String skillsJson) {
        if (jobRecommendationsContainer == null) return;

        jobRecommendationsContainer.removeAll();

        if (skillsJson == null || skillsJson.isEmpty()) {
            Span noSkillsMessage = new Span("Add skills to see job recommendations");
            noSkillsMessage.getStyle()
                    .set("font-style", "italic")
                    .set("text-align", "center")
                    .set("color", "#718096")
                    .set("padding", "20px");
            jobRecommendationsContainer.add(noSkillsMessage);
            return;
        }

        Map<String, Integer> userSkills = new HashMap<>();
        try {
            String[] skillEntries = skillsJson.replace("[", "").replace("]", "").split(",");
            for (String entry : skillEntries) {
                if (!entry.trim().isEmpty()) {
                    String[] parts = entry.replace("\"", "").split(":");
                    if (parts.length == 2) {
                        try {
                            userSkills.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing skills from localStorage: " + e.getMessage());
            return;
        }

        updateJobRecommendations(userSkills);
    }

    private void addJobRecommendationCard(JobMatch jobMatch) {
        HorizontalLayout jobCard = new HorizontalLayout();
        jobCard.setWidthFull();
        jobCard.setPadding(true);
        jobCard.setSpacing(true);
        jobCard.setAlignItems(FlexComponent.Alignment.CENTER);
        jobCard.getStyle()
                .set("background", "linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%)")
                .set("border-radius", "8px")
                .set("border", "1px solid #e2e8f0")
                .set("margin-bottom", "8px")
                .set("transition", "all 0.2s ease")
                .set("cursor", "pointer");

        jobCard.getElement().addEventListener("mouseover", e -> {
            jobCard.getStyle()
                    .set("background", "linear-gradient(135deg, #730D3F, #a91b5b)")
                    .set("color", "white")
                    .set("transform", "translateY(-2px)")
                    .set("box-shadow", "0 4px 12px rgba(115, 13, 63, 0.3)");
        });

        jobCard.getElement().addEventListener("mouseout", e -> {
            jobCard.getStyle()
                    .set("background", "linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%)")
                    .set("color", "inherit")
                    .set("transform", "translateY(0)")
                    .set("box-shadow", "none");
        });

        VerticalLayout jobInfo = new VerticalLayout();
        jobInfo.setSpacing(false);
        jobInfo.setPadding(false);

        Span jobTitle = new Span(jobMatch.jobTitle);
        jobTitle.getStyle()
                .set("font-weight", "600")
                .set("font-size", "0.95rem");

        Span skillMatch = new Span(jobMatch.matchingSkills + "/" + jobMatch.totalSkills + " skills match");
        skillMatch.getStyle()
                .set("font-size", "0.8rem")
                .set("opacity", "0.8");

        jobInfo.add(jobTitle, skillMatch);

        Span matchPercentage = new Span(jobMatch.matchPercentage + "%");
        matchPercentage.getStyle()
                .set("font-weight", "700")
                .set("font-size", "1.1rem")
                .set("color", "#730D3F");

        jobCard.add(jobInfo, matchPercentage);
        jobCard.setFlexGrow(1, jobInfo);

        jobRecommendationsContainer.add(jobCard);
    }

    private VerticalLayout createCalendarCard() {
        VerticalLayout calendarCard = createCard("Calendar", VaadinIcon.CALENDAR.create());

        Calendar calendar = new Calendar(eventRepository, authContext);
        calendar.setWidthFull();

        calendarCard.add(calendar);
        return calendarCard;
    }


    private VerticalLayout createNotesCard() {
        VerticalLayout notesCard = createCard("Personal Notes", VaadinIcon.NOTEBOOK.create());

        com.vaadin.flow.component.textfield.TextArea noteArea = new com.vaadin.flow.component.textfield.TextArea();
        noteArea.setPlaceholder("Write your job search notes, reminders, or any thoughts here...");
        noteArea.setWidthFull();
        noteArea.setHeight("150px");
        noteArea.getStyle()
                .set("font-family", "'Segoe UI', Tahoma, Geneva, Verdana, sans-serif")
                .set("line-height", "1.5")
                .set("resize", "vertical");

        String userEmail = "guest";
        if (username != null) {
            String email = userService.getUserEmail(username);
            if (email != null && !email.isEmpty()) {
                userEmail = email;
            }
        }

        final String noteStorageKey = "plumjob_user_notes_" + userEmail;

        UI.getCurrent().getPage().executeJs(
                        "return localStorage.getItem($0)", noteStorageKey)
                .then(String.class, savedLocalNote -> {
                    if (savedLocalNote != null && !savedLocalNote.isEmpty()) {
                        noteArea.setValue(savedLocalNote);
                        markNotesAsCompleted();
                    } else if (username != null) {
                        String savedServerNote = tagService.getTagValueForType(username, "note");
                        if (savedServerNote != null) {
                            noteArea.setValue(savedServerNote);
                            UI.getCurrent().getPage().executeJs(
                                    "localStorage.setItem($0, $1)", noteStorageKey, savedServerNote);
                            markNotesAsCompleted();
                        }
                    }
                });

        noteArea.addValueChangeListener(event -> {
            UI.getCurrent().getPage().executeJs(
                    "localStorage.setItem($0, $1)", noteStorageKey, event.getValue());
        });

        Button saveNoteButton = new Button("Save Note", VaadinIcon.CLOUD_UPLOAD.create());
        saveNoteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        saveNoteButton.getStyle().set("margin-top", "12px");

        saveNoteButton.addClickListener(e -> {
            String note = noteArea.getValue();
            UI.getCurrent().getPage().executeJs(
                    "localStorage.setItem($0, $1)", noteStorageKey, note);

            if (username != null) {
                tagService.assignTagToUser(username, "note:" + note);
                Notification.show("Note saved to your account and browser!");
            } else {
                Notification.show("Note saved to browser! Log in to save to your account.");
            }

            markNotesAsCompleted();
        });

        notesCard.add(noteArea, saveNoteButton);
        return notesCard;
    }

    private VerticalLayout createStatsCard() {
        VerticalLayout statsCard = createCard("Profile Statistics", VaadinIcon.BAR_CHART.create());

        profileStatsSpan = new Span();
        updateProfileStats();

        statsCard.add(profileStatsSpan);
        return statsCard;
    }

    private VerticalLayout createSkillsCard() {
        VerticalLayout skillsCard = createCard("Skills & Expertise", VaadinIcon.TOOLS.create());

        String[] availableSkills = {
                "Java", "Python", "JavaScript", "C++", "C#", "PHP", "Ruby", "Go", "Rust", "Kotlin",
                "Spring Boot", "React", "Angular", "Vue.js", "Node.js", "Django", "Flask", "Laravel",
                "SQL", "MySQL", "PostgreSQL", "MongoDB", "Redis", "Oracle", "SQLite",
                "HTML", "CSS", "SASS", "Bootstrap", "Tailwind CSS",
                "Docker", "Kubernetes", "AWS", "Azure", "Google Cloud", "Jenkins", "GitLab CI",
                "Machine Learning", "Data Science", "AI", "TensorFlow", "PyTorch",
                "UI/UX Design", "Figma", "Adobe XD", "Photoshop", "Illustrator",
                "Project Management", "Agile", "Scrum", "Kanban", "Jira", "Confluence"
        };

        skillsList = new VerticalLayout();
        skillsList.setSpacing(true);
        skillsList.setPadding(false);

        loadUserSkills();

        HorizontalLayout addSkillLayout = new HorizontalLayout();
        addSkillLayout.setWidthFull();
        addSkillLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        addSkillLayout.getStyle()
                .set("margin-top", "16px")
                .set("padding", "12px")
                .set("border", "2px dashed #cbd5e0")
                .set("border-radius", "8px")
                .set("cursor", "pointer")
                .set("transition", "all 0.2s ease");

        Icon addIcon = VaadinIcon.PLUS.create();
        addIcon.setSize("16px");
        addIcon.setColor("#730D3F");

        Span addText = new Span("Add new skill");
        addText.getStyle()
                .set("color", "#730D3F")
                .set("font-weight", "500")
                .set("margin-left", "8px");

        addSkillLayout.add(addIcon, addText);

        addSkillLayout.addClickListener(e -> showAddSkillDialog(availableSkills));

        addSkillLayout.getElement().addEventListener("mouseover", e -> {
            addSkillLayout.getStyle()
                    .set("border-color", "#730D3F")
                    .set("background", "rgba(115, 13, 63, 0.05)")
                    .set("box-shadow", "0 0 10px rgba(115, 13, 63, 0.1)");
        });

        addSkillLayout.getElement().addEventListener("mouseout", e -> {
            addSkillLayout.getStyle()
                    .set("border-color", "#cbd5e0")
                    .set("background", "transparent")
                    .set("box-shadow", "none");
        });

        skillsCard.add(skillsList, addSkillLayout);
        return skillsCard;
    }

    private void loadUserSkills() {
        skillsList.removeAll();

        String userEmail = "guest";
        if (username != null) {
            String email = userService.getUserEmail(username);
            if (email != null && !email.isEmpty()) {
                userEmail = email;
            }
        }

        final String skillsStorageKey = "plumjob_user_skills_" + userEmail;

        // Always use localStorage as primary source, then sync with database
        UI.getCurrent().getPage().executeJs(
                        "return localStorage.getItem($0)", skillsStorageKey)
                .then(String.class, savedLocalSkills -> {
                    if (savedLocalSkills != null && !savedLocalSkills.isEmpty() && !savedLocalSkills.equals("[]")) {
                        // Load from localStorage first
                        parseAndDisplaySkills(savedLocalSkills);
                    } else if (username != null) {
                        // If localStorage is empty but user is logged in, load from database
                        List<String> userTags = tagService.getUserTags(username);
                        for (String tag : userTags) {
                            if (tag.startsWith("skill:")) {
                                String skillData = tag.substring(6);
                                String[] parts = skillData.split(":");
                                if (parts.length == 2) {
                                    String skillName = parts[0];
                                    String skillLevel = parts[1];
                                    addSkillToDisplay(skillName, skillLevel);
                                }
                            }
                        }
                        // Save to localStorage after loading from database
                        saveSkillsToLocalStorage();
                    }
                    updateJobCompatibility();
                });
    }

    private void parseAndDisplaySkills(String skillsJson) {
        try {
            // Remove brackets and quotes, then split by comma
            String cleanJson = skillsJson.replace("[", "").replace("]", "").replace("\"", "");
            if (cleanJson.trim().isEmpty()) {
                return;
            }

            String[] skillEntries = cleanJson.split(",");
            for (String entry : skillEntries) {
                entry = entry.trim();
                if (!entry.isEmpty()) {
                    String[] parts = entry.split(":");
                    if (parts.length == 2) {
                        String skillName = parts[0].trim();
                        String skillLevel = parts[1].trim();
                        addSkillToDisplay(skillName, skillLevel);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing skills JSON: " + e.getMessage());
        }
    }


    private void saveSkillsToLocalStorage() {
        String userEmail = "guest";
        if (username != null) {
            String email = userService.getUserEmail(username);
            if (email != null && !email.isEmpty()) {
                userEmail = email;
            }
        }

        final String skillsStorageKey = "plumjob_user_skills_" + userEmail;

        // Build skills JSON from currently displayed skills
        List<String> skillEntries = new ArrayList<>();

        // Get skills from the UI components instead of database
        skillsList.getChildren().forEach(component -> {
            if (component instanceof VerticalLayout) {
                VerticalLayout skillContainer = (VerticalLayout) component;
                // Get the first child which should be the header
                skillContainer.getChildren().findFirst().ifPresent(headerComponent -> {
                    if (headerComponent instanceof HorizontalLayout) {
                        HorizontalLayout header = (HorizontalLayout) headerComponent;
                        // Get skill name from first component
                        header.getChildren().findFirst().ifPresent(nameComponent -> {
                            if (nameComponent instanceof Span) {
                                String skillName = ((Span) nameComponent).getText();
                                // Get skill level from the right side
                                header.getChildren().skip(1).findFirst().ifPresent(rightSideComponent -> {
                                    if (rightSideComponent instanceof HorizontalLayout) {
                                        HorizontalLayout rightSide = (HorizontalLayout) rightSideComponent;
                                        rightSide.getChildren().findFirst().ifPresent(levelComponent -> {
                                            if (levelComponent instanceof Span) {
                                                String levelText = ((Span) levelComponent).getText();
                                                String level = levelText.replace("%", "");
                                                skillEntries.add("\"" + skillName + ":" + level + "\"");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        String skillsJson = "[" + String.join(",", skillEntries) + "]";

        UI.getCurrent().getPage().executeJs(
                "localStorage.setItem($0, $1)", skillsStorageKey, skillsJson);
    }



    private void showAddSkillDialog(String[] availableSkills) {
        Dialog dialog = new Dialog();
        dialog.setWidth("400px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        H3 dialogTitle = new H3("Add New Skill");
        dialogTitle.getStyle().set("margin-top", "0");

        ComboBox<String> skillComboBox = new ComboBox<>("Select Skill");
        skillComboBox.setItems(availableSkills);
        skillComboBox.setWidthFull();
        skillComboBox.setPlaceholder("Choose a skill...");

        com.vaadin.flow.component.textfield.IntegerField levelField = new com.vaadin.flow.component.textfield.IntegerField("Proficiency Level (%)");
        levelField.setWidthFull();
        levelField.setMin(1);
        levelField.setMax(100);
        levelField.setValue(50);
        levelField.setStepButtonsVisible(true);

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.setSpacing(true);

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> dialog.close());

        Button addButton = new Button("Add Skill", VaadinIcon.CHECK.create());
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addButton.addClickListener(e -> {
            String skill = skillComboBox.getValue();
            Integer level = levelField.getValue();

            if (skill != null && level != null) {
                // Check if skill already exists
                if (username != null) {
                    List<String> userTags = tagService.getUserTags(username);
                    boolean skillExists = userTags.stream()
                            .anyMatch(tag -> tag.startsWith("skill:" + skill + ":"));

                    if (skillExists) {
                        Notification.show("Skill already exists! Please remove it first to update.");
                        return;
                    }

                    tagService.assignTagToUser(username, "skill:" + skill + ":" + level);
                }

                addSkillToDisplay(skill, level.toString());

                saveSkillsToLocalStorage();

                updateJobCompatibility();

                if (username != null) {
                    Notification.show("Skill saved to your account and browser!");
                } else {
                    Notification.show("Skill saved to browser! Log in to save to your account.");
                }
                dialog.close();
            } else {
                Notification.show("Please select a skill and set proficiency level");
            }
        });

        buttonLayout.add(cancelButton, addButton);
        dialogLayout.add(dialogTitle, skillComboBox, levelField, buttonLayout);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void addSkillToDisplay(String skillName, String skillLevel) {
        VerticalLayout skillContainer = new VerticalLayout();
        skillContainer.setSpacing(false);
        skillContainer.setPadding(false);

        HorizontalLayout skillHeader = new HorizontalLayout();
        skillHeader.setWidthFull();
        skillHeader.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        skillHeader.setAlignItems(FlexComponent.Alignment.CENTER);

        Span skillNameSpan = new Span(skillName);
        skillNameSpan.getStyle()
                .set("font-weight", "600")
                .set("color", "#2d3748")
                .set("font-size", "0.9rem");

        HorizontalLayout rightSide = new HorizontalLayout();
        rightSide.setAlignItems(FlexComponent.Alignment.CENTER);
        rightSide.setSpacing(true);

        Span skillLevelSpan = new Span(skillLevel + "%");
        skillLevelSpan.getStyle()
                .set("font-size", "0.8rem")
                .set("color", "#730D3F")
                .set("font-weight", "600")
                .set("text-shadow", "0 0 5px rgba(115, 13, 63, 0.2)");

        Button removeButton = new Button(VaadinIcon.TRASH.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR);
        removeButton.getStyle().set("min-width", "unset");
        removeButton.addClickListener(e -> removeSkill(skillName, skillContainer));

        rightSide.add(skillLevelSpan, removeButton);
        skillHeader.add(skillNameSpan, rightSide);

        Div skillProgressContainer = new Div();
        skillProgressContainer.getStyle()
                .set("background", "#f1f5f9")
                .set("height", "6px")
                .set("border-radius", "3px")
                .set("overflow", "hidden")
                .set("margin-top", "4px");

        Div skillProgressBar = new Div();
        skillProgressBar.getStyle()
                .set("background", "linear-gradient(90deg, #730D3F, #a91b5b)")
                .set("height", "100%")
                .set("width", skillLevel + "%")
                .set("border-radius", "3px")
                .set("box-shadow", "0 0 8px rgba(115, 13, 63, 0.3)")
                .set("transition", "all 0.3s ease");

        skillProgressContainer.add(skillProgressBar);
        skillContainer.add(skillHeader, skillProgressContainer);
        skillsList.add(skillContainer);

        updateJobCompatibility();
    }

    private void removeSkill(String skillName, VerticalLayout skillContainer) {
        // Remove from database first if user is logged in
        if (username != null) {
            List<String> userTags = tagService.getUserTags(username);
            for (String tag : userTags) {
                if (tag.startsWith("skill:" + skillName + ":")) {
                    tagService.removeTagFromUser(username, tag);
                    break;
                }
            }
        }

        skillsList.remove(skillContainer);

        saveSkillsToLocalStorage();

        updateJobCompatibility();

        if (username != null) {
            Notification.show("Skill removed from your account and browser!");
        } else {
            Notification.show("Skill removed from browser!");
        }
    }

    private void updatePreferencesLabel(String industry, String experience) {
        String industryText = industry != null ? industry : "Not set";
        String experienceText = experience != null ? experience : "Not set";

        currentPreferencesLabel.getElement().setProperty("innerHTML",
                "<strong>Current preferences:</strong><br>" +
                        "Industry: " + industryText + "<br>" +
                        "Experience: " + experienceText);
    }

    private void updateProfileStats() {
        int completionPercentage = calculateProfileCompletion();
        boolean hasNotesValue = hasNotes();
        boolean hasPreferencesValue = hasPreferences();
        boolean hasProfilePictureValue = hasProfilePicture();

        profileStatsSpan.getElement().setProperty("innerHTML",
                "<div style='display: flex; justify-content: space-between; margin-bottom: 12px;'>" +
                        "<span style='font-weight: 600;'>Profile Completion</span>" +
                        "<span style='color: #730D3F; font-weight: 600; text-shadow: 0 0 5px rgba(115, 13, 63, 0.3);'>" + completionPercentage + "%</span>" +
                        "</div>" +
                        "<div style='background: #e2e8f0; height: 8px; border-radius: 4px; overflow: hidden;'>" +
                        "<div style='background: linear-gradient(90deg, #730D3F, #a91b5b); height: 100%; width: " + completionPercentage + "%; transition: width 0.3s ease; box-shadow: 0 0 10px rgba(115, 13, 63, 0.4);'></div>" +
                        "</div>" +
                        "<div style='margin-top: 16px; font-size: 0.9rem; color: #4a5568;'>" +
                        "<div>• Profile picture: " + (hasProfilePictureValue ? "✅" : "❌") + "</div>" +
                        "<div>• Job preferences: " + (hasPreferencesValue ? "✅" : "❌") + "</div>" +
                        "<div>• Personal notes: " + (hasNotesValue ? "✅" : "❌") + "</div>" +
                        "</div>");
    }

    private int calculateProfileCompletion() {
        int completion = 10;

        if (hasProfilePicture()) completion += 30;
        if (hasPreferences()) completion += 30;
        if (hasNotes()) completion += 30;


        return Math.min(completion, 100);
    }


    private boolean hasProfilePicture() {
        if (username != null) {
            byte[] imageBytes = userService.getProfilePicture(username);
            return imageBytes != null && imageBytes.length > 0;
        }
        return false;
    }

    private boolean hasPreferences() {
        if (username != null) {
            String industry = tagService.getTagValueForType(username, "industry");
            String experience = tagService.getTagValueForType(username, "experience");
            return industry != null && experience != null;
        }
        return false;
    }

    private boolean hasNotes() {
        if (username == null) return false;

        // Check if user has ever added notes (persistent flag)
        String userEmail = userService.getUserEmail(username);
        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = "guest";
        }

        // Check server-side first - if user has notes saved on server, they definitely have notes
        String savedServerNote = tagService.getTagValueForType(username, "note");
        if (savedServerNote != null && !savedServerNote.isEmpty()) {
            return true;
        }

        // If not found on server, check the persistent flag
        return hasNotesFlag;
    }



    private void markNotesAsCompleted() {
        if (username == null) return;

        String userEmail = userService.getUserEmail(username);
        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = "guest";
        }

        final String notesFlagKey = "plumjob_user_has_notes_" + userEmail;

        // Set localStorage flag (persistent)
        UI.getCurrent().getPage().executeJs(
                "localStorage.setItem($0, 'true')", notesFlagKey);

        // Set class variable
        hasNotesFlag = true;

        // Update profile stats immediately
        updateProfileStats();
    }


    // Add this method to load the flag when page loads
    private void loadNotesFlag() {
        if (username == null) {
            hasNotesFlag = false;
            updateProfileStats();
            return;
        }

        String userEmail = userService.getUserEmail(username);
        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = "guest";
        }

        final String notesFlagKey = "plumjob_user_has_notes_" + userEmail;

        // Najpierw sprawdź czy są notatki na serwerze
        String savedServerNote = tagService.getTagValueForType(username, "note");
        if (savedServerNote != null && !savedServerNote.isEmpty()) {
            hasNotesFlag = true;
            // Ustaw również flagę w localStorage
            UI.getCurrent().getPage().executeJs(
                    "localStorage.setItem($0, 'true')", notesFlagKey);
            updateProfileStats();
            return;
        }

        // Jeśli nie ma na serwerze, sprawdź localStorage
        UI.getCurrent().getPage().executeJs(
                        "return localStorage.getItem($0)", notesFlagKey)
                .then(String.class, flagValue -> {
                    hasNotesFlag = "true".equals(flagValue);
                    updateProfileStats();
                });
    }
    private void checkAndSetNotesFlag() {
        if (username == null) return;

        String userEmail = userService.getUserEmail(username);
        if (userEmail == null || userEmail.isEmpty()) {
            userEmail = "guest";
        }

        final String noteStorageKey = "plumjob_user_notes_" + userEmail;

        // Sprawdź localStorage
        UI.getCurrent().getPage().executeJs(
                        "return localStorage.getItem($0)", noteStorageKey)
                .then(String.class, savedLocalNote -> {
                    if (savedLocalNote != null && !savedLocalNote.trim().isEmpty()) {
                        markNotesAsCompleted();
                    } else {
                        // Sprawdź serwer
                        String savedServerNote = tagService.getTagValueForType(username, "note");
                        if (savedServerNote != null && !savedServerNote.trim().isEmpty()) {
                            markNotesAsCompleted();
                        }
                    }
                });
    }

    private Div profileContainer;

    private VerticalLayout setupProfileImage() {
        VerticalLayout profileContainerLayout = new VerticalLayout();
        profileContainerLayout.setSpacing(true);
        profileContainerLayout.setPadding(false);
        profileContainerLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        profileContainer = new Div();
        profileContainer.setWidth("120px");
        profileContainer.setHeight("120px");
        profileContainer.getStyle()
                .set("border-radius", "50%")
                .set("overflow", "hidden")
                .set("cursor", "pointer")
                .set("border", "4px solid white")
                .set("box-shadow", "0 4px 12px rgba(0, 0, 0, 0.15)")
                .set("transition", "all 0.3s ease");

        profileContainer.getElement().addEventListener("mouseover", e ->
                profileContainer.getStyle().set("transform", "scale(1.05)"));
        profileContainer.getElement().addEventListener("mouseout", e ->
                profileContainer.getStyle().set("transform", "scale(1)"));

        loadProfileImage();

        profileContainer.addClickListener(e -> showProfileImageOptionsDialog());

        Button changePhotoButton = new Button("Change Photo", VaadinIcon.CAMERA.create());
        changePhotoButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        changePhotoButton.addClickListener(e -> showProfileImageOptionsDialog());

        profileContainerLayout.add(profileContainer, changePhotoButton);

        if (username != null) {
            String firstName = userService.getUserFirstName(username);
            String lastName = userService.getUserLastName(username);
            String email = userService.getUserEmail(username);

            if (firstName != null && lastName != null) {
                H4 nameLabel = new H4(firstName + " " + lastName);
                nameLabel.getStyle()
                        .set("margin", "12px 0 4px 0")
                        .set("color", "#2d3748")
                        .set("text-align", "center");
                profileContainerLayout.add(nameLabel);
            }

            Span emailLabel = new Span(email != null ? email : "Email not available");
            emailLabel.getStyle()
                    .set("color", "#718096")
                    .set("font-size", "0.9rem")
                    .set("text-align", "center");
            profileContainerLayout.add(emailLabel);
        }

        return profileContainerLayout;
    }

    private void showProfileImageOptionsDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth("320px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        H3 dialogTitle = new H3("Profile Photo Options");
        dialogTitle.getStyle().set("margin-top", "0");

        boolean hasProfilePicture = hasProfilePicture();

        Button uploadButton = new Button(hasProfilePicture ? "Change Photo" : "Upload Photo", VaadinIcon.UPLOAD.create());
        uploadButton.setWidthFull();
        uploadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        uploadButton.addClickListener(e -> {
            dialog.close();
            openUploadDialog();
        });

        dialogLayout.add(dialogTitle, uploadButton);

        if (hasProfilePicture) {
            Button removeButton = new Button("Remove Photo", VaadinIcon.TRASH.create());
            removeButton.setWidthFull();
            removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            removeButton.addClickListener(e -> {
                removeProfilePicture();
                dialog.close();
            });
            dialogLayout.add(removeButton);
        }

        Button cancelButton = new Button("Cancel");
        cancelButton.setWidthFull();
        cancelButton.addClickListener(e -> dialog.close());

        dialogLayout.add(cancelButton);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void openUploadDialog() {
        Dialog uploadDialog = new Dialog();
        uploadDialog.setWidth("400px");
        uploadDialog.setCloseOnEsc(true);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setSpacing(true);
        dialogLayout.setPadding(true);

        H3 title = new H3("Upload Profile Photo");
        title.getStyle().set("margin-top", "0");

        FileBuffer fileBuffer = new FileBuffer();
        Upload upload = new Upload(fileBuffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFiles(1);
        upload.setMaxFileSize(5 * 1024 * 1024); // 5MB
        upload.setDropAllowed(true);
        upload.setDropLabel(new Span("Drag and drop your photo here or click to browse"));
        upload.setWidth("100%");

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidthFull();
        buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> uploadDialog.close());
        buttons.add(cancelButton);
        upload.addSucceededListener(event -> {
            if (username != null) {
                try {
                    byte[] imageBytes = fileBuffer.getInputStream().readAllBytes();
                    String mimeType = event.getMIMEType();
                    userService.updateProfilePicture(username, imageBytes, mimeType);

                    loadProfileImage();
                    updateProfileStats();
                    Notification.show("Profile picture uploaded successfully!");
                    uploadDialog.close();
                } catch (IOException e) {
                    Notification.show("Failed to save profile picture: " + e.getMessage());
                }
            }
        });

        dialogLayout.add(title, upload, buttons);
        uploadDialog.add(dialogLayout);
        uploadDialog.open();
    }

    private void removeProfilePicture() {
        if (username != null) {
            userService.updateProfilePicture(username, null, null);
            loadProfileImage();
            updateProfileStats();
            Notification.show("Profile picture removed!");
        }
    }

    private void loadProfileImage() {
        if (profileContainer == null) return;

        profileContainer.removeAll();

        if (username != null) {
            byte[] imageBytes = userService.getProfilePicture(username);
            String contentType = userService.getProfilePictureType(username);

            if (imageBytes != null && imageBytes.length > 0) {
                StreamResource resource = new StreamResource("profile-picture", () ->
                        new ByteArrayInputStream(imageBytes)
                );
                resource.setContentType(contentType != null ? contentType : "image/jpeg");

                Image image = new Image(resource, "Profile picture");
                image.setWidth("100%");
                image.setHeight("100%");
                image.getStyle()
                        .set("object-fit", "cover")
                        .set("border-radius", "50%");

                profileContainer.add(image);
            } else {
                createCameraIcon();
            }
        } else {
            createCameraIcon();
        }
    }

    private void createCameraIcon() {
        Div cameraBackground = new Div();
        cameraBackground.setWidth("100%");
        cameraBackground.setHeight("100%");
        cameraBackground.getStyle()
                .set("background", "#f0f0f0")
                .set("border-radius", "50%")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("border", "1px solid #e0e0e0");

        Icon cameraIcon = VaadinIcon.CAMERA.create();
        cameraIcon.setSize("16px");
        cameraIcon.setColor("#757575");

        cameraBackground.add(cameraIcon);
        profileContainer.add(cameraBackground);
    }

    private int calculateCompatibilityFromLocalStorage(String skillsJson) {
        if (skillsJson == null || skillsJson.isEmpty()) {
            return 0;
        }

        try {
            String[] skillEntries = skillsJson.replace("[", "").replace("]", "").split(",");
            int skillCount = 0;
            int totalSkillLevel = 0;

            for (String entry : skillEntries) {
                if (!entry.trim().isEmpty()) {
                    String[] parts = entry.replace("\"", "").split(":");
                    if (parts.length == 2) {
                        try {
                            int level = Integer.parseInt(parts[1].trim());
                            totalSkillLevel += level;
                            skillCount++;
                        } catch (NumberFormatException e) {
                        }
                    }
                }
            }

            int compatibility = 0;
            if (skillCount > 0) {
                int averageSkillLevel = totalSkillLevel / skillCount;
                compatibility = Math.min(averageSkillLevel, 100);

                int skillBonus = Math.min(skillCount * 2, 20);
                compatibility = Math.min(compatibility + skillBonus, 100);
            }

            return compatibility;
        } catch (Exception e) {
            System.err.println("Error calculating compatibility from localStorage: " + e.getMessage());
            return 0;
        }
    }

    private static class JobMatch {
        String jobTitle;
        int matchPercentage;
        int matchingSkills;
        int totalSkills;

        JobMatch(String jobTitle, int matchPercentage, int matchingSkills, int totalSkills) {
            this.jobTitle = jobTitle;
            this.matchPercentage = matchPercentage;
            this.matchingSkills = matchingSkills;
            this.totalSkills = totalSkills;
        }

        //StickyAdBar adBar = new StickyAdBar(tagService, authContext, userService);
        //add(adBar);

    }
}

