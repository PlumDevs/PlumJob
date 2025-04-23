package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Archive")
@Route(value="archive", layout = MainLayout.class)
public class ArchiveView extends VerticalLayout {

    ArchiveView() {
        System.out.println("Archive");
        add(new Paragraph("This is archive recruitments page"));
    }
}
