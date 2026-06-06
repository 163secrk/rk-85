package com.realestate.service;

import com.realestate.entity.Subscription;
import com.realestate.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public List<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    public Optional<Subscription> findById(Long id) {
        return subscriptionRepository.findById(id);
    }

    public Subscription findBySubscriptionNo(String subscriptionNo) {
        return subscriptionRepository.findBySubscriptionNo(subscriptionNo);
    }

    public List<Subscription> findByCustomerId(Long customerId) {
        return subscriptionRepository.findByCustomerId(customerId);
    }

    public List<Subscription> findByPropertyId(Long propertyId) {
        return subscriptionRepository.findByPropertyId(propertyId);
    }

    public List<Subscription> search(Long customerId, Long propertyId, String status,
                                     LocalDateTime startDate, LocalDateTime endDate) {
        return subscriptionRepository.findByConditions(customerId, propertyId, status, startDate, endDate);
    }

    public Subscription save(Subscription subscription) {
        if (subscription.getSubscriptionNo() == null) {
            subscription.setSubscriptionNo("SUB" + System.currentTimeMillis());
        }
        return subscriptionRepository.save(subscription);
    }

    public Subscription update(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public void deleteById(Long id) {
        subscriptionRepository.deleteById(id);
    }

    public List<Subscription> findByStatus(String status) {
        return subscriptionRepository.findByStatus(status);
    }
}
