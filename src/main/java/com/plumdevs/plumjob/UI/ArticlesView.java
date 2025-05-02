package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Plum Job - Articles")
@Route(value="articles", layout = MainLayout.class)
public class ArticlesView extends VerticalLayout {
    ArticlesView() {
        System.out.println("Articles/Tips");
        add(new H2("Read articles by recruiters from top companies"));
    }
}
