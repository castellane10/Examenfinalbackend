package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exchange-rate")
public class ExchangeRateController {

    private final ExchangeRateLogRepository logRepository;

    @Autowired
    public ExchangeRateController(ExchangeRateLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping("/history")
    public List<ExchangeRateLog> getExchangeRateHistory() {
        return logRepository.findAll();
    }
}
