package com.plumdevs.plumjob.integrationTips;

import com.plumdevs.plumjob.UI.ArticleDetailsView;
import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ArticleDetailsViewIntegrationTest {

    @Autowired
    private ArticleService articleService;

    @Test
    void shouldLoadArticleDetailsWithValidParameter() {
        // Given
        ArticleDetailsView detailsView = new ArticleDetailsView();
        BeforeEvent mockEvent = mock(BeforeEvent.class);
        when(mockEvent.getLocation()).thenReturn(new Location("articles/read/interviews"));

        // When - we simulate navigation with parameter
        assertDoesNotThrow(() -> {
            detailsView.setParameter(mockEvent, "interviews");
        });

        // Then - we check if the view contains article components
        assertTrue(detailsView.getComponentCount() > 0,
                "ArticleDetailsView should contain article components");
    }

    @Test
    void shouldHandleInvalidArticleParameter() {
        // Given
        ArticleDetailsView detailsView = new ArticleDetailsView();
        BeforeEvent mockEvent = mock(BeforeEvent.class);
        when(mockEvent.getLocation()).thenReturn(new Location("articles/read/invalid"));

        // When & Then - an invalid parameter should not cause an error
        // the view should redirect to articles/
        assertDoesNotThrow(() -> {
            detailsView.setParameter(mockEvent, "invalid-article");
        });
    }

    @Test
    void shouldIntegrateWithAllValidArticleParameters() {
        // Given - all articles that are available in the application
        String[] validArticles = {"interviews", "behavioural", "resume", "jobhunt", "portfolio"};

        for (String articleTitle : validArticles) {
            // Given
            ArticleDetailsView detailsView = new ArticleDetailsView();
            BeforeEvent mockEvent = mock(BeforeEvent.class);
            when(mockEvent.getLocation()).thenReturn(new Location("articles/read/" + articleTitle));

            // When & Then
            assertDoesNotThrow(() -> {
                detailsView.setParameter(mockEvent, articleTitle);
                assertTrue(detailsView.getComponentCount() > 0,
                        "Should load article: " + articleTitle);
            });
        }
    }
}