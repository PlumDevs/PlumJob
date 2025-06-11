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
public class PositionsGridIntegrationTest {

    @Autowired
    private PositionsRepository positionsRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_EMAIL = "testuser@example.com";

    @BeforeEach
    void setUp() {
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
        jdbcTemplate.execute(
                "INSERT INTO users (username, password, enabled) VALUES ('" + TEST_USERNAME + "', 'password', true)"
        );
        userInfoRepository.addUserInfo(TEST_USERNAME, "Grid", "Test", TEST_EMAIL);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldCreateActivePositionsGridWithCorrectData() {
        // Given - Create test data
        positionsRepository.addPosition(TEST_USERNAME, "Java Developer", "Tech Corp",
                LocalDate.now(), "applied", "Java position", false);
        positionsRepository.addPosition(TEST_USERNAME, "Python Developer", "Data Corp",
                LocalDate.now().minusDays(5), "interview scheduled", "Python position", false);

        // When - Create grid for active positions
        PositionsGrid activeGrid = new PositionsGrid(userInfoRepository, positionsRepository, true);

        // Then - Grid should contain active positions
        List<RecruitmentItem> gridItems = activeGrid.getListDataView().getItems().toList();
        assertThat(gridItems).hasSize(2);

        assertThat(gridItems)
                .extracting(RecruitmentItem::getPositon)
                .containsExactlyInAnyOrder("Java Developer", "Python Developer");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldCreateArchivePositionsGridWithCorrectData() {
        // Given - Create test data with ended positions
        positionsRepository.addPosition(TEST_USERNAME, "Frontend Dev", "Web Corp",
                LocalDate.now().minusDays(30), "rejected", "Frontend position", true);
        positionsRepository.addPosition(TEST_USERNAME, "Backend Dev", "API Corp",
                LocalDate.now().minusDays(20), "accepted the offer", "Backend position", true);
        positionsRepository.addPosition(TEST_USERNAME, "Active Dev", "Current Corp",
                LocalDate.now(), "applied", "Current position", false);

        // When - Create grid for archive positions
        PositionsGrid archiveGrid = new PositionsGrid(userInfoRepository, positionsRepository, false);

        // Then - Grid should contain only ended positions
        List<RecruitmentItem> gridItems = archiveGrid.getListDataView().getItems().toList();
        assertThat(gridItems).hasSize(2);

        assertThat(gridItems)
                .extracting(RecruitmentItem::getPositon)
                .containsExactlyInAnyOrder("Frontend Dev", "Backend Dev");
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldFilterGridByStage() {
        // Given - Create positions with different stages
        positionsRepository.addPosition(TEST_USERNAME, "Position 1", "Company A",
                LocalDate.now(), "applied", "Desc 1", false);
        positionsRepository.addPosition(TEST_USERNAME, "Position 2", "Company B",
                LocalDate.now(), "interview scheduled", "Desc 2", false);
        positionsRepository.addPosition(TEST_USERNAME, "Position 3", "Company C",
                LocalDate.now(), "applied", "Desc 3", false);

        PositionsGrid grid = new PositionsGrid(userInfoRepository, positionsRepository, true);

        // When - Filter by "applied" stage
        grid.filterByStage("applied");

        // Then - Should show only applied positions
        List<RecruitmentItem> filteredItems = grid.getListDataView().getItems().toList();
        assertThat(filteredItems).hasSize(2);
        assertThat(filteredItems)
                .allMatch(item -> "applied".equals(item.getStage()));
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldShowAllItemsWhenFilteredByAll() {
        // Given - Create positions with different stages
        positionsRepository.addPosition(TEST_USERNAME, "Position 1", "Company A",
                LocalDate.now(), "applied", "Desc 1", false);
        positionsRepository.addPosition(TEST_USERNAME, "Position 2", "Company B",
                LocalDate.now(), "interview scheduled", "Desc 2", false);

        PositionsGrid grid = new PositionsGrid(userInfoRepository, positionsRepository, true);

        // First filter by specific stage
        grid.filterByStage("applied");
        assertThat(grid.getListDataView().getItems().toList()).hasSize(1);

        // When - Filter by "All"
        grid.filterByStage("All");

        // Then - Should show all items
        List<RecruitmentItem> allItems = grid.getListDataView().getItems().toList();
        assertThat(allItems).hasSize(2);
    }

    @Test
    @WithMockUser(username = TEST_USERNAME)
    void shouldHandleEmptyGridCorrectly() {
        // Given - No positions in database

        // When - Create grid
        PositionsGrid grid = new PositionsGrid(userInfoRepository, positionsRepository, true);

        // Then - Grid should be empty
        List<RecruitmentItem> items = grid.getListDataView().getItems().toList();
        assertThat(items).isEmpty();
    }
}