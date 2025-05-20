package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Plum Job - CV Builder")
@Route(value="CVBuilder", layout = MainLayout.class)
public class CVBuilderView extends VerticalLayout {

    private final ArticleService articleService = new ArticleService();
    CVBuilderView() {
        System.out.println("CV Builder");
        add(new H2("Create your own resume!"));
        add(new Paragraph("With our custom, ATS-friendly templates catching a recruiter's eye will be a breeze! Just choose one below and use our built-in editor to get the resume of your dreams."));


        Button templateOne = new Button("Template 1", event -> {
            UI.getCurrent().getPage().executeJs("window.location.href = '/CVBuilder/template1';");
        });

        Button templateTwo = new Button("Template 2", event -> {
            UI.getCurrent().getPage().executeJs("window.location.href = '/CVBuilder/template2';");
        });

        Button templateThree = new Button("Template 3", event -> {
            UI.getCurrent().getPage().executeJs("window.location.href = '/CVBuilder/template3';");
        });


        //temporary media - CV template thumbnail
        String tempThumbnail = "https://www.colorhexa.com/820346.png";


        Image tempImage1 = new Image(tempThumbnail,"Template One Thumbnail");
        tempImage1.setHeight(220, Unit.PIXELS);
        tempImage1.setWidth(220, Unit.PIXELS);

        Image tempImage2 = new Image(tempThumbnail, "Template Two Thumbnail");
        tempImage2.setHeight(220, Unit.PIXELS);
        tempImage2.setWidth(220, Unit.PIXELS);

        Image tempImage3 = new Image(tempThumbnail, "Template Three Thumbnail");
        tempImage3.setHeight(220, Unit.PIXELS);
        tempImage3.setWidth(220, Unit.PIXELS);

        VerticalLayout choiceOne = new VerticalLayout(tempImage1, templateOne);
        choiceOne.setAlignItems(Alignment.CENTER);
        VerticalLayout choiceTwo = new VerticalLayout(tempImage2, templateTwo);
        choiceTwo.setAlignItems(Alignment.CENTER);
        VerticalLayout choiceThree = new VerticalLayout(tempImage3, templateThree);
        choiceThree.setAlignItems(Alignment.CENTER);

        templateOne.addClassName("plum-button");
        templateTwo.addClassName("plum-button");
        templateThree.addClassName("plum-button");

        HorizontalLayout templates = new HorizontalLayout(choiceOne, choiceTwo, choiceThree);
        add(templates);

       //add(articleService.createArticleThumbnail("resume", "Crafting a Standout Tech CV"));

    }
}