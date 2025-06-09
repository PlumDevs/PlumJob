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
class UserInfoRepositoryIntegrationTest {

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM UserInfo");
        jdbcTemplate.execute("DELETE FROM authorities");
        jdbcTemplate.execute("DELETE FROM users");
    }

    @Test
    void shouldAddUserInfoSuccessfully() {
        // Given
        String username = "newuser";
        String firstName = "new";
        String lastName = "Doe";
        String email = "new.doe@example.com";

        // Create user in users table first
        jdbcTemplate.execute(
                "INSERT INTO users (username, password, enabled) VALUES ('" + username + "', 'password', true)"
        );

        // When
        userInfoRepository.addUserInfo(username, firstName, lastName, email);

        // Then
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM UserInfo WHERE username = ?",
                Integer.class, username
        );
        assertThat(count).isEqualTo(1);

        // Verify data
        String storedEmail = jdbcTemplate.queryForObject(
                "SELECT user_email FROM UserInfo WHERE username = ?",
                String.class, username
        );
        assertThat(storedEmail).isEqualTo(email);
    }

    @Test
    void shouldCheckEmailExistsCorrectly() {
        // Given
        String username = "testuser";
        String email = "test@example.com";

        jdbcTemplate.execute(
                "INSERT INTO users (username, password, enabled) VALUES ('" + username + "', 'password', true)"
        );
        userInfoRepository.addUserInfo(username, "Test", "User", email);

        // When
        Integer existsCount = userInfoRepository.emailExists(email);
        Integer notExistsCount = userInfoRepository.emailExists("nonexistent@example.com");

        // Then
        assertThat(existsCount).isEqualTo(1);
        assertThat(notExistsCount).isEqualTo(0);
    }

    @Test
    void shouldSetCorrectDefaultValues() {
        // Given
        String username = "defaulttest";

        jdbcTemplate.execute(
                "INSERT INTO users (username, password, enabled) VALUES ('" + username + "', 'password', true)"
        );

        // When
        userInfoRepository.addUserInfo(username, "Default", "Test", "default@test.com");

        // Then - Check that default values are set correctly
        Boolean isActive = jdbcTemplate.queryForObject(
                "SELECT is_active FROM UserInfo WHERE username = ?",
                Boolean.class, username
        );
        assertThat(isActive).isTrue();

        // Check that creation date is set to current date
        Object creationDate = jdbcTemplate.queryForObject(
                "SELECT account_creation_date FROM UserInfo WHERE username = ?",
                Object.class, username
        );
        assertThat(creationDate).isNotNull();
    }
}