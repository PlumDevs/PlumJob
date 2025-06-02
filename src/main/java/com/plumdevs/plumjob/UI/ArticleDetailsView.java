package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@PermitAll
@Route(value="articles/read/", layout = MainLayout.class)
public class ArticleDetailsView extends VerticalLayout implements HasUrlParameter<String>, AfterNavigationObserver {

    private final ArticleService articleService = new ArticleService();

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        scrollIntoView();
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try {
            Component articleContent = articleService.readArticle(parameter);
            add(articleContent);



        } catch (IOException e) {
            getUI().ifPresent(ui ->
                    ui.navigate("articles/"));
        }

    }

}
