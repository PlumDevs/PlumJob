package com.plumdevs.plumjob.unitArchiveActive;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class PositionsGridTest {

    private List<RecruitmentItem> testData;

    @BeforeEach
    void setUp() {
        testData = Arrays.asList(
                new RecruitmentItem("Java Developer", "Google", "applied", "Backend", LocalDate.now()),
                new RecruitmentItem("Frontend Dev", "Microsoft", "rejected", "React", LocalDate.now()),
                new RecruitmentItem("DevOps", "Amazon", "applied", "Cloud", LocalDate.now()),
                new RecruitmentItem("QA Engineer", "Netflix", "interview scheduled", "Testing", LocalDate.now()),
                new RecruitmentItem("Data Scientist", "Meta", "received offer", "ML/AI", LocalDate.now())
        );
    }

    @Test
    @DisplayName("Should filter items by specific stage correctly")
    void shouldFilterByStage_WhenSpecificStageProvided() {
        // Given
        String filterStage = "applied";

        // When
        List<RecruitmentItem> filteredResult = testData.stream()
                .filter(item -> filterStage.equals(item.getStage()))
                .toList();

        // Then
        assertEquals(2, filteredResult.size());
        assertTrue(filteredResult.stream().allMatch(item -> "applied".equals(item.getStage())));
        assertEquals("Java Developer", filteredResult.get(0).getPositon());
        assertEquals("DevOps", filteredResult.get(1).getPositon());
    }

    @Test
    @DisplayName("Should show all items when filter is 'All'")
    void shouldShowAllItems_WhenFilterIsAll() {
        // Given
        String filterStage = "All";

        // When
        List<RecruitmentItem> result;
        if ("All".equals(filterStage)) {
            result = testData;
        } else {
            result = testData.stream()
                    .filter(item -> filterStage.equals(item.getStage()))
                    .collect(Collectors.toList());
        }

        // Then
        assertEquals(5, result.size());
        assertEquals(testData, result);
    }

    @Test
    @DisplayName("Should return empty list when no items match filter")
    void shouldReturnEmptyList_WhenNoItemsMatchFilter() {
        // Given
        String filterStage = "non-existent-stage";

        // When
        List<RecruitmentItem> filteredResult = testData.stream()
                .filter(item -> filterStage.equals(item.getStage()))
                .toList();

        // Then
        assertTrue(filteredResult.isEmpty());
    }

    @Test
    @DisplayName("Should filter by 'rejected' stage correctly")
    void shouldFilterByRejectedStage_WhenRejectedStageProvided() {
        // Given
        String filterStage = "rejected";

        // When
        List<RecruitmentItem> filteredResult = testData.stream()
                .filter(item -> filterStage.equals(item.getStage()))
                .toList();

        // Then
        assertEquals(1, filteredResult.size());
        assertEquals("Frontend Dev", filteredResult.get(0).getPositon());
        assertEquals("rejected", filteredResult.get(0).getStage());
    }

    @Test
    @DisplayName("Should filter by 'interview scheduled' stage correctly")
    void shouldFilterByInterviewScheduledStage_WhenInterviewScheduledStageProvided() {
        // Given
        String filterStage = "interview scheduled";

        // When
        List<RecruitmentItem> filteredResult = testData.stream()
                .filter(item -> filterStage.equals(item.getStage()))
                .toList();

        // Then
        assertEquals(1, filteredResult.size());
        assertEquals("QA Engineer", filteredResult.get(0).getPositon());
        assertEquals("interview scheduled", filteredResult.get(0).getStage());
    }

    @Test
    @DisplayName("Should handle null stage in filtering")
    void shouldHandleNullStage_InFiltering() {
        // Given
        List<RecruitmentItem> dataWithNull = Arrays.asList(
                new RecruitmentItem("Java Developer", "Google", "applied"),
                new RecruitmentItem("Frontend Dev", "Microsoft", null)
        );
        String filterStage = "applied";

        // When
        List<RecruitmentItem> filteredResult = dataWithNull.stream()
                .filter(item -> filterStage.equals(item.getStage()))
                .toList();

        // Then
        assertEquals(1, filteredResult.size());
        assertEquals("Java Developer", filteredResult.get(0).getPositon());
    }

    @Test
    @DisplayName("Should maintain original order when filtering")
    void shouldMaintainOriginalOrder_WhenFiltering() {
        // Given
        String filterStage = "applied";

        // When
        List<RecruitmentItem> filteredResult = testData.stream()
                .filter(item -> filterStage.equals(item.getStage()))
                .toList();

        // Then
        assertEquals("Java Developer", filteredResult.get(0).getPositon());
        assertEquals("DevOps", filteredResult.get(1).getPositon());
        // We check if the order has not changed
        assertTrue(testData.indexOf(filteredResult.get(0)) < testData.indexOf(filteredResult.get(1)));
    }
}