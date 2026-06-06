package com.realestate.service;

import com.realestate.entity.Customer;
import com.realestate.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> search(String name, String phone, String status, String level) {
        return customerRepository.findByConditions(name, phone, status, level);
    }

    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer update(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    public List<Customer> findByStatus(String status) {
        return customerRepository.findByStatus(status);
    }

    public List<Customer> findByLevel(String level) {
        return customerRepository.findByLevel(level);
    }
}
