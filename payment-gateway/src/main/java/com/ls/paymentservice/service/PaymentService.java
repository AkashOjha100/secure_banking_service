package com.ls.paymentservice.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    public Order createOrder(int amount , String currency, String receipt) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(apiKey, apiSecret);
        JSONObject order = new JSONObject();
        order.put("amount",amount * 100); //convert into rupees
        order.put("currency",currency);
        order.put("receipt",receipt);

        return client.orders.create(order);
    }
}
