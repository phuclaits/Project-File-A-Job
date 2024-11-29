package com.doan.AppTuyenDung.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.doan.AppTuyenDung.DTO.GetAllExpTypeAdmin.ExpTypeDTO;
import com.doan.AppTuyenDung.DTO.GetAllExpTypeAdmin.ResponseAllExpType;
import com.doan.AppTuyenDung.DTO.GetAllJobLevelAdmin.JobLevelDTO;
import com.doan.AppTuyenDung.DTO.GetAllJobLevelAdmin.ResponseAllJobLevel;
import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.JobtypeDTO;
import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.ResponseAllJobType;
import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.ResponseDelete;
import com.doan.AppTuyenDung.DTO.GetAllSalaryTypeAdmin.ResponseAllSalaryType;
import com.doan.AppTuyenDung.DTO.GetAllSalaryTypeAdmin.SalaryTypeDTO;
import com.doan.AppTuyenDung.DTO.GetAllWorkTypeAdmin.ResponseAllWorkType;
import com.doan.AppTuyenDung.DTO.GetAllWorkTypeAdmin.WorkTypeDTO;
import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Services.AllCodeService;
import com.doan.AppTuyenDung.Services.JWTUtils;
import com.doan.AppTuyenDung.entity.CodeExpType;
import com.doan.AppTuyenDung.entity.CodeJobLevel;
import com.doan.AppTuyenDung.entity.CodeJobType;
import com.doan.AppTuyenDung.entity.CodeSalaryType;
import com.doan.AppTuyenDung.entity.CodeWorkType;

@RestController
@RequestMapping("/admin/code")
@PreAuthorize("hasAnyAuthority('ADMIN')")
public class CodeAdminController {

    @Autowired
    private JWTUtils jwtUtils; 

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private AllCodeService allCodeService;

    @PostMapping("/create-jobType")
    public ResponseEntity<Map<String, Object>> handleCreateJobType(@RequestHeader("Authorization") String token, 
                                                            @ModelAttribute CodeJobType data,
                                                            @RequestPart("fileimage" ) MultipartFile fileimage) {
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
        Map<String, Object> response = allCodeService.handleCreateNewJobType(data,fileimage);
        return ResponseEntity.ok(response);
    }
    
    
    @PutMapping("/update-jobtype")
    public ResponseEntity<Map<String, Object>> handleUpdateJobType(@RequestHeader("Authorization") String token, 
                                                        @ModelAttribute CodeJobType data,
                                                        @RequestPart("filethumb" ) MultipartFile filethumb) 
    {

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
        data.setType("JOBTYPE");
        Map<String, Object> response = allCodeService.handleUpdateJobType(data,filethumb);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/get-all-jobtype")
    public ResponseEntity<ResponseAllJobType<Page<JobtypeDTO>>> getAllJobType(@RequestHeader("Authorization") String token,
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
            ResponseAllJobType<Page<JobtypeDTO>> response = new ResponseAllJobType<>(-1, "Error from Client without access", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
			if (search != null && !search.isEmpty()) 
			{
				search = "%" + search + "%";
			}
            Page<JobtypeDTO> jobtype = allCodeService.GetAllJobType(search,pageable);
            ResponseAllJobType<Page<JobtypeDTO>> response = new ResponseAllJobType<>(0, "Lấy dữ liệu thành công", jobtype);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseAllJobType<Page<JobtypeDTO>> response = new ResponseAllJobType<>(-1, "Error from server", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
    @DeleteMapping("/delete-jobtype")
    public ResponseEntity<?> handleDelete(@RequestHeader("Authorization") String token,@RequestParam String code) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseDelete response = new ResponseDelete(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return allCodeService.handleDeleteAllCode(code);
    }

    @GetMapping("/get-detail-JobType-by-code")
    public ResponseEntity<ResponseAllJobType> getDetail(@RequestHeader("Authorization") String token, @RequestParam String code) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseAllJobType response = new ResponseAllJobType(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        ResponseEntity<ResponseAllJobType> responseEntity = allCodeService.getDetailJobTypeByCode(code);
        ResponseAllJobType response = responseEntity.getBody();
        return ResponseEntity.ok(response);
    }

    // JOB LEVEL

    @PostMapping("/create-jobLevel")
    public ResponseEntity<Map<String, Object>> handleCreateJobType(@RequestHeader("Authorization") String token, 
                                                                    @RequestBody CodeJobLevel data) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        System.out.println(phonenumber);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        data.setImage("");
        Map<String, Object> response = allCodeService.handleCreateNewJobLevel(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-joblevel")
    public ResponseEntity<ResponseAllJobType<Page<JobLevelDTO>>> getAllJobLevel(@RequestHeader("Authorization") String token,
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
            ResponseAllJobType<Page<JobLevelDTO>> response = new ResponseAllJobType<>(-1, "Error from Client without access", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
			if (search != null && !search.isEmpty()) 
			{
				search = "%" + search + "%";
			}
            Page<JobLevelDTO> jobtype = allCodeService.GetAllJobLevel(search,pageable);
            ResponseAllJobType<Page<JobLevelDTO>> response = new ResponseAllJobType<>(0, "Lấy dữ liệu thành công", jobtype);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseAllJobType<Page<JobLevelDTO>> response = new ResponseAllJobType<>(-1, "Error from server", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }
    @DeleteMapping("/delete-joblevel")
    public ResponseEntity<?> handleDeleteJobLevel(@RequestHeader("Authorization") String token,@RequestParam String code) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseDelete response = new ResponseDelete(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return allCodeService.handleDeleteJobLevel(code);
    }

    @PutMapping("/update-joblevel")
    public ResponseEntity<Map<String, Object>> handleUpdateJobLevel(@RequestHeader("Authorization") String token, 
                                                        @ModelAttribute CodeJobLevel data) 
    {

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
        data.setType("JOBLEVEL");
        Map<String, Object> response = allCodeService.handleUpdateJobLevel(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-detail-JobLevel-by-code")
    public ResponseEntity<ResponseAllJobLevel> getDetailJobLevelByCode(@RequestHeader("Authorization") String token, @RequestParam String code) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseAllJobLevel response = new ResponseAllJobLevel(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        ResponseEntity<ResponseAllJobLevel> responseEntity = allCodeService.getDetailJobLevelByCode(code);
        ResponseAllJobLevel response = responseEntity.getBody();
        return ResponseEntity.ok(response);
    }

    // WORK TYPE
    @PostMapping("/create-worktype")
    public ResponseEntity<Map<String, Object>> handleCreateWorkType(@RequestHeader("Authorization") String token, 
                                                                    @RequestBody CodeWorkType data) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        System.out.println(phonenumber);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        data.setImage("");
        Map<String, Object> response = allCodeService.handleCreateNewWorkType(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-worktype")
    public ResponseEntity<ResponseAllJobType<Page<WorkTypeDTO>>> getAllWorkType(@RequestHeader("Authorization") String token,
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
            ResponseAllJobType<Page<WorkTypeDTO>> response = new ResponseAllJobType<>(-1, "Error from Client without access", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
			if (search != null && !search.isEmpty()) 
			{
				search = "%" + search + "%";
			}
            Page<WorkTypeDTO> jobtype = allCodeService.GetAllWorkType(search,pageable);
            ResponseAllJobType<Page<WorkTypeDTO>> response = new ResponseAllJobType<>(0, "Lấy dữ liệu thành công", jobtype);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseAllJobType<Page<WorkTypeDTO>> response = new ResponseAllJobType<>(-1, "Error from server", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @DeleteMapping("/delete-worktype")
    public ResponseEntity<?> handleDeleteWorkType(@RequestHeader("Authorization") String token,@RequestParam String code) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseDelete response = new ResponseDelete(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return allCodeService.handleDeleteWorkType(code);
    }

    @PutMapping("/update-worktype")
    public ResponseEntity<Map<String, Object>> handleUpdateWorkType(@RequestHeader("Authorization") String token, 
                                                                    @RequestBody CodeWorkType data) 
    {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        System.out.println(phonenumber);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        data.setType("WORKTYPE");
        Map<String, Object> response = allCodeService.handleUpdateWorkType(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-detail-WorkType-by-code")
    public ResponseEntity<ResponseAllWorkType> getDetailWorkTypeByCode(@RequestHeader("Authorization") String token, @RequestParam String code) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseAllWorkType response = new ResponseAllWorkType(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        ResponseEntity<ResponseAllWorkType> responseEntity = allCodeService.getDetailWorkTypeByCode(code);
        ResponseAllWorkType response = responseEntity.getBody();
        return ResponseEntity.ok(response);
    }
    // SALARY TYPE
    @PostMapping("/create-salarytype")
    public ResponseEntity<Map<String, Object>> handleCreateWorkType(@RequestHeader("Authorization") String token, 
                                                                    @RequestBody CodeSalaryType data) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        System.out.println(phonenumber);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        data.setImage("");
        Map<String, Object> response = allCodeService.handleCreateNewSalaryType(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-salarytype")
    public ResponseEntity<ResponseAllSalaryType<Page<SalaryTypeDTO>>> getAllSalaryType(@RequestHeader("Authorization") String token,
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
            ResponseAllSalaryType<Page<SalaryTypeDTO>> response = new ResponseAllSalaryType<>(-1, "Error from Client without access", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
			if (search != null && !search.isEmpty()) 
			{
				search = "%" + search + "%";
			}
            Page<SalaryTypeDTO> jobtype = allCodeService.GetAllSalaryType(search,pageable);
            ResponseAllSalaryType<Page<SalaryTypeDTO>> response = new ResponseAllSalaryType<>(0, "Lấy dữ liệu thành công", jobtype);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseAllSalaryType<Page<SalaryTypeDTO>> response = new ResponseAllSalaryType<>(-1, "Error from server", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    @DeleteMapping("/delete-salarytype")
    public ResponseEntity<?> handleDeleteSalaryType(@RequestHeader("Authorization") String token,@RequestParam String code) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseDelete response = new ResponseDelete(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return allCodeService.handleDeleteSalaryType(code);
    }

    @PutMapping("/update-salarytype")
    public ResponseEntity<Map<String, Object>> handleUpdateSalaryType(@RequestHeader("Authorization") String token, 
                                                                        @RequestBody CodeSalaryType data) 
    {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        System.out.println(phonenumber);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        data.setType("SALARYTYPE");
        Map<String, Object> response = allCodeService.handleUpdateSalaryType(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-detail-SalaryType-by-code")
    public ResponseEntity<ResponseAllSalaryType> getDetailSalaryTypeByCode(@RequestHeader("Authorization") String token, @RequestParam String code) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseAllSalaryType response = new ResponseAllSalaryType(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        ResponseEntity<ResponseAllSalaryType> responseEntity = allCodeService.getDetailSalaryTypeByCode(code);
        ResponseAllSalaryType response = responseEntity.getBody();
        return ResponseEntity.ok(response);
    }

    // exp
    @PostMapping("/create-exptype")
    public ResponseEntity<Map<String, Object>> handleCreateExpType(@RequestHeader("Authorization") String token, 
                                                            @RequestBody CodeExpType data) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        System.out.println(phonenumber);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        data.setImage("");
        Map<String, Object> response = allCodeService.handleCreateNewExpType(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-all-exptype")
    public ResponseEntity<ResponseAllExpType<Page<ExpTypeDTO>>> getAllExpType(@RequestHeader("Authorization") String token,
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
            ResponseAllExpType<Page<ExpTypeDTO>> response = new ResponseAllExpType<>(-1, "Error from Client without access", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
			if (search != null && !search.isEmpty()) 
			{
				search = "%" + search + "%";
			}
            Page<ExpTypeDTO> exptype = allCodeService.GetAllExpType(search,pageable);
            ResponseAllExpType<Page<ExpTypeDTO>> response = new ResponseAllExpType<>(0, "Lấy dữ liệu thành công", exptype);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ResponseAllExpType<Page<ExpTypeDTO>> response = new ResponseAllExpType<>(-1, "Error from server", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }


    @DeleteMapping("/delete-exptype")
    public ResponseEntity<?> handleDeleteExpType(@RequestHeader("Authorization") String token,@RequestParam String code) {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseDelete response = new ResponseDelete(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        return allCodeService.handleDeleteExpType(code);
    }

    @PutMapping("/update-exptype")
    public ResponseEntity<Map<String, Object>> handleUpdateSalaryType(@RequestHeader("Authorization") String token, 
                                                        @RequestBody CodeExpType data) 
    {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        System.out.println(phonenumber);
        var account = accountRepo.findByPhonenumber(phonenumber);
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        data.setType("EXPTYPE");
        Map<String, Object> response = allCodeService.handleUpdateExpType(data);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-detail-ExpType-by-code")
    public ResponseEntity<ResponseAllExpType> getDetailExpTypeByCode(@RequestHeader("Authorization") String token, @RequestParam String code) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7).trim();
        }
        // Giải mã token 
        String phonenumber = jwtUtils.extractUserName(token);
        var account = accountRepo.findByPhonenumber(phonenumber);
        System.out.println(account.getRoleCode().getCode());
        if (account == null || !account.getRoleCode().getCode().equals("ADMIN") ) {
            ResponseAllExpType response = new ResponseAllExpType(-1, "Error from Client without access");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        
        ResponseEntity<ResponseAllExpType> responseEntity = allCodeService.getDetailExpTypeByCode(code);
        ResponseAllExpType response = responseEntity.getBody();
        return ResponseEntity.ok(response);
    }


}
