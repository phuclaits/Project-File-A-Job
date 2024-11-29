package com.doan.AppTuyenDung.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doan.AppTuyenDung.DTO.Response.ApiResponse;
import com.doan.AppTuyenDung.Services.SmsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth/otp")
public class SmsController {
	@Autowired 
	SmsService smsService;
	@PostMapping("/request-otp")
	public ApiResponse<String> requestOTP(@RequestBody String phoneNumber) {
		ApiResponse<String> apiRp = new ApiResponse<String>();
		apiRp.setMessage("Yêu cầu xác thực OTP thành công!");
		apiRp.setResult(smsService.requestOtp(phoneNumber));
		return apiRp;
	}
	@PostMapping("/verify-otp")
	public ApiResponse<Boolean> verifyOTP(@RequestBody String requestId,@RequestBody String OTP ) {
		ApiResponse<Boolean> apiRp = new ApiResponse<Boolean>();
		apiRp.setMessage("Xác thực OTP thành công!");
		apiRp.setResult(smsService.verifyOtp(requestId, OTP));
		return apiRp;
	}
	
}
