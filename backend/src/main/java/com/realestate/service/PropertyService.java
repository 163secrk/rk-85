package com.realestate.service;

import com.realestate.entity.Property;
import com.realestate.repository.PropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    public List<Property> findAll() {
        return propertyRepository.findAll();
    }

    public Optional<Property> findById(Long id) {
        return propertyRepository.findById(id);
    }

    public List<Property> search(String title, String status, String type, String city,
                                 String district, BigDecimal minPrice, BigDecimal maxPrice) {
        return propertyRepository.findByConditions(title, status, type, city, district, minPrice, maxPrice);
    }

    public Property save(Property property) {
        return propertyRepository.save(property);
    }

    public Property update(Property property) {
        return propertyRepository.save(property);
    }

    public void deleteById(Long id) {
        propertyRepository.deleteById(id);
    }

    public List<Property> findByStatus(String status) {
        return propertyRepository.findByStatus(status);
    }

    public List<Property> findByType(String type) {
        return propertyRepository.findByType(type);
    }
}
