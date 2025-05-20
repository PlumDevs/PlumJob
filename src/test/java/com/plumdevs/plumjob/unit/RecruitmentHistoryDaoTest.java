package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.RecruitmentHistoryDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitmentHistoryDaoTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks RecruitmentHistoryDao dao;

    @Test
    @DisplayName("showUserHistory returns data")
    void showUserHistoryReturnsData() {
        List<Map<String,Object>> fake = List.of(Map.of("k","v"));
        when(jdbc.queryForList("CALL sp_showUserHistory(?,?)","u",true)).thenReturn(fake);
        var res = dao.showUserHistory("u", true);
        assertThat(res).isSameAs(fake);
    }

    @Test
    @DisplayName("insertHistory throws on FK violation")
    void insertHistoryThrows() {
        when(jdbc.update(anyString(), any(), any(), any(), any(), any(), any(), any()))
                .thenThrow(new DataIntegrityViolationException("fk"));
        assertThatThrownBy(() -> dao.insertHistory("u","pos","c",Date.valueOf(LocalDate.now()),"st","d",0))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
