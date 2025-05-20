package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.TagCodesDao;
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
class TagCodesDaoTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks TagCodesDao dao;

    @Test
    @DisplayName("createTag returns id")
    void createTagReturnsId() {
        when(jdbc.update("INSERT INTO TagCodes(tag_name) VALUES (?)","t")).thenReturn(1);
        assertThat(dao.createTag("t")).isEqualTo(1);
    }

    @Test
    @DisplayName("duplicateTag throws DuplicateKeyException")
    void duplicateTagThrows() {
        when(jdbc.update(anyString(), eq("dup"))).thenThrow(new DuplicateKeyException("dup"));
        assertThatThrownBy(() -> dao.createTag("dup")).isInstanceOf(DuplicateKeyException.class);
    }
}
