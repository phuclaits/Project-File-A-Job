package com.doan.AppTuyenDung.Services;

import com.doan.AppTuyenDung.Exception.AppException;
import com.doan.AppTuyenDung.Exception.ErrorCode;
import com.vonage.client.VonageClient;
import com.vonage.client.verify.CheckResponse;
import com.vonage.client.verify.VerifyResponse;
import com.vonage.client.verify.VerifyStatus;

import org.springframework.stereotype.Service;

@Service
public class SmsService {
    static VonageClient client = VonageClient.builder().apiKey("03e6274f").apiSecret("dPSux6jRPBynk8xc").build();
    
    public String requestOtp(String phoneNumber) {
        VerifyResponse response = client.getVerifyClient().verify(phoneNumber, "AppTuyenDung");

        if (response.getStatus() == VerifyStatus.OK) {
            System.out.printf("RequestID: %s", response.getRequestId());
            return response.getRequestId();
        } else {
            System.out.printf("ERROR! %s: %s", response.getStatus(), response.getErrorText());
            throw new AppException(ErrorCode.ERROROTP);
        }
    }
    public boolean verifyOtp(String REQUEST_ID, String CODE) {
        CheckResponse response = client.getVerifyClient().check(REQUEST_ID, CODE);

        if (response.getStatus() == VerifyStatus.OK) {
            System.out.println("Verification Successful");
            return true;
        } else {
            System.out.println("Verification failed: " + response.getErrorText());
            throw new AppException(ErrorCode.ERROROTP);
        }
    }
    public String cancelVeify(String REQUEST_ID) {
        client.getVerifyClient().cancelVerification(REQUEST_ID);
        System.out.println("Verification cancelled.");
        return "Verification cancelled";
    }
}
