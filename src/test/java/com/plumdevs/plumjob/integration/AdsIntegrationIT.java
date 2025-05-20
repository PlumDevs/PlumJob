package com.plumdevs.plumjob.integration;

import com.plumdevs.plumjob.base.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AdsIntegrationIT extends IntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void cleanAds() {
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("TRUNCATE TABLE Ads");
        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    @DisplayName("Default is_active is NULL when omitted")
    void defaultIsActive() {
        jdbc.update(
                "INSERT INTO Ads(who_created,ad_start,ad_end,offer_link) VALUES (?,?,?,?)",
                "creator1", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()), "http://example.com"
        );
        Boolean isActive = jdbc.queryForObject(
                "SELECT is_active FROM Ads WHERE who_created='creator1'",
                Boolean.class
        );
        assertNull(isActive, "Domyślnie is_active powinno być NULL");
    }

    @Test
    @DisplayName("Explicit is_active value is stored")
    void explicitIsActive() {
        jdbc.update(
                "INSERT INTO Ads(who_created,ad_start,ad_end,offer_link,is_active) VALUES (?,?,?,?,?)",
                "creator2", Date.valueOf("2025-06-01"), Date.valueOf("2025-06-10"), "http://foo", true
        );
        Boolean isActive = jdbc.queryForObject(
                "SELECT is_active FROM Ads WHERE who_created='creator2'",
                Boolean.class
        );
        assertTrue(isActive);
    }

    @Test
    @DisplayName("Edge‑case: ad_end before ad_start is allowed (no CHECK constraint)")
    void dateOrderNotEnforced() {
        jdbc.update(
                "INSERT INTO Ads(who_created,ad_start,ad_end,offer_link) VALUES (?,?,?,?)",
                "creator3", Date.valueOf("2025-07-10"), Date.valueOf("2025-07-01"), "http://bar"
        );
        List<Date[]> dates = jdbc.query(
                "SELECT ad_start, ad_end FROM Ads WHERE who_created='creator3'",
                (rs, rowNum) -> new Date[]{ rs.getDate(1), rs.getDate(2) }
        );
        assertTrue(dates.get(0)[1].before(dates.get(0)[0]));
    }

    @Test
    @DisplayName("Deletion of Ads works and does not cascade")
    void deleteAds() {
        jdbc.update(
                "INSERT INTO Ads(who_created,ad_start,ad_end,offer_link) VALUES (?,?,?,?)",
                "creator4", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()), "http://baz"
        );
        int deleted = jdbc.update(
                "DELETE FROM Ads WHERE who_created='creator4'"
        );
        assertEquals(1, deleted);
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM Ads WHERE who_created='creator4'",
                Integer.class
        );
        assertEquals(0, count);
    }
}
