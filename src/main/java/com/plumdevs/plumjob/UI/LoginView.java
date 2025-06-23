package com.plumdevs.plumjob.UI;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route(value = "login", autoLayout = false)
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView() {

        Image logo = new Image("https://raw.githubusercontent.com/PlumDevs/PlumJob/refs/heads/master/src/main/resources/META-INF/resources/img/logo.png", "Plum");
        logo.setWidth(180, Unit.PIXELS);
        logo.setHeight(70, Unit.PIXELS);
        add(logo);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);


        LoginForm login = new LoginForm();
        login.setAction("login");
        login.setForgotPasswordButtonVisible(false);
        add(login);

        Button registerButton = new Button("Register");
        registerButton.addClassName("plum-text");

        RouterLink routerLink = new RouterLink("", RegisterView.class);
        routerLink.getElement().appendChild(registerButton.getElement());

        Button aboutButton = new Button("About");
        aboutButton.addClassName("plum-text-white");

        RouterLink aboutLink = new RouterLink("", AboutView.class);
        aboutLink.getElement().appendChild(aboutButton.getElement());

        Button forgotPasswordButton = new Button("Forgot Password?");
        forgotPasswordButton.addClassName("plum-text");
        forgotPasswordButton.addClickListener(event -> {
            Notification.show("Please contact support to reset your password at plumjob.contant@gmail.com", 3000, Notification.Position.MIDDLE);
        });

        HorizontalLayout bottomLinks = new HorizontalLayout(routerLink, forgotPasswordButton);
        add(bottomLinks);
        add(aboutLink);

    }
}