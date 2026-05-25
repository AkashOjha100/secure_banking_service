package com.ls.customer.service;

import com.ls.customer.entity.Customer;
import com.ls.customer.entity.KycDetails;
import com.ls.customer.enums.CustomerStatus;
import com.ls.customer.enums.KycStatus;
import com.ls.customer.repository.CustomerRepository;
import com.ls.customer.repository.KycRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KycService {

    @Autowired
    private KycRepository kycRepo;
    @Autowired
    private CustomerRepository customerRepo;

    public KycDetails submitKyc(Long customerId ,KycDetails kycDetails) {
        Customer customer = customerRepo.findById(customerId).orElseThrow(()->
                new RuntimeException("Customer not found!"));
        kycDetails.setCustomer(customer);
        kycDetails.setStatus(KycStatus.PENDING);
        return kycRepo.save(kycDetails);
    }
    public KycDetails getKyc(Long customerId){
        return kycRepo.findByCustomerId(customerId).orElseThrow(()->
                new RuntimeException("KYC not found!"));
    }

    public KycDetails updateKyc(Long customerId,KycDetails request) {
        KycDetails kyc = getKyc(customerId);
        kyc.setPanNumber(request.getPanNumber());
        kyc.setAadhaarNumber(request.getAadhaarNumber());
        return kycRepo.save(kyc);
    }
    public void approveKyc(Long kycId){
        KycDetails kyc = kycRepo.findById(kycId).orElseThrow(()->
                new RuntimeException("KYC not found!"));
        kyc.setStatus(KycStatus.VERIFIED);
        Customer customer =kyc.getCustomer();
        customer.setStatus(CustomerStatus.ACTIVE);
        customerRepo.save(customer);
        kycRepo.save(kyc);
    }
    public void rejectKyc(Long kycId){
        KycDetails kyc =kycRepo.findById(kycId).orElseThrow(()->
                new RuntimeException("KYC not found!"));
        kyc.setStatus(KycStatus.REJECTED);
        kycRepo.save(kyc);
    }
}
