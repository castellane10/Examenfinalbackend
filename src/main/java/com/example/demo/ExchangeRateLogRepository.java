package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateLogRepository extends JpaRepository<ExchangeRateLog, Long> {
}
