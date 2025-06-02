package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class TagCodesDao {
    private final JdbcTemplate jdbc;
    public TagCodesDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int createTag(String name) {
        return jdbc.update("INSERT INTO TagCodes(tag_name) VALUES (?)", name);
    }
}