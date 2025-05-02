package com.plumdevs.plumjob.UI;

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
import org.apache.catalina.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@AnonymousAllowed
@Route(value = "register", autoLayout = false)
public class RegisterView extends VerticalLayout {

    private final JdbcUserDetailsManager userDetailsManager;

    public RegisterView(JdbcUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;

        Image logo = new Image("https://raw.githubusercontent.com/PlumDevs/PlumJob/refs/heads/master/src/main/resources/META-INF/resources/img/logo.png", "Plum");
        logo.setWidth(180, Unit.PIXELS);
        logo.setHeight(70, Unit.PIXELS);
        add(logo);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        TextField firstNameField = new TextField("First Name");
        TextField lastNameField = new TextField("Last Name");
        EmailField emailAddressField = new EmailField("E-mail");
        TextField usernameField = new TextField("Username");
        PasswordField passwordField = new PasswordField("Password");
        Button registerButton = new Button("Register");

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

            if (emailExists(email)) {
                Notification.show("Email already in use", 1000, Notification.Position.MIDDLE);
                return;
            }


            UserDetails user = User.withUsername(username)
                    //.password(passwordEncoder.encode(password))
                    .password("{noop}" + password)
                    .roles("USER")
                    .build();
            userDetailsManager.createUser(user);

            //TODO: ADD ALSO email and full name TO USER INFO MANUALLY, more organized using repo etc.
            addUserInfo(username, firstName, lastName, email);

            //TODO: HERE AND IN LOGIN, ADD PASSWORD ENCODER

            Notification.show("Registration successful!", 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate("login"));
        });


        H2 registerTitle = new H2("Register");
        add(registerTitle);

        firstNameField.setWidth("200px");
        lastNameField.setWidth("200px");

        HorizontalLayout nameLayout = new HorizontalLayout(firstNameField, lastNameField);
        nameLayout.setSpacing("15px");

        add(nameLayout);

        usernameField.setWidth("200px");
        passwordField.setWidth("200px");

        emailAddressField.setWidth("415px");
        add(emailAddressField);

        HorizontalLayout passwordLayout = new HorizontalLayout(usernameField, passwordField);
        passwordLayout.setSpacing("15px");

        add(passwordLayout);

        Button loginButton = new Button("Back to login");
        loginButton.addClassName("plum-text");

        RouterLink routerLink = new RouterLink("", LoginView.class);
        routerLink.getElement().appendChild(loginButton.getElement());

        HorizontalLayout buttonLayout = new HorizontalLayout(routerLink, registerButton);
        buttonLayout.setSpacing(true);

        add(buttonLayout);

    }


    @Autowired
    private DataSource dataSource; //from app.properties

    public boolean emailExists(String email) { //TODO: move to other place to structure metter
        String sql = "SELECT 1 FROM UserInfo WHERE user_email = " + email;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    //TODO: Move that somewhere else too
    public boolean addUserInfo(String username, String firstName, String lastName, String email) {
        String sql = "INSERT INTO UserInfo VALUES('" + username + "', '" + firstName + "', '" + lastName + "', '" + email + "', CURDATE(), TRUE);"; //maybe make that into a function in db?
        System.out.println(sql);
        try (Connection conn = dataSource.getConnection();
             Statement statement = conn.createStatement()) {

            statement.executeUpdate(sql);

        } catch (Exception e) {
            //e.printStackTrace(); sometimes shows syntax errors that actually are not syntax errors
            return false;
        }

        return true;
    }
}
