package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.RecruitmentProceduresDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecruitmentProceduresDaoTest {

    @Mock
    private JdbcTemplate jdbc;

    @InjectMocks
    private RecruitmentProceduresDao dao;

    @Test
    void updateStatus_callsStoredProcedure() {
        when(jdbc.update("CALL sp_updateStatus(?,?)", 123, "newStage"))
                .thenReturn(1);

        dao.updateStatus(123, "newStage");

        verify(jdbc).update("CALL sp_updateStatus(?,?)", 123, "newStage");
    }

    @Test
    void getUserStatusChanges_returnsStoredProcedureResult() {
        List<Map<String, Object>> fake = new ArrayList<>();
        when(jdbc.queryForList(
                "CALL sp_getUserStatusChanges(?,?,?)",
                "user1",
                Date.valueOf(LocalDate.of(2025, 1, 1)),
                Date.valueOf(LocalDate.of(2025, 12, 31))
        )).thenReturn(fake);

        List<Map<String, Object>> result = dao.getUserStatusChanges(
                "user1",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
        );

        assertThat(result).isSameAs(fake);
    }

    @Test
    void deleteRecruitmentRecord_invokesStoredProcedure() {
        when(jdbc.update("CALL sp_DeleteRecruitmentRecord(?)", 55))
                .thenReturn(1);

        dao.deleteRecruitmentRecord(55);

        verify(jdbc).update("CALL sp_DeleteRecruitmentRecord(?)", 55);
    }

    @Test
    void addNewRecruitment_invokesStoredProcedureWithAllParams() {
        LocalDate startDate = LocalDate.of(2025, 5, 20);
        when(jdbc.update(
                "CALL sp_addNewRecruitment(?,?,?,?,?,?,?)",
                "userX",
                "Dev",
                "Acme",
                Date.valueOf(startDate),
                "toApply",
                "desc",
                false
        )).thenReturn(1);

        dao.addNewRecruitment(
                "userX",
                "Dev",
                "Acme",
                startDate,
                "toApply",
                "desc",
                false
        );

        verify(jdbc).update(
                "CALL sp_addNewRecruitment(?,?,?,?,?,?,?)",
                "userX",
                "Dev",
                "Acme",
                Date.valueOf(startDate),
                "toApply",
                "desc",
                false
        );
    }
}
