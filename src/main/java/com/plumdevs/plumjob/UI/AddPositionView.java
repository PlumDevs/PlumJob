package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("Add new position - Plum Job")
@PermitAll
@Route(value="addPosition", layout = MainLayout.class)
public class AddPositionView extends VerticalLayout {

    AddPositionView(){

        H2 title = new H2("Add new position");
        Paragraph paragraph = new Paragraph("Enter information about the job posting below.");

        TextField position = new TextField("Position");
        TextField company = new TextField("Company");
        TextArea description = new TextArea("Description");
        TextField link = new TextField("Offer link");
        ComboBox status = new ComboBox("Status");

        Button submit = new Button("Submit");

        submit.addClassName("plum-button");

        position.setWidthFull();
        company.setWidthFull();
        //description.setWidthFull();
        link.setWidthFull();

        status.setItems(List.of(
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

        Image articlePreview = new Image("https://m.media-amazon.com/images/I/41qW0-s6kSL._AC_UF894,1000_QL80_.jpg", "Where to look for job offers - article preview");
        //articlePreview.setSizeFull();
        articlePreview.setWidth(550, Unit.PIXELS);
        articlePreview.setHeight(450, Unit.PIXELS);
        //TODO: SWAP FOR REAL DATA ABOUT WHERE TO LOOK FOR JOB OFFERS
        //great to both fill the empty space on the right, and to provide some extra value
        //best if on click guides to the full article or just straight job offer page(s)

        position.setMaxLength(50);
        company.setMaxLength(50);
        link.setMaxLength(100);
        description.setMaxLength(100);

        HorizontalLayout lineOne = new HorizontalLayout(position, company);
        HorizontalLayout lineTwo = new HorizontalLayout(link, status);
        VerticalLayout right = new VerticalLayout();
        VerticalLayout left = new VerticalLayout();
        HorizontalLayout page = new HorizontalLayout();

        lineOne.setPadding(false);
        lineOne.setWidth(600, Unit.PIXELS);
        lineTwo.setPadding(false);
        lineTwo.setWidth(600, Unit.PIXELS);
        description.setWidth(600, Unit.PIXELS);

        page.setSizeFull();

        left.add(title, paragraph);
        left.add(lineOne, lineTwo);
        left.add(description);
        left.add(submit);

        right.add(articlePreview); //about where to look for job offers

        page.add(left, right);
        add(page);

    }
}
