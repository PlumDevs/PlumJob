package com.plumdevs.plumjob.unit.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Repository
public class ArticleDao {
    private final JdbcTemplate jdbc;
    public ArticleDao(JdbcTemplate jdbc) { this.jdbc = jdbc; }
    public int createArticle(String name, Date creationDate) {
        return jdbc.update("INSERT INTO Article(article_name,creation_date) VALUES (?,?)", name, creationDate);
    }
}