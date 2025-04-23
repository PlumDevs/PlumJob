package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value="home", layout = MainLayout.class)
public class HomeView extends VerticalLayout {

    HomeView() {
        System.out.println("Home");
        add(new H1("Welcome to Plum Job"));
        add(new Paragraph("This is your homepage"));
    }
}
