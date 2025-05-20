package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.TagOfferDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TagOfferDaoTest {

    @Mock
    JdbcTemplate jdbc;

    @InjectMocks
    TagOfferDao dao;

    @Test
    @DisplayName("assignOfferTag returns count on success")
    void assignOfferTagReturnsCount() {
        String sql = "INSERT INTO TagOffer(offer_id,tag_id) VALUES (?,?)";
        when(jdbc.update(eq(sql), eq(1), eq(2))).thenReturn(1);

        int result = dao.assignOfferTag(1, 2);

        assertThat(result).isEqualTo(1);
        verify(jdbc).update(sql, 1, 2);
    }

    @Test
    @DisplayName("assignOfferTag throws on FK violation")
    void assignOfferTagThrows() {
        when(jdbc.update(anyString(), anyInt(), anyInt()))
                .thenThrow(new DataIntegrityViolationException("fk"));

        assertThatThrownBy(() -> dao.assignOfferTag(1, 1))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("assignOfferTag throws on duplicate")
    void assignOfferTagDuplicateThrows() {
        when(jdbc.update(anyString(), anyInt(), anyInt()))
                .thenThrow(new DuplicateKeyException("dup"));

        assertThatThrownBy(() -> dao.assignOfferTag(1, 1))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
