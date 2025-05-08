package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.component.PositionsGrid;
import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.repository.PositionsRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll

@PageTitle("Plum Job - Archive")
@Route(value="archive", layout = MainLayout.class)
public class ArchiveView extends VerticalLayout {

    ArchiveView(UserInfoRepository userInfoRepository, PositionsRepository positionsRepository) {
        System.out.println("Archive");
        H2 title = new H2("Archive recruitments");

        HorizontalLayout top = new HorizontalLayout(title);
        top.setWidthFull();
        add(top);

        add(new PositionsGrid(userInfoRepository, positionsRepository, false));
    }
}
