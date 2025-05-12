package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.component.PositionsGrid;
import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.plumdevs.plumjob.repository.PositionsRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
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
    ActiveView(UserInfoRepository userInfoRepository, PositionsRepository positionsRepository) {
        System.out.println("Active recruitments");

        H2 title = new H2("Active recruitments");

        Button addNewPosition = new Button("Add new position");
        addNewPosition.addClassName("plum-button");
        addNewPosition.addClickListener(buttonClickEvent -> getUI().ifPresent(ui ->
                ui.navigate("addPosition")));

        PositionsGrid grid = new PositionsGrid(userInfoRepository, positionsRepository, true);

        ComboBox<String> stageFilter = new ComboBox<>();

        stageFilter.setItems("All",
                "to apply",
                "applied",
                "OA in progress",
                "after OA",
                "interview scheduled",
                "after interview",
                "received offer",
                "rejected",
                "ghosted",
                "accepted the offer");
        stageFilter.setValue("All"); // Default

        stageFilter.addValueChangeListener(event -> {
            String selected = event.getValue();
            grid.filterByStage(selected);
        });

        HorizontalLayout top = new HorizontalLayout(title, addNewPosition, stageFilter);
        top.setWidthFull();

        add(top);
        add(grid);
    }

}

