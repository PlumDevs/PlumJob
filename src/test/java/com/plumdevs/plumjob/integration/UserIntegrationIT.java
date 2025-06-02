package com.plumdevs.plumjob.integration;

import com.plumdevs.plumjob.base.IntegrationTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserIntegrationIT extends IntegrationTestBase {

    @Autowired
    private JdbcTemplate jdbc;

    @BeforeEach
    void cleanUsersAndAuthorities() {
        // wyłączamy chwilowo FK, żeby móc skasować wszystko bez błędów
        jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("DELETE FROM authorities");
        jdbc.execute("DELETE FROM users");
        jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    @DisplayName("Unique username constraint")
    void uniqueUsername() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                "dup", "pass1", true);
        assertThrows(DuplicateKeyException.class, () ->
                        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                                "dup", "pass2", false),
                "Powtórne wstawienie tego samego username powinno rzucić DuplicateKeyException"
        );
    }

    @Test
    @DisplayName("Unique authority per user enforced by ix_auth_username")
    void uniqueAuthorityPerUser() {
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                "u2", "p", true);
        jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)",
                "u2", "ROLE_A");
        assertThrows(DuplicateKeyException.class, () ->
                        jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)",
                                "u2", "ROLE_A"),
                "Powtórne wstawienie tej samej pary (username,authority) powinno rzucić DuplicateKeyException"
        );
    }

    @Test
    @DisplayName("Users & Authorities basic CRUD and FK guard")
    void usersAndAuthoritiesCrud() {
        // wstawiamy raz
        jdbc.update("INSERT INTO users(username,password,enabled) VALUES (?,?,?)",
                "u1", "p1", true);
        Integer cnt = jdbc.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = 'u1'", Integer.class
        );
        assertEquals(1, cnt);

        // nie można wstawić authorities dla nieistniejącego usera
        assertThrows(Exception.class, () ->
                jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)",
                        "no_such", "ROLE_X")
        );

        // nie można usunąć usera, dopóki istnieją dla niego authorities
        jdbc.update("INSERT INTO authorities(username,authority) VALUES (?,?)",
                "u1", "ROLE_USER");
        assertThrows(Exception.class, () ->
                jdbc.update("DELETE FROM users WHERE username = 'u1'")
        );
    }
}
