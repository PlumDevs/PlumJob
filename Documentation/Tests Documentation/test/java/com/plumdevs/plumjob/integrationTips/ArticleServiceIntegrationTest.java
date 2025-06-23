package com.plumdevs.plumjob.integrationTips;

import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ArticleServiceIntegrationTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void shouldReadArticleFromFile() throws IOException {
        // Given - real article file in resources/articles/
        String articleTitle = "interviews";

        // When -we read the article via the website
        VerticalLayout article = articleService.readArticle(articleTitle);

        // Then - we check if the article has been loaded correctly
        assertNotNull(article);
        assertTrue(article.getComponentCount() > 0, "Article should have components");

        // We check if the first component is a title
        Component firstComponent = article.getComponentAt(0);
        assertNotNull(firstComponent);
        assertEquals("com.vaadin.flow.component.html.H2", firstComponent.getClass().getName());
    }

    @Test
    void shouldReadArticlePreview() {
        // Given
        String articleTitle = "interviews";
        int maxLines = 3;

        // When
        String preview = articleService.readArticlePreview(articleTitle, maxLines);

        // Then
        assertNotNull(preview);
        assertFalse(preview.isEmpty());
        assertNotEquals("Preview unavailable.", preview);
    }

    @Test
    void shouldCreateArticleThumbnail() {
        // Given
        String articleTitle = "interviews";
        String displayTitle = "Test Article Title";

        // When
        Component thumbnail = articleService.createArticleThumbnail(articleTitle, displayTitle);

        // Then
        assertNotNull(thumbnail);
        assertEquals("com.vaadin.flow.component.card.Card", thumbnail.getClass().getName());
    }

    @Test
    void shouldHandleNonExistentArticle() {
        // Given
        String nonExistentArticle = "non-existent-article";

        // When & Then
        assertThrows(IOException.class, () -> {
            articleService.readArticle(nonExistentArticle);
        });
    }

    @Test
    void shouldReturnPreviewUnavailableForNonExistentArticle() {
        // Given
        String nonExistentArticle = "non-existent-article";

        // When
        String preview = articleService.readArticlePreview(nonExistentArticle, 3);

        // Then
        assertEquals("Preview unavailable.", preview);
    }

    @Test
    void shouldReadAllAvailableArticles() throws IOException {
        // Given - all articles we see in ArticlesView
        String[] articleTitles = {"interviews", "behavioural", "resume", "jobhunt", "portfolio"};

        for (String articleTitle : articleTitles) {
            // When
            VerticalLayout article = articleService.readArticle(articleTitle);
            String preview = articleService.readArticlePreview(articleTitle, 3);
            Component thumbnail = articleService.createArticleThumbnail(articleTitle, "Test Title");

            // Then
            assertNotNull(article, "Article should be readable: " + articleTitle);
            assertNotNull(preview, "Preview should be available: " + articleTitle);
            assertNotNull(thumbnail, "Thumbnail should be created: " + articleTitle);
            assertFalse(preview.equals("Preview unavailable."),
                    "Preview should not be unavailable for: " + articleTitle);
        }
    }
}