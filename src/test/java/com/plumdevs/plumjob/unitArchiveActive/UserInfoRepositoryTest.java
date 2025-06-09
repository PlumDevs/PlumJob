package com.plumdevs.plumjob.unitArchiveActive;

import com.plumdevs.plumjob.repository.UserInfoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserInfoRepositoryTest {

    @Mock
    private UserInfoRepository userInfoRepository;

    @Test
    void shouldReturnTrueWhenEmailExists() {
        // Given
        String existingEmail = "test@example.com";
        when(userInfoRepository.emailExists(existingEmail)).thenReturn(1);

        // When
        Integer result = userInfoRepository.emailExists(existingEmail);

        // Then
        assertEquals(1, result);
        assertTrue(result > 0);
        verify(userInfoRepository).emailExists(existingEmail);
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        // Given
        String nonExistingEmail = "nonexistent@example.com";
        when(userInfoRepository.emailExists(nonExistingEmail)).thenReturn(0);

        // When
        Integer result = userInfoRepository.emailExists(nonExistingEmail);

        // Then
        assertEquals(0, result);
        assertFalse(result > 0);
        verify(userInfoRepository).emailExists(nonExistingEmail);
    }

    @Test
    void shouldAddUserInfoSuccessfully() {
        // Given
        String username = "testuser";
        String firstName = "John";
        String lastName = "Doe";
        String email = "john.doe@example.com";

        // When
        userInfoRepository.addUserInfo(username, firstName, lastName, email);

        // Then
        verify(userInfoRepository).addUserInfo(username, firstName, lastName, email);
    }

    @Test
    void shouldHandleNullValuesInAddUserInfo() {
        // Given
        String username = "testuser";
        String firstName = null;
        String lastName = "Doe";
        String email = "test@example.com";

        // When
        userInfoRepository.addUserInfo(username, firstName, lastName, email);

        // Then
        verify(userInfoRepository).addUserInfo(username, firstName, lastName, email);
    }

    @Test
    void shouldHandleEmptyEmailInEmailExists() {
        // Given
        String emptyEmail = "";
        when(userInfoRepository.emailExists(emptyEmail)).thenReturn(0);

        // When
        Integer result = userInfoRepository.emailExists(emptyEmail);

        // Then
        assertEquals(0, result);
        verify(userInfoRepository).emailExists(emptyEmail);
    }
}