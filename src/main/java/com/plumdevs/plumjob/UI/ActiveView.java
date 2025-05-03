package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;

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

        grid.addColumn(RecruitmentItem::getPositon).setHeader("Position");
        grid.addColumn(RecruitmentItem::getCompany).setHeader("Company");
        grid.addColumn(RecruitmentItem::getStatus).setHeader("Status");

        //grid.addColumn(RecruitmentItem::).setHeader("Date started");
        //grid.addColumn(RecruitmentItem::).setHeader("Last updated");
        //grid.addColumn(RecruitmentItem::).setHeader("Link");

        //FOR FRONTEND TESTING PURPOSES ONLY
        List<RecruitmentItem> items = new ArrayList<>();
        items.add(new RecruitmentItem("Software Engineer", "Google", "Interview scheduled"));
        items.add(new RecruitmentItem("Site Reliability Engineer", "IBM", "After OA"));
        items.add(new RecruitmentItem("Software Engineer", "Relativity", "Applied"));
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
