package com.doan.AppTuyenDung.Controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64;

import com.doan.AppTuyenDung.DTO.AcceptCompanyByAdminDTO;
import com.doan.AppTuyenDung.DTO.AddUserToCompanyEmployeer;
import com.doan.AppTuyenDung.DTO.BanPostByAdminDTO;
import com.doan.AppTuyenDung.DTO.CompanyRequest;
import com.doan.AppTuyenDung.DTO.CreateUserByEmployeerDTO;
import com.doan.AppTuyenDung.DTO.Request.CompanyDTO;
import com.doan.AppTuyenDung.DTO.Response.ApiResponse;
import com.doan.AppTuyenDung.DTO.Response.CompanyResponse;
import com.doan.AppTuyenDung.DTO.Response.PostResponse;
import com.doan.AppTuyenDung.Exception.AppException;
import com.doan.AppTuyenDung.Exception.ErrorCode;
import com.doan.AppTuyenDung.Repositories.AccountRepository;

import com.doan.AppTuyenDung.Repositories.CodeRuleRepository;
import com.doan.AppTuyenDung.Repositories.CompanyRepository;
import com.doan.AppTuyenDung.Repositories.PostRepository;
import com.doan.AppTuyenDung.Repositories.UserRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeCensorStatusRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeStatusRepository;
import com.doan.AppTuyenDung.Services.CloudinaryService;
import com.doan.AppTuyenDung.Services.CompanyService;
import com.doan.AppTuyenDung.Services.JWTUtils;
import com.doan.AppTuyenDung.entity.Company;
import com.doan.AppTuyenDung.entity.Post;
import com.doan.AppTuyenDung.entity.User;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AccountRepository accountRepo;


    @PutMapping("/update-company")
    public ResponseEntity<Map<String, Object>> updateCompanybyCompany(@RequestHeader("Authorization") String token,
                                                             @ModelAttribute Company data, 
                                                             @RequestPart(value= "thumbnailImage", required = false) MultipartFile thumbnailImage,
                                                             @RequestPart(value = "coverimagefile", required = false) MultipartFile coverimagefile,
                                                             @RequestPart(value = "filePDF", required = false) MultipartFile filePDF) 
    {   
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if(account.getUser().getCompany().getId() != data.getId()) {
            Map<String, Object> result = new HashMap<>();
            result.put("errCode", 2);
            result.put("errMessage", "You are not allowed to update this company!");
            return ResponseEntity.badRequest().body(result);
        }
        if(data.getId() == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("errCode", 1);
            result.put("errMessage", "Missing required parameters!");
            return ResponseEntity.badRequest().body(result);
        }
        try{
            String base64Pdf = Base64.getEncoder().encodeToString(filePDF.getBytes());
            String result = "data:application/pdf;base64," + base64Pdf;
            data.setFile(result.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        Map<String, Object> response = companyService.updateCompanybyCompany(data, thumbnailImage, coverimagefile);
        return ResponseEntity.ok(response);
    }



    @PutMapping("/update/{companyID}")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ApiResponse<CompanyResponse> updateCompany(@PathVariable int companyID, @RequestBody CompanyDTO companyDTO) {
        CompanyResponse updateCPN = companyService.updateCompany(companyID, companyDTO);
        ApiResponse<CompanyResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(updateCPN);
        apiResponse.setMessage("Sửa công ty thành công");
        return apiResponse;
    }


    @GetMapping("/get_company/{companyID}")
    public ApiResponse<CompanyResponse> getCompanyId(@PathVariable int companyID) {
        ApiResponse<CompanyResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(companyService.getCompanyByID(companyID));
        apiResponse.setMessage("Tìm thấy công ty với id: " + companyID);
        return apiResponse;
    }

    @GetMapping("/get_post_times/{companyID}")
    public ApiResponse<List<PostResponse>> getPostTimes(@PathVariable int companyID) {
        ApiResponse<List<PostResponse>> apiResponse = new ApiResponse<>();
        CompanyResponse companyResponse = companyService.getCompanyByID(companyID);
        if (companyResponse != null) {
            List<PostResponse> postTimes = companyService.getPostIdsAndTimeEnds(companyID);     
            apiResponse.setResult(postTimes);
            apiResponse.setMessage("Danh sách ID và thời gian hết hạn của các bài viết cho công ty ID: " + companyResponse.getId());
        }
        return apiResponse;
    }

    @GetMapping("/get-detail-company-by-userId")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ResponseEntity<?> getDetailCompanyByUserId(
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "companyId", required = false) String companyId) {
        Integer parsedUserId = userId != null && !"null".equalsIgnoreCase(userId) ? Integer.parseInt(userId) : null;
        Integer parsedCompanyId = companyId != null && !"null".equalsIgnoreCase(companyId) ? Integer.parseInt(companyId)
                : null;
        if (userId == null && companyId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errCode", 1);
            errorResponse.put("errMessage", "Missing required parameters!");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        Map<String, Object> response = companyService.getDetailCompanyByUserId(parsedUserId, parsedCompanyId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create-company")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> createNewCompany(@RequestHeader("Authorization") String token,
            @ModelAttribute Company company, @RequestPart("filethumb") MultipartFile filethumb,
            @RequestPart("fileCover") MultipartFile fileCover,
            @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token
        String phonenumber = jwtUtils.extractUserName(token);

        System.out.println(phonenumber);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        company.setUser(account.getUser());

        try {
            String base64Pdf = Base64.getEncoder().encodeToString(file.getBytes());
            String result = "data:application/pdf;base64," + base64Pdf;
            company.setFile(result.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> response = companyService.createNewCompany(company, filethumb, fileCover);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-list-company")
    public ResponseEntity<Map<String, Object>> getListCompany(@RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(required = false) String search) {
        Pageable pageable = PageRequest.of(offset, limit);
        if (search != null && !search.isEmpty()) {
            search = "%" + search + "%";
        }
        Map<String, Object> response = companyService.GetListCompany(search, pageable);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/getAllCompanyByAdmin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAllCompaniesByAdmin(@RequestParam(defaultValue = "5") int limit,@RequestParam(defaultValue = "0") int offset, 
                        @RequestParam(required = false) String search, @RequestParam(required = false) String censorCode) {
        
    if (search != null && search.isEmpty()) {
        search = null;
    }
    if (censorCode != null && censorCode.isEmpty()) {
        censorCode = null;
    }
        Pageable pageable = PageRequest.of(offset, limit);
        Map<String, Object> response = companyService.getAllCompaniesByAdmin(search, censorCode, pageable);
        return ResponseEntity.ok(response);
    }      

    @PutMapping("/Accept-company")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> acceptCompanyByRuleAdmin(@RequestBody AcceptCompanyByAdminDTO data) {
        Map<String, Object> result = companyService.acceptCompanyByRuleAdmin(data);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/Ban-company")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> BanCompanyByRuleAdmin(@RequestParam Integer companyId) {
        Map<String, Object> result = companyService.BanCompanyByRuleAdmin(companyId);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/UnBan-company")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> UnBanCompanyByRuleAdmin(@RequestParam Integer companyId) {
        Map<String, Object> result = companyService.UnBanCompanyByRuleAdmin(companyId);
        return ResponseEntity.ok(result);
    }
    

    @PutMapping("/AddUser-ToCompany")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    public ResponseEntity<Map<String, Object>> addUserToCompany(
            @RequestBody AddUserToCompanyEmployeer data) {
        Map<String, Object> result = companyService.handleAddUserCompany(data);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/CreateEmployee-FromCompany")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY')")
    public ResponseEntity<Map<String, Object>> createNewUser(@RequestBody CreateUserByEmployeerDTO data) {
        Map<String, Object> result = companyService.handleCreateNewUser(data);
        return ResponseEntity.ok(result);
    }
  
    @GetMapping("/getAllUserByCompanyId")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> getAllUserByCompanyId(@RequestParam Integer companyId,@RequestParam int limit,@RequestParam int offset) {
        
        Map<String, Object> result = companyService.getAllUserByCompanyId(companyId, limit, offset);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/CancelCompanyByEmployer")
    @PreAuthorize("hasAnyAuthority('COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> CancelCompanyByEmployer(@RequestParam Integer userId) {
        Map<String, Object> result = companyService.CancelCompanyByEmployer(userId);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/quantity-company") 
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ApiResponse<Integer> countCompanyActive() {
    	try {
        	ApiResponse<Integer>  apiRs = new ApiResponse<Integer>();
        	apiRs.setMessage("Số lượng công ty đang hoạt động");
        	apiRs.setResult(companyService.countCompanyActive());
        	return apiRs;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }

    @GetMapping("/checkStatusCompany")
    public ResponseEntity<Map<String, Object>> checkstatusCompany(@RequestParam("companyId") Integer companyId) {
        if (companyId == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("errCode", 3);
            result.put("errMessage", "Công ty không tồn tại");
            return ResponseEntity.ok(result);
        }
        ResponseEntity<Map<String, Object>> responseEntity = companyService.checkstatusCompany(companyId);
        return responseEntity;
        
    }
    
}
