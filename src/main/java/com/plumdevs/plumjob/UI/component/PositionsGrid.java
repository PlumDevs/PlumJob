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
import java.util.stream.Collectors;

public class PositionsGrid extends Grid<RecruitmentItem> {

    private List<RecruitmentItem> originalItems = new ArrayList<>();

    public PositionsGrid(UserInfoRepository userInfoRepository, PositionsRepository positionsRepository, boolean active) {

        setWidthFull();

        addColumn(RecruitmentItem::getPositon).setHeader("Position").setSortable(true);
        addColumn(RecruitmentItem::getCompany).setHeader("Company").setSortable(true);
        addColumn(RecruitmentItem::getStage).setHeader("Stage").setSortable(true);

        setItemDetailsRenderer(new ComponentRenderer<RecruitmentItemDetails, RecruitmentItem>(RecruitmentItemDetails::new, RecruitmentItemDetails::setItem));
        setDetailsVisibleOnClick(true);

        //List<RecruitmentItem> items;


        if (active) {
            //addColumn(RecruitmentItem::getStage).setHeader("Stage").setSortable(true);
            originalItems = new ArrayList<>(positionsRepository.findActivePositions(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        }

        else {
            originalItems = new ArrayList<>(positionsRepository.findArchivePositions(((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        }

        setItems(originalItems);


    }

    public void filterByStage(String stage) {
        if ("All".equals(stage)) {
            setItems(originalItems);
        } else {
            setItems(originalItems.stream()
                    .filter(item -> stage.equals(item.getStage()))
                    .collect(Collectors.toList()));
        }
    }
}

//TODO: RESTRUCTURE STATUSES VIEWING - FETCH THE TEXT VALUE, NOT NUMBER
//TODO: FILTERING BY STATUSES
//TODO: STATUS EDITING