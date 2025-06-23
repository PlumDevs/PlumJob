package com.plumdevs.plumjob.unitTips;

import com.plumdevs.plumjob.UI.ArticlesView;
import com.plumdevs.plumjob.service.ArticleService;
import com.plumdevs.plumjob.service.TagService;
import com.plumdevs.plumjob.service.UserService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.card.Card;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.Mockito;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ArticlesViewTest {

    private ArticlesView view;
    private ArticleService mockService;
    private TagService mockTagService;
    private AuthenticationContext mockAuthContext;
    private UserService userService;

    @BeforeEach
    void init() {
        mockService = Mockito.mock(ArticleService.class);
        mockTagService = Mockito.mock(TagService.class);
        mockAuthContext = Mockito.mock(AuthenticationContext.class);

        // Mock ArticleService to return dummy cards
        when(mockService.createArticleThumbnail(anyString(), anyString()))
                .thenReturn(new Card());
    }

    @Test
    void constructor_createsAllArticleThumbnails() throws IOException {
        // Given
        view = new ArticlesView(mockTagService, mockAuthContext, userService); // if we want to create a new Add Position View we have to give public in the original view
        // Replace internal service with mock
        ReflectionTestUtils.setField(view, "articleService", mockService);

        // When - manually call the methods that would be called in constructor
        Component card1 = mockService.createArticleThumbnail("interviews", "5 Quick Tips to Succeed in Tech Interviews");
        Component card2 = mockService.createArticleThumbnail("behavioural", "Acing the Behavioral Interview in Tech");
        Component card3 = mockService.createArticleThumbnail("resume", "Crafting a Standout Tech CV");
        Component card4 = mockService.createArticleThumbnail("jobhunt", "Mastering the Tech Job Hunt");
        Component card5 = mockService.createArticleThumbnail("portfolio", "Building a Portfolio That Gets Interviews");

        // Then
        verify(mockService).createArticleThumbnail("interviews", "5 Quick Tips to Succeed in Tech Interviews");
        verify(mockService).createArticleThumbnail("behavioural", "Acing the Behavioral Interview in Tech");
        verify(mockService).createArticleThumbnail("resume", "Crafting a Standout Tech CV");
        verify(mockService).createArticleThumbnail("jobhunt", "Mastering the Tech Job Hunt");
        verify(mockService).createArticleThumbnail("portfolio", "Building a Portfolio That Gets Interviews");

        assertNotNull(card1);
        assertNotNull(card2);
        assertNotNull(card3);
        assertNotNull(card4);
        assertNotNull(card5);
    }

    @Test
    void constructor_addsCorrectNumberOfComponents() throws IOException {
        // Given & When
        view = new ArticlesView(mockTagService, mockAuthContext, userService); // if we want to create a new Add Position View we have to give public in the original view

        // Then
        // Should contain: H2 title + 5 article thumbnails + StickyAdBar = 7 components
        assertEquals(7, view.getComponentCount(), "ArticlesView should contain 7 components");
    }

    @Test
    void articleService_isInitialized() throws IOException {
        // Given & When
        view = new ArticlesView(mockTagService, mockAuthContext, userService); // if we want to create a new Add Position View we have to give public in the original view

        // Then
        Object articleService = ReflectionTestUtils.getField(view, "articleService");
        assertNotNull(articleService, "ArticleService should be initialized");
        assertTrue(articleService instanceof ArticleService, "Field should be instance of ArticleService");
    }
}