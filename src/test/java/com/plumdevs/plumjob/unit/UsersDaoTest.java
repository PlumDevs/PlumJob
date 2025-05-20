package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.UsersDao;
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
class UsersDaoTest {
    @Mock JdbcTemplate jdbc;
    @InjectMocks UsersDao dao;

    @Test
    @DisplayName("createUser returns rows affected")
    void createUserReturnsCount() {
        when(jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)","u1","pass",true))
                .thenReturn(1);
        assertThat(dao.createUser("u1","pass",true)).isEqualTo(1);
    }

    @Test
    @DisplayName("duplicate username throws DuplicateKeyException")
    void duplicateUsernameThrows() {
        when(jdbc.update(anyString(), eq("u1"), any(), any()))
                .thenThrow(new DuplicateKeyException("duplicate"));
        assertThatThrownBy(() -> dao.createUser("u1","p",true))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
