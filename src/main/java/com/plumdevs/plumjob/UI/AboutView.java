package com.plumdevs.plumjob.UI;

import ch.qos.logback.core.pattern.SpacePadder;
import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.IOException;

@PageTitle("Plum Job - About")
@AnonymousAllowed
@Route(value="about", layout = MainLayout.class) //the view will appear on localhost:8080/about, and use layout from MainLayout besides about own unique components
public class AboutView extends VerticalLayout {

    public int AVATAR_SIZE = 70;
    public int BOX_SIZE = 150;

    AboutView() throws IOException {
        System.out.println("About");
        add(new H2("Have you ever felt like looking for a job is more stressful than the actual work?"));

        add(new Paragraph(" Plum Job is a web platform designed to make the job-hunting experience for young seekers more organized and less frustrating. To support this mission, we’ve designed features like: application archive with real-time updatable statuses, CV builder that transforms user data into a clean, ATS-friendly PDF using one of our beautiful templates, blog filled with tips and insights from seasoned recruiters, and visual graph summarizing each user’s job-seeking journey."));

        Paragraph motto = new Paragraph("According to multiple dictionaries, plum job means a highly desirable job or position, often characterised by excellent pay, benefits, working conditions, and opportunities for advancement. Which is exactly the type of job we strive for our users to find in these uncertain times.");
        motto.addClassName("italics");

        add(motto);

        add(new H2("Our Team"));

        HorizontalLayout team = new HorizontalLayout();

        team.add(createAvatarBox("Kinga Ż.", "Project Manager", "Software Engineer"));
        team.add(createAvatarBox("Dominik Sz.", "Software Tester", ""));
        team.add(createAvatarBox("Martyna C.", "Software Engineer", ""));
        team.add(createAvatarBox("Wojciech P.", "Database Engineer", "Documentation Lead"));
        team.add(createAvatarBox("Piotr Sz.", "Software Engineer", ""));

        add(team);

    }


    private VerticalLayout createAvatarBox(String name, String roleOne, String roleTwo) {
        Avatar avatar = new Avatar(name);

        avatar.setHeight(AVATAR_SIZE, Unit.PIXELS);
        avatar.setWidth(AVATAR_SIZE, Unit.PIXELS);

        Span nameSpan = new Span(name);
        Span roleOneSpan = new Span(roleOne);

        nameSpan.getStyle().set("font-weight", "bold");
        nameSpan.addClassName("plum-text");
        roleOneSpan.getStyle().set("color", "gray");
        roleOneSpan.getStyle().set("font-size", "smaller");

        VerticalLayout block = new VerticalLayout(avatar, nameSpan, roleOneSpan);

        if (!roleTwo.equals("")) {
            Span roleTwoSpan = new Span(roleTwo);
            roleTwoSpan.getStyle().set("color", "gray");
            roleTwoSpan.getStyle().set("font-size", "smaller");
            block.add(roleTwoSpan);
        }

        block.setAlignItems(FlexComponent.Alignment.CENTER);
        block.setSpacing(false);
        block.setPadding(false);
        block.setHeight(BOX_SIZE, Unit.PIXELS);
        block.setWidth(BOX_SIZE, Unit.PIXELS);
        return block;
    }
}
