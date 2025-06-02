package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.ArticleDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleDaoTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks ArticleDao dao;

    @Test
    @DisplayName("createArticle returns id")
    void createArticleReturnsId() {
        when(jdbc.update("INSERT INTO Article(article_name,creation_date) VALUES (?,?)","A",Date.valueOf(LocalDate.now())))
                .thenReturn(1);
        assertThat(dao.createArticle("A", Date.valueOf(LocalDate.now()))).isEqualTo(1);
    }
}
