package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class UserInfoDao {
    private final JdbcTemplate jdbc;
    public UserInfoDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int saveMinimal(String username, String legalName, String lastName) {
        return jdbc.update(
                "INSERT INTO UserInfo(username,user_legalname,user_lastname) VALUES (?,?,?)",
                username, legalName, lastName);
    }
    public int saveFull(String username, String legalName, String lastName, String email, Date creationDate, boolean isActive) {
        return jdbc.update(
                "INSERT INTO UserInfo(username,user_legalname,user_lastname,user_email,account_creation_date,is_active) VALUES (?,?,?,?,?,?)",
                username, legalName, lastName, email, creationDate, isActive);
    }
    public Map<String, Object> findByUsername(String username) {
        return jdbc.queryForMap(
                "SELECT user_legalname,user_lastname,user_email,account_creation_date,is_active FROM UserInfo WHERE username = ?",
                username);
    }
}