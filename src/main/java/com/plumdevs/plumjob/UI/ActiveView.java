package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableFunction;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@PageTitle("Plum Job - Active recruitments")
@Route(value="active", layout = MainLayout.class)
@RouteAlias(value="/")
public class ActiveView extends VerticalLayout {
    ActiveView() {
        System.out.println("Active recruitments");

        H2 title = new H2("Active recruitments");

        Button addNewPosition = new Button("Add new position");
        addNewPosition.addClassName("plum-button");
        addNewPosition.addClickListener(buttonClickEvent -> getUI().ifPresent(ui ->
                ui.navigate("addPosition")));

        HorizontalLayout top = new HorizontalLayout(title, addNewPosition);
        top.setWidthFull();

        Grid<RecruitmentItem> grid = new Grid(RecruitmentItem.class, false);
        grid.setWidthFull();

        grid.addColumn(RecruitmentItem::getPositon).setHeader("Position").setSortable(true);
        grid.addColumn(RecruitmentItem::getCompany).setHeader("Company").setSortable(true);
        grid.addColumn(RecruitmentItem::getStatus).setHeader("Status").setSortable(true);


        grid.setItemDetailsRenderer(new ComponentRenderer<RecruitmentItemDetails, RecruitmentItem>(RecruitmentItemDetails::new, RecruitmentItemDetails::setItem));
        //grid.setDetailsVisibleOnClick(false); //false for the extra button
        grid.setDetailsVisibleOnClick(true);

        /*
        grid.addColumn(
                new ComponentRenderer<Button, RecruitmentItem>((SerializableFunction<RecruitmentItem, Button>) item -> {
                    Button toggle = new Button("...");
                    toggle.addClickListener(event -> {
                        grid.setDetailsVisible(item, !grid.isDetailsVisible(item));
                    });
                    toggle.setClassName("plum-text");
                    return toggle;
                })
        ).setWidth("1rem");
        //Without that, default: details appear on click on the row
         */


        //grid.addColumn(RecruitmentItem::).setHeader("Date started");
        //grid.addColumn(RecruitmentItem::).setHeader("Last updated");
        //grid.addColumn(RecruitmentItem::).setHeader("Link");
        // TODO: Item filters, search bar and item details view

        //FOR FRONTEND TESTING PURPOSES ONLY
        List<RecruitmentItem> items = new ArrayList<>();
        items.add(new RecruitmentItem("Software Engineer", "Google", "Interview scheduled", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.", LocalDate.of(2025, 1, 3)));
        items.add(new RecruitmentItem("Site Reliability Engineer", "IBM", "After OA", "Great job", LocalDate.of(2024, 12, 3)));
        items.add(new RecruitmentItem("Software Engineer", "Relativity", "Applied", "Please hire me", LocalDate.of(2025, 3, 4)));
        items.add(new RecruitmentItem("Backend Developer", "Amazon", "Interview scheduled"));
        items.add(new RecruitmentItem("Frontend Engineer", "Meta", "Applied"));
        items.add(new RecruitmentItem("Data Scientist", "Netflix", "OA received"));
        items.add(new RecruitmentItem("DevOps Engineer", "Microsoft", "After OA"));
        items.add(new RecruitmentItem("Machine Learning Engineer", "NVIDIA", "Applied"));
        items.add(new RecruitmentItem("Full Stack Developer", "Adobe", "Interview scheduled"));
        items.add(new RecruitmentItem("Cloud Engineer", "Oracle", "Interview scheduled"));
        items.add(new RecruitmentItem("iOS Developer", "Airbnb", "Phone screen done"));
        items.add(new RecruitmentItem("Android Developer", "Twitter", "Applied"));
        items.add(new RecruitmentItem("Data Analyst", "LinkedIn", "OA received"));
        items.add(new RecruitmentItem("Security Engineer", "Palantir", "Applied"));
        items.add(new RecruitmentItem("Product Manager", "Salesforce", "Applied"));
        items.add(new RecruitmentItem("UX Designer", "Spotify", "Applied"));
        items.add(new RecruitmentItem("AI Researcher", "OpenAI", "Interview scheduled"));
        items.add(new RecruitmentItem("Game Developer", "Riot Games", "Applied"));

        grid.setItems(items);

        add(top, grid);
    }

}

