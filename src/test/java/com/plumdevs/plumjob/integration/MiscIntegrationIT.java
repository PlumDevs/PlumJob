package com.plumdevs.plumjob.integration;

import com.plumdevs.plumjob.base.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MiscIntegrationIT extends IntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void cleanMisc() {
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("TRUNCATE TABLE ErrorLogs");
        jdbc.execute("TRUNCATE TABLE Article");
        jdbc.execute("TRUNCATE TABLE Template");
        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    @DisplayName("Template CRUD and date columns")
    void templateCrud() {
        jdbc.update(
                "INSERT INTO Template(template_name,template_desc,creation_date,last_update) " +
                        "VALUES (?,?,?,?)",
                "T1","Desc", Date.valueOf("2025-05-01"), Date.valueOf("2025-05-02")
        );
        Integer count = jdbc.queryForObject(
                "SELECT COUNT(*) FROM Template WHERE template_name='T1'", Integer.class
        );
        assertEquals(1, count);

        // aktualizacja last_update
        jdbc.update(
                "UPDATE Template SET last_update=? WHERE template_name='T1'",
                Date.valueOf("2025-06-01")
        );
        Date lu = jdbc.queryForObject(
                "SELECT last_update FROM Template WHERE template_name='T1'", Date.class
        );
        assertEquals(Date.valueOf("2025-06-01"), lu);
    }

    @Test
    @DisplayName("Article CRUD and date columns")
    void articleCrud() {
        jdbc.update(
                "INSERT INTO Article(article_name,creation_date,last_update) VALUES (?,?,?)",
                "A1", Date.valueOf("2025-05-03"), null
        );
        Integer countA = jdbc.queryForObject(
                "SELECT COUNT(*) FROM Article WHERE article_name='A1'", Integer.class
        );
        assertEquals(1, countA);

        // ustawiamy last_update ręcznie
        jdbc.update(
                "UPDATE Article SET last_update=? WHERE article_name='A1'",
                Date.valueOf("2025-06-02")
        );
        Date lu = jdbc.queryForObject(
                "SELECT last_update FROM Article WHERE article_name='A1'", Date.class
        );
        assertEquals(Date.valueOf("2025-06-02"), lu);
    }

    @Test
    @DisplayName("ErrorLogs CRUD and error_date")
    void errorLogsCrud() {
        jdbc.update(
                "INSERT INTO ErrorLogs(error_code,error_date) VALUES (?,?)",
                500, Date.valueOf("2025-05-04")
        );
        Integer cnt = jdbc.queryForObject(
                "SELECT COUNT(*) FROM ErrorLogs WHERE error_code=500", Integer.class
        );
        assertEquals(1, cnt);
    }
}
