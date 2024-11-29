package com.doan.AppTuyenDung.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cloudinary.Api;
import com.doan.AppTuyenDung.DTO.GetAllUserAdmin.AccountDTO;
import com.doan.AppTuyenDung.DTO.GetAllUserAdmin.CustomResponse;
import com.doan.AppTuyenDung.DTO.Request.ChangePasswordRequest;
import com.doan.AppTuyenDung.DTO.Response.ApiResponse;
import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Services.AccountService;
import com.doan.AppTuyenDung.Services.JWTUtils;

import java.util.Map;

@RestController
@RequestMapping
public class AccountController {
	@Autowired
	AccountService accountService;

	@Autowired
    private JWTUtils jwtUtils; 

	@Autowired
    private AccountRepository accountRepo;
	// @PutMapping("/app-tuyen-dung/api/v1/account/change_password/{idUser}")
	// ApiResponse<String> changePassword(@PathVariable int idUser, @RequestBody ChangePasswordRequest cPasswordRequest) {
	// 	ApiResponse<String> apiRQ = new ApiResponse<String>();
	// 	apiRQ.setMessage(accountService.changePassword(idUser, cPasswordRequest));
	// 	return apiRQ;
	// }
 
	@PostMapping("account/change-password")
    public ResponseEntity<Map<String, Object>> changePasswordByPhone(@RequestBody ChangePasswordRequest data) {
        Map<String, Object> response = accountService.changePasswordByPhone(data.getId(),data.getOldpassword() ,data.getPassword());
        return ResponseEntity.ok(response);
    }

	
	@PreAuthorize("hasAnyAuthority('ADMIN')")
	@GetMapping("/user/users")
    public ResponseEntity<CustomResponse<Page<AccountDTO>>> getAllUsers(@RequestHeader("Authorization") String token,
										@RequestParam(defaultValue = "10") int limit,
										@RequestParam(defaultValue = "0") int offset,
										@RequestParam(required = false) String search) 
	{
        Pageable pageable = PageRequest.of(offset, limit);

		 if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            CustomResponse<Page<AccountDTO>> response = new CustomResponse<>(-1, "Error from Client without access", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
			if (search != null && !search.isEmpty()) 
			{
				search = "%" + search + "%";
			}
            Page<AccountDTO> users = accountService.getAllUsers(search,pageable);
            CustomResponse<Page<AccountDTO>> response = new CustomResponse<>(0, null, users);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            CustomResponse<Page<AccountDTO>> response = new CustomResponse<>(-1, "Error from server", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
	@PutMapping("/auth/forget-password/{phoneNumber}")
	public ApiResponse<String> forgetPassword(@PathVariable String phoneNumber) {
		ApiResponse<String> apiRs = new ApiResponse<String>();
		apiRs.setMessage("Lấy lại password thành công");
		apiRs.setResult(accountService.forgetPassword(phoneNumber));
		return apiRs;
	}
}
