package com.doan.AppTuyenDung.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doan.AppTuyenDung.DTO.Request.ApplyEmailRequest;
import com.doan.AppTuyenDung.DTO.Request.ResponseEmailRequest;
import com.doan.AppTuyenDung.DTO.Response.ApiResponse;
import com.doan.AppTuyenDung.Services.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("send-mail/send-application-email")
    public ApiResponse<String> sendApplicationEmail(@RequestBody ApplyEmailRequest request) {
		ApiResponse<String> apiResponse = new ApiResponse<String>();
    	try {
            emailService.sendApplicationEmails(request.getUserEmail(), request.getCompanyEmail(), request.getJobTitle(), request.getUserName());
        	apiResponse.setMessage("Application emails sent to user and company.");
	    } catch (Exception e) {
			apiResponse.setMessage(e.getMessage());
	    	apiResponse.setCode(404);
		}

        return apiResponse;
    	
    }

    @PostMapping("send-mail/send-response-email")
    public ApiResponse<String>  sendResponseEmail(@RequestBody ResponseEmailRequest request) {
    	ApiResponse<String> apiResponse = new ApiResponse<String>();
    	try {
        emailService.sendResponseEmails(request.getUserEmail(), request.getCompanyEmail(), request.getJobTitle(), request.getUserName(), request.getStatusApproved());
        apiResponse.setMessage("Response emails sent to user and company.");
        return apiResponse;
    	} catch (Exception e) {
			apiResponse.setMessage(e.getMessage());
	    	apiResponse.setCode(404);
		}

        return apiResponse;
    }
}
