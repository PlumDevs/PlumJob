package com.plumdevs.plumjob.repository;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitmentHistoryService {

    @Autowired
    PositionsRepository positionsRepository;

    List<RecruitmentItem> findActivePositions(String username) {
        return positionsRepository.findActivePositions(username);
    }

    List<RecruitmentItem> findArchivePositions(String username) {
        return positionsRepository.findArchivePositions(username);
    }

}
