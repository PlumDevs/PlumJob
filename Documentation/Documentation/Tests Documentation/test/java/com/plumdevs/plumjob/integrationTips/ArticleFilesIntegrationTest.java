package com.plumdevs.plumjob.integrationTips;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ArticleFilesIntegrationTest {

    @Test
    void shouldHaveAllRequiredArticleFiles() {
        // Given - all articles that are available in the application
        String[] requiredArticles = {"interviews", "behavioural", "resume", "jobhunt", "portfolio"};

        for (String articleTitle : requiredArticles) {
            // When & Then
            ClassPathResource resource = new ClassPathResource("articles/" + articleTitle + ".txt");
            assertTrue(resource.exists(),
                    "Article file should exist: articles/" + articleTitle + ".txt");
        }
    }

    @Test
    void shouldHaveValidArticleFileStructure() throws IOException {
        // Given
        String[] articleTitles = {"interviews", "behavioural", "resume", "jobhunt", "portfolio"};

        for (String articleTitle : articleTitles) {
            // When
            ClassPathResource resource = new ClassPathResource("articles/" + articleTitle + ".txt");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            String[] lines = reader.lines().toArray(String[]::new);
            reader.close();

            // Then - we check the basic structure of the article file
            assertTrue(lines.length >= 4,
                    "Article should have at least 4 lines: " + articleTitle);

            assertNotNull(lines[0]);
            assertFalse(lines[0].trim().isEmpty(),
                    "Article should have title: " + articleTitle);

            assertNotNull(lines[1]);
            assertFalse(lines[1].trim().isEmpty(),
                    "Article should have author: " + articleTitle);

            assertNotNull(lines[2]);
            assertFalse(lines[2].trim().isEmpty(),
                    "Article should have content: " + articleTitle);
        }
    }

    @Test
    void shouldReadArticleFilesWithCorrectEncoding() throws IOException {
        // Given
        String articleTitle = "interviews";
        ClassPathResource resource = new ClassPathResource("articles/" + articleTitle + ".txt");

        // When - we read from UTF-8
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

        String content = reader.lines()
                .reduce("", (a, b) -> a + "\n" + b);
        reader.close();

        // Then
        assertNotNull(content);
        assertFalse(content.trim().isEmpty());
        assertFalse(content.contains("�"), "Content should not contain encoding errors");
    }

    @Test
    void shouldHaveArticleTemplate() {
        // Given & When
        ClassPathResource template = new ClassPathResource("articles/article_writing_template.md");

        // Then
        assertTrue(template.exists(), "Article writing template should exist");
    }
}