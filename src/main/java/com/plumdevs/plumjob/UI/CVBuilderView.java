package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Custom CV Builder")
@Route(value="CVBuilder", layout = MainLayout.class)
public class CVBuilderView extends VerticalLayout {
    CVBuilderView() {
        System.out.println("CV Builder");
        add(new Paragraph("Create your own resume"));
    }
}
