package com.realestate.service;

import com.realestate.entity.Contract;
import com.realestate.entity.Subscription;
import com.realestate.repository.ContractRepository;
import com.realestate.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    public Optional<Contract> findById(Long id) {
        return contractRepository.findById(id);
    }

    public Contract findByContractNo(String contractNo) {
        return contractRepository.findByContractNo(contractNo);
    }

    public List<Contract> findByCustomerId(Long customerId) {
        return contractRepository.findByCustomerId(customerId);
    }

    public List<Contract> findByPropertyId(Long propertyId) {
        return contractRepository.findByPropertyId(propertyId);
    }

    public List<Contract> findByStatus(String status) {
        return contractRepository.findByStatus(status);
    }

    public List<Contract> search(Long customerId, String status,
                                  LocalDateTime startDate, LocalDateTime endDate) {
        return contractRepository.findByConditions(customerId, status, startDate, endDate);
    }

    @Transactional
    public Contract save(Contract contract) {
        if (contract.getContractNo() == null) {
            contract.setContractNo("HT" + System.currentTimeMillis());
        }
        if (contract.getStatus() == null) {
            contract.setStatus("草稿");
        }
        return contractRepository.save(contract);
    }

    @Transactional
    public Contract update(Contract contract) {
        return contractRepository.save(contract);
    }

    @Transactional
    public void deleteById(Long id) {
        contractRepository.deleteById(id);
    }

    @Transactional
    public Contract createFromSubscription(Long subscriptionId) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subscriptionId);
        if (!subscriptionOpt.isPresent()) {
            throw new RuntimeException("认购记录不存在");
        }

        Contract existingContract = contractRepository.findBySubscriptionId(subscriptionId);
        if (existingContract != null) {
            return existingContract;
        }

        Subscription subscription = subscriptionOpt.get();
        Contract contract = new Contract();

        contract.setSubscriptionId(subscription.getId());
        contract.setSubscriptionNo(subscription.getSubscriptionNo());
        contract.setCustomerId(subscription.getCustomerId());
        contract.setCustomerName(subscription.getCustomerName());
        contract.setCustomerPhone(subscription.getCustomerPhone());
        contract.setCustomerIdCard(subscription.getCustomerIdCard());
        contract.setPropertyId(subscription.getPropertyId());
        contract.setPropertyTitle(subscription.getPropertyTitle());
        contract.setPropertyAddress(subscription.getPropertyAddress());
        contract.setArea(subscription.getArea());
        contract.setTotalPrice(subscription.getTotalPrice());
        contract.setUnitPrice(subscription.getUnitPrice());
        contract.setDepositAmount(subscription.getDepositAmount());
        contract.setFirstPayment(subscription.getFirstPayment());
        contract.setRemainingAmount(subscription.getRemainingAmount());
        contract.setPaymentMethod(subscription.getPaymentMethod());
        contract.setPaymentPeriod(subscription.getPaymentPeriod());
        contract.setSignDate(subscription.getSignDate());
        contract.setDeliveryDate(subscription.getDeliveryDate());
        contract.setAgentName(subscription.getAgentName());
        contract.setStatus("草稿");

        return contractRepository.save(contract);
    }

    @Transactional
    public Contract updateStatus(Long id, String status) {
        Optional<Contract> contractOpt = contractRepository.findById(id);
        if (!contractOpt.isPresent()) {
            throw new RuntimeException("合同不存在");
        }
        Contract contract = contractOpt.get();
        
        if ("已签署".equals(status)) {
            contract.setSignDate(LocalDateTime.now());
        } else if ("已备案".equals(status)) {
            contract.setRecordDate(LocalDateTime.now());
        }
        
        contract.setStatus(status);
        return contractRepository.save(contract);
    }
}
