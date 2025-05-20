package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.RecruitmentStatusDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitmentStatusDaoTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks RecruitmentStatusDao dao;

    @Test
    @DisplayName("createStatus returns id")
    void createStatusReturnsId() {
        when(jdbc.update("INSERT INTO RecruitmentStatus(status_name) VALUES (?)", "newStatus"))
                .thenReturn(1);
        assertThat(dao.createStatus("newStatus")).isEqualTo(1);
        verify(jdbc).update("INSERT INTO RecruitmentStatus(status_name) VALUES (?)", "newStatus");
    }

    @Test
    @DisplayName("duplicate status throws DuplicateKeyException")
    void duplicateStatusThrows() {
        when(jdbc.update(anyString(), eq("dupStatus")))
                .thenThrow(new DuplicateKeyException("dup"));
        assertThatThrownBy(() -> dao.createStatus("dupStatus"))
                .isInstanceOf(DuplicateKeyException.class);
    }
}