package com.realestate.service;

import com.realestate.entity.Agent;
import com.realestate.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    public Optional<Agent> findById(Long id) {
        return agentRepository.findById(id);
    }

    public List<Agent> findByStatus(String status) {
        return agentRepository.findByStatus(status);
    }

    public List<String> findAllActiveAgentNames() {
        return agentRepository.findAllActiveAgentNames();
    }

    public Agent save(Agent agent) {
        return agentRepository.save(agent);
    }

    public Agent update(Agent agent) {
        return agentRepository.save(agent);
    }

    public void deleteById(Long id) {
        agentRepository.deleteById(id);
    }

    public Optional<Agent> findByPhone(String phone) {
        return agentRepository.findByPhone(phone);
    }

    public List<Agent> search(String name, String status) {
        if (name != null && !name.trim().isEmpty()) {
            return agentRepository.findByNameContaining(name);
        }
        if (status != null && !status.trim().isEmpty()) {
            return agentRepository.findByStatus(status);
        }
        return agentRepository.findAll();
    }
}
