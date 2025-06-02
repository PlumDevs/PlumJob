package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Repository
public class TagUsersDao {
    private final JdbcTemplate jdbc;
    public TagUsersDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int assignTag(String userId, int tagId) {
        return jdbc.update("INSERT INTO TagUsers(user_id,tag_id) VALUES (?,?)", userId, tagId);
    }
}