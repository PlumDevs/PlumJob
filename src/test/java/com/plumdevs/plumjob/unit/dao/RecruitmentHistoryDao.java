package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class RecruitmentHistoryDao {
    private final JdbcTemplate jdbc;
    public RecruitmentHistoryDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public List<Map<String, Object>> showUserHistory(String userId, boolean onlyActive) {
        return jdbc.queryForList("CALL sp_showUserHistory(?,?)", userId, onlyActive);
    }
    public int insertHistory(String userId, String pos, String comp, Date start, String stage, String desc, int ended) {
        return jdbc.update(
                "INSERT INTO RecruitmentHistory(user_id,position,company,user_start_date,stage,description,ended) VALUES (?,?,?,?,?,?,?)",
                userId, pos, comp, start, stage, desc, ended);
    }
}
