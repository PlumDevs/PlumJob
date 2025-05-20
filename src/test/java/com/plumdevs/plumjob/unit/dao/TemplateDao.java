package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Repository
public class TemplateDao {
    private final JdbcTemplate jdbc;
    public TemplateDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int createTemplate(String name, String desc) {
        return jdbc.update("INSERT INTO Template(template_name,template_desc) VALUES (?,?)", name, desc);
    }
}