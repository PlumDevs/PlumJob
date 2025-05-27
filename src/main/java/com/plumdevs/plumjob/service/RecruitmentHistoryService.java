package com.plumdevs.plumjob.service;

import com.plumdevs.plumjob.entity.RecruitmentItem;
import com.plumdevs.plumjob.repository.PositionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitmentHistoryService {

    @Autowired
    PositionsRepository positionsRepository;

    public List<RecruitmentItem> findActivePositions(String username) {
        return positionsRepository.findActivePositions(username);
    }

    public List<RecruitmentItem> findArchivePositions(String username) {
        return positionsRepository.findArchivePositions(username);
    }

}
