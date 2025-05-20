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
        // 1) Wyłączamy FK
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        // 2) Czyścimy WSZYSTKIE tabele w kolejności od najbardziej zależnych:
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
        // 3) Włączamy FK
        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");

    }

    @Test
    @DisplayName("TagCodes, TagUsers, TagOffer CRUD + FK")
    void tagsCrud() {
        // 1) Dodajemy kod tagu
        jdbc.update("INSERT INTO TagCodes(tag_name) VALUES (?)", "java");
        Integer tagId = jdbc.queryForObject(
                "SELECT tag_id FROM TagCodes WHERE tag_name='java' LIMIT 1", Integer.class
        );

        // 2) Dodajemy użytkownika
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                "u6","p",true);

        // 3) Wstawiamy dwukrotnie tę samą parę user/tag (ponieważ schemat nie wymusza unikalności)
        jdbc.update("INSERT INTO TagUsers(user_id,tag_id) VALUES (?,?)", "u6", tagId);
        jdbc.update("INSERT INTO TagUsers(user_id,tag_id) VALUES (?,?)", "u6", tagId);

        // 4) Sprawdzamy, że mamy dokładnie 2 wiersze
        Integer count2 = jdbc.queryForObject(
                "SELECT COUNT(*) FROM TagUsers WHERE user_id=? AND tag_id=?",
                Integer.class, "u6", tagId
        );
        assertEquals(2, count2, "Powinny istnieć 2 wiersze, bo brak unikalności pary user/tag");

        // 5) Dodajemy wpis do historii rekrutacji, żeby móc przetestować TagOffer
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) " +
                        "VALUES (?,?,?,?,?,?,?)",
                "u6","Dev","Z",Date.valueOf(LocalDate.now()),"toApply",null,0
        );
        Integer hid = jdbc.queryForObject(
                "SELECT history_id FROM RecruitmentHistory WHERE user_id='u6'", Integer.class
        );

        // 6) Poprawne wstawienie do TagOffer
        jdbc.update("INSERT INTO TagOffer(offer_id,tag_id) VALUES (?,?)", hid, tagId);

        // 7) Wstawienie z nieistniejącym history_id MUSI rzucić FK violation
        assertThrows(Exception.class, () ->
                jdbc.update("INSERT INTO TagOffer(offer_id,tag_id) VALUES (?,?)", 999, tagId)
        );
    }
}
