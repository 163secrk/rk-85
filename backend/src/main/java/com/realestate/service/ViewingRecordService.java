package com.realestate.service;

import com.realestate.entity.ViewingRecord;
import com.realestate.repository.ViewingRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ViewingRecordService {

    @Autowired
    private ViewingRecordRepository viewingRecordRepository;

    public List<ViewingRecord> findAll() {
        return viewingRecordRepository.findAll();
    }

    public Optional<ViewingRecord> findById(Long id) {
        return viewingRecordRepository.findById(id);
    }

    public List<ViewingRecord> findByCustomerId(Long customerId) {
        return viewingRecordRepository.findByCustomerId(customerId);
    }

    public List<ViewingRecord> findByPropertyId(Long propertyId) {
        return viewingRecordRepository.findByPropertyId(propertyId);
    }

    public List<ViewingRecord> search(Long customerId, String customerName, Long propertyId, String agentName,
                                      String status, LocalDateTime startTime, LocalDateTime endTime) {
        return viewingRecordRepository.findByConditions(customerId, customerName, propertyId, agentName, status, startTime, endTime);
    }

    public ViewingRecord save(ViewingRecord viewingRecord) {
        return viewingRecordRepository.save(viewingRecord);
    }

    public ViewingRecord update(ViewingRecord viewingRecord) {
        return viewingRecordRepository.save(viewingRecord);
    }

    public void deleteById(Long id) {
        viewingRecordRepository.deleteById(id);
    }
}
