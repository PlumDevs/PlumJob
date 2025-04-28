package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Logout")
@Route(value="logout")
public class LogoutView extends VerticalLayout {
    LogoutView() {
        System.out.println("Logout");
        add(new Paragraph("Thank you for using Plum Job!"));
    }
}
