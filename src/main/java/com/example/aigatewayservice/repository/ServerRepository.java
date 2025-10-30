package com.example.aigatewayservice.repository;

import com.example.aigatewayservice.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {
}