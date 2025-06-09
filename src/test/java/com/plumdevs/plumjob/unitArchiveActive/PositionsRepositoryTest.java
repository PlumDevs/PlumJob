package com.plumdevs.plumjob.unitArchiveActive;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.plumdevs.plumjob.repository.PositionsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PositionsRepositoryTest {

    @Mock
    private PositionsRepository positionsRepository;

    @Test
    @DisplayName("Should find active positions for given username")
    void shouldFindActivePositions_WhenUsernameExists() {
        // Given
        String username = "testuser";
        List<RecruitmentItem> expectedItems = Arrays.asList(
                new RecruitmentItem("Java Developer", "Google", "applied"),
                new RecruitmentItem("Frontend Dev", "Microsoft", "interview scheduled")
        );
        when(positionsRepository.findActivePositions(username)).thenReturn(expectedItems);

        // When
        List<RecruitmentItem> result = positionsRepository.findActivePositions(username);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java Developer", result.get(0).getPositon());
        assertEquals("Google", result.get(0).getCompany());
        assertEquals("applied", result.get(0).getStage());
        assertEquals("Frontend Dev", result.get(1).getPositon());
        verify(positionsRepository, times(1)).findActivePositions(username);
    }

    @Test
    @DisplayName("Should return empty list when no active positions found")
    void shouldReturnEmptyList_WhenNoActivePositionsFound() {
        // Given
        String username = "userWithNoPositions";
        when(positionsRepository.findActivePositions(username)).thenReturn(Collections.emptyList());

        // When
        List<RecruitmentItem> result = positionsRepository.findActivePositions(username);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(positionsRepository).findActivePositions(username);
    }

    @Test
    @DisplayName("Should find archive positions for given username")
    void shouldFindArchivePositions_WhenUsernameExists() {
        // Given
        String username = "testuser";
        List<RecruitmentItem> expectedItems = Arrays.asList(
                new RecruitmentItem("DevOps Engineer", "Amazon", "rejected"),
                new RecruitmentItem("Backend Dev", "Netflix", "accepted the offer")
        );
        when(positionsRepository.findArchivePositions(username)).thenReturn(expectedItems);

        // When
        List<RecruitmentItem> result = positionsRepository.findArchivePositions(username);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("DevOps Engineer", result.get(0).getPositon());
        assertEquals("rejected", result.get(0).getStage());
        assertEquals("Backend Dev", result.get(1).getPositon());
        assertEquals("accepted the offer", result.get(1).getStage());
        verify(positionsRepository).findArchivePositions(username);
    }

    @Test
    @DisplayName("Should return empty list when no archive positions found")
    void shouldReturnEmptyList_WhenNoArchivePositionsFound() {
        // Given
        String username = "userWithNoArchive";
        when(positionsRepository.findArchivePositions(username)).thenReturn(Collections.emptyList());

        // When
        List<RecruitmentItem> result = positionsRepository.findArchivePositions(username);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(positionsRepository).findArchivePositions(username);
    }

    @Test
    @DisplayName("Should add new position successfully")
    void shouldAddNewPosition_WhenValidDataProvided() {
        // Given
        String username = "testuser";
        String position = "Backend Developer";
        String company = "Netflix";
        LocalDate startDate = LocalDate.of(2024, 5, 15);
        String stage = "applied";
        String description = "Great opportunity";
        boolean ended = false;

        // When
        positionsRepository.addPosition(username, position, company, startDate, stage, description, ended);

        // Then
        verify(positionsRepository, times(1)).addPosition(username, position, company, startDate, stage, description, ended);
    }

    @Test
    @DisplayName("Should add position with null description")
    void shouldAddPosition_WhenDescriptionIsNull() {
        // Given
        String username = "testuser";
        String position = "Frontend Developer";
        String company = "Google";
        LocalDate startDate = LocalDate.now();
        String stage = "to apply";
        String description = null;
        boolean ended = false;

        // When
        positionsRepository.addPosition(username, position, company, startDate, stage, description, ended);

        // Then
        verify(positionsRepository).addPosition(username, position, company, startDate, stage, description, ended);
    }

    @Test
    @DisplayName("Should update status successfully")
    void shouldUpdateStatus_WhenValidDataProvided() {
        // Given
        int historyId = 123;
        String newStage = "interview scheduled";

        // When
        positionsRepository.updateStatus(historyId, newStage);

        // Then
        verify(positionsRepository, times(1)).updateStatus(historyId, newStage);
    }

    @Test
    @DisplayName("Should update status to rejected")
    void shouldUpdateStatus_WhenStatusIsRejected() {
        // Given
        int historyId = 456;
        String newStage = "rejected";

        // When
        positionsRepository.updateStatus(historyId, newStage);

        // Then
        verify(positionsRepository).updateStatus(historyId, newStage);
    }

    @Test
    @DisplayName("Should delete position successfully")
    void shouldDeletePosition_WhenValidHistoryIdProvided() {
        // Given
        int historyId = 789;

        // When
        positionsRepository.deletePosition(historyId);

        // Then
        verify(positionsRepository, times(1)).deletePosition(historyId);
    }
}