// src/test/java/com/plumdevs/plumjob/unit/AcceptanceDao.java
package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;

public class AcceptanceDao {
    private final JdbcTemplate jdbc;

    public AcceptanceDao(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public int getAcceptedCount() {
        return jdbc.queryForObject("SELECT get_accepted()", Integer.class);
    }
}
