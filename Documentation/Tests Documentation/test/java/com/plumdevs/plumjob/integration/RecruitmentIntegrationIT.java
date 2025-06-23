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

        // clear all related tables in leaf to root order
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

        // recreate the default list of statuses needed in tests
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
    @DisplayName("sp_showUserHistory edge‑cases") // THIS TEST HAS DETECTED AN ERROR
    void spShowUserHistoryEdgeCases() {
        // no entries → empty list
        List<?> empty = jdbc.queryForList("CALL sp_showUserHistory(?,?)", "none", false);
        assertTrue(empty.isEmpty(), "There should be 0 lines when there is no history");

        // mix ended=0 and ended=1
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

        // onlyActive=false → only the one with ended=0
        assertEquals(1,
                jdbc.queryForList("CALL sp_showUserHistory(?,?)","r1", false).size(),
                "It should return 1 row with ended=0"
        );
        // onlyActive=true → only the one with ended=1
        assertEquals(1,
                jdbc.queryForList("CALL sp_showUserHistory(?,?)","r1", true).size(),
                "It should return 1 row with ended=1"
        );

        // null as onlyActive → we don't filter after ended, so 2 rows
        List<?> both = jdbc.queryForList("CALL sp_showUserHistory(?,?)","r1", (Object) null);
        assertEquals(2,
                both.size(),
                "null as flag should return all rows"
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

        // change to ending state
        jdbc.update("CALL sp_updateStatus(?,?)", hid, "accepted the offer");
        assertEquals(1,
                jdbc.queryForObject("SELECT ended FROM RecruitmentHistory WHERE history_id=?", Integer.class, hid),
                "ended should be set to 1");
        assertEquals(1,
                jdbc.queryForObject("SELECT COUNT(*) FROM RecruitmentStatusHistory WHERE recruitment_history_id=?", Integer.class, hid),
                "Status history should have 1 entry");

        // change to a non-terminating state
        jdbc.update("CALL sp_updateStatus(?,?)", hid, "in progress");
        assertEquals(0,
                jdbc.queryForObject("SELECT ended FROM RecruitmentHistory WHERE history_id=?", Integer.class, hid),
                "ended should be set to 0");
        assertEquals(2,
                jdbc.queryForObject("SELECT COUNT(*) FROM RecruitmentStatusHistory WHERE recruitment_history_id=?", Integer.class, hid),
                "Status history should have 2 entries");
    }

    @Test
    @DisplayName("Function get_accepted covers only stage='10'")
    void functionGetAcceptedEdge() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                "r3","p",true);
        // three entries: two with stage='10', one other
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
        assertEquals(2, cnt, "get_accepted() \n" + "it should only count stage entries='10'");
    }

}
