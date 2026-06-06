package com.realestate.service;

import com.realestate.entity.VRPanorama;
import com.realestate.repository.VRPanoramaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VRPanoramaService {

    @Autowired
    private VRPanoramaRepository vrPanoramaRepository;

    public List<VRPanorama> findAll() {
        return vrPanoramaRepository.findAll();
    }

    public Optional<VRPanorama> findById(Long id) {
        return vrPanoramaRepository.findById(id);
    }

    public List<VRPanorama> findByPropertyId(Long propertyId) {
        return vrPanoramaRepository.findByPropertyIdOrderBySortOrderAsc(propertyId);
    }

    public List<VRPanorama> search(Long propertyId, String name, String status) {
        return vrPanoramaRepository.findByConditions(propertyId, name, status);
    }

    public VRPanorama save(VRPanorama vrPanorama) {
        return vrPanoramaRepository.save(vrPanorama);
    }

    public VRPanorama update(VRPanorama vrPanorama) {
        return vrPanoramaRepository.save(vrPanorama);
    }

    public void deleteById(Long id) {
        vrPanoramaRepository.deleteById(id);
    }

    public VRPanorama incrementViewCount(Long id) {
        Optional<VRPanorama> optional = vrPanoramaRepository.findById(id);
        if (optional.isPresent()) {
            VRPanorama vr = optional.get();
            vr.setViewCount(vr.getViewCount() + 1);
            return vrPanoramaRepository.save(vr);
        }
        return null;
    }
}
