package com.plumdevs.plumjob.UI.layout;

import com.plumdevs.plumjob.UI.*;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

@CssImport("./themes/plum-theme-light/styles.css")
@Layout
public class MainLayout extends AppLayout {

    public MainLayout() {

        //Header
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setPadding(true);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        Image logo = new Image("img/logo.png", "Plum Job logo");
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
        SideNavItem homeLink = new SideNavItem("Home",
                HomeView.class);
        nav.addItem(homeLink);

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

        SideNavItem aboutLink = new SideNavItem("About",
                AboutView.class);
        nav.addItem(aboutLink);

        SideNavItem logoutLink = new SideNavItem("Logout",
                LogoutView.class);
        nav.addItem(logoutLink);

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

    }
}