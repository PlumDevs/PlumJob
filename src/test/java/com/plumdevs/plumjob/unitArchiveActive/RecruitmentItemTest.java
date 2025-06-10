package com.plumdevs.plumjob.unitArchiveActive;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class RecruitmentItemTest {

    @Test
    void shouldCreateDefaultRecruitmentItem() {
        // When
        RecruitmentItem item = new RecruitmentItem();

        // Then
        assertEquals("", item.getPositon());
        assertEquals("", item.getCompany());
        assertEquals("", item.getStage());
        assertNotNull(item.getStartDate());
    }

    @Test
    void shouldCreateRecruitmentItemWithBasicParams() {
        // When
        RecruitmentItem item = new RecruitmentItem("Java Developer", "Google", "applied");

        // Then
        assertEquals("Java Developer", item.getPositon());
        assertEquals("Google", item.getCompany());
        assertEquals("applied", item.getStage());
        assertNotNull(item.getStartDate());
    }

    @Test
    void shouldCreateRecruitmentItemWithDescription() {
        // When
        RecruitmentItem item = new RecruitmentItem("DevOps", "Amazon", "interview", "Cloud role");

        // Then
        assertEquals("DevOps", item.getPositon());
        assertEquals("Amazon", item.getCompany());
        assertEquals("interview", item.getStage());
        assertEquals("Cloud role", item.getDescription());
        assertNotNull(item.getStartDate());
    }

    @Test
    void shouldCreateRecruitmentItemWithAllParams() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 10, 15);

        // When
        RecruitmentItem item = new RecruitmentItem("Java Developer", "Google", "applied", "Backend role", startDate);

        // Then
        assertEquals("Java Developer", item.getPositon());
        assertEquals("Google", item.getCompany());
        assertEquals("applied", item.getStage());
        assertEquals("Backend role", item.getDescription());
        assertEquals(startDate, item.getStartDate());
    }

    @Test
    void shouldSetAndGetHistoryId() {
        // Given
        RecruitmentItem item = new RecruitmentItem();

        // When
        item.setHistory_id(123);

        // Then
        assertEquals(123, item.getHistory_id());
    }

    @Test
    void shouldSetAndGetPosition() {
        // Given
        RecruitmentItem item = new RecruitmentItem();

        // When
        item.setPositon("Frontend Developer");

        // Then
        assertEquals("Frontend Developer", item.getPositon());
    }

    @Test
    void shouldSetAndGetCompany() {
        // Given
        RecruitmentItem item = new RecruitmentItem();

        // When
        item.setCompany("Microsoft");

        // Then
        assertEquals("Microsoft", item.getCompany());
    }

    @Test
    void shouldSetAndGetStage() {
        // Given
        RecruitmentItem item = new RecruitmentItem();

        // When
        item.setStage("rejected");

        // Then
        assertEquals("rejected", item.getStage());
    }

    @Test
    void shouldSetAndGetDescription() {
        // Given
        RecruitmentItem item = new RecruitmentItem();

        // When
        item.setDescription("Interesting position");

        // Then
        assertEquals("Interesting position", item.getDescription());
    }

    @Test
    void shouldSetAndGetStartDate() {
        // Given
        RecruitmentItem item = new RecruitmentItem();
        LocalDate newDate = LocalDate.of(2024, 5, 20);

        // When
        item.setStartDate(newDate);

        // Then
        assertEquals(newDate, item.getStartDate());
    }

    @Test
    void shouldHandleNullDescription() {
        // Given
        RecruitmentItem item = new RecruitmentItem();

        // When
        item.setDescription(null);

        // Then
        assertNull(item.getDescription());
    }
}