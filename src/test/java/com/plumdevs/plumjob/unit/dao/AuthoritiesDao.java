package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@Repository
public class AuthoritiesDao {
    private final JdbcTemplate jdbc;
    public AuthoritiesDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int addAuthority(String username, String authority) {
        return jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)",
                username, authority);
    }
}