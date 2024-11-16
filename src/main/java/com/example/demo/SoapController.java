package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/soap")
public class SoapController {

    private final SoapClient soapClient;

    @Autowired
    public SoapController(SoapClient soapClient) {
        this.soapClient = soapClient;
    }

    @GetMapping("/tipocambio")
    public String getTipoCambioDia() {
        return soapClient.callTipoCambioDiaString();
    }
}
