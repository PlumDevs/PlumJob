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

        //Header
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        Image logo = new Image("img/logo.png", "Plum Job logo");
        //Image logo = new Image("https://private-user-images.githubusercontent.com/147309514/435481240-b7c77535-383d-4c42-9346-6ef7bcac2a55.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3NDcyNDg2MzMsIm5iZiI6MTc0NzI0ODMzMywicGF0aCI6Ii8xNDczMDk1MTQvNDM1NDgxMjQwLWI3Yzc3NTM1LTM4M2QtNGM0Mi05MzQ2LTZlZjdiY2FjMmE1NS5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjUwNTE0JTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI1MDUxNFQxODQ1MzNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1hOTI0YjI4Njg1YmM2NjMzMjY1YTdlNmQyMDI1OTk5NDg5YzdhZDhjYTI1MGQxNWE2MDUwODY3ZmVlMzBmNmZlJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCJ9.40rbexNwjNjTjvVijHu_IDdf6BPLqmttYoh_ZPVlynk", "Plum Job logo");
        logo.setWidth(160, Unit.PIXELS);
        logo.setHeight(60, Unit.PIXELS);
        header.add(logo);
        //Navigation on the side
        SideNav nav = new SideNav();
        DrawerToggle toggle = new DrawerToggle();

        Scroller scroller = new Scroller(nav);
        scroller.setClassName(LumoUtility.Padding.SMALL);

        addToDrawer(scroller);
        addToNavbar(toggle, header);

        // #### navigation items ####

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


        // #### end navigation links ####

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

        String username = authContext.getPrincipalName().orElse(null); // TODO: Consider moving this logic to a UserService
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