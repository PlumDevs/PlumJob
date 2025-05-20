package com.plumdevs.plumjob.unit;

import com.plumdevs.plumjob.unit.dao.AuthoritiesDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthoritiesDaoTest {

    @Mock
    JdbcTemplate jdbc;

    @InjectMocks
    AuthoritiesDao dao;

    private final String SQL = "INSERT INTO authorities(username,authority) VALUES (?,?)";

    @Test
    @DisplayName("addAuthority returns count")
    void addAuthorityReturnsCount() {
        when(jdbc.update(eq(SQL), eq("u1"), eq("ROLE")))
                .thenReturn(1);

        int result = dao.addAuthority("u1", "ROLE");

        assertThat(result).isEqualTo(1);
        verify(jdbc).update(SQL, "u1", "ROLE");
    }

    @Test
    @DisplayName("duplicate authority throws DuplicateKeyException")
    void duplicateAuthorityThrows() {
        when(jdbc.update(eq(SQL), eq("u1"), eq("ROLE")))
                .thenThrow(new DuplicateKeyException("dup"));

        assertThatThrownBy(() -> dao.addAuthority("u1", "ROLE"))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("addAuthority on missing user throws DataIntegrityViolationException")
    void missingUserThrows() {
        when(jdbc.update(eq(SQL), eq("noUser"), eq("ROLE")))
                .thenThrow(new DataIntegrityViolationException("fk"));

        assertThatThrownBy(() -> dao.addAuthority("noUser", "ROLE"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
