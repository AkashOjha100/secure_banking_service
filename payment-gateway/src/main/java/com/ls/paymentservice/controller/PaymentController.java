package com.ls.paymentservice.controller;

import com.ls.paymentservice.dto.PaymentRequest;
import com.ls.paymentservice.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody PaymentRequest request) throws RazorpayException {
        Order order = paymentService.createOrder(
                request.getAmount(),
                request.getCurrency(),
                "recipient_100"
        );
        return ResponseEntity.ok(order.toString());
    }
}
