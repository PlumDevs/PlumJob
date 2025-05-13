package com.plumdevs.plumjob.entity;

public class ArticleContent {
    private final String fullText;
    private final String preview;

    public ArticleContent(String fullText, int previewLines) {
        this.fullText = fullText;

        String[] lines = fullText.split("\n");
        StringBuilder previewBuilder = new StringBuilder();
        for (int i = 0; i < Math.min(previewLines, lines.length); i++) {
            previewBuilder.append(lines[i]).append("\n");
        }
        this.preview = previewBuilder.toString().trim();
    }

    public String getFullText() {
        return fullText;
    }

    public String getPreview() {
        return preview;
    }
}
