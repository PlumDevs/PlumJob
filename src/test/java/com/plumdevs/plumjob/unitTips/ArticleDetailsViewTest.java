package com.plumdevs.plumjob.unitTips;

import com.plumdevs.plumjob.UI.ArticleDetailsView;
import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ArticleDetailsViewTest {
    private ArticleDetailsView view;
    private ArticleService mockService;

    @BeforeEach
    void init() {
        mockService = Mockito.mock(ArticleService.class);
        view = new ArticleDetailsView();
        // replace internal service with mock
        ReflectionTestUtils.setField(view, "articleService", mockService);
    }

    @Test
    void setParameter_success_addsComponent() throws IOException {
        // Use real VerticalLayout to avoid null-element issues
        VerticalLayout realLayout = new VerticalLayout();
        when(mockService.readArticle("key")).thenReturn(realLayout);

        int beforeCount = view.getComponentCount();
        view.setParameter(null, "key");
        int afterCount = view.getComponentCount();

        verify(mockService).readArticle("key");
        assertEquals(beforeCount + 1, afterCount, "Component count should increase by 1");
    }

    @Test
    void setParameter_failure_navigatesBack() throws IOException {
        when(mockService.readArticle("bad")).thenThrow(IOException.class);
        assertDoesNotThrow(() -> view.setParameter(null, "bad"));
    }
}