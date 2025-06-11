package com.plumdevs.plumjob.unitTips.resourcesTips;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ResourcesArticlesTest {
    @Test
    void allFiles_nonEmpty() throws Exception {
        Path dir = Path.of(getClass().getClassLoader()
                .getResource("articles").toURI());
        try (Stream<Path> s = Files.list(dir)) {
            s.filter(p -> p.toString().endsWith(".txt"))
                    .forEach(p -> {
                        try {
                            assertTrue(Files.size(p) > 0,
                                    "File " + p.getFileName() + " should not be empty");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    @Test
    void templateContainsRequiredInstructions() throws Exception {
        Path tpl = Path.of(getClass().getClassLoader()
                .getResource("articles/article_writing_template.md").toURI());
        String content = Files.readString(tpl);
        assertAll(
                () -> assertTrue(content.contains("## How to add new article for the Tips module?"),
                        "Template should explain how to add new article"),
                () -> assertTrue(content.contains("__1st line:__ Title"),
                        "Template should describe 1st line format"),
                () -> assertTrue(content.contains("__2nd line:__ First and last name of the author"),
                        "Template should describe 2nd line format"),
                () -> assertTrue(content.contains("__3rd line:__ Subtitle"),
                        "Template should describe 3rd line format"),
                () -> assertTrue(content.contains("__Last line__ of the article: Summary"),
                        "Template should describe last line format")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"interviews", "behavioural", "resume", "jobhunt", "portfolio"})
    void requiredArticleFiles_exist(String articleName) throws Exception {
        // Given
        String resourcePath = "articles/" + articleName + ".txt";

        // When & Then
        var resource = getClass().getClassLoader().getResource(resourcePath);
        assertNotNull(resource, "Article file should exist: " + resourcePath);

        Path articlePath = Path.of(resource.toURI());
        assertTrue(Files.exists(articlePath), "Article file should be accessible: " + articleName);
        assertTrue(Files.isReadable(articlePath), "Article file should be readable: " + articleName);
    }

    @ParameterizedTest
    @ValueSource(strings = {"interviews", "behavioural", "resume", "jobhunt", "portfolio"})
    void articleFiles_haveValidStructure(String articleName) throws Exception {
        // Given
        Path articlePath = Path.of(getClass().getClassLoader()
                .getResource("articles/" + articleName + ".txt").toURI());

        // When
        List<String> lines = Files.readAllLines(articlePath);

        // Then
        assertTrue(lines.size() >= 4,
                "Article should have at least 4 lines (title, author, content, summary): " + articleName);

        // We check if the lines are not empty
        assertFalse(lines.get(0).trim().isEmpty(),
                "First line (title) should not be empty: " + articleName);
        assertFalse(lines.get(1).trim().isEmpty(),
                "Second line (author) should not be empty: " + articleName);
        assertFalse(lines.get(2).trim().isEmpty(),
                "Third line (content) should not be empty: " + articleName);
        assertFalse(lines.get(lines.size() - 1).trim().isEmpty(),
                "Last line (summary) should not be empty: " + articleName);
    }

    @Test
    void articleFiles_useCorrectEncoding() throws Exception {
        // Given
        Path dir = Path.of(getClass().getClassLoader()
                .getResource("articles").toURI());

        // When & Then
        try (Stream<Path> s = Files.list(dir)) {
            s.filter(p -> p.toString().endsWith(".txt"))
                    .forEach(p -> {
                        try {
                            String content = Files.readString(p);
                            // We check if there are any problems with coding
                            assertFalse(content.contains("�"),
                                    "File should not contain encoding errors: " + p.getFileName());
                            assertFalse(content.contains("\uFFFD"),
                                    "File should not contain replacement characters: " + p.getFileName());
                        } catch (IOException e) {
                            fail("Should be able to read file with UTF-8 encoding: " + p.getFileName());
                        }
                    });
        }
    }

    @Test
    void articleFiles_haveSensibleContentLength() throws Exception {
        // Given
        String[] requiredArticles = {"interviews", "behavioural", "resume", "jobhunt", "portfolio"};

        for (String articleName : requiredArticles) {
            // When
            Path articlePath = Path.of(getClass().getClassLoader()
                    .getResource("articles/" + articleName + ".txt").toURI());
            String content = Files.readString(articlePath);

            // Then
            assertTrue(content.length() >= 100,
                    "Article should have reasonable content length (at least 100 chars): " + articleName);
            assertTrue(content.length() <= 50000,
                    "Article should not be extremely long (max 50k chars): " + articleName);
        }
    }

    @Test
    void articleDirectory_containsOnlyExpectedFiles() throws Exception {
        // Given
        Path dir = Path.of(getClass().getClassLoader()
                .getResource("articles").toURI());

        // When
        List<String> allFiles = Files.list(dir)
                .map(p -> p.getFileName().toString())
                .sorted()
                .toList();

        // Then
        // We check if there are any unexpected files
        for (String fileName : allFiles) {
            assertTrue(
                    fileName.endsWith(".txt") || fileName.endsWith(".md"),
                    "Articles directory should contain only .txt and .md files, found: " + fileName
            );
        }

        // We check if all required files are present
        assertTrue(allFiles.contains("test-article.txt"), "Should contain test-article.txt");
        assertTrue(allFiles.contains("interviews.txt"), "Should contain interviews.txt");
        assertTrue(allFiles.contains("behavioural.txt"), "Should contain behavioural.txt");
        assertTrue(allFiles.contains("resume.txt"), "Should contain resume.txt");
        assertTrue(allFiles.contains("jobhunt.txt"), "Should contain jobhunt.txt");
        assertTrue(allFiles.contains("portfolio.txt"), "Should contain portfolio.txt");
        assertTrue(allFiles.contains("article_writing_template.md"), "Should contain template");
    }

    @Test
    void template_isValidMarkdown() throws Exception {
        // Given
        Path templatePath = Path.of(getClass().getClassLoader()
                .getResource("articles/article_writing_template.md").toURI());

        // When
        String content = Files.readString(templatePath);

        // Then - basic Markdown syntax checks
        assertTrue(content.contains("#"), "Template should contain markdown headers");
        assertTrue(content.contains("__"), "Template should contain bold formatting");

        // We check if there is any incorrect syntax
        long openingBold = content.chars().filter(ch -> ch == '_').count();
        assertTrue(openingBold % 2 == 0, "Template should have balanced markdown formatting");
    }
}