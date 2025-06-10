package com.plumdevs.plumjob.integrationActiveArchive;

import com.plumdevs.plumjob.UI.component.PositionsGrid;
import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.plumdevs.plumjob.repository.PositionsRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class RecruitmentItemIntegrationTest {

    @Test
    void shouldCreateRecruitmentItemWithAllFields() {
        // Given
        String position = "Software Engineer";
        String company = "Tech Corp";
        String stage = "applied";
        String description = "Great opportunity";
        LocalDate startDate = LocalDate.of(2025, 1, 15);

        // When
        RecruitmentItem item = new RecruitmentItem(position, company, stage, description, startDate);

        // Then
        assertThat(item.getPositon()).isEqualTo(position);
        assertThat(item.getCompany()).isEqualTo(company);
        assertThat(item.getStage()).isEqualTo(stage);
        assertThat(item.getDescription()).isEqualTo(description);
        assertThat(item.getStartDate()).isEqualTo(startDate);
    }

    @Test
    void shouldCreateRecruitmentItemWithDefaultConstructor() {
        // When
        RecruitmentItem item = new RecruitmentItem();

        // Then
        assertThat(item.getPositon()).isEqualTo("");
        assertThat(item.getCompany()).isEqualTo("");
        assertThat(item.getStage()).isEqualTo("");
        assertThat(item.getStartDate()).isEqualTo(LocalDate.now());
    }

    @Test
    void shouldAllowModificationOfFields() {
        // Given
        RecruitmentItem item = new RecruitmentItem("Initial", "Initial Corp", "applied");

        // When
        item.setPositon("Updated Position");
        item.setCompany("Updated Company");
        item.setStage("interview scheduled");
        item.setDescription("Updated description");
        LocalDate newDate = LocalDate.of(2025, 2, 1);
        item.setStartDate(newDate);

        // Then
        assertThat(item.getPositon()).isEqualTo("Updated Position");
        assertThat(item.getCompany()).isEqualTo("Updated Company");
        assertThat(item.getStage()).isEqualTo("interview scheduled");
        assertThat(item.getDescription()).isEqualTo("Updated description");
        assertThat(item.getStartDate()).isEqualTo(newDate);
    }
}