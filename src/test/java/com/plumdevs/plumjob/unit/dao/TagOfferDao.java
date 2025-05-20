package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Repository
public class TagOfferDao {
    private final JdbcTemplate jdbc;
    public TagOfferDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int assignOfferTag(int offerId, int tagId) {
        return jdbc.update("INSERT INTO TagOffer(offer_id,tag_id) VALUES (?,?)", offerId, tagId);
    }
}