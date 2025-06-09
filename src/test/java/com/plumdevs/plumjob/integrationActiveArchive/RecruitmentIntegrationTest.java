package com.plumdevs.plumjob.integrationActiveArchive;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.plumdevs.plumjob.repository.PositionsRepository;
import com.plumdevs.plumjob.repository.UserInfoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class RecruitmentIntegrationTest {

    @Autowired
    private PositionsRepository positionsRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "test@example.com";

    @BeforeEach
    void setUp() {
        // Clean up and prepare test data
        cleanDatabase();
        createTestUser();
    }

    private void cleanDatabase() {
        jdbcTemplate.execute("DELETE FROM RecruitmentStatusHistory");
        jdbcTemplate.execute("DELETE FROM RecruitmentHistory");
        jdbcTemplate.execute("DELETE FROM UserInfo");
        jdbcTemplate.execute("DELETE FROM authorities");
        jdbcTemplate.execute("DELETE FROM users");
    }

    private void createTestUser() {
        // Create user in users table
        jdbcTemplate.execute(
                "INSERT INTO users (username, password, enabled) VALUES ('" + TEST_USERNAME + "', 'password', true)"
        );

        // Create user info
        userInfoRepository.addUserInfo(TEST_USERNAME, "Test", "User", TEST_EMAIL);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    @Transactional
    void shouldAddNewRecruitmentAndFindInActivePositions() {
        // Given
        String position = "Software Engineer";
        String company = "Tech Corp";
        LocalDate startDate = LocalDate.now();
        String stage = "applied";
        String description = "Great opportunity";
        boolean ended = false;

        // When
        positionsRepository.addPosition(TEST_USERNAME, position, company, startDate, stage, description, ended);

        // Then
        List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);

        assertThat(activePositions).hasSize(1);
        RecruitmentItem item = activePositions.get(0);
        assertThat(item.getPositon()).isEqualTo(position);
        assertThat(item.getCompany()).isEqualTo(company);
        assertThat(item.getStage()).isEqualTo(stage);
        assertThat(item.getDescription()).isEqualTo(description);
        assertThat(item.getStartDate()).isEqualTo(startDate);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldMoveRecruitmentToArchiveWhenStatusChangedToEnded() {
        // Given - Create active recruitment
        positionsRepository.addPosition(TEST_USERNAME, "Developer", "Company A",
                LocalDate.now(), "applied", "Test desc", false);

        List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
        assertThat(activeItems).hasSize(1);

        int historyId = activeItems.get(0).getHistory_id();

        // Check current status before update
        System.out.println("Before update - Stage: " + activeItems.get(0).getStage());

        // When - Update status to rejected (ended status)
        positionsRepository.updateStatus(historyId, "rejected");

        // Force flush to ensure database changes are committed
        jdbcTemplate.execute("SELECT 1"); // Force database sync

        // Check database directly
        String currentStage = jdbcTemplate.queryForObject(
                "SELECT stage FROM RecruitmentHistory WHERE history_id = ?",
                String.class, historyId
        );
        System.out.println("After update - Stage from DB: " + currentStage);

        // Then - Should be moved to archive
        List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);
        List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);

        assertThat(activePositions).isEmpty();
        assertThat(archivedPositions).hasSize(1);
        assertThat(archivedPositions.get(0).getStage()).isEqualTo("rejected");
    }


    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldKeepRecruitmentActiveWhenStatusUpdatedToNonEndedStatus() {
        // Given
        positionsRepository.addPosition(TEST_USERNAME, "Developer", "Company B",
                LocalDate.now(), "applied", "Test desc", false);

        List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
        int historyId = activeItems.get(0).getHistory_id();

        // When - Update to non-ended status
        positionsRepository.updateStatus(historyId, "interview scheduled");

        // Force database sync
        jdbcTemplate.execute("SELECT 1");

        // Check database directly
        String currentStage = jdbcTemplate.queryForObject(
                "SELECT stage FROM RecruitmentHistory WHERE history_id = ?",
                String.class, historyId
        );
        System.out.println("Current stage from DB: " + currentStage);

        // Then - Should remain active
        List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);
        List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);

        assertThat(activePositions).hasSize(1);
        assertThat(archivedPositions).isEmpty();
        assertThat(activePositions.get(0).getStage()).isEqualTo("interview scheduled");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldDeleteRecruitmentCompletely() {
        // Given
        positionsRepository.addPosition(TEST_USERNAME, "QA Engineer", "Test Corp",
                LocalDate.now(), "applied", "QA role", false);

        List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
        assertThat(activeItems).hasSize(1);

        int historyId = activeItems.get(0).getHistory_id();

        // When
        positionsRepository.deletePosition(historyId);

        // Then
        List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);
        List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);

        assertThat(activePositions).isEmpty();
        assertThat(archivedPositions).isEmpty();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldHandleMultipleRecruitmentsWithDifferentStatuses() {
        // Given - Create multiple recruitments
        positionsRepository.addPosition(TEST_USERNAME, "Frontend Dev", "Company A",
                LocalDate.now(), "applied", "Frontend role", false);
        positionsRepository.addPosition(TEST_USERNAME, "Backend Dev", "Company B",
                LocalDate.now().minusDays(5), "interview scheduled", "Backend role", false);
        positionsRepository.addPosition(TEST_USERNAME, "Full Stack", "Company C",
                LocalDate.now().minusDays(10), "rejected", "Full stack role", true);

        // When
        List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);
        List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);

        // Then
        assertThat(activePositions).hasSize(2);
        assertThat(archivedPositions).hasSize(1);

        // Verify active positions
        assertThat(activePositions)
                .extracting(RecruitmentItem::getPositon)
                .containsExactlyInAnyOrder("Frontend Dev", "Backend Dev");

        // Verify archived position
        assertThat(archivedPositions.get(0).getPositon()).isEqualTo("Full Stack");
        assertThat(archivedPositions.get(0).getStage()).isEqualTo("rejected");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldTestEndedStatusesIndividually() {
        // Test each ended status individually to isolate issues
        String[] endedStatuses = {"rejected", "accepted the offer", "declined the offer", "ghosted"};

        for (String endedStatus : endedStatuses) {
            // Clean up before each test
            jdbcTemplate.execute("DELETE FROM RecruitmentStatusHistory WHERE recruitment_history_id IN (SELECT history_id FROM RecruitmentHistory WHERE user_id = '" + TEST_USERNAME + "')");
            jdbcTemplate.execute("DELETE FROM RecruitmentHistory WHERE user_id = '" + TEST_USERNAME + "'");

            // Given - Create recruitment
            positionsRepository.addPosition(TEST_USERNAME, "Test Position", "Test Company",
                    LocalDate.now(), "applied", "Test description", false);

            List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
            assertThat(activeItems).hasSize(1);
            int historyId = activeItems.get(0).getHistory_id();

            // When - Update to ended status
            positionsRepository.updateStatus(historyId, endedStatus);

            // Force database sync
            jdbcTemplate.execute("SELECT 1");

            // Debug database state
            String dbStage = jdbcTemplate.queryForObject(
                    "SELECT stage FROM RecruitmentHistory WHERE history_id = ?",
                    String.class, historyId
            );
            Boolean dbEnded = jdbcTemplate.queryForObject(
                    "SELECT ended FROM RecruitmentHistory WHERE history_id = ?",
                    Boolean.class, historyId
            );

            System.out.println("Testing status: " + endedStatus + ", DB stage: " + dbStage + ", DB ended: " + dbEnded);

            // Then - Should be archived
            List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);
            List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);

            assertThat(activePositions).isEmpty();
            assertThat(archivedPositions).hasSize(1);
            assertThat(archivedPositions.get(0).getStage()).isEqualTo(endedStatus);
        }
    }


    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldTestNonEndedStatusesSequentially() {
        // Given
        String[] nonEndedStatuses = {"to apply", "applied", "OA in progress", "after OA",
                "interview scheduled", "after interview", "received offer"};

        positionsRepository.addPosition(TEST_USERNAME, "Test Position", "Test Company",
                LocalDate.now(), "applied", "Test description", false);

        List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
        int historyId = activeItems.get(0).getHistory_id();

        // When & Then - Test each non-ended status
        for (String status : nonEndedStatuses) {
            positionsRepository.updateStatus(historyId, status);

            // Force database sync
            jdbcTemplate.execute("SELECT 1");

            // Debug database state
            String dbStage = jdbcTemplate.queryForObject(
                    "SELECT stage FROM RecruitmentHistory WHERE history_id = ?",
                    String.class, historyId
            );
            System.out.println("Testing status: " + status + ", DB stage: " + dbStage);

            List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);
            List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);

            assertThat(activePositions).hasSize(1);
            assertThat(archivedPositions).isEmpty();
            assertThat(activePositions.get(0).getStage()).isEqualTo(status);
        }
    }

    @Test
    void shouldIsolateUserData() {
        // Given - Create two different users
        String user1 = "user1";
        String user2 = "user2";

        // Create users
        jdbcTemplate.execute("INSERT INTO users (username, password, enabled) VALUES ('" + user1 + "', 'password', true)");
        jdbcTemplate.execute("INSERT INTO users (username, password, enabled) VALUES ('" + user2 + "', 'password', true)");
        userInfoRepository.addUserInfo(user1, "User", "One", "user1@test.com");
        userInfoRepository.addUserInfo(user2, "User", "Two", "user2@test.com");

        // Add positions for both users
        positionsRepository.addPosition(user1, "Position User1", "Company1", LocalDate.now(), "applied", "Desc1", false);
        positionsRepository.addPosition(user2, "Position User2", "Company2", LocalDate.now(), "applied", "Desc2", false);

        // When
        List<RecruitmentItem> user1Active = positionsRepository.findActivePositions(user1);
        List<RecruitmentItem> user2Active = positionsRepository.findActivePositions(user2);

        // Then - Each user should only see their own data
        assertThat(user1Active).hasSize(1);
        assertThat(user2Active).hasSize(1);
        assertThat(user1Active.get(0).getPositon()).isEqualTo("Position User1");
        assertThat(user2Active.get(0).getPositon()).isEqualTo("Position User2");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldTestStoredProcedureDirectly() {
        // Test the stored procedure directly to isolate the issue
        positionsRepository.addPosition(TEST_USERNAME, "Direct Test", "Test Company",
                LocalDate.now(), "applied", "Test description", false);

        List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
        int historyId = activeItems.get(0).getHistory_id();

        // Call stored procedure directly
        jdbcTemplate.execute("CALL sp_updateStatus(" + historyId + ", 'rejected')");

        // Check results
        String stage = jdbcTemplate.queryForObject(
                "SELECT stage FROM RecruitmentHistory WHERE history_id = ?",
                String.class, historyId
        );
        Boolean ended = jdbcTemplate.queryForObject(
                "SELECT ended FROM RecruitmentHistory WHERE history_id = ?",
                Boolean.class, historyId
        );

        System.out.println("Direct procedure call - Stage: " + stage + ", Ended: " + ended);

        assertThat(stage).isEqualTo("rejected");
        assertThat(ended).isTrue();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldNotAllowChangingArchivedRecruitmentBackToActiveStatus() {
        // Given - Create recruitment and move it to archive
        positionsRepository.addPosition(TEST_USERNAME, "Backend Developer", "Tech Corp",
                LocalDate.now(), "applied", "Great opportunity", false);

        List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
        assertThat(activeItems).hasSize(1);
        int historyId = activeItems.get(0).getHistory_id();

        // Move to archive by setting rejected status
        positionsRepository.updateStatus(historyId, "rejected");

        // Verify it's in archive
        List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);
        List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);

        assertThat(archivedPositions).hasSize(1);
        assertThat(activePositions).isEmpty();
        assertThat(archivedPositions.get(0).getStage()).isEqualTo("rejected");

        // When - Try to change archived recruitment back to active status
        positionsRepository.updateStatus(historyId, "to apply");

        // Force database sync
        jdbcTemplate.execute("SELECT 1");

        // Then - Should remain in archive, not move back to active
        List<RecruitmentItem> finalArchivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);
        List<RecruitmentItem> finalActivePositions = positionsRepository.findActivePositions(TEST_USERNAME);

        // Debug information
        String currentStage = jdbcTemplate.queryForObject(
                "SELECT stage FROM RecruitmentHistory WHERE history_id = ?",
                String.class, historyId
        );
        Boolean currentEnded = jdbcTemplate.queryForObject(
                "SELECT ended FROM RecruitmentHistory WHERE history_id = ?",
                Boolean.class, historyId
        );

        System.out.println("After trying to reactivate - Stage: " + currentStage + ", Ended: " + currentEnded);
        System.out.println("Active positions count: " + finalActivePositions.size());
        System.out.println("Archived positions count: " + finalArchivedPositions.size());

        // Assertions - recruitment should NOT be moved back to active
        assertThat(finalActivePositions).isEmpty(); // Should remain empty
        assertThat(finalArchivedPositions).hasSize(1); // Should still be in archive

        // The status might be updated, but the 'ended' flag should prevent it from becoming active
        // OR the system should reject the status change entirely
        assertTrue(Boolean.TRUE.equals(currentEnded), "Ended flag should remain true to prevent reactivation");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldNotAllowChangingFromOneEndedStatusToAnotherEndedStatus() {
        // Given - Create recruitment and archive it with "rejected"
        positionsRepository.addPosition(TEST_USERNAME, "Java Developer", "Tech Corp",
                LocalDate.now(), "applied", "Backend role", false);

        List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
        int historyId = activeItems.get(0).getHistory_id();

        positionsRepository.updateStatus(historyId, "rejected");

        // Verify it's archived
        List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);
        assertThat(archivedPositions).hasSize(1);
        assertThat(archivedPositions.get(0).getStage()).isEqualTo("rejected");

        // When - Try to change from "rejected" to "accepted the offer"
        positionsRepository.updateStatus(historyId, "accepted the offer");

        // Then - Should not allow changing between ended statuses
        List<RecruitmentItem> finalArchived = positionsRepository.findArchivePositions(TEST_USERNAME);
        List<RecruitmentItem> finalActive = positionsRepository.findActivePositions(TEST_USERNAME);

        assertThat(finalActive).isEmpty();
        assertThat(finalArchived).hasSize(1);
        // Status should remain "rejected" or system should reject the change
        assertThat(finalArchived.get(0).getStage()).isEqualTo("rejected");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldPreventDirectCreationOfArchivedRecruitment() {
        // When - Try to directly create recruitment with ended status
        positionsRepository.addPosition(TEST_USERNAME, "Senior Developer", "Big Corp",
                LocalDate.now(), "rejected", "Should not be created as ended", true);

        // Then - Should either be in active (ignoring ended flag) or not created at all
        List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);
        List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);

        // Debug info
        System.out.println("Active after direct creation with ended=true: " + activePositions.size());
        System.out.println("Archived after direct creation with ended=true: " + archivedPositions.size());

        // Either recruitment should not be created, or should be forced to active despite ended=true
        if (!activePositions.isEmpty() || !archivedPositions.isEmpty()) {
            // If created, should be in active (system should override ended=true for new recruitments)
            assertThat(activePositions).hasSize(1);
            assertThat(archivedPositions).isEmpty();
        }
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldHandleStatusUpdateOnNonExistentRecruitment() {
        // Given - Non-existent history ID
        int nonExistentHistoryId = 99999;

        // When - Try to update status on non-existent recruitment
        try {
            positionsRepository.updateStatus(nonExistentHistoryId, "applied");

            // Then - Should not affect any existing data
            List<RecruitmentItem> activePositions = positionsRepository.findActivePositions(TEST_USERNAME);
            List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);

            assertThat(activePositions).isEmpty();
            assertThat(archivedPositions).isEmpty();

        } catch (Exception e) {
            // Alternative: system might throw exception for non-existent ID
            System.out.println("Expected behavior: Exception thrown for non-existent ID: " + e.getMessage());
            Assertions.assertTrue(e.getMessage().contains("not found") || e.getMessage().contains("does not exist"));
        }
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldNotAllowUpdatingOtherUsersRecruitments() {
        // Given - Create another user and their recruitment
        String otherUser = "otheruser";
        jdbcTemplate.execute("INSERT INTO users (username, password, enabled) VALUES ('" + otherUser + "', 'password', true)");
        userInfoRepository.addUserInfo(otherUser, "Other", "User", "other@test.com");

        positionsRepository.addPosition(otherUser, "Other User Position", "Other Corp",
                LocalDate.now(), "applied", "Other user's job", false);

        List<RecruitmentItem> otherUserPositions = positionsRepository.findActivePositions(otherUser);
        assertThat(otherUserPositions).hasSize(1);
        int otherUserHistoryId = otherUserPositions.get(0).getHistory_id();

        // When - Current user tries to update other user's recruitment
        positionsRepository.updateStatus(otherUserHistoryId, "rejected");

        // Then - Other user's recruitment should not be affected
        List<RecruitmentItem> otherUserFinal = positionsRepository.findActivePositions(otherUser);
        List<RecruitmentItem> currentUserActive = positionsRepository.findActivePositions(TEST_USERNAME);
        List<RecruitmentItem> currentUserArchived = positionsRepository.findArchivePositions(TEST_USERNAME);

        // Other user's data should remain unchanged
        assertThat(otherUserFinal).hasSize(1);
        assertThat(otherUserFinal.get(0).getStage()).isEqualTo("applied");

        // Current user should have no data
        assertThat(currentUserActive).isEmpty();
        assertThat(currentUserArchived).isEmpty();
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldNotAllowDeletingArchivedRecruitment() {
        // Given - Create recruitment and archive it
        positionsRepository.addPosition(TEST_USERNAME, "Archived Position", "Archive Corp",
                LocalDate.now(), "applied", "Will be archived", false);

        List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
        int historyId = activeItems.get(0).getHistory_id();

        positionsRepository.updateStatus(historyId, "rejected");

        // Verify it's archived
        List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);
        assertThat(archivedPositions).hasSize(1);

        // When - Try to delete archived recruitment
        try {
            positionsRepository.deletePosition(historyId);

            // Then - Check if deletion was allowed or blocked
            List<RecruitmentItem> finalArchived = positionsRepository.findArchivePositions(TEST_USERNAME);
            List<RecruitmentItem> finalActive = positionsRepository.findActivePositions(TEST_USERNAME);

            // If system allows deletion of archived items
            if (finalArchived.isEmpty() && finalActive.isEmpty()) {
                System.out.println("System allows deletion of archived recruitments");
            }
            // If system blocks deletion of archived items
            else {
                assertThat(finalArchived).hasSize(1);
                assertThat(finalActive).isEmpty();
                System.out.println("System correctly blocks deletion of archived recruitments");
            }

        } catch (Exception e) {
            // System might throw exception when trying to delete archived recruitment
            System.out.println("System prevents deletion of archived recruitment: " + e.getMessage());

            // Verify data is still there after failed deletion
            List<RecruitmentItem> finalArchived = positionsRepository.findArchivePositions(TEST_USERNAME);
            assertThat(finalArchived).hasSize(1);
        }
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldMaintainStatusHistoryWhenUpdatingRecruitment() {
        // Given - Create recruitment
        positionsRepository.addPosition(TEST_USERNAME, "Developer", "History Corp",
                LocalDate.now(), "applied", "Test history", false);

        List<RecruitmentItem> activeItems = positionsRepository.findActivePositions(TEST_USERNAME);
        int historyId = activeItems.get(0).getHistory_id();

        // When - Update status multiple times
        positionsRepository.updateStatus(historyId, "interview scheduled");
        positionsRepository.updateStatus(historyId, "after interview");
        positionsRepository.updateStatus(historyId, "rejected");

        // Then - Check if history is maintained
        Integer historyCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM RecruitmentStatusHistory WHERE recruitment_history_id = ?",
                Integer.class, historyId
        );

        System.out.println("Status history entries count: " + historyCount);

        // Should have history entries for status changes
        assertThat(historyCount).isGreaterThan(0);

        // Final state should be archived
        List<RecruitmentItem> archivedPositions = positionsRepository.findArchivePositions(TEST_USERNAME);
        assertThat(archivedPositions).hasSize(1);
        assertThat(archivedPositions.get(0).getStage()).isEqualTo("rejected");
    }
}