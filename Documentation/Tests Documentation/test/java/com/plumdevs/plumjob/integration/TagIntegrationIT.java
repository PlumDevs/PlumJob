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

public class TagIntegrationIT extends IntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void cleanAllAndSeed() {
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        String[] tables = {
                "RecruitmentStatusHistory",
                "TagOffer",
                "TagUsers",
                "RecruitmentHistory",
                "authorities",
                "UserInfo",
                "Ads",
                "TagCodes",
                "users",
                "Template",
                "Article",
                "ErrorLogs"
        };
        for (String tbl : tables) {
            jdbc.execute("DELETE FROM " + tbl);
        }
        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");

    }

    @Test
    @DisplayName("TagCodes, TagUsers, TagOffer CRUD + FK")
    void tagsCrud() {
        jdbc.update("INSERT INTO TagCodes(tag_name) VALUES (?)", "java");
        Integer tagId = jdbc.queryForObject(
                "SELECT tag_id FROM TagCodes WHERE tag_name='java' LIMIT 1", Integer.class
        );

        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                "u6","p",true);

        // We insert the same user/tag pair twice (because the scheme does not enforce uniqueness)
        jdbc.update("INSERT INTO TagUsers(user_id,tag_id) VALUES (?,?)", "u6", tagId);
        jdbc.update("INSERT INTO TagUsers(user_id,tag_id) VALUES (?,?)", "u6", tagId);

        // We check that we have exactly 2 rows
        Integer count2 = jdbc.queryForObject(
                "SELECT COUNT(*) FROM TagUsers WHERE user_id=? AND tag_id=?",
                Integer.class, "u6", tagId
        );
        assertEquals(2, count2, "There should be 2 rows because the user/tag pair is not unique");

        // We add an entry to the recruitment history to be able to test TagOffer
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) " +
                        "VALUES (?,?,?,?,?,?,?)",
                "u6","Dev","Z",Date.valueOf(LocalDate.now()),"toApply",null,0
        );
        Integer hid = jdbc.queryForObject(
                "SELECT history_id FROM RecruitmentHistory WHERE user_id='u6'", Integer.class
        );

        // Correct insertion into TagOffer
        jdbc.update("INSERT INTO TagOffer(offer_id,tag_id) VALUES (?,?)", hid, tagId);

        // Inserting with non-existent history_id MUST cause FK violation
        assertThrows(Exception.class, () ->
                jdbc.update("INSERT INTO TagOffer(offer_id,tag_id) VALUES (?,?)", 999, tagId)
        );
    }
}
