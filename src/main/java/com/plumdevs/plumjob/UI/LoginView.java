package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login")
@Route(value="login")
public class LoginView extends VerticalLayout {
    LoginView() {
        System.out.println("Login");
        add(new Paragraph("Log in to Plum Job"));
    }
}
