package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.AdsDao;
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
class AdsDaoTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks AdsDao dao;

    @Test
    @DisplayName("createAd returns count")
    void createAdReturns() {
        when(jdbc.update("INSERT INTO Ads(who_created,ad_start,ad_end,offer_link) VALUES (?,?,?,?)","u1", Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()),"link"))
                .thenReturn(1);
        assertThat(dao.createAd("u1",Date.valueOf(LocalDate.now()),Date.valueOf(LocalDate.now()),"link")).isEqualTo(1);
    }

    @Test
    @DisplayName("findByUser returns list")
    void findByUserReturns() {
        List<Map<String,Object>> fake = List.of(Map.of("ad_id",1));
        when(jdbc.queryForList(anyString(), eq("u1"))).thenReturn(fake);
        assertThat(dao.findByUser("u1")).isSameAs(fake);
    }

    @Test
    @DisplayName("deleteUnexpected throws on FK violation")
    void deleteThrowsOnFk() {
        when(jdbc.update(anyString(), eq("u1")))
                .thenThrow(new DataIntegrityViolationException("fk"));
        assertThatThrownBy(() -> dao.deleteByUser("u1"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
