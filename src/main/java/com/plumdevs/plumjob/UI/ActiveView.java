package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Active recruitments")
@Route(value="active", layout = MainLayout.class)
public class ActiveView extends VerticalLayout {
    ActiveView() {
        System.out.println("Active recruitments");
        add(new Paragraph("This is active recruitments page"));
    }
}
