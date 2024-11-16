package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/soap")
public class SoapController {

    private final SoapClient soapClient;

    @Autowired
    public SoapController(SoapClient soapClient) {
        this.soapClient = soapClient;
    }

    @GetMapping("/tipocambio")
    public Map<String, Object> getTipoCambioDia() {
        try {
            // Llama al cliente SOAP y obtiene la respuesta
            String soapResponse = soapClient.callTipoCambioDiaString();

            // Extrae los datos relevantes de la respuesta SOAP
            String exchangeRate = soapClient.parseExchangeRateFromResponse(soapResponse);
            String date = soapClient.parseDateFromResponse(soapResponse);

            // Construye un mapa con los datos relevantes
            Map<String, Object> result = new HashMap<>();
            result.put("exchangeRate", exchangeRate);
            result.put("date", date);
            result.put("currency", "USD");

            return result; // Spring Boot lo convierte automáticamente a JSON
        } catch (Exception e) {
            // Manejo de errores
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "No se pudo obtener el tipo de cambio");
            errorResponse.put("message", e.getMessage());
            return errorResponse;
        }
    }
}
