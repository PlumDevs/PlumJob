package com.plumdevs.plumjob.repository;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.plumdevs.plumjob.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PositionsRepository extends JpaRepository<User, Long>  {

    @Query(value = "CALL sp_showUserHistory(:username, 1);", nativeQuery = true)
    List<RecruitmentItem> findActivePositions(@Param("username") String username);
    @Query(value = "CALL sp_showUserHistory(:username, 0);", nativeQuery = true)
    List<RecruitmentItem> findArchivePositions(@Param("username") String username);
}
