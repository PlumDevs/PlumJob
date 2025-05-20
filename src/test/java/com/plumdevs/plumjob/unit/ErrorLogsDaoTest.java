package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.ErrorLogsDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ErrorLogsDaoTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks ErrorLogsDao dao;

    @Test
    @DisplayName("logError records entry")
    void logErrorRecordsEntry() {
        // Use matchers for both parameters: error_code and error_date
        when(jdbc.update(anyString(), anyInt(), any()))
                .thenReturn(1);

        int result = dao.logError(123);

        assertThat(result).isEqualTo(1);
        verify(jdbc).update(eq("INSERT INTO ErrorLogs(error_code,error_date) VALUES (?,?)"), eq(123), any());
    }
}