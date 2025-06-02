package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.AcceptanceDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AcceptanceDaoTest {

    @Mock
    JdbcTemplate jdbc;

    @InjectMocks
    AcceptanceDao dao;

    @Test
    void getAcceptedCount_queriesFunction_andReturnsValue() {
        when(jdbc.queryForObject("SELECT get_accepted()", Integer.class)).thenReturn(42);

        int cnt = dao.getAcceptedCount();

        assertThat(cnt).isEqualTo(42);
        verify(jdbc).queryForObject("SELECT get_accepted()", Integer.class);
    }

    @Test
    void getAcceptedCount_throws_whenJdbcThrows() {
        when(jdbc.queryForObject(anyString(), eq(Integer.class)))
                .thenThrow(new IllegalStateException("oops"));

        assertThatThrownBy(() -> dao.getAcceptedCount())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("oops");
    }
}