package com.plumdevs.plumjob.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CounterRepository {

    private final JdbcTemplate jdbcTemplate;

    public CounterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int countAcceptedOffers() {
        String sql = "SELECT COUNT(*) FROM plum.RecruitmentHistory WHERE stage = 'accepted the offer'";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
