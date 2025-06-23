package com.plumdevs.plumjob.unitTips.serviceTips;

import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceTest {
    private ArticleService service;

    @BeforeEach
    void setUp() {
        service = new ArticleService();
    }

    @Test
    void readArticle_existingFile_returnsLayoutWithCorrectStructure() throws IOException {
        // Given - test with real file (for basic functionality)
        String articleTitle = "resume";

        // When
        VerticalLayout layout = service.readArticle(articleTitle);

        // Then
        assertNotNull(layout);
        assertTrue(layout.getComponentCount() > 0, "Layout should contain components");

        // We check the structure
        assertEquals("com.vaadin.flow.component.html.H2",
                layout.getComponentAt(0).getClass().getName());
    }

    @Test
    void readArticle_missingFile_throwsIOException() {
        // Given
        String nonExistentArticle = "definitely-does-not-exist-" + System.currentTimeMillis();

        // When & Then
        assertThrows(IOException.class, () -> service.readArticle(nonExistentArticle));
    }

    @Test
    void readArticlePreview_existingFile_returnsContentWithoutTitleAndAuthor() {
        // Given
        String articleTitle = "resume";
        int maxLines = 3;

        // When
        String preview = service.readArticlePreview(articleTitle, maxLines);

        // Then
        assertNotNull(preview);
        assertFalse(preview.isBlank());
        assertNotEquals("Preview unavailable.", preview);

        // Preview should not contain blank lines at the beginning/end
        assertEquals(preview, preview.trim());
    }

    @Test
    void readArticlePreview_withDifferentMaxLines_respectsLimit() {
        // Given
        String articleTitle = "resume";

        // When
        String shortPreview = service.readArticlePreview(articleTitle, 1);
        String longPreview = service.readArticlePreview(articleTitle, 5);

        // Then
        assertNotNull(shortPreview);
        assertNotNull(longPreview);

        // The short preview should be shorter or equal to the long one
        assertTrue(shortPreview.length() <= longPreview.length());
    }

    @Test
    void readArticlePreview_missingFile_returnsUnavailableMessage() {
        // Given
        String nonExistentArticle = "nonexistent-" + System.currentTimeMillis();

        // When
        String preview = service.readArticlePreview(nonExistentArticle, 3);

        // Then
        assertEquals("Preview unavailable.", preview);
    }

    @Test
    void readArticlePreview_zeroMaxLines_returnsEmptyString() {
        // Given
        String articleTitle = "resume";

        // When
        String preview = service.readArticlePreview(articleTitle, 0);

        // Then
        assertEquals("", preview);
    }

    @Test
    void createArticleThumbnail_returnsCardWithCorrectComponents() {
        // Given
        String articleTitle = "resume";
        String displayTitle = "Test Resume Title";

        // When
        var component = service.createArticleThumbnail(articleTitle, displayTitle);

        // Then
        assertNotNull(component);
        assertTrue(component instanceof Card, "Should return Card component");

        Card card = (Card) component;

        // We check if the card contains the required components
        boolean hasH3Title = card.getChildren().anyMatch(c -> c instanceof H3);
        boolean hasParagraphSnippet = card.getChildren().anyMatch(c -> c instanceof Paragraph);

        assertTrue(hasH3Title, "Card should contain H3 title");
        assertTrue(hasParagraphSnippet, "Card should contain Paragraph snippet");

        // We check if the card is full width
        assertTrue(card.getWidthUnit().isPresent());
    }

    @Test
    void createArticleThumbnail_withNonExistentArticle_stillCreatesCard() {
        // Given
        String nonExistentArticle = "nonexistent-article";
        String displayTitle = "Test Title";

        // When
        var component = service.createArticleThumbnail(nonExistentArticle, displayTitle);

        // Then
        assertNotNull(component);
        assertTrue(component instanceof Card);

        // The card should be created even if the article does not exist
    }

    @Test
    void createArticleThumbnail_withEmptyDisplayTitle_handlesGracefully() {
        // Given
        String articleTitle = "resume";
        String emptyTitle = "";

        // When & Then
        assertDoesNotThrow(() -> {
            var component = service.createArticleThumbnail(articleTitle, emptyTitle);
            assertNotNull(component);
        });
    }

    @Test
    void createArticleThumbnail_withNullDisplayTitle_handlesGracefully() {
        // Given
        String articleTitle = "resume";
        String nullTitle = null;

        // When & Then
        assertDoesNotThrow(() -> {
            var component = service.createArticleThumbnail(articleTitle, nullTitle);
            assertNotNull(component);
        });
    }
}