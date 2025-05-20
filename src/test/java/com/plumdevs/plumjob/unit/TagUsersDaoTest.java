package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.TagUsersDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TagUsersDaoTest {

    @Mock
    JdbcTemplate jdbc;

    @InjectMocks
    TagUsersDao dao;

    @Test
    @DisplayName("assignTag returns rows affected")
    void assignTagReturnsCount() {
        // happy path
        when(jdbc.update(
                eq("INSERT INTO TagUsers(user_id,tag_id) VALUES (?,?)"),
                eq("u"), eq(1)))
                .thenReturn(1);

        int result = dao.assignTag("u", 1);

        assertThat(result).isEqualTo(1);
        verify(jdbc).update(
                "INSERT INTO TagUsers(user_id,tag_id) VALUES (?,?)", "u", 1
        );
    }

    @Test
    @DisplayName("assignTag throws on FK violation")
    void assignTagThrows() {
        // stubujemy dowolne wywołanie update(...) na FK-violation
        when(jdbc.update(anyString(), anyString(), anyInt()))
                .thenThrow(new DataIntegrityViolationException("fk"));

        assertThatThrownBy(() -> dao.assignTag("u", 1))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
