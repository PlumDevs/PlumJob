package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Repository
public class ErrorLogsDao {
    private final JdbcTemplate jdbc;
    public ErrorLogsDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int logError(int code) {
        return jdbc.update("INSERT INTO ErrorLogs(error_code,error_date) VALUES (?,?)", code, new Date(System.currentTimeMillis()));
    }
}