package com.doan.AppTuyenDung.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doan.AppTuyenDung.DTO.PaymentOrderRequestCvDTO;
import com.doan.AppTuyenDung.DTO.PaymentOrderRequestDTO;
import com.doan.AppTuyenDung.Services.PaymentService;
import com.paypal.api.payments.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
@RestController
@RequestMapping("/payment")
@PreAuthorize("hasAnyAuthority('COMPANY')")
public class PaymentPayPalController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/get-payment-link")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestParam(required = false) Integer id, @RequestParam(required = false) Integer amount) {
        Map<String, Object> response = paymentService.getPaymentLink(id,amount);
        
        if (response.get("errCode").equals(0)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/execute")
    public ResponseEntity<Map<String, Object>> executePayment(@RequestBody PaymentOrderRequestDTO paymentOrderRequest) {
        Map<String, Object> response = paymentService.paymentOrderSuccess(paymentOrderRequest);
        
        if (response.get("errCode").equals(0)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/get-payment-link-cv")
    public ResponseEntity<Map<String, Object>> createPaymentCV(@RequestParam(required = false) Integer id, @RequestParam(required = false) Integer amount) {
        Map<String, Object> response = paymentService.getPaymentLinkCV(id,amount);
        
        if (response.get("errCode").equals(0)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PostMapping("/execute-cv")
    public ResponseEntity<Map<String, Object>> executePaymentCV(@RequestBody PaymentOrderRequestCvDTO paymentOrderRequest) {
        Map<String, Object> response = paymentService.paymentOrderSuccessCV(paymentOrderRequest);
        
        if (response.get("errCode").equals(0)) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
}
