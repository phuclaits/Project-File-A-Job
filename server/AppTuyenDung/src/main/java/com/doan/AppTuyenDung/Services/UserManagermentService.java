package com.doan.AppTuyenDung.Services;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import com.doan.AppTuyenDung.DTO.Request.ProfileUserRequest;
import com.doan.AppTuyenDung.DTO.Request.ReqRes;
import com.doan.AppTuyenDung.DTO.Request.UserSettingDTO;
import com.doan.AppTuyenDung.DTO.Request.UserUpdateRequest;
import com.doan.AppTuyenDung.DTO.Response.AccountResponse;
import com.doan.AppTuyenDung.DTO.Response.CodeResponse;
import com.doan.AppTuyenDung.DTO.Response.SkillIdRespones;
import com.doan.AppTuyenDung.DTO.Response.SkillResponse;
import com.doan.AppTuyenDung.DTO.Response.UserResponse;
import com.doan.AppTuyenDung.DTO.Response.UserSettingResponse;
import com.doan.AppTuyenDung.DTO.Response.UserUpdateResponse;
import com.doan.AppTuyenDung.Exception.AppException;
import com.doan.AppTuyenDung.Exception.ErrorCode;
import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Repositories.CodeGenderRepository;
import com.doan.AppTuyenDung.Repositories.CodeRuleRepository;
import com.doan.AppTuyenDung.entity.Account;
import com.doan.AppTuyenDung.entity.CodeExpType;
import com.doan.AppTuyenDung.entity.CodeGender;
import com.doan.AppTuyenDung.entity.CodeJobType;
import com.doan.AppTuyenDung.entity.CodeProvince;
import com.doan.AppTuyenDung.entity.CodeRule;
import com.doan.AppTuyenDung.entity.CodeSalaryType;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.multipart.MultipartFile;
import com.doan.AppTuyenDung.DTO.CloudinaryResponse;
import com.doan.AppTuyenDung.DTO.InfoPostDetailDto;
import com.doan.AppTuyenDung.DTO.UserAccountDTO;
import com.doan.AppTuyenDung.Repositories.UserRepository;
import com.doan.AppTuyenDung.Repositories.UserSettingRepository;
import com.doan.AppTuyenDung.Repositories.UserSkillRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeExpTypeRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeJobTypeRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeProvinceRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeSalaryTypeRepository;
import com.doan.AppTuyenDung.Repositories.Specification.UserSpecification;
import com.doan.AppTuyenDung.Services.Notification.UserService;
import com.doan.AppTuyenDung.entity.User;
import com.doan.AppTuyenDung.entity.UserSetting;
import com.doan.AppTuyenDung.entity.UserSkill;



@Service
public class UserManagermentService {
	@Autowired
	private UserRepository usersRepo;
    @Autowired
    private AccountRepository accountRepo;
    @Autowired
    private CodeGenderRepository codeGenderRepo;
    @Autowired
    private CodeRuleRepository ruleRepo;
    @Autowired
    private UserSettingRepository userSettingRepository;
    @Autowired
    private UserSkillRepository userSkillRepository;
    @Autowired
    private CodeJobTypeRepository codeJobTypeRepository;
    @Autowired
    private CodeSalaryTypeRepository codeSalaryTypeRepository;
    @Autowired
    private CodeProvinceRepository codeProvinceRepository;
    @Autowired
    private CodeExpTypeRepository codeExpTypeRepository;
	@Autowired
	private JWTUtils jwtUtils;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserService userService;

	public ReqRes register(ReqRes registrationRequest) {
		ReqRes resp = new ReqRes();

		try {
            boolean accountExists = checkAccountExist(registrationRequest.getPhonenumber());
            if(accountExists) {
                resp.setMessage("Phone number is existed!");
                resp.setStatusCode(500);
                return resp;
            }
            CodeGender gender = codeGenderRepo.findByCode(registrationRequest.getGenderCode());
            if(gender == null) {
            	resp.setMessage("Gender is null!");
                resp.setStatusCode(500);
                return resp;
            }
			User user = new User();
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            user.setEmail(registrationRequest.getEmail());
            user.setImage(registrationRequest.getImage());
            user.setGenderCode(gender);
            User UserResult = usersRepo.save(user);
            if (user!=null) {
                com.doan.AppTuyenDung.ModelFirebase.User uFirease = new com.doan.AppTuyenDung.ModelFirebase.User();
                uFirease.setPhoneNumber(registrationRequest.getPhonenumber());
                uFirease.setRole(registrationRequest.getRoleCode());
                uFirease.setRegistrationToken(null);
                userService.createUser(uFirease);
            	CodeRule rule = ruleRepo.findByCode(registrationRequest.getRoleCode());
                Account account = new Account();
                account.setPhonenumber(registrationRequest.getPhonenumber());
                account.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
                account.setRoleCode(rule);
                account.setUser(user);
                Account accountResult = accountRepo.save(account);
                if (account.getId()>0) {
                    resp.setUser(UserResult);
                    resp.setMessage("User Saved Successfully");
                    resp.setStatusCode(200);
                }
            }
		}catch(Exception e) {
			resp.setStatusCode(500);
			resp.setError(e.getMessage());
		}
		return resp;
	}
	public ReqRes login(ReqRes loginRequest) {
		ReqRes resp = new ReqRes();

		try {
            // Kiểm tra thông tin đăng nhập
            if (loginRequest.getPhonenumber() == null || loginRequest.getPassword() == null) {
                resp.setStatusCode(400);
                resp.setError("Các trường phải đảm bảo đầy đủ!");
                return resp;
            }
            // check tài khoản tồn tại = số điện thoại
            var account = accountRepo.findByPhonenumber(loginRequest.getPhonenumber());
            if (account == null) {
                resp.setStatusCode(404);
                resp.setError("Không tìm thấy số điện thoại! ");
                return resp;
            }
            // xác thực user
			authenticationManager
							.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getPhonenumber()
									, loginRequest.getPassword()));
            // check acc bị ban
            if ("S1".equals(account.getStatusCode())) {
                resp.setStatusCode(403);
                resp.setError("Tài khoản của bạn đã bị khoá. Vui lòng liên hệ hỗ trợ!");
                return resp;
            }
			var jwt = jwtUtils.generateToken(account);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), account);
			CodeRule rule = ruleRepo.findByCode(account.getRoleCode().getCode());
            // done
			resp.setStatusCode(200);
			resp.setRoleCode(rule.getCode());
			resp.setToken(jwt);
			resp.setRefreshToken(refreshToken);
			resp.setExpirationTime("24Hrs");
	        resp.setMessage("Successfully Logged In");
		}catch(Exception e) {
			resp.setStatusCode(500);
			resp.setError(e.getMessage());
		}
		return resp;
	}
	public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        try{
            String phoneNumber = jwtUtils.extractUserName(refreshTokenReqiest.getToken());
            Account account = accountRepo.findByPhonenumber(phoneNumber);
            if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), account)) {
                var jwt = jwtUtils.generateToken(account);
                response.setStatusCode(200);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenReqiest.getToken());
                response.setExpirationTime("24Hr");
                response.setMessage("Successfully Refreshed Token");
            }
            response.setStatusCode(200);
            return response;

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            return response;
        }
    }


    public List<AccountResponse> getAllUsers() throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        List<User> lstUser = usersRepo.findAll();
        List<AccountResponse> lstRP = new ArrayList<AccountResponse>();
        try {
            if(lstUser==null) {
            	throw new AppException(ErrorCode.LISTISNULL);
            }
            else {
                for(User u : lstUser) {
                	lstRP.add(mapToUserResponse(u.getId()));
                }
            }
            
        } catch (Exception e) {
        	throw new Exception(e);
        }
        return lstRP;
    }


    public AccountResponse getUsersById(Integer id) throws Exception {
        AccountResponse accountResponse = new AccountResponse();
        try {
           accountResponse = mapToUserResponse(id);
        } catch (Exception e) {
        	throw new Exception(e);
        }
        return accountResponse;
    }


    public UserUpdateResponse updateUser(UserUpdateRequest updatedUser, MultipartFile fileImage) throws Exception {
    	UserUpdateResponse reqRes = new UserUpdateResponse();

        try {
            Optional<User> userOptional = usersRepo.findById(updatedUser.getId());
            if (userOptional.isPresent()) {
                User existingUser = userOptional.get();
                existingUser.setId(updatedUser.getId());
                existingUser.setFirstName(updatedUser.getFirstName());
                existingUser.setLastName(updatedUser.getLastName());
                existingUser.setAddress(updatedUser.getAddress());
                existingUser.setLastName(updatedUser.getLastName());
                existingUser.setEmail(updatedUser.getEmail());
                CodeGender gender = codeGenderRepo.findByCode(updatedUser.getGenderCode());
                existingUser.setGenderCode(gender);
                
                // existingUser.setImage(updatedUser.getImage());
                String imageUrl = "";
                String imageJobType = updatedUser.getFirstName()+updatedUser.getLastName()+generateRandomNumbers(10)+"Images";
                if (fileImage != null) {
                    try {
                        CloudinaryResponse thumbnailResponse = cloudinaryService.uploadFile(fileImage,imageJobType);
                        imageUrl = thumbnailResponse.getUrl();
                        existingUser.setImage(imageUrl);
                    } catch (Exception e) {
                        throw new AppException(ErrorCode.ERRORCLOUD);
                    }
                    
                }
                else{
                    existingUser.setImage(updatedUser.getImage());
                }
                
                existingUser.setDob(updatedUser.getDob());
                User savedUser = usersRepo.save(existingUser);
                reqRes = mapToUserUpdateResponse(existingUser.getId());

                //reqRes.setMessage("User updated successfully");
            } else {
            	throw new AppException(ErrorCode.USER_EXISTED);
            }
        } catch (Exception e) {
        	throw new Exception(e);
        }
        return reqRes;
    }
    private UserUpdateResponse mapToUserUpdateResponse(Integer id) {
    	 Account account = accountRepo.findByUserId(id);
        if (account == null) {
            return null;
        }

        UserUpdateResponse userUpdateResponse = new UserUpdateResponse();

        if (account.getUser() != null) {
            userUpdateResponse.setId(account.getUser().getId());
            userUpdateResponse.setFirstName(account.getUser().getFirstName());
            userUpdateResponse.setLastName(account.getUser().getLastName());
            userUpdateResponse.setEmail(account.getUser().getEmail());
            userUpdateResponse.setAddressUser(account.getUser().getAddress());
            userUpdateResponse.setImage(account.getUser().getImage());
            userUpdateResponse.setDobUser(account.getUser().getDob() != null ? account.getUser().getDob().toString() : null);
            userUpdateResponse.setIdCompany(account.getUser().getCompanyId());

            if (account.getUser().getGenderCode() != null) {
                userUpdateResponse.setGenderCodeValue(account.getUser().getGenderCode().getValue());
            }
        }

        if (account.getRoleCode() != null) {
            userUpdateResponse.setCodeRoleAccount(account.getRoleCode().getCode());
            userUpdateResponse.setCodeRoleValue(account.getRoleCode().getValue());
        }
        userUpdateResponse.setPhoneNumber(account.getPhonenumber());
        if (account.getStatusCode() != null) {
            userUpdateResponse.setCodeStatusValue(account.getStatusCode().getValue());
        }
        userUpdateResponse.setCreatedAtUser(account.getCreatedAt() != null ? account.getCreatedAt().toString() : null);

        return userUpdateResponse;
    }

    public ProfileUserRequest getProfile(String token) {
    	String phoneNumber = jwtUtils.extractUserName(token);
    	Account account = accountRepo.findByPhonenumber(phoneNumber);
    	User user = account.getUser();
    	ProfileUserRequest profile = new ProfileUserRequest();
    	profile.setFirstName(user.getFirstName());
    	profile.setLastName(user.getLastName());
    	profile.setAddress(user.getAddress());
    	profile.setDob(user.getDob());
    	profile.setEmail(user.getEmail());
    	profile.setGender(user.getGenderCode().getValue());
    	profile.setImage(user.getImage());
    	profile.setPhonenumber(account.getPhonenumber());
    	//profile.setStatus(account.getStatusCode().getValue()); chưa có status tam thoi de active
    	profile.setStatus("active");
    	return profile;
    }

    private boolean checkAccountExist(String phoneNumber) {
        return accountRepo.existsByPhonenumber(phoneNumber);
    }
    @Transactional
	public String setDataUserSetting(UserSettingDTO data) {
	    try {
	    	if (data.getIdUser() == null) {
		        return "Missing required parameters!";
		    }
		    User user = usersRepo.findById(data.getIdUser()).orElse(null);
		    if (user == null) {
		        return "Không tồn tại người dùng này";
		    }

		    createOrUpdateUserSetting(data, user);

		    if (data.getListSkills() != null && !data.getListSkills().isEmpty()) {
		        
                userSkillRepository.deleteByUserId(user.getId());

		        List<UserSkill> userSkills = data.getListSkills().stream().map(skillId -> {
		            UserSkill userSkill = new UserSkill();
		            userSkill.setUserId(user.getId());
		            userSkill.setSkillId(skillId);
		            return userSkill;
		        }).toList();
		        userSkillRepository.saveAll(userSkills);
		    }
		} catch (Exception e) {
			throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}

	    return "Hệ thống đã ghi nhận lựa chọn";
	}

	private void createOrUpdateUserSetting(UserSettingDTO data, User user) {
	    UserSetting userSetting = userSettingRepository.findByUserId(user.getId());
	    if (userSetting == null) {
	        userSetting = new UserSetting();
	        userSetting.setUser(user);
	    }
	    CodeJobType categoryJobCode = codeJobTypeRepository.findByCode(data.getCategoryJobCode());
	    CodeSalaryType salaryJobCode = codeSalaryTypeRepository.findByCode(data.getSalaryJobCode());
	    CodeProvince addressCode = codeProvinceRepository.findByCode(data.getAddressCode());
	    CodeExpType experienceJobCode = codeExpTypeRepository.findByCode(data.getExperienceJobCode());
	    userSetting.setCategoryJobCode(categoryJobCode);
	    userSetting.setSalaryJobCode(salaryJobCode);
	    userSetting.setAddressCode(addressCode);
	    userSetting.setExperienceJobCode(experienceJobCode);
	    userSetting.setFile(data.getFile());
	    userSetting.setIsTakeMail(data.getIsTakeMail());
	    userSetting.setIsFindJob(data.getIsFindJob());

	    userSettingRepository.save(userSetting);
	}
    public Account getUserFromToken(String token) {
        String phonenumber = jwtUtils.extractUserName(token);
        return accountRepo.findByPhonenumber(phonenumber);
    }
    private AccountResponse mapToUserResponse(Integer Id) {
        Account account = accountRepo.findByUserId(Id);
        AccountResponse accountResponse = new AccountResponse();
        if (account != null) {
            CodeResponse roleDataResponse = new CodeResponse();
            if (account.getRoleCode() != null) {
                roleDataResponse.setValue(account.getRoleCode().getValue());
                roleDataResponse.setCode(account.getRoleCode().getCode());
            }
            accountResponse.setCodeRoleAccount(roleDataResponse);

            UserResponse userAccountResponse = new UserResponse();
            if (account.getUser() != null) {
                userAccountResponse.setId(account.getUser().getId());
                userAccountResponse.setFirstName(account.getUser().getFirstName());
                userAccountResponse.setLastName(account.getUser().getLastName());
                userAccountResponse.setEmail(account.getUser().getEmail());
                userAccountResponse.setAddressUser(account.getUser().getAddress());
                userAccountResponse.setImage(account.getUser().getImage());
                userAccountResponse.setDobUser(account.getUser().getDob());
                userAccountResponse.setCompanyId(account.getUser().getCompanyId());

                CodeResponse genderCode = new CodeResponse();
                if (account.getUser().getGenderCode() != null) {
                    genderCode.setValue(account.getUser().getGenderCode().getValue());
                    genderCode.setCode(account.getUser().getGenderCode().getCode());
                }
                userAccountResponse.setGenderCodeValue(genderCode);

                UserSettingResponse userSettingResponse = new UserSettingResponse();
                if (account.getUser().getUserSetting() != null) {
                    userSettingResponse.setId(account.getUser().getUserSetting().getId());
                    if (account.getUser().getUserSetting().getCategoryJobCode() != null) {
                    	CodeResponse codeRes = new CodeResponse();
                    	codeRes.setCode(account.getUser().getUserSetting().getCategoryJobCode().getCode());
                    	codeRes.setValue(account.getUser().getUserSetting().getCategoryJobCode().getValue());
                        userSettingResponse.setCategoryJobCode(codeRes);
                    }
                    if (account.getUser().getUserSetting().getAddressCode() != null) {
                    	CodeResponse codeRes = new CodeResponse();
                    	codeRes.setCode(account.getUser().getUserSetting().getAddressCode().getCode());
                    	codeRes.setValue(account.getUser().getUserSetting().getAddressCode().getValue());
                        userSettingResponse.setAddressCode(codeRes);
                    }
                    if (account.getUser().getUserSetting().getSalaryJobCode() != null) {
                    	CodeResponse codeRes = new CodeResponse();
                    	codeRes.setCode(account.getUser().getUserSetting().getSalaryJobCode().getCode());
                    	codeRes.setValue(account.getUser().getUserSetting().getSalaryJobCode().getValue());
                        userSettingResponse.setSalaryJobCode(codeRes);
                    }
                    if (account.getUser().getUserSetting().getExperienceJobCode() != null) {
                    	CodeResponse codeRes = new CodeResponse();
                    	codeRes.setCode(account.getUser().getUserSetting().getExperienceJobCode().getCode());
                    	codeRes.setValue(account.getUser().getUserSetting().getExperienceJobCode().getValue());
                        userSettingResponse.setExperienceJobCode(codeRes);
                    }
                    userSettingResponse.setIsTakeMail(account.getUser().getUserSetting().getIsTakeMail());
                    userSettingResponse.setIsFindJob(account.getUser().getUserSetting().getIsFindJob());
                    userSettingResponse.setUserId(account.getUser().getId());
                    if(account.getUser().getUserSetting().getFile()!= null) {
                        byte[] pdfBytes = account.getUser().getUserSetting().getFile();
                        String dataFile = new String(pdfBytes, StandardCharsets.UTF_8);
                        userSettingResponse.setFile(dataFile);
                    }
                    else {
                    	userSettingResponse.setFile(null);
                    }
                }
                userAccountResponse.setUserSettingData(userSettingResponse);
            }
            accountResponse.setUserAccountData(userAccountResponse);

            List<UserSkill> lstUSkill = userSkillRepository.findByUserId(Id);
            List<SkillIdRespones> skillResponses = lstUSkill.stream()
                .map(userSkill -> {
                    SkillIdRespones skillIdResponse = new SkillIdRespones();
                    skillIdResponse.setSkillId(userSkill.getSkill().getId());
                    skillIdResponse.setUserId(userSkill.getUserId());

                    SkillResponse skillResponse = new SkillResponse();
                    if (userSkill.getSkill() != null) {
                        skillResponse.setId(userSkill.getSkill().getId());
                        skillResponse.setName(userSkill.getSkill().getName());
                        skillResponse.setCategoryJobCode(userSkill.getSkill().getCategoryJobCode());
                    }

                    skillIdResponse.setSkill(skillResponse);
                    return skillIdResponse;
                })
                .collect(Collectors.toList());

            accountResponse.setListSkills(skillResponses);
            accountResponse.setId(account.getId());
            accountResponse.setPhoneNumber(account.getPhonenumber());
            accountResponse.setCodeStatusValue(account.getStatusCode() != null ? account.getStatusCode().getCode() : null);
            accountResponse.setUserId(account.getUser() != null ? account.getUser().getId() : null);
            accountResponse.setCreatedAtUser(account.getCreatedAt());
            accountResponse.setUpdatedAtUser(new Date());
        }

        return accountResponse;
    }


    public static String generateRandomNumbers(int count) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < count; i++) {
            int randomNumber = random.nextInt(100); // Số ngẫu nhiên từ 0 đến 99
            stringBuilder.append(randomNumber);
        }

        return stringBuilder.toString();
    }
    public Page<AccountResponse> searchUsers(String firstName, String lastName, String categoryJobCode, 
            String salaryJobCode, String experienceJobCode, String skillName, Pageable pageable) {
    	Specification<User> spec = UserSpecification.filterUsers(firstName, lastName, categoryJobCode, 
	                                           salaryJobCode, experienceJobCode, skillName);
    	Page<AccountResponse> pageRs = mapUserPageToUserResponsePage(usersRepo.findAll(spec, pageable));
    	return pageRs;
    }
    public Page<AccountResponse> mapUserPageToUserResponsePage(Page<User> userPage) {
        List<AccountResponse> userResponses = userPage.getContent().stream()
            .map(user -> mapToUserResponse(user.getId()))
            .collect(Collectors.toList());

        return new PageImpl<>(userResponses, userPage.getPageable(), userPage.getTotalElements());
    }
    public List<String> PhoneByJobType( String categoryJobCode) {
    	Specification<User> spec = UserSpecification.filterUsers(null, null, categoryJobCode, 
    			null, null, null);
    	List<User> lstU = usersRepo.findAll(spec);
    	List<String> lstPhone = new ArrayList<String>();
    	for(User u : lstU) {
    		Account account = accountRepo.findByUserId(u.getId());
    		lstPhone.add(account.getPhonenumber());
    	}
    	return lstPhone;
    }
    public List<String> PhoneByCompany( int idCompany) {
    	List<User> lstU = usersRepo.findByCompanyId(idCompany);
    	List<String> lstPhone = new ArrayList<String>();
    	for(User u : lstU) {
    		Account account = accountRepo.findByUserId(u.getId());
    		lstPhone.add(account.getPhonenumber());
    	}
    	return lstPhone;
    }
    public Integer getIdCompanyByPhone(String phone) {
    	Account a = accountRepo.findByPhonenumber(phone);
    	return a.getUser().getCompany().getId();
    }
    public String PhoneByUserId( Integer userId) {
    	Account account = accountRepo.findByUserId(userId);
    	String phoneUser = account.getPhonenumber();
    	return phoneUser;
    }
    public int countCandidate() {
    	 List<Account> lstaccount = accountRepo.findAll();
    	 int count = 0;
    	 for(Account a : lstaccount) {
    		 if(a.getRoleCode().getCode().equals("CANDIDATE")&& a.getStatusCode() != null && a.getStatusCode().getCode().equals("S1")) {
    			 count++;
    		 }
    	 }
    	 return count;
    }
}