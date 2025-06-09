package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public class RecruitmentProceduresDao {
    private final JdbcTemplate jdbc;
    public RecruitmentProceduresDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    public void updateStatus(int historyId, String newStage) {
        jdbc.update("CALL sp_updateStatus(?,?)", historyId, newStage);
    }

    public List<Map<String,Object>> getUserStatusChanges(String userId, LocalDate start, LocalDate end) {
        return jdbc.queryForList(
                "CALL sp_getUserStatusChanges(?,?,?)",
                userId,
                Date.valueOf(start),
                Date.valueOf(end)
        );
    }

    public void deleteRecruitmentRecord(int historyId) {
        jdbc.update("CALL sp_DeleteRecruitmentRecord(?)", historyId);
    }

    public void addNewRecruitment(
            String userId,
            String position,
            String company,
            LocalDate startDate,
            String stage,
            String description,
            boolean ended
    ) {
        jdbc.update(
                "CALL sp_addNewRecruitment(?,?,?,?,?,?,?)",
                userId,
                position,
                company,
                Date.valueOf(startDate),
                stage,
                description,
                ended
        );
    }
}
