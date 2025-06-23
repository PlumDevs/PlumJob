package com.plumdevs.plumjob.UI.layout;

import com.plumdevs.plumjob.UI.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

@CssImport("./themes/plum-theme-light/styles.css")
@AnonymousAllowed
@Layout
public class MainLayout extends AppLayout {

    public MainLayout(HttpServletRequest request, HttpServletResponse response, AuthenticationContext authContext) {

        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        Image logo = new Image("img/logo.png", "Plum Job logo");
        logo.setWidth(160, Unit.PIXELS);
        logo.setHeight(60, Unit.PIXELS);
        header.add(logo);

        SideNav nav = new SideNav();
        DrawerToggle toggle = new DrawerToggle();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, header);

        SideNavItem activeLink = new SideNavItem("Active recruitments",
                ActiveView.class, VaadinIcon.BULLETS.create());
        nav.addItem(activeLink);

        SideNavItem archiveLink = new SideNavItem("Archive",
                ArchiveView.class, VaadinIcon.ARCHIVE.create());
        nav.addItem(archiveLink);

        SideNavItem CVBuilderLink = new SideNavItem("CV Builder",
                CVBuilderView.class, VaadinIcon.EDIT.create());
        nav.addItem(CVBuilderLink);

        SideNavItem articlesLink = new SideNavItem("Tips",
                ArticlesView.class, VaadinIcon.BOOK.create());
        nav.addItem(articlesLink);

        SideNavItem aboutLink = new SideNavItem("About",
                AboutView.class, VaadinIcon.QUESTION_CIRCLE.create());
        nav.addItem(aboutLink);


        SideNavItem profileLink = new SideNavItem("Your profile",
                UserProfileView.class, VaadinIcon.USER.create());
        nav.addItem(profileLink);


        setPrimarySection(Section.DRAWER);

        HorizontalLayout footer = new HorizontalLayout();

        footer.setWidthFull();
        footer.setPadding(true);
        footer.setAlignItems(FlexComponent.Alignment.CENTER);
        footer.add(new Paragraph("© Plum Job"));
        footer.addClassName("footer");
        addToDrawer(footer);

        Button authButton = getButton(request, response, authContext);
        header.add(authButton);

    }

    private Button getButton(HttpServletRequest request, HttpServletResponse response, AuthenticationContext authContext) {

        String username = authContext.getPrincipalName().orElse(null);
        Button authButton;


        if (username == null) {

            authButton = new Button("Login");
            authButton.addClickListener(e -> {
                new SecurityContextLogoutHandler().logout(request, response, null);
                getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
            });
        }

        else {

            authButton = new Button("Logout");
            authButton.addClickListener(e -> {
                new SecurityContextLogoutHandler().logout(request, response, null);
                getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
            });
        }

        authButton.addClassName("transparent-button");
        return authButton;
    }
}