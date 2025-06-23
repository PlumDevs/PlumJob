package com.plumdevs.plumjob.service;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.card.CardVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    public ArticleService(){};

    public VerticalLayout readArticle(String articleTitle) throws IOException {

        Resource resource = new ClassPathResource("articles/" + articleTitle + ".txt");
        File file = resource.getFile();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String[] array = bufferedReader.lines().toArray(String[]::new);
        System.out.println(Arrays.asList(array));

        VerticalLayout article = new VerticalLayout();
        H2 title = new H2(array[0]);

        HorizontalLayout author = new HorizontalLayout(new Avatar(array[1]), new Paragraph(array[1]));

        article.add(title, author);

        Paragraph paragraph = new Paragraph(array[2]);
        article.add(paragraph);

        int numOfParagraphs = array.length - 4;

        for (int i = 3; i < numOfParagraphs; i+=2) {
            H4 sectionTitle = new H4(array[i + 1]);
            paragraph = new Paragraph(array[i + 1]);
            article.add(sectionTitle, paragraph);
        }

        paragraph = new Paragraph(array[array.length - 1]);
        article.add(paragraph);

        return article;
    }

    public String readArticlePreview(String articleTitle, int maxLines) {
        try {
            ClassPathResource resource = new ClassPathResource("articles/" + articleTitle + ".txt");
            InputStream inputStream = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            StringBuilder preview = new StringBuilder();
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null && count < maxLines) {
                if (count > 1) {
                    preview.append(line).append("\n");
                }
                count++;
            }

            return preview.toString().trim();

        } catch (IOException e) {
            return "Preview unavailable.";
        }
    }

    public Component createArticleThumbnail(String articleTitle, String displayTitle) {
        String preview = readArticlePreview(articleTitle, 3);

        H3 title = new H3(displayTitle);
        Paragraph snippet = new Paragraph(preview);

        Button go = new Button("Read More");
        go.addClassName("plum-button");
        go.addClickListener(click ->
                go.getUI().ifPresent(ui -> ui.navigate("articles/read/" + articleTitle))
        );

        Card card = new Card();
        card.addThemeVariants(
                CardVariant.LUMO_OUTLINED,
                CardVariant.LUMO_ELEVATED,
                CardVariant.LUMO_HORIZONTAL
        );

        card.add(title, snippet, go);
        card.setWidthFull();

        return card;
    }
}
