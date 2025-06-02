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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationDatabaseTestsIT extends IntegrationTestBase {

    @Autowired
    protected JdbcTemplate jdbc;

    @BeforeEach
    void resetDatabase() {
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        String[] tables = {
                "RecruitmentStatusHistory", "TagOffer", "TagUsers", "RecruitmentHistory",
                "authorities", "UserInfo", "Ads", "TagCodes", "users",
                "Template", "Article", "ErrorLogs"
        };
        for (String tbl : tables) {
            jdbc.execute("DELETE FROM " + tbl);
        }
        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    @DisplayName("Schema: all tables exist")
    void shouldHaveAllTables() {
        List<String> tables = jdbc.queryForList(
                "SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = DATABASE()",
                String.class
        );
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
    @DisplayName("Users & Authorities CRUD and FK checks")
    void usersAndAuthoritiesCrud() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "u1", "pass", true);
        Integer cnt = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE username='u1'", Integer.class);
        assertEquals(1, cnt);

        jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)", "u1", "ROLE");
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)", "no", "ROLE")
        );

        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("DELETE FROM users WHERE username='u1'")
        );
    }

    @Test
    @DisplayName("UserInfo minimal insert and full insert")
    void userInfoCrud() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "u2", "p", true);
        jdbc.update("INSERT INTO UserInfo(username,user_legalname,user_lastname) VALUES (?,?,?)", "u2", "J", "K");
        Map<String, Object> row1 = jdbc.queryForMap(
                "SELECT user_legalname,user_lastname,user_email,account_creation_date,is_active FROM UserInfo WHERE username='u2'"
        );
        assertEquals("J", row1.get("user_legalname"));
        assertNull(row1.get("user_email"));

        jdbc.update("DELETE FROM UserInfo WHERE username='u2'");
        jdbc.update(
                "INSERT INTO UserInfo(username,user_legalname,user_lastname,user_email,account_creation_date,is_active) VALUES (?,?,?,?,?,?)",
                "u2","L","M","email@example.com", Date.valueOf(LocalDate.now()), true
        );
        Map<String,Object> row2 = jdbc.queryForMap(
                "SELECT user_legalname,user_lastname,user_email,is_active FROM UserInfo WHERE username='u2'"
        );
        assertEquals("L", row2.get("user_legalname"));
        assertEquals("email@example.com", row2.get("user_email"));
        assertTrue((Boolean)row2.get("is_active"));
    }

    @Test
    @DisplayName("Ads edge-case: ad_end before ad_start")
    void adsDateEdgeCase() {
        jdbc.update(
                "INSERT INTO Ads(who_created,ad_start,ad_end,offer_link) VALUES (?,?,?,?)",
                "u3", Date.valueOf("2025-06-10"), Date.valueOf("2025-06-01"), "link"
        );
        List<Date[]> dates = jdbc.query(
                "SELECT ad_start,ad_end FROM Ads WHERE who_created='u3'",
                (rs,i)-> new Date[]{rs.getDate(1),rs.getDate(2)}
        );
        assertTrue(dates.get(0)[1].before(dates.get(0)[0]));
    }

    @Test
    @DisplayName("RecruitmentStatus CRUD")
    void recruitmentStatusCrud() {
        jdbc.update("INSERT INTO RecruitmentStatus(status_name) VALUES (?)", "newStatus");
        Integer id = jdbc.queryForObject(
                "SELECT status_id FROM RecruitmentStatus WHERE status_name='newStatus'", Integer.class
        );
        assertNotNull(id);
        jdbc.update("DELETE FROM RecruitmentStatus WHERE status_id=?", id);
    }

    @Test
    @DisplayName("RecruitmentHistory CRUD, FK, sp_showUserHistory")
    void recruitmentHistoryAndSp() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "u4", "p", true);
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                "u4","Dev","X",Date.valueOf("2025-05-01"),"applied",null,0
        );
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                "u4","Dev","X",Date.valueOf("2025-05-01"),"accepted",null,1
        );
        List<Map<String,Object>> active = jdbc.queryForList("CALL sp_showUserHistory(?,?)","u4",false);
        List<Map<String,Object>> ended = jdbc.queryForList("CALL sp_showUserHistory(?,?)","u4",true);
        assertEquals(1, active.size());
        assertEquals(1, ended.size());
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update(
                        "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                        "no","p","c",Date.valueOf("2025-05-01"),"toApply",null,0
                )
        );
    }

    @Test
    @DisplayName("sp_updateStatus effect")
    void spUpdateStatusAndHistory() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "u5", "p", true);
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                "u5","Dev","Y",Date.valueOf("2025-05-01"),"toApply",null,0
        );
        Integer hid = jdbc.queryForObject(
                "SELECT history_id FROM RecruitmentHistory WHERE user_id='u5'", Integer.class
        );
        jdbc.update("CALL sp_updateStatus(?,?)", hid, "accepted the offer");
        Integer ended = jdbc.queryForObject(
                "SELECT ended FROM RecruitmentHistory WHERE history_id=?", Integer.class, hid
        );
        assertEquals(1, ended);
        Integer histCount = jdbc.queryForObject(
                "SELECT COUNT(*) FROM RecruitmentStatusHistory WHERE recruitment_history_id=?", Integer.class, hid
        );
        assertEquals(1, histCount);
        jdbc.update("DELETE FROM RecruitmentStatusHistory WHERE recruitment_history_id=?", hid);
    }

    @Test
    @DisplayName("TagCodes, TagUsers, TagOffer CRUD + FK")
    void tagsCrud() {
        jdbc.update("INSERT INTO TagCodes(tag_name) VALUES (?)", "java");
        Integer tagId = jdbc.queryForObject(
                "SELECT tag_id FROM TagCodes WHERE tag_name='java' LIMIT 1", Integer.class
        );
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "u6", "p", true);
        jdbc.update("INSERT INTO TagUsers(user_id,tag_id) VALUES (?,?)", "u6", tagId);
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("INSERT INTO TagUsers(user_id,tag_id) VALUES (?,?)", "u6",999)
        );
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                "u6","Dev","Z",Date.valueOf("2025-05-01"),"toApply",null,0
        );
        Integer hid = jdbc.queryForObject(
                "SELECT history_id FROM RecruitmentHistory WHERE user_id='u6'", Integer.class
        );
        jdbc.update("INSERT INTO TagOffer(offer_id,tag_id) VALUES (?,?)", hid, tagId);
        assertThrows(DataIntegrityViolationException.class, () ->
                jdbc.update("INSERT INTO TagOffer(offer_id,tag_id) VALUES (?,?)",999,tagId)
        );
    }

    @Test
    @DisplayName("Template, Article, ErrorLogs CRUD")
    void miscCrud() {
        jdbc.update("INSERT INTO Template(template_name,template_desc) VALUES (?,?)","T1","D");
        assertEquals(1, jdbc.queryForObject(
                "SELECT COUNT(*) FROM Template WHERE template_name='T1'", Integer.class
        ));
        jdbc.update("DELETE FROM Template WHERE template_name='T1'");

        jdbc.update("INSERT INTO Article(article_name,creation_date) VALUES (?,?)","A1",Date.valueOf("2025-05-01"));
        assertEquals(1, jdbc.queryForObject(
                "SELECT COUNT(*) FROM Article WHERE article_name='A1'", Integer.class
        ));
        jdbc.update("DELETE FROM Article WHERE article_name='A1'");

        jdbc.update("INSERT INTO ErrorLogs(error_code,error_date) VALUES (?,?)",123,Date.valueOf("2025-05-01"));
        assertEquals(1, jdbc.queryForObject(
                "SELECT COUNT(*) FROM ErrorLogs WHERE error_code=123", Integer.class
        ));
        jdbc.update("DELETE FROM ErrorLogs WHERE error_code=123");
    }

    @Test
    @DisplayName("Function get_accepted counts correct rows")
    void functionGetAccepted() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)","u7","p",true);
        jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)","u7","ROLE");
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                "u7","D","C",Date.valueOf("2025-05-01"),"10",null,1
        );
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                "u7","D","C",Date.valueOf("2025-05-02"),"10",null,1
        );
        jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                "u7","D","C",Date.valueOf("2025-05-03"),"5",null,0
        );
        Integer cnt = jdbc.queryForObject("SELECT get_accepted()", Integer.class);
        assertEquals(2, cnt);
    }
}
