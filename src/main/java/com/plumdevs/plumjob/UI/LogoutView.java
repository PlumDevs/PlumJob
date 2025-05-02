package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@PageTitle("Logout")
@Route(value="logout")
public class LogoutView extends VerticalLayout {
    LogoutView() {
        System.out.println("Logout");
        Image logo = new Image("https://raw.githubusercontent.com/PlumDevs/PlumJob/refs/heads/master/src/main/resources/META-INF/resources/img/logo.png", "Plum");
        logo.setWidth(180, Unit.PIXELS);
        logo.setHeight(70, Unit.PIXELS);
        add(logo);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(new H2("Thank you for using Plum Job!"));

        Button loginButton = new Button("Back to login");
        loginButton.addClassName("plum-button");

        RouterLink routerLink = new RouterLink("", LoginView.class);
        routerLink.getElement().appendChild(loginButton.getElement());
        add(routerLink);
    }
}
