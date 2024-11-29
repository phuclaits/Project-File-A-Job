package com.doan.AppTuyenDung.Controller;


import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doan.AppTuyenDung.DTO.CvDTO;
import com.doan.AppTuyenDung.DTO.FilterRequest;
import com.doan.AppTuyenDung.DTO.Response.ApiResponse;
import com.doan.AppTuyenDung.DTO.Response.CvByPostResponse;
import com.doan.AppTuyenDung.DTO.Response.CvStatusResponse;
import com.doan.AppTuyenDung.DTO.Response.CvsResponse;
import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Services.CompanyService;
import com.doan.AppTuyenDung.Services.CvService;
import com.doan.AppTuyenDung.Services.JWTUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequestMapping("/cv")
public class CvController {
	@Autowired
	private CvService cvService;
	@Autowired
	private JWTUtils jwtUtils;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private CompanyService companyService;

	@GetMapping("/get-all-cv-by-userId")

	public ApiResponse<Page<CvsResponse>> getAllCvByUserId(@RequestParam(defaultValue = "5") int limit
															,@RequestParam(defaultValue = "0") int offset
															,@RequestParam (required = false) Integer userId) {
		ApiResponse<Page<CvsResponse>> apiRs = new ApiResponse<>();
		Pageable pageable = PageRequest.of(offset, limit);
        Page<CvsResponse> listCv = cvService.getAllCvByUserId(userId,pageable);
        apiRs.setMessage("Lấy thành công danh sách cv của userId: "+ userId);
        apiRs.setResult(listCv);
        return apiRs;
	}
	@PostMapping("/createCVnew")
	@PreAuthorize("hasAnyAuthority('CANDIDATE')")
	public ResponseEntity<Map<String, Object>> CreateCVnew(@RequestHeader("Authorization") String token, 
    @ModelAttribute CvDTO cvs,@RequestPart("filePDF" ) MultipartFile filePDF) {
		Map<String, Object> responseERORR = new HashMap<>();
		if(filePDF == null)
		{
			responseERORR.put("errCode", -1);
            responseERORR.put("errMessage", "Cần bổ sung File PDF Cv");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);    // File PDF không tồn tại
		}
		if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);

        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
		
		try{
            String base64Pdf = Base64.getEncoder().encodeToString(filePDF.getBytes());
            String result = "data:application/pdf;base64," + base64Pdf;
            cvs.setFile(result.getBytes());
        } catch(Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> response = cvService.handleCreateCv(cvs);
        return ResponseEntity.ok(response);
	}

	@GetMapping("/getAllListByPost")
	public CompletableFuture<ResponseEntity<Map<String, Object>>> getAllListCvByPost(
        @RequestParam(defaultValue = "5") Integer limit,
        @RequestParam(defaultValue = "0") Integer offset,
        @RequestParam(required = false) Integer postId) {
    return cvService.getAllListCvByPostAsync(offset, limit, postId)
            .thenApply(response -> ResponseEntity.ok(response));
}

	@GetMapping("/status") 
	@PreAuthorize("hasAnyAuthority('COMPANY','EMPLOYER')")
	public ApiResponse<Page<CvStatusResponse>> getByStatus(@RequestParam String status, 
															@RequestParam Integer idCompany, 
															@RequestParam(defaultValue = "0") int page,
															@RequestParam(defaultValue = "5") int size) {
		Pageable pageable = PageRequest.of(page, size);
		ApiResponse<Page<CvStatusResponse>> rp = new ApiResponse<Page<CvStatusResponse>>();
		rp.setMessage("Lây cv theo trạng thái thành công");
		rp.setResult(cvService.getCvByStatus(status, idCompany, pageable));
		return rp;
	}

    @GetMapping("/detail/{id}") 
	@PreAuthorize("hasAnyAuthority('COMPANY','EMPLOYER')")
	public ApiResponse<CvStatusResponse> getCvById(@PathVariable Integer id) {
		ApiResponse<CvStatusResponse> rp = new ApiResponse<CvStatusResponse>();
		rp.setMessage("Hiện thị chi tiết cv thành công");
		rp.setResult(cvService.getCvById(id));
		return rp;
	}
	@GetMapping("/user-detail/{id}") 
    // @PreAuthorize("hasAnyAuthority('CANDIDATE')")
	public ApiResponse<CvStatusResponse> getCvDetailUser(@PathVariable Integer id) {
		ApiResponse<CvStatusResponse> rp = new ApiResponse<CvStatusResponse>();
		rp.setMessage("Hiển thị chi tiết cv theo user thành công");
		rp.setResult(cvService.getCvDetail(id));
		return rp;
	}



	@PostMapping("/filter")
	// @PreAuthorize("hasAnyAuthority('COMPANY')")
    public ResponseEntity<Map<String, Object>> filterCVBySelection(@RequestBody FilterRequest filterRequest) {
		System.out.println(filterRequest);
        Map<String, Object> response = cvService.filterCVBySelection(filterRequest);
        return ResponseEntity.ok(response);
    }
	@PostMapping("/checkSeeCandidate")
	// @PreAuthorize("hasAnyAuthority('COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> checkSeeCandidate(
            @RequestParam(value = "userId", required = false) Integer userId,
            @RequestParam(value = "companyId", required = false) Integer companyId) {
        Map<String, Object> response = companyService.checkSeeCandidate(userId, companyId);
        return ResponseEntity.ok(response);
    }


	@GetMapping("/statusApplication")
    public CompletableFuture<ResponseEntity<?>> getCVsByStatus(@RequestParam String status) {
        if (status == null || status.isEmpty()) {
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(Map.of(
                "errCode", 1,
                "errMessage", "Missing required parameters!"
            )));
        }
        return cvService.getCVsByStatusAsync(status)
                .thenApply(response -> ResponseEntity.ok(response));
    }

    @PutMapping("/accept")
    public ResponseEntity<Map<String, Object>> acceptCV(@RequestBody Map<String, Object> request) {
        Integer id = (Integer) request.get("id");
        Map<String, Object> response = cvService.updateCVStatus(id, "accepted");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/reject")
    public ResponseEntity<Map<String, Object>> rejectCV(@RequestBody Map<String, Object> request) {
        Integer id = (Integer) request.get("id");
        Map<String, Object> response = cvService.updateCVStatus(id, "rejected");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/review")
    public ResponseEntity<Map<String, Object>> reviewCV(@RequestBody Map<String, Object> request) {
        Integer id = (Integer) request.get("id");
        Map<String, Object> response = cvService.updateCVStatus(id, "under-review");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list-accepted")
    public ResponseEntity<Map<String, Object>> getAcceptedCVs() {
        try {
            Map<String, Object> response = cvService.getCVsByStatus("accepted");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errCode", -1);
            errorResponse.put("errMessage", "Error from server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PutMapping("/schedule-interview")
    public ResponseEntity<Map<String, Object>> scheduleInterview(@RequestBody Map<String, Object> requestBody) {
        try {
            Integer cvId = Integer.valueOf(requestBody.get("cvId").toString());
            String interviewTime = requestBody.get("interviewTime").toString();

            if (cvId == null || interviewTime == null || interviewTime.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("errCode", 1);
                errorResponse.put("errMessage", "Missing required parameters!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }

            Map<String, Object> response = cvService.scheduleInterview(cvId, interviewTime);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("errCode", -1);
            errorResponse.put("errMessage", "Error from server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/by-post/{id}")
	@PreAuthorize("hasAnyAuthority('COMPANY','EMPLOYER')")
    public ApiResponse<Page<CvByPostResponse>> getCvByPostId(@PathVariable Integer id,
											    		@RequestParam(defaultValue = "0") int page,
														@RequestParam(defaultValue = "5") int size) {
		ApiResponse<Page<CvByPostResponse>> rp = new ApiResponse<Page<CvByPostResponse>>();
		Pageable pageable = PageRequest.of(page, size);
		rp.setMessage("Hiển thị chi tiết cv theo user thành công");
		rp.setResult(cvService.getCvByPostId(id,pageable));
		return rp;
	}
    @GetMapping("/statical-by-status")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ApiResponse<Map<String, Integer>> staticCvByStatus() {
    	ApiResponse<Map<String, Integer>> rp = new ApiResponse<Map<String, Integer>>();
		rp.setMessage("Thống kê số lượng cv theo trạng thái thành công");
		rp.setResult(cvService.staticalCvByStatus());
		return rp;
    }
    @GetMapping("/statical-by-status-company/{id}")
    @PreAuthorize("hasAnyAuthority('COMPANY','EMPLOYER')")
    public ApiResponse<Map<String, Integer>> staticCvByStatusnCompany(@PathVariable Integer id) {
    	ApiResponse<Map<String, Integer>> rp = new ApiResponse<Map<String, Integer>>();
		rp.setMessage("Thông kê số lượng cv theo trạng thái và công ty thành công");
		rp.setResult(cvService.staticalCvByStatusnCompany(id));
		return rp;
    }
}
