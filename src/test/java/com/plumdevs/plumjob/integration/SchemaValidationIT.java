package com.plumdevs.plumjob.integration;

import com.plumdevs.plumjob.base.IntegrationTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SchemaValidationIT extends IntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    @DisplayName("All tables exist")
    void allTablesExist() {
        List<String> tables = jdbc.queryForList(
                "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE()",
                String.class);
        String[] expected = {
                "users","authorities","userinfo","ads","recruitmentstatus",
                "recruitmenthistory","tagcodes","tagusers","tagoffer",
                "template","article","errorlogs","recruitmentstatushistory"
        };
        for (String tbl : expected) {
            assertTrue(
                    tables.stream().anyMatch(t -> t.equalsIgnoreCase(tbl)),
                    "Missing table: " + tbl
            );
        }
    }

    @Test
    @DisplayName("Foreign keys defined correctly")
    void foreignKeysExist() {
        List<Map<String,Object>> fks = jdbc.queryForList(
                "SELECT TABLE_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                        "FROM information_schema.KEY_COLUMN_USAGE " +
                        "WHERE TABLE_SCHEMA = DATABASE() AND REFERENCED_TABLE_NAME IS NOT NULL"
        );

        // authorities → users
        assertTrue(
                fks.stream().anyMatch(m ->
                        m.get("TABLE_NAME").toString().equalsIgnoreCase("authorities")
                                && m.get("REFERENCED_TABLE_NAME").toString().equalsIgnoreCase("users")
                ),
                "Expected FK authorities(username) → users(username)"
        );

        // UserInfo → users
        assertTrue(
                fks.stream().anyMatch(m ->
                        m.get("TABLE_NAME").toString().equalsIgnoreCase("UserInfo")
                                && m.get("REFERENCED_TABLE_NAME").toString().equalsIgnoreCase("users")
                ),
                "Expected FK UserInfo(username) → users(username)"
        );

        // TagOffer → RecruitmentHistory
        assertTrue(
                fks.stream().anyMatch(m ->
                        m.get("TABLE_NAME").toString().equalsIgnoreCase("TagOffer")
                                && m.get("REFERENCED_TABLE_NAME").toString().equalsIgnoreCase("RecruitmentHistory")
                ),
                "Expected FK TagOffer(offer_id) → RecruitmentHistory(history_id)"
        );
    }

    @Test
    @DisplayName("Unique index on authorities(username,authority)")
    void uniqueIndexAuthorities() {
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM information_schema.STATISTICS " +
                        "WHERE TABLE_SCHEMA = DATABASE() " +
                        "  AND TABLE_NAME = 'authorities' " +
                        "  AND INDEX_NAME = 'ix_auth_username' " +
                        "  AND NON_UNIQUE = 0",
                Integer.class
        );
        assertEquals(
                2, count,
                "Index ix_auth_username powinien obejmować dokładnie 2 kolumny (username, authority), a jest: " + count
        );
    }

}
