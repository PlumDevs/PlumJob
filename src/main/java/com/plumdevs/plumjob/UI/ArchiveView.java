package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll

@PageTitle("Plum Job - Archive")
@Route(value="archive", layout = MainLayout.class)
public class ArchiveView extends VerticalLayout {

    ArchiveView() {
        System.out.println("Archive");
        add(new Paragraph("This is archive recruitments page"));
    }
}
