package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.plumdevs.plumjob.service.UserService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.button.Button;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

@AnonymousAllowed
@Route(value = "register", autoLayout = false)
public class RegisterView extends VerticalLayout {
    private final JdbcUserDetailsManager userDetailsManager;
    private final UserService userService;

    public RegisterView(JdbcUserDetailsManager userDetailsManager, UserService userService) {
        this.userDetailsManager = userDetailsManager;
        this.userService = userService;

        Image logo = new Image("https://raw.githubusercontent.com/PlumDevs/PlumJob/refs/heads/master/src/main/resources/META-INF/resources/img/logo.png", "Plum");
        logo.setWidth(180, Unit.PIXELS);
        logo.setHeight(70, Unit.PIXELS);
        //logo.setId("logo");
        add(logo);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        TextField firstNameField = new TextField("First Name");
        //firstNameField.setId("firstName");
        TextField lastNameField = new TextField("Last Name");
        //lastNameField.setId("lastName");
        EmailField emailAddressField = new EmailField("E-mail");
        //emailAddressField.setId("email");
        TextField usernameField = new TextField("Username");
        //usernameField.setId("username");
        PasswordField passwordField = new PasswordField("Password");
        //passwordField.setId("password");
        Button registerButton = new Button("Register");
        //registerButton.setId("register-btn");
        //addClassName("register-view");
        registerButton.addClassName("plum-button");

        registerButton.addClickListener(e -> {
            String firstName = firstNameField.getValue().trim(); //to save in UserInfo
            String lastName = lastNameField.getValue().trim();
            String email = emailAddressField.getValue().trim();

            String username = usernameField.getValue().trim(); //to save automatically in user by UserDetailsManager
            String password = passwordField.getValue().trim();

            if (username.isEmpty() || emailAddressField.isEmpty() || password.isEmpty() || firstNameField.isEmpty() || lastNameField.isEmpty()) {
                Notification.show("Fields cannot be empty", 1000, Notification.Position.MIDDLE);
                return;
            }

            if (userDetailsManager.userExists(username)) {
                Notification.show("Username already in use", 1000, Notification.Position.MIDDLE);
                return;
            }

            if (userService.emailExists(email).equals(1)) {
                Notification.show("Email already in use", 1000, Notification.Position.MIDDLE);
                return;
            }


            UserDetails user = User.withUsername(username)
                    //.password(passwordEncoder.encode(password))
                    .password("{noop}" + password)
                    .roles("USER")
                    .build();
            userDetailsManager.createUser(user);

            userService.addUserInfo(username, firstName, lastName, email);

            //TODO: HERE AND IN LOGIN, ADD PASSWORD ENCODER

            Notification.show("Registration successful!", 3000, Notification.Position.MIDDLE);
            //Notification.show("Registration successful!", 3000, Notification.Position.MIDDLE).getElement().setAttribute("id", "success-msg");
            getUI().ifPresent(ui -> ui.navigate("login"));
        });


        H2 registerTitle = new H2("Register");
        //registerTitle.setId("register-title");
        add(registerTitle);

        firstNameField.setWidth("200px");
        lastNameField.setWidth("200px");

        HorizontalLayout nameLayout = new HorizontalLayout(firstNameField, lastNameField);
        nameLayout.setSpacing("15px");
        //nameLayout.setId("name-layout");

        add(nameLayout);

        usernameField.setWidth("200px");
        passwordField.setWidth("200px");

        emailAddressField.setWidth("415px");
        add(emailAddressField);

        HorizontalLayout passwordLayout = new HorizontalLayout(usernameField, passwordField);
        passwordLayout.setSpacing("15px");
        //passwordLayout.setId("password-layout");

        add(passwordLayout);

        Button loginButton = new Button("Back to login");
        //loginButton.setId("login-btn");
        loginButton.addClassName("plum-text");

        RouterLink routerLink = new RouterLink("", LoginView.class);
        routerLink.getElement().appendChild(loginButton.getElement());

        HorizontalLayout buttonLayout = new HorizontalLayout(routerLink, registerButton);
        buttonLayout.setSpacing(true);
        //buttonLayout.setId("button-layout");

        add(buttonLayout);

    }

}