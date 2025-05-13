package com.plumdevs.plumjob.UI;

import com.plumdevs.plumjob.UI.layout.MainLayout;
import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@PermitAll
@PageTitle("Plum Job - Articles")
@Route(value="articles", layout = MainLayout.class)
public class ArticlesView extends VerticalLayout {

    private final ArticleService articleService = new ArticleService();

    ArticlesView() throws IOException {
        System.out.println("Articles/Tips");
        add(new H2("Read articles by recruiters from top companies"));

        setWidthFull();

        add(articleService.createArticleThumbnail("interviews", "5 Quick Tips to Succeed in Tech Interviews"));
        add(articleService.createArticleThumbnail("behavioural", "Acing the Behavioral Interview in Tech"));
        add(articleService.createArticleThumbnail("resume", "Crafting a Standout Tech CV"));

    }

}
