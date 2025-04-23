package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Tips from top recruiters")
@Route(value="articles", layout = MainLayout.class)
public class ArticlesView extends VerticalLayout {
    ArticlesView() {
        System.out.println("Articles/Tips");
        add(new Paragraph("Read articles by recruiters from top companies"));
    }
}
