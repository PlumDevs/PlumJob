package com.plumdevs.plumjob.repository;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PositionsRepository extends JpaRepository<RecruitmentItem, Long>  {

    @Query(value = "CALL sp_showUserHistory(:username, true);", nativeQuery = true)
    List<RecruitmentItem> findActivePositions(@Param("username") String username);
    @Query(value = "CALL sp_showUserHistory(:username, false);", nativeQuery = true)
    List<RecruitmentItem> findArchivePositions(@Param("username") String username);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO RecruitmentHistory (user_id, position, company, user_start_date, stage, description, ended) VALUES(:user_id, :position_name, :company_name, null, :stage, null, :ended);", nativeQuery = true)
    void addPosition(@Param("user_id") String user_id, //username
                     @Param("position_name") String position,
                     @Param("company_name") String company,
                     @Param("stage") int stage,
                     @Param("ended") boolean ended);
    //INSERT DO RECRUITMENTOFFER
    //TODO: depending on the status, assign bool ended, by a function
    //TODO: and fix status, STATUS UPDATE FUNCTIONALITY
}
