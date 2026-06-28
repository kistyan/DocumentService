package org.example.documentservice.repository;

import org.example.documentservice.model.WorkAcceptanceAct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkAcceptanceActRepository extends JpaRepository<WorkAcceptanceAct, UUID> {
}
