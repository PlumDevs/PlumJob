
package com.plumdevs.plumjob.repository;

import com.plumdevs.plumjob.entity.UserInfo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    @Query(value = "SELECT COUNT(*) FROM UserInfo WHERE user_email = :user_email", nativeQuery = true)
    Integer emailExists(@Param("user_email") String user_email);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO UserInfo (username, user_legalname, user_lastname, user_email, account_creation_date, is_active) VALUES (:username, :user_legalname, :user_lastname, :user_email, CURDATE(), TRUE)", nativeQuery = true)
    void addUserInfo(@Param("username") String username,
                        @Param("user_legalname") String firstName,
                        @Param("user_lastname") String lastName,
                        @Param("user_email") String email);

    @Query(value = "SELECT user_legalname FROM UserInfo WHERE username = :username", nativeQuery = true)
    String getUserFirstNameByUsername(@Param("username") String username);

    @Query(value = "SELECT user_lastname FROM UserInfo WHERE username = :username", nativeQuery = true)
    String getUserLastNameByUsername(@Param("username") String username);

    @Query(value = "SELECT user_email FROM UserInfo WHERE username = :username", nativeQuery = true)
    String getUserEmailByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query(value = "UPDATE UserInfo SET profile_picture = :profilePicture, profile_picture_type = :profilePictureType " +
            "WHERE username = :username", nativeQuery = true)
    void updateProfilePicture(@Param("username") String username,
                              @Param("profilePicture") byte[] profilePicture,
                              @Param("profilePictureType") String profilePictureType);

    @Query(value = "SELECT profile_picture FROM UserInfo WHERE username = :username", nativeQuery = true)
    byte[] findProfilePictureByUsername(@Param("username") String username);

    @Query(value = "SELECT profile_picture_type FROM UserInfo WHERE username = :username", nativeQuery = true)
    String findProfilePictureTypeByUsername(@Param("username") String username);

    @Query(value = "SELECT username FROM UserInfo WHERE username = :username", nativeQuery = true)
    String findUsername(@Param("username") String username);
}

