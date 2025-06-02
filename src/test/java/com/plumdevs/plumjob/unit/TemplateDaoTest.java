package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.TemplateDao;
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
class TemplateDaoTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks TemplateDao dao;

    @Test
    @DisplayName("createTemplate returns id")
    void createTemplateReturnsId() {
        when(jdbc.update("INSERT INTO Template(template_name,template_desc) VALUES (?,?)","T","D")).thenReturn(1);
        assertThat(dao.createTemplate("T","D")).isEqualTo(1);
    }
}
