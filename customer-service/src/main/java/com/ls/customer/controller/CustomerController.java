package com.ls.customer.controller;

import com.ls.customer.dto.CustomerRequest;
import com.ls.customer.dto.CustomerResponse;
import com.ls.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/create")
    public CustomerResponse create(@RequestBody CustomerRequest request){
        return customerService.createCustomer(request);
    }
}
