package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Plum Job - About")
@AnonymousAllowed
@Route(value="about", layout = MainLayout.class) //the view will appear on localhost:8080/about, and use layout from MainLayout besides about own unique components
public class AboutView extends VerticalLayout {

    public int AVATAR_SIZE = 70;
    public int BOX_SIZE = 120;

    AboutView() {
        System.out.println("About");
        add(new H2("About"));

        //TODO: Add real text about the project
        add(new Paragraph("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."));

        add(new H2("Our Team"));

        HorizontalLayout team = new HorizontalLayout();

        team.add(createAvatarBox("Kinga Ż.", "Project Manager", "Software Engineer"));
        team.add(createAvatarBox("Dominik Sz.", "Software Tester", ""));
        team.add(createAvatarBox("Martyna C.", "Software Engineer", ""));
        team.add(createAvatarBox("Wojciech P.", "Database Engineer", ""));
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
