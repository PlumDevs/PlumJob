package com.plumdevs.plumjob.integration;

import com.plumdevs.plumjob.base.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecruitmentIntegrationIT extends IntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void cleanUp() {
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        // — wyczyść wszystkie powiązane tabele w porządku od liści do korzeni —
        jdbc.execute("DELETE FROM RecruitmentStatusHistory");
        jdbc.execute("DELETE FROM TagOffer");
        jdbc.execute("DELETE FROM TagUsers");
        jdbc.execute("DELETE FROM RecruitmentHistory");
        jdbc.execute("DELETE FROM authorities");
        jdbc.execute("DELETE FROM UserInfo");
        jdbc.execute("DELETE FROM Ads");
        jdbc.execute("DELETE FROM RecruitmentStatus");
        jdbc.execute("DELETE FROM TagCodes");
        jdbc.execute("DELETE FROM Template");
        jdbc.execute("DELETE FROM Article");
        jdbc.execute("DELETE FROM ErrorLogs");
        jdbc.execute("DELETE FROM users");

        // — odtwórz domyślną listę statusów potrzebnych w testach —
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (1, 'toApply')");
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (2, 'applied')");
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (3, 'onlineAssesment')");
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (4, 'afterOA')");
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (5, 'interviewScheduled')");
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (6, 'afterInterview')");
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (7, 'jobOffer')");
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (8, 'rejected')");
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (9, 'ghosted')");
        jdbc.update("INSERT INTO RecruitmentStatus(status_id,status_name) VALUES (10, 'accepted')");

        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }


    @Test
    @DisplayName("sp_showUserHistory edge‑cases") // TEN TEST WYKRYL BLAD
    void spShowUserHistoryEdgeCases() {
        // 1) brak wpisów → pusta lista
        List<?> empty = jdbc.queryForList("CALL sp_showUserHistory(?,?)", "none", false);
        assertTrue(empty.isEmpty(), "Powinno być 0 wierszy gdy nie ma historii");

        // 2) mix ended=0 i ended=1
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                "r1","p",true);
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) " +
                        "VALUES (?,?,?,?,?,?,?)",
                "r1","P","C",Date.valueOf("2025-05-01"),"s","d",0);
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) " +
                        "VALUES (?,?,?,?,?,?,?)",
                "r1","P","C",Date.valueOf("2025-05-01"),"s","d",1);

        // onlyActive=false → tylko ten z ended=0
        assertEquals(1,
                jdbc.queryForList("CALL sp_showUserHistory(?,?)","r1", false).size(),
                "Powinien zwrócić 1 wiersz z ended=0"
        );
        // onlyActive=true → tylko ten z ended=1
        assertEquals(1,
                jdbc.queryForList("CALL sp_showUserHistory(?,?)","r1", true).size(),
                "Powinien zwrócić 1 wiersz z ended=1"
        );

        // 3) null jako onlyActive → nie filtrujemy po ended, więc 2 wiersze
        List<?> both = jdbc.queryForList("CALL sp_showUserHistory(?,?)","r1", (Object) null);
        assertEquals(2,
                both.size(),
                "null jako flag powinien zwrócić wszystkie wiersze"
        );
    }

    @Test
    @DisplayName("sp_updateStatus and history tracking")
    void spUpdateStatusAndHistory() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                "r2","p",true);
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) " +
                        "VALUES (?,?,?,?,?,?,?)",
                "r2","X","Y",Date.valueOf("2025-05-01"),"toApply","d",0);

        Integer hid = jdbc.queryForObject(
                "SELECT history_id FROM RecruitmentHistory WHERE user_id='r2'", Integer.class);

        // 1) zmiana na stan kończący
        jdbc.update("CALL sp_updateStatus(?,?)", hid, "accepted the offer");
        assertEquals(1,
                jdbc.queryForObject("SELECT ended FROM RecruitmentHistory WHERE history_id=?", Integer.class, hid),
                "ended powinno być ustawione na 1");
        assertEquals(1,
                jdbc.queryForObject("SELECT COUNT(*) FROM RecruitmentStatusHistory WHERE recruitment_history_id=?", Integer.class, hid),
                "Historia statusów powinna mieć 1 wpis");

        // 2) zmiana na stan nie‑kończący
        jdbc.update("CALL sp_updateStatus(?,?)", hid, "in progress");
        assertEquals(0,
                jdbc.queryForObject("SELECT ended FROM RecruitmentHistory WHERE history_id=?", Integer.class, hid),
                "ended powinno być ustawione na 0");
        assertEquals(2,
                jdbc.queryForObject("SELECT COUNT(*) FROM RecruitmentStatusHistory WHERE recruitment_history_id=?", Integer.class, hid),
                "Historia statusów powinna mieć 2 wpisy");
    }

    @Test
    @DisplayName("Function get_accepted covers only stage='10'")
    void functionGetAcceptedEdge() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                "r3","p",true);
        // trzy wpisy: dwa ze stage='10', jeden inny
        jdbc.update("INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) " +
                        "VALUES (?,?,?,?,?,?,?)",
                "r3","A","B",Date.valueOf("2025-05-01"),"10",null,1);
        jdbc.update("INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) " +
                        "VALUES (?,?,?,?,?,?,?)",
                "r3","A","B",Date.valueOf("2025-05-02"),"10",null,0);
        jdbc.update("INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) " +
                        "VALUES (?,?,?,?,?,?,?)",
                "r3","A","B",Date.valueOf("2025-05-03"),"5", null,1);

        Integer cnt = jdbc.queryForObject("SELECT get_accepted()", Integer.class);
        assertEquals(2, cnt, "get_accepted() powinno zliczać tylko wpisy ze stage='10'");
    }

}
