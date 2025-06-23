package com.plumdevs.plumjob.integrationTips;

import com.plumdevs.plumjob.UI.ArticlesView;
import com.plumdevs.plumjob.service.ArticleService;
import com.plumdevs.plumjob.service.TagService;
import com.plumdevs.plumjob.service.UserService;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ArticlesViewIntegrationTest {

    @Autowired
    private ArticleService articleService;

    @MockitoBean
    private TagService tagService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthenticationContext authContext;

    @Test
    void shouldCreateArticlesViewWithRealArticleService() throws IOException {
        // Given - real ArticleService and other mocked dependencies
        when(authContext.getPrincipalName()).thenReturn(java.util.Optional.of("testuser"));

        // When - we create ArticlesView (simulates page loading)
        ArticlesView articlesView = new ArticlesView(tagService, authContext, userService); // the same problem with private method, we need to change it form private to public if we want to use it

        // Then - we check if the view was created with components
        assertNotNull(articlesView);
        assertTrue(articlesView.getComponentCount() > 0, "ArticlesView should have components");

        // We check if it contains a header
        boolean hasHeader = articlesView.getChildren()
                .anyMatch(component -> component.getClass().getName().contains("H2"));
        assertTrue(hasHeader, "ArticlesView should contain H2 header");

        // We check if it contains article cards
        long cardCount = articlesView.getChildren()
                .filter(component -> component.getClass().getName().contains("Card"))
                .count();
        assertTrue(cardCount >= 5, "ArticlesView should contain at least 5 article cards");
    }

    @Test
    void shouldIntegrateWithAllArticleFiles() {
        String[] expectedArticles = {"interviews", "behavioural", "resume", "jobhunt", "portfolio"};

        for (String articleTitle : expectedArticles) {
            // When & Then
            assertDoesNotThrow(() -> {
                articleService.readArticle(articleTitle);
            }, "Article should be readable: " + articleTitle);

            String preview = articleService.readArticlePreview(articleTitle, 3);
            assertNotEquals("Preview unavailable.", preview,
                    "Preview should be available for: " + articleTitle);
        }
    }
}