package com.plumdevs.plumjob.repository;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface PositionsRepository extends JpaRepository<RecruitmentItem, Long>  {

    @Query(value = "CALL sp_showUserHistory(:username, true);", nativeQuery = true)
    List<RecruitmentItem> findActivePositions(@Param("username") String username);
    @Query(value = "CALL sp_showUserHistory(:username, false);", nativeQuery = true)
    List<RecruitmentItem> findArchivePositions(@Param("username") String username);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO RecruitmentHistory (user_id, position, company, user_start_date, stage, description, ended) VALUES(:user_id, :position_name, :company_name, :user_start_date, :stage, :description, :ended);", nativeQuery = true)
    void addPosition(@Param("user_id") String user_id, //username
                     @Param("position_name") String position,
                     @Param("company_name") String company,
                     @Param("user_start_date") LocalDate user_start_date,
                     @Param("stage") String stage,
                     @Param("description") String description,
                     @Param("ended") boolean ended);

    //TODO: INSERT TO THE SECOND TABLE THAT KEEPS WHOLE HISTORY TOO!
    //TODO: WRAP IT WITH A FUNCTION IN DATABASE - AND AUTOMATICALLY WHEN ENDING STATUS IS CHOSEN, SET ENDED TO 1
    @Transactional
    @Modifying
    @Query(value = "UPDATE RecruitmentHistory SET stage = :stage WHERE history_id = :history_id;", nativeQuery = true)
    void updateStatus(
            @Param("history_id") int history_id,
            @Param("stage") String stage
            );

    @Transactional
    @Modifying
    @Query(value = "UPDATE RecruitmentHistory SET description = :description WHERE history_id = :history_id;", nativeQuery = true)
    void updateDescription(
            @Param("history_id") int history_id,
            @Param("description") String description
    );
}
