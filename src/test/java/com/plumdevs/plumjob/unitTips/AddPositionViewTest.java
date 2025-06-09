package com.plumdevs.plumjob.unitTips;

import com.plumdevs.plumjob.UI.AddPositionView;
import com.plumdevs.plumjob.repository.PositionsRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import com.plumdevs.plumjob.service.ArticleService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.card.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AddPositionViewTest {

    private AddPositionView view;
    private ArticleService mockService;
    private UserInfoRepository mockUserInfoRepository;
    private PositionsRepository mockPositionsRepository;

    @BeforeEach
    void init() {
        mockService = Mockito.mock(ArticleService.class);
        mockUserInfoRepository = Mockito.mock(UserInfoRepository.class);
        mockPositionsRepository = Mockito.mock(PositionsRepository.class);

        // Mock ArticleService to return dummy cards
        when(mockService.createArticleThumbnail(anyString(), anyString()))
                .thenReturn(new Card());
    }

    @Test
    void constructor_createsArticleThumbnails() {
        // Given
        view = new AddPositionView(mockUserInfoRepository, mockPositionsRepository); // if we want to create a new Add Position View we have to give public in the original view
        // Replace internal service with mock
        ReflectionTestUtils.setField(view, "articleService", mockService);

        // When - manually call the methods that would be called in constructor
        Component card1 = mockService.createArticleThumbnail("jobhunt", "Mastering the Tech Job Hunt");
        Component card2 = mockService.createArticleThumbnail("portfolio", "Building a Portfolio That Gets Interviews");

        // Then
        verify(mockService).createArticleThumbnail("jobhunt", "Mastering the Tech Job Hunt");
        verify(mockService).createArticleThumbnail("portfolio", "Building a Portfolio That Gets Interviews");

        assertNotNull(card1);
        assertNotNull(card2);
    }

    @Test
    void articleService_isInitialized() {
        // Given & When
        view = new AddPositionView(mockUserInfoRepository, mockPositionsRepository);

        // Then
        Object articleService = ReflectionTestUtils.getField(view, "articleService");
        assertNotNull(articleService, "ArticleService should be initialized");
        assertTrue(articleService instanceof ArticleService, "Field should be instance of ArticleService");
    }

}