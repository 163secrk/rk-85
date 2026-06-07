package com.realestate.repository;

import com.realestate.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findByStatus(String status);

    Optional<Agent> findByPhone(String phone);

    List<Agent> findByNameContaining(String name);

    @Query("SELECT a.name FROM Agent a WHERE a.status = '在职' ORDER BY a.name")
    List<String> findAllActiveAgentNames();
}
