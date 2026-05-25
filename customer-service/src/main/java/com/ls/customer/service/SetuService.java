package com.ls.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import java.util.Map;

@Service
public class SetuService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${setu.base-url}")
    private String baseUrl;

    @Value("${setu.client-id}")
    private String clientId;

    @Value("${setu.client-secret}")
    private String clientSecret;

    @Value("${setu.pan-product-id}")
    private String panProductId;

    @Value("${setu.digilocker-product-key}")
    private String digiLockerProductId;

    public String verifyPan(String pan) {

        Map<String, Object> body = Map.of(
                "pan", pan,
                "consent", "Y",
                "reason", "Kyc verification"
        );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);
        headers.set("x-product-instance-id", panProductId);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(
                        baseUrl + "/api/verify/pan",
                        HttpMethod.POST,
                        request,
                        String.class
                );

        return response.getBody();
    }
    public String createDigiLockerRequest(){
        Map<String, Object> body = Map.of("redirectUrl", "http://localhost:9002/kyc/callback");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);
        headers.set("x-product-instance-id", digiLockerProductId);
        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);
        ResponseEntity<String> response =
                restTemplate.exchange(
                        baseUrl + "/api/digilocker",
                        HttpMethod.POST,
                        request,
                        String.class
                );
        return response.getBody();
    }

    public String fetchAadhar(String requestId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", clientId);
        headers.set("x-client-secret", clientSecret);
        headers.set("x-product-instance-id", digiLockerProductId);
        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(headers);
        ResponseEntity<String> response =
                restTemplate.exchange(
                        baseUrl + "/api/digilocker/" + requestId + "/status",
                        HttpMethod.GET,
                        request,
                        String.class
                );
        return response.getBody();
    }
    /*
    public String verifyPan(String pan){
        Map<String, Object> body = Map.of(
                "pan",pan,
                "consent","Y",
                "reason","Kyc verification"
        );
        return webClient.post().uri(baseUrl+"/api/verify/pan")
                .header("x-client-id",clientId)
                .header("x-client-secret",clientSecret)
                .header("x-product-instance-id",panProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve().bodyToMono(String.class).block();
    }

    public String createDigiLockerRequest(){
        Map<String, Object> body = Map.of("redirectUrl","http://localhost:9002/kyc/callback");
        return webClient.post()
                .uri(baseUrl+"/api/digilocker")
                .header("x-client-id",clientId)
                .header("x-client-secret",clientSecret)
                .header("x-product-instance-id",digilockerProductId)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve().bodyToMono(String.class).block();
    }

    public String checkStatus(String requestId){
        return webClient.get()
                .uri(baseUrl+"/api/digilocker/"+requestId+"/status")
                .header("x-client-id",clientId)
                .header("x-client-secret",clientSecret)
                .header("x-product-instance-id",digilockerProductId)
                .retrieve().bodyToMono(String.class).block();
    }

    public String fetchAadhar(String requestId){
        return webClient.get()
                .uri(baseUrl+"/api/digilocker/"+requestId+"/aadhar")
                .header("x-client-id",clientId)
                .header("x-client-secret",clientSecret)
                .header("x-product-instance-id",digilockerProductId)
                .retrieve().bodyToMono(String.class).block();
    }

     */
}
