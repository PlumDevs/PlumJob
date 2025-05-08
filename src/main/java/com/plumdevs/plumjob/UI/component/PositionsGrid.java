package com.plumdevs.plumjob.UI.component;

import com.plumdevs.plumjob.UI.RecruitmentItemDetails;
import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.plumdevs.plumjob.repository.PositionsRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.plumdevs.plumjob.service.UserService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class PositionsGrid extends Grid<RecruitmentItem> {

    public PositionsGrid(UserInfoRepository userInfoRepository, PositionsRepository positionsRepository, boolean active) {

        setWidthFull();

        addColumn(RecruitmentItem::getPositon).setHeader("Position").setSortable(true);
        addColumn(RecruitmentItem::getCompany).setHeader("Company").setSortable(true);
        addColumn(RecruitmentItem::getStage).setHeader("Stage").setSortable(true);

        setItemDetailsRenderer(new ComponentRenderer<RecruitmentItemDetails, RecruitmentItem>(RecruitmentItemDetails::new, RecruitmentItemDetails::setItem));
        setDetailsVisibleOnClick(true);

        List<RecruitmentItem> items;


        if (active) {
            //addColumn(RecruitmentItem::getStage).setHeader("Stage").setSortable(true);
            items = new ArrayList<>(positionsRepository.findActivePositions(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        }

        else {
            items = new ArrayList<>(positionsRepository.findArchivePositions(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        }

        setItems(items);

    }
}
