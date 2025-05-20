package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Repository
public class AdsDao {
    private final JdbcTemplate jdbc;
    public AdsDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int createAd(String whoCreated, Date adStart, Date adEnd, String link) {
        return jdbc.update(
                "INSERT INTO Ads(who_created,ad_start,ad_end,offer_link) VALUES (?,?,?,?)",
                whoCreated, adStart, adEnd, link);
    }
    public List<Map<String, Object>> findByUser(String username) {
        return jdbc.queryForList(
                "SELECT * FROM Ads WHERE who_created = ?",
                username);
    }
    public int deleteByUser(String username) {
        return jdbc.update("DELETE FROM Ads WHERE who_created = ?", username);
    }
}