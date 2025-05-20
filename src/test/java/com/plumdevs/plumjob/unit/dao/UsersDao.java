package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class UsersDao {
    private final JdbcTemplate jdbc;
    public UsersDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int createUser(String username, String password, boolean enabled) {
        return jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                username, password, enabled);
    }
}