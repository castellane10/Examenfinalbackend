package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.xml.transform.StringSource;
import org.jsoup.Jsoup;


import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SoapClient {

    private final WebServiceTemplate webServiceTemplate;
    private final ExchangeRateLogRepository logRepository;

    @Autowired
    public SoapClient(WebServiceTemplate webServiceTemplate, ExchangeRateLogRepository logRepository) {
        this.webServiceTemplate = webServiceTemplate;
        this.logRepository = logRepository;
    }

    public String callTipoCambioDiaString() {
        // El mensaje SOAP que enviaremos
        String soapRequest =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "<soap:Body>" +
                "<TipoCambioDiaString xmlns=\"http://www.banguat.gob.gt/variables/ws/\" />" +
                "</soap:Body>" +
                "</soap:Envelope>";
    
        // Convierte el mensaje en una fuente XML
        Source requestSource = new StringSource(soapRequest);
    
        // Define el callback para el SOAPAction y el cuerpo del mensaje
        WebServiceMessageCallback requestCallback = message -> {
            SoapMessage soapMessage = (SoapMessage) message;
            soapMessage.setSoapAction("http://www.banguat.gob.gt/variables/ws/TipoCambioDiaString");
    
            TransformerFactory.newInstance().newTransformer()
                    .transform(requestSource, soapMessage.getSoapBody().getPayloadResult());
        };
    
        // Definimos el extractor para manejar la respuesta
        WebServiceMessageExtractor<String> responseExtractor = message -> {
            StringWriter writer = new StringWriter();
            TransformerFactory.newInstance().newTransformer()
                    .transform(message.getPayloadSource(), new StreamResult(writer));
            return writer.toString();
        };
    
        // Ejecuta la solicitud y obtiene la respuesta
        String response = webServiceTemplate.sendAndReceive(
            "https://www.banguat.gob.gt/variables/ws/TipoCambio.asmx",
            requestCallback,
            responseExtractor
        );
    
        // Procesa y guarda en la base de datos
        String exchangeRate = parseExchangeRateFromResponse(response);
        ExchangeRateLog log = new ExchangeRateLog();
        log.setRequestDateTime(LocalDateTime.now());
        log.setExchangeRate(exchangeRate);
        log.setCurrency("USD"); // Asignar valor predeterminado de moneda
        log.setRequestNumber(UUID.randomUUID().toString()); // Genera un número de solicitud único
        logRepository.save(log);
    
        return response;
    }
    

    // Método auxiliar para extraer el tipo de cambio del XML de respuesta
    public String parseExchangeRateFromResponse(String response) {
        // Busca las etiquetas <TipoCambioDiaStringResult>
        String startTag = "<TipoCambioDiaStringResult>";
        String endTag = "</TipoCambioDiaStringResult>";
        
        int startIndex = response.indexOf(startTag);
        int endIndex = response.indexOf(endTag);
        
        // Verifica que las etiquetas existan
        if (startIndex == -1 || endIndex == -1 || startIndex + startTag.length() > endIndex) {
            throw new IllegalArgumentException("La respuesta SOAP no contiene la etiqueta <TipoCambioDiaStringResult>");
        }
        
        // Extrae el contenido codificado dentro de <TipoCambioDiaStringResult>
        String encodedXml = response.substring(startIndex + startTag.length(), endIndex);
        
        // Decodifica el contenido HTML
        String decodedXml = Jsoup.parse(encodedXml).text(); // Decodifica &lt; -> <
        
        // Ahora procesa el XML decodificado para extraer <referencia>
        String referenceStartTag = "<referencia>";
        String referenceEndTag = "</referencia>";
        
        int refStartIndex = decodedXml.indexOf(referenceStartTag);
        int refEndIndex = decodedXml.indexOf(referenceEndTag);
        
        // Verifica que las etiquetas <referencia> existan
        if (refStartIndex == -1 || refEndIndex == -1 || refStartIndex + referenceStartTag.length() > refEndIndex) {
            throw new IllegalArgumentException("El XML decodificado no contiene la etiqueta <referencia>");
        }
        
        // Retorna el valor dentro de <referencia>
        return decodedXml.substring(refStartIndex + referenceStartTag.length(), refEndIndex);
    }
    // Método para extraer <fecha> del XML decodificado
    public String parseDateFromResponse(String response) {
        // Decodifica el contenido HTML
        String decodedXml = Jsoup.parse(response).text();
        System.out.println("XML decodificado: " + decodedXml);
    
        String dateStartTag = "<fecha>";
        String dateEndTag = "</fecha>";
    
        int startIndex = decodedXml.indexOf(dateStartTag);
        int endIndex = decodedXml.indexOf(dateEndTag);
    
        if (startIndex == -1 || endIndex == -1 || startIndex + dateStartTag.length() > endIndex) {
            throw new IllegalArgumentException("El XML decodificado no contiene la etiqueta <fecha>");
        }
    
        return decodedXml.substring(startIndex + dateStartTag.length(), endIndex);
    }
    
    

    
}
