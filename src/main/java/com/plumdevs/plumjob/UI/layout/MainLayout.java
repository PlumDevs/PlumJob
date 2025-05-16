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



    public MainLayout(HttpServletRequest request, HttpServletResponse response) {

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
                ActiveView.class);
        nav.addItem(activeLink);

        SideNavItem archiveLink = new SideNavItem("Archive",
                ArchiveView.class);
        nav.addItem(archiveLink);

        SideNavItem CVBuilderLink = new SideNavItem("CV Builder",
                CVBuilderView.class);
        nav.addItem(CVBuilderLink);

        SideNavItem articlesLink = new SideNavItem("Tips",
                ArticlesView.class);
        nav.addItem(articlesLink);

        SideNavItem profileLink = new SideNavItem("Your Profile",
                UserProfileView.class);
        nav.addItem(profileLink);

        SideNavItem aboutLink = new SideNavItem("About",
                AboutView.class);
        nav.addItem(aboutLink);

        /*
        SideNavItem logoutLink = new SideNavItem("Logout",
                LogoutView.class);
        nav.addItem(logoutLink);

         */

        // #### end navigation links ####

        setPrimarySection(Section.DRAWER);

        //TODO: pageTitle from @PageTitle

        //TODO: (Stage 2 task) Place adds in the MainLayout (here) and in separate file contain logic to make them work


        //TODO: This is how you do a todo

        HorizontalLayout footer = new HorizontalLayout();

        footer.setWidthFull();
        footer.setPadding(true);
        footer.setAlignItems(FlexComponent.Alignment.CENTER);
        footer.add(new Paragraph("© Plum Job"));
        footer.addClassName("footer");
        addToDrawer(footer);

        Button logoutButton = new Button("Logout", e -> {
            new SecurityContextLogoutHandler().logout(request, response, null);
            getUI().ifPresent(ui -> ui.getPage().setLocation("/logout"));
        });

        logoutButton.addClassName("transparent-button");
        header.add(logoutButton);
    }

}