package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Custom CV Builder")
@Route(value="CVBuilder", layout = MainLayout.class)
public class CVBuilderView extends VerticalLayout {
    CVBuilderView() {
        System.out.println("CV Builder");
        add(new Paragraph("Create your own resume"));

        Button templateOne = new Button("Template 1", event -> {
            UI.getCurrent().getPage().executeJs("window.location.href = '/CVBuilder/template1';");
        });

        Button templateTwo = new Button("Template 2", event -> {
            UI.getCurrent().getPage().executeJs("window.location.href = '/CVBuilder/template2';");
        });

        Button templateThree = new Button("Template 3", event -> {
            UI.getCurrent().getPage().executeJs("window.location.href = '/CVBuilder/template3';");
        });

        add(templateOne, templateTwo, templateThree);

        templateOne.addClassName("plum-button");
        templateTwo.addClassName("plum-button");
        templateThree.addClassName("plum-button");

    }
}
