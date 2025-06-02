package com.plumdevs.plumjob.integration;

import com.plumdevs.plumjob.base.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AdditionalDatabaseTestsIT extends IntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private PlatformTransactionManager txManager;

    @BeforeEach
    void cleanAll() {
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        String[] tables = {"RecruitmentStatusHistory","TagOffer","TagUsers","RecruitmentHistory",
                "authorities","UserInfo","Ads","TagCodes","users",
                "Template","Article","ErrorLogs"};
        for (String tbl : tables) {
            jdbc.execute("DELETE FROM " + tbl);
        }
        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    @DisplayName("Unique username constraint")
    void uniqueUsername() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "uUnique", "p", true);
        assertThrows(DuplicateKeyException.class, () ->
                jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "uUnique", "p2", false)
        );
    }

    @Test
    @DisplayName("Unique authority per user enforced by ix_auth_username")
    void uniqueAuthorityIndex() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "uAuth", "p", true);
        jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)", "uAuth", "ROLE_A");
        assertThrows(DuplicateKeyException.class, () ->
                jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)", "uAuth", "ROLE_A")
        );
    }

    @Test
    @DisplayName("Ads.is_active default is NULL when omitted")
    void adsDefaultValue() {
        jdbc.update("INSERT INTO Ads(who_created,ad_start,ad_end,offer_link) VALUES (?,?,?,?)",
                "uDef", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()), "http://x"
        );
        Boolean active = jdbc.queryForObject(
                "SELECT is_active FROM Ads WHERE who_created='uDef'", Boolean.class
        );
        assertNull(active);
    }

    @Test
    @DisplayName("Delete parent without cascade throws FK violation")
    void noCascadeDelete() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "uDel", "p", true);
        jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)", "uDel", "ROLE_X");
        assertThrows(Exception.class, () ->
                jdbc.update("DELETE FROM users WHERE username='uDel'")
        );
    }

    @Test
    @DisplayName("Transaction rollback on error")
    void transactionRollback() {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("txTest");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def);
        try {
            jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "uTx", "p", true);
            // Force SQL syntax error
            jdbc.update("INSER INTO users(username) VALUES (?)", "bad");
            txManager.commit(status);
            fail("Should have thrown exception");
        } catch (Exception e) {
            txManager.rollback(status);
        }
        Integer cnt = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE username='uTx'", Integer.class);
        assertEquals(0, cnt);
    }

    @Test
    @DisplayName("Bulk insert performance check")
    void bulkInsertPerformance() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)", "uPerf", "p", true);
        int n = 200;
        for (int i = 0; i < n; i++) {
            jdbc.update(
                    "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                    "uPerf","Pos","Comp",Date.valueOf(LocalDate.now()),"applied",null,0
            );
        }
        List<Map<String, Object>> rows = jdbc.queryForList("CALL sp_showUserHistory(?,?)", "uPerf", true); // dalem true a ma by false jesli zmienimi procedure sp_showUserHistory
        assertEquals(n, rows.size());                                                                                 // chodzi o to ze zapytanie moze nie dzialac jak tego chcemy
    }

    // Further tests (migrations, DAO/JPA) can be added similarly.
}
