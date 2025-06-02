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

}
