package com.plumdevs.plumjob.integrationTips;

import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ArticleUserFlowIntegrationTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void shouldSimulateCompleteArticleReadingFlow() throws IOException {
        // Simulates the full flow: ArticlesView -> thumbnail -> ArticleDetailsView

        // The user sees a list of articles
        String articleTitle = "interviews";
        String displayTitle = "5 Quick Tips to Succeed in Tech Interviews";

        // We generate thumbnail as in ArticlesView
        Component thumbnail = articleService.createArticleThumbnail(articleTitle, displayTitle);
        assertNotNull(thumbnail);
        assertTrue(thumbnail instanceof Card, "Thumbnail should be a Card component");

        // User clicks "Read More" -> goes to ArticleDetailsView
        VerticalLayout fullArticle = articleService.readArticle(articleTitle);
        assertNotNull(fullArticle);
        assertTrue(fullArticle.getComponentCount() > 0, "Full article should have components");

        // We check if the preview was consistent with the full article
        String preview = articleService.readArticlePreview(articleTitle, 3);
        assertNotNull(preview);
        assertFalse(preview.equals("Preview unavailable."));

        // The full article should contain more content than the preview
        assertTrue(fullArticle.getComponentCount() >= 3,
                "Full article should have more content than preview");
    }

    @Test
    void shouldHandleArticleNavigationFromAddPositionView() {
        // Simulates the case when the user is in AddPositionView and sees the articles on the right

        String[] articlesInAddPosition = {"jobhunt", "portfolio"};

        for (String articleTitle : articlesInAddPosition) {
            Component thumbnail = articleService.createArticleThumbnail(articleTitle, "Test Title");
            assertNotNull(thumbnail, "Thumbnail should be created for: " + articleTitle);

            assertDoesNotThrow(() -> {
                VerticalLayout fullArticle = articleService.readArticle(articleTitle);
                assertNotNull(fullArticle);
                assertTrue(fullArticle.getComponentCount() > 0);
            }, "Should be able to navigate to article: " + articleTitle);
        }
    }

    @Test
    void shouldMaintainConsistencyAcrossAllViews() throws IOException {
        // Consistency Test - The same article should work in all places
        String articleTitle = "resume";

        Component thumbnailFromArticlesView = articleService.createArticleThumbnail(
                articleTitle, "Crafting a Standout Tech CV");

        Component thumbnailFromAddPosition = articleService.createArticleThumbnail(
                articleTitle, "Different Display Title");

        VerticalLayout fullArticle = articleService.readArticle(articleTitle);

        String preview = articleService.readArticlePreview(articleTitle, 3);

        assertNotNull(thumbnailFromArticlesView);
        assertNotNull(thumbnailFromAddPosition);
        assertNotNull(fullArticle);
        assertNotNull(preview);
        assertFalse(preview.equals("Preview unavailable."));

        assertEquals(thumbnailFromArticlesView.getClass(), thumbnailFromAddPosition.getClass());
    }
}