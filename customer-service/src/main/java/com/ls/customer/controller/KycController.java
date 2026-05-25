package com.ls.customer.controller;

import com.ls.customer.entity.KycDetails;
import com.ls.customer.service.KycService;
import com.ls.customer.service.SetuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kyc")
public class KycController {

    @Autowired
    private KycService kycService;
    @Autowired
    private SetuService setuService;

    @PostMapping("/{customerId}")
    public KycDetails submitKyc(@PathVariable Long customerId,
                                @RequestBody KycDetails request) {
        return kycService.submitKyc(customerId, request);
    }
    @PostMapping("/verify_pan")
    public ResponseEntity<?> verifyPan(@RequestParam String pan) {
        String response = setuService.verifyPan(pan);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/digilocker")
    public ResponseEntity<?> getDigilocker() {
        String response =setuService.createDigiLockerRequest();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> getCallback(@RequestParam("id") String requestId,
                                              @RequestParam("success") boolean success,
                                              @RequestParam(value ="scope",required = false) String scope) {
        if(!success){
            return ResponseEntity.ok("user denied consent");
        }
        if(scope!=null && scope.contains("AADHAAR")){
            String aadharData = setuService.fetchAadhar(requestId);
        }
        return ResponseEntity.ok("callback received");
    }


}
