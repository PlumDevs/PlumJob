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
class ArchiveActiveWorkflowIntegrationTest {

    @Autowired
    private PositionsRepository positionsRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TEST_USERNAME = "workflowuser";

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM RecruitmentStatusHistory");
        jdbcTemplate.execute("DELETE FROM RecruitmentHistory");
        jdbcTemplate.execute("DELETE FROM UserInfo");
        jdbcTemplate.execute("DELETE FROM authorities");
        jdbcTemplate.execute("DELETE FROM users");

        jdbcTemplate.execute(
                "INSERT INTO users (username, password, enabled) VALUES ('" + TEST_USERNAME + "', 'password', true)"
        );
        userInfoRepository.addUserInfo(TEST_USERNAME, "Workflow", "User", "workflow@test.com");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldCompleteFullRecruitmentLifecycle() {
        // Add new position (should appear in active)
        positionsRepository.addPosition(TEST_USERNAME, "Full Stack Dev", "Lifecycle Corp",
                LocalDate.now(), "to apply", "Complete lifecycle test", false);

        List<RecruitmentItem> active = positionsRepository.findActivePositions(TEST_USERNAME);
        List<RecruitmentItem> archive = positionsRepository.findArchivePositions(TEST_USERNAME);

        assertThat(active).hasSize(1);
        assertThat(archive).isEmpty();
        assertThat(active.get(0).getStage()).isEqualTo("to apply");

        int historyId = active.get(0).getHistory_id();

        // Progress through stages (should remain active)
        String[] progressStages = {"applied", "OA in progress", "after OA", "interview scheduled", "after interview"};

        for (String stage : progressStages) {
            positionsRepository.updateStatus(historyId, stage);

            active = positionsRepository.findActivePositions(TEST_USERNAME);
            archive = positionsRepository.findArchivePositions(TEST_USERNAME);

            assertThat(active).hasSize(1);
            assertThat(archive).isEmpty();
            assertThat(active.get(0).getStage()).isEqualTo(stage);
        }

        // Get job offer (should remain active)
        positionsRepository.updateStatus(historyId, "received offer");

        active = positionsRepository.findActivePositions(TEST_USERNAME);
        archive = positionsRepository.findArchivePositions(TEST_USERNAME);

        assertThat(active).hasSize(1);
        assertThat(archive).isEmpty();
        assertThat(active.get(0).getStage()).isEqualTo("received offer");

        // Accept offer (should move to archive)
        positionsRepository.updateStatus(historyId, "accepted the offer");

        active = positionsRepository.findActivePositions(TEST_USERNAME);
        archive = positionsRepository.findArchivePositions(TEST_USERNAME);

        assertThat(active).isEmpty();
        assertThat(archive).hasSize(1);
        assertThat(archive.get(0).getStage()).isEqualTo("accepted the offer");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldHandleRejectionScenario() {
        // Given - Position progresses to interview
        positionsRepository.addPosition(TEST_USERNAME, "Rejected Position", "Rejection Corp",
                LocalDate.now(), "applied", "Will be rejected", false);

        List<RecruitmentItem> active = positionsRepository.findActivePositions(TEST_USERNAME);
        int historyId = active.get(0).getHistory_id();

        positionsRepository.updateStatus(historyId, "interview scheduled");
        positionsRepository.updateStatus(historyId, "after interview");

        // When - Gets rejected
        positionsRepository.updateStatus(historyId, "rejected");

        // Then - Should be in archive
        active = positionsRepository.findActivePositions(TEST_USERNAME);
        List<RecruitmentItem> archive = positionsRepository.findArchivePositions(TEST_USERNAME);

        assertThat(active).isEmpty();
        assertThat(archive).hasSize(1);
        assertThat(archive.get(0).getStage()).isEqualTo("rejected");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldMaintainStatusHistory() {
        // Given
        positionsRepository.addPosition(TEST_USERNAME, "History Test", "History Corp",
                LocalDate.now(), "applied", "Test status history", false);

        List<RecruitmentItem> active = positionsRepository.findActivePositions(TEST_USERNAME);
        int historyId = active.get(0).getHistory_id();

        // When - Update status multiple times
        positionsRepository.updateStatus(historyId, "interview scheduled");
        positionsRepository.updateStatus(historyId, "after interview");
        positionsRepository.updateStatus(historyId, "received offer");

        // Then - Should have status history entries
        Integer historyCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM RecruitmentStatusHistory WHERE recruitment_history_id = ?",
                Integer.class, historyId
        );

        // Should have at least 3 status changes (plus potentially the initial one from addPosition)
        assertThat(historyCount).isGreaterThanOrEqualTo(3);
    }
}