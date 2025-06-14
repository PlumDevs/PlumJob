package com.plumdevs.plumjob.UI.component;

import java.util.*;

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
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.*;
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

        Column<RecruitmentItem> deleteColumn = addComponentColumn(item -> {
            Button deleteButton = new Button(VaadinIcon.TRASH.create());
            deleteButton.getElement().getThemeList().add("error");
            deleteButton.addClickListener(e -> {
                positionsRepository.deletePosition(item.getHistory_id());
                getListDataView().removeItem(item);
            });
            return deleteButton;
        }).setWidth("150px").setFlexGrow(0);

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

            if (validStatusChange(item.getStage(), newStage)) {
                item.setStage(newStage);
                positionsRepository.updateStatus(item.getHistory_id(), newStage); //database call to update
                editor.save();
                UI.getCurrent().getPage().reload(); //reload to make the ended value changes visible too
            }

            else {
                Notification.show("Stage change cannot be applied", 3000, Notification.Position.MIDDLE);
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

    public static final Map<String, Set<String>> VALID_STATUS_CHANGES = new HashMap<>();

    static {
        VALID_STATUS_CHANGES.put("to apply", new HashSet<>(Arrays.asList("applied")));
        VALID_STATUS_CHANGES.put("applied", new HashSet<>(Arrays.asList("OA in progress", "interview scheduled")));
        VALID_STATUS_CHANGES.put("OA in progress", new HashSet<>(Arrays.asList("after OA")));
        VALID_STATUS_CHANGES.put("after OA", new HashSet<>(Arrays.asList("interview scheduled")));
        VALID_STATUS_CHANGES.put("interview scheduled", new HashSet<>(Arrays.asList("after interview")));
        VALID_STATUS_CHANGES.put("after interview", new HashSet<>(Arrays.asList("received offer", "rejected", "ghosted")));
        VALID_STATUS_CHANGES.put("received offer", new HashSet<>(Arrays.asList("accepted the offer", "declined the offer", "ghosted")));
        VALID_STATUS_CHANGES.put("accepted the offer", new HashSet<>());
        VALID_STATUS_CHANGES.put("declined the offer", new HashSet<>());
        VALID_STATUS_CHANGES.put("rejected", new HashSet<>());
        VALID_STATUS_CHANGES.put("ghosted", new HashSet<>());
    }


    public boolean validStatusChange(String before, String after) {
        Set<String> allowed = VALID_STATUS_CHANGES.get(before);
        return allowed != null && allowed.contains(after);
    }
}

//TODO: RESTRUCTURE STATUSES VIEWING - FETCH THE TEXT VALUE, NOT NUMBER
//TODO: FILTERING BY STATUSES
//TODO: STATUS EDITING