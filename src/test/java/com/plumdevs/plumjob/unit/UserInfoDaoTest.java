package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.UserInfoDao;
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
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInfoDaoTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks UserInfoDao dao;

    @Test
    @DisplayName("saveMinimal inserts correct SQL")
    void saveMinimalInserts() {
        String sql = "INSERT INTO UserInfo(username,user_legalname,user_lastname) VALUES (?,?,?)";
        when(jdbc.update(sql,"u1","J","K")).thenReturn(1);
        assertThat(dao.saveMinimal("u1","J","K")).isEqualTo(1);
        verify(jdbc).update(sql,"u1","J","K");
    }

    @Test
    @DisplayName("saveFull throws on FK violation")
    void saveFullThrowsOnFk() {
        String sql = "INSERT INTO UserInfo(username,user_legalname,user_lastname,user_email,account_creation_date,is_active) VALUES (?,?,?,?,?,?)";
        when(jdbc.update(eq(sql), any(),any(),any(),any(),any(),any()))
                .thenThrow(new DataIntegrityViolationException("fk"));
        assertThatThrownBy(() -> dao.saveFull("u1","L","M","e@e", Date.valueOf(LocalDate.now()),true))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("findByUsername returns data map")
    void findByUsernameReturns() {
        Map<String,Object> fake = Map.of("user_legalname","J","user_lastname","K");
        when(jdbc.queryForMap(anyString(), eq("u1"))).thenReturn(fake);
        assertThat(dao.findByUsername("u1")).isSameAs(fake);
    }
}
