package com.plumdevs.plumjob.UI.component;

import java.util.Arrays;
import com.plumdevs.plumjob.UI.RecruitmentItemDetails;
import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.plumdevs.plumjob.repository.PositionsRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.plumdevs.plumjob.service.UserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PositionsGrid extends Grid<RecruitmentItem> {

    private List<RecruitmentItem> originalItems = new ArrayList<>();

    public PositionsGrid(UserInfoRepository userInfoRepository, PositionsRepository positionsRepository, boolean active) {

        setWidthFull();

        addColumn(RecruitmentItem::getPositon).setHeader("Position").setSortable(true);
        addColumn(RecruitmentItem::getCompany).setHeader("Company").setSortable(true);
        Column<RecruitmentItem> stageColumn = addColumn(RecruitmentItem::getStage).setHeader("Stage").setSortable(true);

        Editor<RecruitmentItem> editor = getEditor();

        Column<RecruitmentItem> editColumn = addComponentColumn(person -> {
            Button editButton = new Button("Edit");
            editButton.addClickListener(e -> {
                if (editor.isOpen())
                    editor.cancel();
                getEditor().editItem(person);
            });
            return editButton;
        }).setWidth("150px").setFlexGrow(0);
        //button to edit, now add its functionality... TODO

        Binder<RecruitmentItem> binder = new Binder<>(RecruitmentItem.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        //temp

        ComboBox<String> stageComboBox = new ComboBox<>();
        stageComboBox.setItems(List.of( //TODO: MAKE REUSABLE
                "to apply",
                "applied",
                "OA in progress",
                "after OA",
                "interview scheduled",
                "after interview",
                "received offer",
                "rejected",
                "declined the offer",
                "ghosted",
                "accepted the offer"
        ));
        stageComboBox.setWidthFull();

        binder.bind(stageComboBox, RecruitmentItem::getStage, RecruitmentItem::setStage);
        stageColumn.setEditorComponent(stageComboBox);

        setItemDetailsRenderer(new ComponentRenderer<RecruitmentItemDetails, RecruitmentItem>(RecruitmentItemDetails::new, RecruitmentItemDetails::setItem));
        setDetailsVisibleOnClick(true);

        binder.forField(stageComboBox)
                .asRequired("Stage is required")
                .withValidator(stageComboBox.getDefaultValidator())
                .bind(RecruitmentItem::getStage, RecruitmentItem::setStage);

        //List<RecruitmentItem> items;

        Button saveButton = new Button("Save");

        saveButton.addClickListener(e -> {
            RecruitmentItem item = editor.getItem();
            String newStage = stageComboBox.getValue();

            if (newStage != null) {
                item.setStage(newStage);
                positionsRepository.updateStatus(item.getHistory_id(), newStage); //database call to update
                editor.save();
                UI.getCurrent().getPage().reload(); //reload to make the ended value changes visible too
                //TODO: gotta test that if does not affect performance too much
            }

            else {
                Notification.show("Stage cannot be empty", 3000, Notification.Position.MIDDLE);
            }
        });

        Button cancelButton = new Button(VaadinIcon.CLOSE.create(),
                e -> editor.cancel());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);
        //TODO: ON SAVE CLICK, UPDATE @QUERY TO DATABASE


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