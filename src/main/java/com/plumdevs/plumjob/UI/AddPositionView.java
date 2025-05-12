package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.repository.PositionsRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@PageTitle("Add new position - Plum Job")
@PermitAll
@Route(value="addPosition", layout = MainLayout.class)
public class AddPositionView extends VerticalLayout {

    AddPositionView(UserInfoRepository userInfoRepository, PositionsRepository positionsRepository){

        H2 title = new H2("Add new position");
        Paragraph paragraph = new Paragraph("Enter information about the job posting below.");

        final TextField positionField = new TextField("Position");
        final TextField companyField = new TextField("Company");
        final TextArea descriptionField = new TextArea("Description");
        final TextField linkField = new TextField("Offer link");
        final ComboBox<String> statusField = new ComboBox("Status");

        Button submit = new Button("Submit");
        Button backToActive = new Button("Back");

        submit.addClassName("plum-button");
        backToActive.addClassName("light-button");
        backToActive.addClickListener(buttonClickEvent -> getUI().ifPresent(ui ->
                ui.navigate("active")));

        positionField.setWidthFull();
        companyField.setWidthFull();
        //description.setWidthFull();
        linkField.setWidthFull();

        statusField.setItems(List.of(
                "to apply",
                "applied",
                "OA in progress",
                "after OA",
                "interview scheduled",
                "after interview",
                "received offer",
                "rejected",
                "ghosted",
                "accepted the offer"
        ));

        statusField.setClassNameGenerator((item) -> {
            switch (item) {
                case "received offer", "accepted the offer":
                    return "status-green";
                case "rejected":
                    return "status-red";
                default:
                    return "";
            }
        });

        Image articlePreview = new Image("https://m.media-amazon.com/images/I/41qW0-s6kSL._AC_UF894,1000_QL80_.jpg", "Where to look for job offers - article preview");
        //articlePreview.setSizeFull();
        articlePreview.setWidth(550, Unit.PIXELS);
        articlePreview.setHeight(450, Unit.PIXELS);
        //TODO: SWAP FOR REAL DATA ABOUT WHERE TO LOOK FOR JOB OFFERS
        //great to both fill the empty space on the right, and to provide some extra value
        //best if on click guides to the full article or just straight job offer page(s)

        positionField.setMaxLength(50);
        companyField.setMaxLength(50);
        linkField.setMaxLength(100);
        descriptionField.setMaxLength(100);

        HorizontalLayout lineOne = new HorizontalLayout(positionField, companyField);
        HorizontalLayout lineTwo = new HorizontalLayout(linkField, statusField);
        HorizontalLayout buttons = new HorizontalLayout(backToActive, submit);
        VerticalLayout right = new VerticalLayout();
        VerticalLayout left = new VerticalLayout();
        HorizontalLayout page = new HorizontalLayout();

        lineOne.setPadding(false);
        lineOne.setWidth(600, Unit.PIXELS);
        lineTwo.setPadding(false);
        lineTwo.setWidth(600, Unit.PIXELS);
        descriptionField.setWidth(600, Unit.PIXELS);

        page.setSizeFull();

        left.add(title, paragraph);
        left.add(lineOne, lineTwo);
        left.add(descriptionField);
        left.add(buttons);

        right.add(articlePreview); //about where to look for job offers

        page.add(left, right);
        add(page);


        //NEW POSITION CREATION
        submit.addClickListener(e -> {
            String position = "";
            position += positionField.getValue().trim();
            String company = "";
            company += companyField.getValue().trim();
            String description = "";
            description += descriptionField.getValue().trim();
            String link = "";
            link += linkField.getValue().trim();

            String status = "";
            status += statusField.getValue().trim();

            int statusNumber = getStatusNumber(status);


            positionsRepository.addPosition((((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()), position, company, statusNumber, description, ifEnded(status));

            Notification.show("Position added successfully", 3000, Notification.Position.MIDDLE);

        });


    }

    private static int getStatusNumber(String status) {

        int statusNumber;

        switch(status) {
            case "to apply":
                statusNumber = 1;
                break;
            case "applied":
                statusNumber = 2;
                break;
            case "OA in progress":
                statusNumber = 3;
                break;
            case "after OA":
                statusNumber = 4;
                break;
            case "interview scheduled":
                statusNumber = 5;
                break;
            case "after interview":
                statusNumber = 6;
                break;
            case "received offer":
                statusNumber = 7;
                break;
            case "rejected":
                statusNumber = 8;
                break;
            case "ghosted":
                statusNumber = 9;
                break;
            case "accepted the offer":
                statusNumber = 10;
                break;
            default:
                statusNumber = 1;
        }
        return statusNumber;
    }

    private static boolean ifEnded(String status) {

        switch(status) {
            case "accepted the offer", "rejected":
                return true;
            default:
                return false;
        }

    }
}
