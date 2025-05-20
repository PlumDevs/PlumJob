package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.component.PositionsGrid;
import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.repository.PositionsRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;


import com.plumdevs.plumjob.UI.component.StickyAdBar;
import com.plumdevs.plumjob.service.TagService;
import com.vaadin.flow.spring.security.AuthenticationContext;





@PermitAll

@PageTitle("Plum Job - Archive")
@Route(value="archive", layout = MainLayout.class)
public class ArchiveView extends VerticalLayout {

    ArchiveView(UserInfoRepository userInfoRepository,
                PositionsRepository positionsRepository,
                TagService tagService,
                AuthenticationContext authContext)
    {
        System.out.println("Archive");
        H2 title = new H2("Archive recruitments");

        PositionsGrid grid = new PositionsGrid(userInfoRepository, positionsRepository, false);

        ComboBox<String> stageFilter = new ComboBox<>();

        stageFilter.setItems("All",
                "rejected",
                "declined the offer",
                "ghosted",
                "accepted the offer");
        stageFilter.setValue("All");

        stageFilter.addValueChangeListener(event -> {
            String selected = event.getValue();
            grid.filterByStage(selected);
        });

        HorizontalLayout top = new HorizontalLayout(title, stageFilter);
        top.setWidthFull();
        add(top);

        add(grid);

        StickyAdBar adBar = new StickyAdBar(tagService, authContext);
        add(adBar);

    }
}