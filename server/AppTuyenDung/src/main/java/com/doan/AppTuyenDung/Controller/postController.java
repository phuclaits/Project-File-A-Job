package com.doan.AppTuyenDung.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doan.AppTuyenDung.DTO.AcceptPostByAdminDTO;
import com.doan.AppTuyenDung.DTO.ActivePostByAdminDTO;
import com.doan.AppTuyenDung.DTO.BanPostByAdminDTO;
import com.doan.AppTuyenDung.DTO.DetailPostDTO;
import com.doan.AppTuyenDung.DTO.GetAllPostRuleAdminDTO;
import com.doan.AppTuyenDung.DTO.PostFilterDTO;
import com.doan.AppTuyenDung.DTO.ReupPostDTO;
import com.doan.AppTuyenDung.DTO.UpdatePostDTO;
import com.doan.AppTuyenDung.DTO.CreatePostEmployerCompany.PostDTO;
import com.doan.AppTuyenDung.DTO.Response.ApiResponse;
import com.doan.AppTuyenDung.DTO.Response.PostJobTypeCountDTO;
import com.doan.AppTuyenDung.DTO.Response.PostResponse;
import com.doan.AppTuyenDung.Exception.AppException;
import com.doan.AppTuyenDung.Exception.ErrorCode;
import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeStatusRepository;
import com.doan.AppTuyenDung.Repositories.CompanyRepository;
import com.doan.AppTuyenDung.Repositories.NoteReponsitory;
import com.doan.AppTuyenDung.Repositories.UserRepository;
import com.doan.AppTuyenDung.Services.postService;
import com.doan.AppTuyenDung.entity.Account;
import com.doan.AppTuyenDung.entity.CodeStatus;
import com.doan.AppTuyenDung.entity.Company;
import com.doan.AppTuyenDung.entity.User;



@RestController
@RequestMapping("/post")
public class postController {
    @Autowired
    private NoteReponsitory noteReponsitoryService;
    @Autowired
    private postService postService;
    @Autowired ModelMapper modelMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CodeStatusRepository codeStatusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @GetMapping("/get-filter-post")
    public ResponseEntity<Page<DetailPostDTO>> getFilteredDetailPosts(@RequestParam(required = false) String categoryJobCode,
                                                              @RequestParam(required = false) String addressCode,
                                                              @RequestParam(required = false) String search,
                                                              @RequestParam(required = false) List<String> experienceJobCodes,
                                                              @RequestParam(required = false) List<String> categoryWorktypeCodes,
                                                              @RequestParam(required = false) List<String> salaryJobCodes,
                                                              @RequestParam(required = false) List<String> categoryJoblevelCodes,
                                                              @RequestParam(required = false) Integer isHot,
                                                              @RequestParam(defaultValue = "0") int offset,
                                                              @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<DetailPostDTO> detailPosts = postService.getFilteredDetailPosts(categoryJobCode, addressCode, (search !=null ? search.toUpperCase() : null),
                                                                            experienceJobCodes, categoryWorktypeCodes,
                                                                            salaryJobCodes, categoryJoblevelCodes,
                                                                            isHot, pageable);
        return ResponseEntity.ok(detailPosts);
    }
    @GetMapping("/get-list-job-count-post")
    public Page<PostJobTypeCountDTO> getListJobTypeAndCountPost(@RequestParam(defaultValue = "0") int offset,
                                                                @RequestParam(defaultValue = "10") int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        return  postService.getPostJobTypeAndCountPost(pageable);
    }
    @GetMapping("/get-detail-post-by-id") 
    public Map<String, Object> getPostDetailById(@RequestParam Integer id) {
        return postService.getPostDetailById(id);
    }
    
    @GetMapping("/get-statistical-post")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> getStatisticalTypePost(
            @RequestParam int limit) {

        Map<String, Object> response = postService.getStatisticalTypePost(limit);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-search-post")
    public ApiResponse<Page<PostResponse>> getSearchDetailPosts(@RequestParam(required = false) String name,
    														  @RequestParam(required = false) String categoryJobCode,
                                                              @RequestParam(required = false) String addressCode,
                                                              @RequestParam(required = false) List<String> experienceJobCode,
                                                              @RequestParam(required = false) List<String> categoryWorktypeCode,
                                                              @RequestParam(required = false) List<String> salaryJobCode,
                                                              @RequestParam(required = false) List<String> categoryJoblevelCode,
                                                              @RequestParam(required = false) Integer isHot,
                                                              @RequestParam(defaultValue = "0") int offset,
                                                              @RequestParam(defaultValue = "10") int limit) {
    	ApiResponse<Page<PostResponse>> apiRP = new ApiResponse<Page<PostResponse>>();
    	try {
    		Pageable pageable = PageRequest.of(offset, limit);
            Page<PostResponse> postRP = postService.searchPosts(name,categoryJobCode, categoryWorktypeCode,
    											        		addressCode, experienceJobCode,
    											        		categoryJoblevelCode, salaryJobCode,
                                                                isHot, pageable);
            apiRP.setResult(postRP);
            return apiRP;
    	}
    	catch (Exception e) {
    		System.out.println(e);
    		throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }
    @GetMapping("/get-search-sort-post")
    public ApiResponse<Page<PostResponse>> getSearchnSortDetailPosts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryJobCode,
            @RequestParam(required = false) String addressCode,
            @RequestParam(required = false) List<String> experienceJobCode,
            @RequestParam(required = false) List<String> categoryWorktypeCode,
            @RequestParam(required = false) List<String> salaryJobCode,
            @RequestParam(required = false) List<String> categoryJoblevelCode,
            @RequestParam(required = false) Integer isHot,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        ApiResponse<Page<PostResponse>> apiRP = new ApiResponse<Page<PostResponse>>();
        try {
            Sort sort = Sort.by(
                    Sort.Order.desc("isHot"),
                    Sort.Order.desc("createdAt")
            );

            Pageable pageable = PageRequest.of(offset, limit, sort);
            
            Page<PostResponse> postRP = postService.searchPosts(
                    name, categoryJobCode, categoryWorktypeCode,
                    addressCode, experienceJobCode, categoryJoblevelCode,
                    salaryJobCode, isHot, pageable);

            apiRP.setResult(postRP);
            return apiRP;
        } catch (Exception e) {
            System.out.println(e);
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @GetMapping("/statistical-cv")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    //Chưa gắn Fe
    public ApiResponse<Page<PostResponse>> getStatisticalCv(@RequestParam(value = "fromDate", required = false) 
													    @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
													    @RequestParam(value = "toDate", required = false)  
													    @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
                                                        @RequestParam(required = false) String companyId,
                                                        @RequestParam(defaultValue = "0") int offset,
                                                        @RequestParam(defaultValue = "10") int limit) {
    	ApiResponse<Page<PostResponse>> apiRP = new ApiResponse<Page<PostResponse>>();
    	try {
    		Pageable pageable = PageRequest.of(offset, limit);
            Page<PostResponse> postRP = postService.statisticalCv(fromDate, toDate, companyId, pageable);
            apiRP.setResult(postRP);
            System.out.println(fromDate +" vs " + toDate);
            return apiRP;
    	}
    	catch (Exception e) {
    		System.out.println(e);
    		throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
    }
    @PostMapping("/create-new-post")
    @PreAuthorize("hasAnyAuthority('COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> HandleCreateNewPost(@RequestBody PostDTO data) {
        Account findAccount = accountRepository.findByUserId(data.getUserId());
        CodeStatus codeStatusS2 = codeStatusRepository.findByCode("S2");
        if(findAccount.getStatusCode() == codeStatusS2)
        {
            Map<String, Object> response1 = new HashMap<>();
            response1.put("errCode", 2);
            response1.put("errMessage", "Tài khoản bạn đang tạm bị khoá, vui lòng liên hệ với quản trị viên lahoangphuc03@gmail.com");
            return ResponseEntity.ok(response1);
        }
        
        Optional<User> user = userRepository.findById(data.getUserId());
        int idCompany = user.get().getCompanyId();
        Optional<Company> company = companyRepository.findById(idCompany);
        if(!company.isPresent() || company.get().getStatusCode().equals(codeStatusS2))
        {
            Map<String, Object> response1 = new HashMap<>();
            response1.put("errCode", 2);
            response1.put("errMessage", "Công ty đang tạm bị đình chỉ, vui lòng liên hệ với quản trị viên lahoangphuc03@gmail.com");
            return ResponseEntity.ok(response1);
        }
        Map<String, Object> response = postService.CreateNewPost(data);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/get-list-post-admin")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ResponseEntity<Page<PostFilterDTO>> Getlistpostadmin(@RequestParam(required = false) Integer idCompany,
                                                              @RequestParam(defaultValue = "0") int offset,
                                                              @RequestParam(defaultValue = "5") int limit,
                                                              @RequestParam(required = false) String search,
                                                              @RequestParam(required = false) String censorCode) {
        if (censorCode == null || censorCode.isEmpty()) {
            censorCode = null;
        }
        if (search == null || search.isEmpty()) {
            search = null;
        }
        Pageable pageable = PageRequest.of(offset, limit);
        Page<PostFilterDTO> response = postService.GetlistpostadminService(pageable, idCompany, search, censorCode);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/get-All-Post-RoleAdmin")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<GetAllPostRuleAdminDTO>> getAllPostRoleAdmin(@RequestParam(defaultValue = "0") int offset,
                                                                        @RequestParam(defaultValue = "5") int limit,
                                                                        @RequestParam(required = false) String search,
                                                                        @RequestParam(required = false) String censorCode) {
        if (censorCode == null || censorCode.isEmpty()) {
            censorCode = null;
        }
        if (search == null || search.isEmpty()) {
            search = null;
        }   
        Pageable pageable = PageRequest.of(offset, limit);
        Page<GetAllPostRuleAdminDTO> listPost = postService.GetAllPostRoleAdminService(pageable, search, censorCode);
        return ResponseEntity.ok(listPost);
    }

    //admin accept post 
    @PutMapping("/accept-post")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> acceptPostByRuleAdmin(@RequestBody AcceptPostByAdminDTO data) {
        Map<String, Object> result = postService.acceptPostByRuleAdmin(data);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/ban-post")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> banPostByRuleAdmin(@RequestBody BanPostByAdminDTO data) {
        Map<String, Object> result = postService.banPostByRuleAdmin(data);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/activate-post")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Map<String, Object>> activatePostByRuleAdmin(@RequestBody ActivePostByAdminDTO data) {
        Map<String, Object> result = postService.activatePostByRuleAdmin(data);
        return ResponseEntity.ok(result);
    }
    @PutMapping("/update-post")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> updatePost(@RequestBody UpdatePostDTO data) {
        Map<String, Object> result = postService.handleUpdatePost(data);
        return ResponseEntity.ok(result);
    }
    @GetMapping("/check-post")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    //Chưa gắn fe
    public ResponseEntity<Map<String, Object>> CheckPost(@RequestParam Integer detailPostId, 
    @RequestParam Integer id) {
        Map<String, Object> result = postService.checkPostExists(detailPostId,id);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/reup-post")
    @PreAuthorize("hasAnyAuthority('COMPANY','EMPLOYER')")
    public ResponseEntity<Map<String, Object>> reupPost(@RequestBody ReupPostDTO data) {
        Map<String, Object> result = postService.handleReupPost(data);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-note-by-post")
    public ResponseEntity<Map<String, Object>> getListNoteByPost(
            @RequestParam Integer id,
            @RequestParam int limit,
            @RequestParam int offset) {
            System.out.println("get note by post");
        return postService.getListNoteByPost(id, limit, offset);
    }
    @GetMapping("/job-type-count")
    @PreAuthorize("hasAnyAuthority('ADMIN','COMPANY','EMPLOYER')")
    public ApiResponse<Map<String, Integer>> getJobTypeCount() {
    	ApiResponse<Map<String, Integer>> apiRs  = new ApiResponse<Map<String, Integer>>();
    	apiRs.setMessage("Danh sách post theo nghề");
    	apiRs.setResult(postService.getJobTypeCount());
        return apiRs;
    }

}
