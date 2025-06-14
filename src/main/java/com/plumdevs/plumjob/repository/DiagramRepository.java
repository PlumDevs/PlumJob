package com.plumdevs.plumjob.repository;

import com.plumdevs.plumjob.entity.DiagramLink;
import com.plumdevs.plumjob.entity.RecruitmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagramRepository extends JpaRepository<DiagramLink, Long> {

    @Query(value = "CALL sp_getUserStatusChangesFullPath(:userId)", nativeQuery = true)
    List<DiagramLink> callDiagramLinkProcedure(@Param("userId") String userId);

}
