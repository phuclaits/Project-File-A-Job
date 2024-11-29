package com.doan.AppTuyenDung.Controller;


import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import com.doan.AppTuyenDung.DTO.Request.ReqRes;
import com.doan.AppTuyenDung.DTO.Request.UserSettingDTO;
import com.doan.AppTuyenDung.DTO.Request.UserUpdateRequest;
import com.doan.AppTuyenDung.DTO.Response.AccountResponse;
import com.doan.AppTuyenDung.DTO.Response.ApiResponse;
import com.doan.AppTuyenDung.DTO.Response.UserUpdateResponse;
import com.doan.AppTuyenDung.Exception.AppException;
import com.doan.AppTuyenDung.Exception.ErrorCode;
import com.doan.AppTuyenDung.DTO.UserAccountDTO;
import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Repositories.UserRepository;
import com.doan.AppTuyenDung.Services.JWTUtils;
import com.doan.AppTuyenDung.Services.UserManagermentService;





@RestController
@RequestMapping
public class UserManagementController {
	@Autowired
    private UserManagermentService usersManagementService;
    @Autowired
    private JWTUtils jwtUtils; 
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private UserRepository userRepository;
    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> regeister(@RequestBody ReqRes reg){
        return ResponseEntity.ok(usersManagementService.register(reg));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.login(req));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes req){
        return ResponseEntity.ok(usersManagementService.refreshToken(req));
    }
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/user/get-all-users")
    public ApiResponse<List<UserUpdateResponse>> getAllUsers() throws Exception{

    	var authentication = SecurityContextHolder.getContext().getAuthentication();
//    	System.out.println("Phone "+authentication.getName());
//    	authentication.getAuthorities().forEach(grant -> System.out.println(grant.getAuthority()));
    	ApiResponse apiResponse = new ApiResponse<>();
    	try {
        	apiResponse.setMessage("Danh sách người dừng: ");
        	apiResponse.setResult(usersManagementService.getAllUsers());
            return apiResponse;
		} catch (Exception e) {
			System.out.println(e);
        	throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }

    @GetMapping("/user/get-users/{userId}")
    public ApiResponse<AccountResponse> getUSerByID(@PathVariable Integer userId) throws Exception{
    	ApiResponse apiResponse = new ApiResponse<>();
    	try {
        	apiResponse.setMessage("Tìm thấy người dùng với id: "+userId);
        	apiResponse.setResult(usersManagementService.getUsersById(userId));
            return apiResponse;
		} catch (Exception e) {
			System.out.println(e);
        	throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}

    }
    
    @PutMapping("/user/update")
    public ApiResponse<AccountResponse> updateUser(@ModelAttribute UserUpdateRequest reqres,
                                             @RequestPart(value="fileImage",required = false) MultipartFile fileImage) throws Exception{
    	ApiResponse apiResponse = new ApiResponse<>();
    	try {
        	apiResponse.setMessage("Cập nhật user thành công với id: "+reqres.getId());
        	apiResponse.setResult(usersManagementService.updateUser(reqres,fileImage));
            return apiResponse;
		} catch (Exception e) {
			System.out.println(e);
        	throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }
    @PostMapping("/user/set-user-setting")
    @PreAuthorize("hasAnyAuthority('CANDIDATE')")
    public ApiResponse setDataUserSetting(@RequestHeader("Authorization") String token,
                                          @ModelAttribute UserSettingDTO data,
                                          @RequestPart(value="filepdf",required = false) MultipartFile filepdf ) 
    {
        
        // check user 
        ApiResponse apiRS = new ApiResponse<>();
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);

        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null ) {
            apiRS.setCode(-1);
            apiRS.setMessage("Lỗi không tìm thấy account");
        }
        // if(account.getId() != data.getIdUser())
        // {
        //     apiRS.setCode(-1);
        //     apiRS.setMessage("Không thể cập nhật thông tin người dùng");
        //     return apiRS;
        // }

        try{
            String base64Pdf = Base64.getEncoder().encodeToString(filepdf.getBytes());
            String result = "data:application/pdf;base64," + base64Pdf;
            data.setFile(result.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }


    	
    	apiRS.setMessage(usersManagementService.setDataUserSetting(data));
        return apiRS;
    }


    @PostMapping("/user/get-info")
    public ResponseEntity<List<UserAccountDTO>> getUserInfo(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);

        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        List<UserAccountDTO> userAccountInfo = userRepository.findInfoUser(phonenumber);
        return ResponseEntity.ok(userAccountInfo);
    }



    @GetMapping("/user/check-phonenumber-user")
    public ResponseEntity<?> getMethodName(@RequestParam String phonenumber) {
        boolean exists = accountRepo.existsByPhonenumber(phonenumber);
        if (exists) {
            return ResponseEntity.ok(true);
        } 
        // Nếu không tồn tại, trả về sdt đó
        else {
            return ResponseEntity.ok(phonenumber);
        }
    }
    @GetMapping("/user/search-users")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ApiResponse<Page<AccountResponse>> searchUsers(@RequestParam(required = false) String firstName,
                                  @RequestParam(required = false) String lastName,
                                  @RequestParam(required = false) String categoryJobCode,
                                  @RequestParam(required = false) String salaryJobCode,
                                  @RequestParam(required = false) String experienceJobCode,
                                  @RequestParam(required = false) String skillName,
                                  @RequestParam(defaultValue = "0") int page, 
                                  @RequestParam(defaultValue = "10") int size) { 
    	try {
            Pageable pageable = PageRequest.of(page, size);
            ApiResponse<Page<AccountResponse>> apiRs = new ApiResponse<Page<AccountResponse>>();
            apiRs.setMessage("Tìm kiếm user thành công");
            apiRs.setResult(usersManagementService.searchUsers(firstName, lastName, categoryJobCode, salaryJobCode, experienceJobCode, skillName, pageable));
            return apiRs;
		} catch (Exception e) {
			System.out.println(e);
        	throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }
    @GetMapping("/user/phone")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ApiResponse<List<String>> searchPhoneUser(@RequestParam(required = false) String categoryJobCode) { 
    	try {
            ApiResponse<List<String>> apiRs = new ApiResponse<List<String>>();
            apiRs.setMessage("Tìm kiếm số điện thoại của user theo lĩnh vục");
            apiRs.setResult(usersManagementService.PhoneByJobType(categoryJobCode));
            return apiRs;
		} catch (Exception e) {
			System.out.println(e);
        	throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }
    @GetMapping("/user/phone-of-company/{id}")
    public ApiResponse<List<String>> searchPhoneUserOfCompany(@PathVariable Integer id) { 
    	try {
            ApiResponse<List<String>> apiRs = new ApiResponse<List<String>>();
            apiRs.setMessage("Tìm kiếm danh sách số điện thoại của user theo công ty");
            apiRs.setResult(usersManagementService.PhoneByCompany(id));
            return apiRs;
		} catch (Exception e) {
			System.out.println(e);
        	throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }
    @GetMapping("/user/companyId/{phone}")
    public ApiResponse<Integer> getIdCompany(@PathVariable String phone) { 
    	try {
            ApiResponse<Integer> apiRs = new ApiResponse<Integer>();
            apiRs.setMessage("Tìm kiếm id công ti theo số điện thoại ");
            apiRs.setResult(usersManagementService.getIdCompanyByPhone(phone));
            return apiRs;
		} catch (Exception e) {
			System.out.println(e);
        	throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }
    @GetMapping("/user/phone/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ApiResponse<String> searchPhoneByUserId(@PathVariable Integer userId) { 
    	try {
            ApiResponse<String> apiRs = new ApiResponse<String>();
            apiRs.setMessage("Tìm kiếm số điện thoại của user theo id");
            apiRs.setResult(usersManagementService.PhoneByUserId(userId));
            return apiRs;
		} catch (Exception e) {
			System.out.println(e);
        	throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }
    @GetMapping("/user/quantity_candidate")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ApiResponse<Integer> countUserCandidate() { 
    	try {
            ApiResponse<Integer> apiRs = new ApiResponse<Integer>();
            apiRs.setMessage("Số lượng ứng viên hoạt động");
            apiRs.setResult(usersManagementService.countCandidate());
            return apiRs;
		} catch (Exception e) {
			System.out.println(e);
        	throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }
    
}