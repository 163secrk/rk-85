package com.realestate.service;

import com.realestate.entity.FollowUpRecord;
import com.realestate.repository.FollowUpRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FollowUpRecordService {

    @Autowired
    private FollowUpRecordRepository followUpRecordRepository;

    public List<FollowUpRecord> findAll() {
        return followUpRecordRepository.findAll();
    }

    public Optional<FollowUpRecord> findById(Long id) {
        return followUpRecordRepository.findById(id);
    }

    public List<FollowUpRecord> findByCustomerId(Long customerId) {
        return followUpRecordRepository.findByCustomerIdOrderByFollowTimeDesc(customerId);
    }

    public List<FollowUpRecord> search(Long customerId, String agentName, String followType,
                                       LocalDateTime startTime, LocalDateTime endTime) {
        return followUpRecordRepository.findByConditions(customerId, agentName, followType, startTime, endTime);
    }

    public FollowUpRecord save(FollowUpRecord followUpRecord) {
        return followUpRecordRepository.save(followUpRecord);
    }

    public FollowUpRecord update(FollowUpRecord followUpRecord) {
        return followUpRecordRepository.save(followUpRecord);
    }

    public void deleteById(Long id) {
        followUpRecordRepository.deleteById(id);
    }
}
