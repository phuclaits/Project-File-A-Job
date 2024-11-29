package com.doan.AppTuyenDung.Services;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.ZoneId;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doan.AppTuyenDung.DTO.Request.CompanyDTO;
import com.doan.AppTuyenDung.DTO.Response.CodeResponse;
import com.doan.AppTuyenDung.DTO.Response.CompanyResponse;
import com.doan.AppTuyenDung.DTO.Response.PostResponse;
import com.doan.AppTuyenDung.DTO.Response.postDetailResponse;
import com.doan.AppTuyenDung.Exception.AppException;
import com.doan.AppTuyenDung.Exception.ErrorCode;
import com.doan.AppTuyenDung.Repositories.CodeCensorstatusRepository;
import com.doan.AppTuyenDung.Repositories.CodeGenderRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeStatusRepository;
import com.doan.AppTuyenDung.Repositories.CompanyRepository;
import com.doan.AppTuyenDung.Repositories.PostRepository;
import com.doan.AppTuyenDung.Repositories.UserRepository;
import com.doan.AppTuyenDung.entity.CodeStatus;
import com.doan.AppTuyenDung.entity.CodeCensorstatus;
import com.doan.AppTuyenDung.entity.CodeGender;
import com.doan.AppTuyenDung.entity.Company;
import com.doan.AppTuyenDung.entity.Post;
import com.doan.AppTuyenDung.entity.User;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

import com.doan.AppTuyenDung.DTO.AcceptCompanyByAdminDTO;
import com.doan.AppTuyenDung.DTO.AddUserToCompanyEmployeer;
import com.doan.AppTuyenDung.DTO.BanPostByAdminDTO;
import com.doan.AppTuyenDung.DTO.CloudinaryResponse;
import com.doan.AppTuyenDung.DTO.CompanyGetListDTO;
import com.doan.AppTuyenDung.DTO.CreateUserByEmployeerDTO;
import com.doan.AppTuyenDung.DTO.GetAllCompaniesByAdmin;
import com.doan.AppTuyenDung.DTO.GetAllUserByCompanyIdDTO;
import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Repositories.CodeRuleRepository;
import com.doan.AppTuyenDung.entity.Account;
import com.doan.AppTuyenDung.entity.CodeRule;
@Service
public class CompanyService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private CodeStatusRepository codeStatusRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CodeRuleRepository codeRuleRepository;
    @Autowired
    private CodeCensorstatusRepository censorstatusRepository;
    @Autowired
    private CodeStatusRepository codestatusRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
	private PasswordEncoder passwordEncoder;
    @Autowired
    private CodeGenderRepository codeGenderRepository;
    

    public CompanyResponse banCompany(int id) {
      Optional<Company> companyOptional = companyRepository.findById(id);
      Company company = companyOptional.get();
      String status = company.getStatusCode().getCode();
      if(status != "S2") {
        CodeStatus code = codeStatusRepository.findByCode("S2");
        company.setStatusCode(code);
    }
        Company companyRs = companyRepository.save(company);
      return convertEntityToDTO(company);
    }
    public CompanyResponse unBanCompany(int id) {
      Optional<Company> companyOptional = companyRepository.findById(id);
      Company company = companyOptional.get();
      String status = company.getStatusCode().getCode();
      if(status != "S1") {
        CodeStatus code = codeStatusRepository.findByCode("S1");
        company.setStatusCode(code);
      }
      Company companyRs = companyRepository.save(company);
      return convertEntityToDTO(company);
    }
    public List<CompanyResponse> getCompanies() {
      List<Company> lstCompany = companyRepository.findAll();
      List<CompanyResponse> lstCpnDTO = new ArrayList<CompanyResponse>(); 
      for(Company c : lstCompany) {
        lstCpnDTO.add(convertEntityToDTO(c));
      }
      return lstCpnDTO;
    }
    public CompanyResponse getCompanyByID(int id) {
      Optional<Company> companyOptional = companyRepository.findById(id);
      Company company = companyOptional.get();
      return convertEntityToDTO(company); 
    }
    public List<PostResponse> getPostIdsAndTimeEnds(int companyId) {
        Optional<Company> companyOptional = companyRepository.findById(companyId);
        Company company = companyOptional.get();
        
        List<Post> lstPost = postRepository.findByUserId(company.getUser().getId());
    
        return lstPost.stream()
                .map(post -> {
                    PostResponse postResponse = new PostResponse();
                    postResponse.setId(post.getId());
                    postResponse.setTimeEnd(post.getTimeEnd());
                    return postResponse;
                })
                .collect(Collectors.toList());
    }
    public CompanyResponse updateCompany(int companyId, CompanyDTO companyDTO) {
        Optional<Company> companyOptional = companyRepository.findById(companyId);
        if (companyOptional.isPresent()) {
            Company company = companyOptional.get();
            convertDTOToEntity(companyDTO, company);
            company.setUpdatedAt(new Date()); 
            Company updatedCompany = companyRepository.save(company);
            return convertEntityToDTO(updatedCompany);
        } else {
            throw new AppException(ErrorCode.NOTEXISTCOMPANY);
        }
    }

    private void convertDTOToEntity(CompanyDTO companyDTO, Company company) {
    	CodeStatus code = codeStatusRepository.findByCode(companyDTO.getStatusCode());
    	CodeCensorstatus codeCS = censorstatusRepository.findByCode(companyDTO.getCensorCode());
        company.setName(companyDTO.getName());
        company.setThumbnail(companyDTO.getThumbnail());
        company.setCoverImage(companyDTO.getCoverImage());
        company.setDescriptionHTML(companyDTO.getDescriptionHTML());
        company.setDescriptionMarkdown(companyDTO.getDescriptionMarkdown());
        company.setWebsite(companyDTO.getWebsite());
        company.setAddress(companyDTO.getAddress());
        company.setPhonenumber(companyDTO.getPhonenumber());
        company.setAmountEmployer(companyDTO.getAmountEmployer());
        company.setTaxnumber(companyDTO.getTaxnumber());
        company.setStatusCode(code);
        company.setFile(companyDTO.getFile().getBytes());
        company.setAllowPost(companyDTO.getAllowPost());
        company.setAllowHotPost(companyDTO.getAllowHotPost());
        company.setAllowCvFree(companyDTO.getAllowCvFree());
        company.setAllowCV(companyDTO.getAllowCV());
        company.setCensorCode(codeCS);
        Optional<User> userOptional = userRepository.findById(companyDTO.getIdUser());
        User user = userOptional.get();
        company.setUser(user);
    }

    private CompanyResponse convertEntityToDTO(Company company) {
    	CompanyResponse companyResponse = new CompanyResponse();
    	companyResponse.setId(company.getId());
    	companyResponse.setName(company.getName() != null ? company.getName() : "Chưa có tên");
    	companyResponse.setThumbnail(company.getThumbnail() != null ? company.getThumbnail() : "Chưa có thumbnail");
    	companyResponse.setCoverImage(company.getCoverImage() != null ? company.getCoverImage() : "Chưa có cover image");
    	companyResponse.setDescriptionHTML(company.getDescriptionHTML() != null ? company.getDescriptionHTML() : "Chưa có mô tả HTML");
    	companyResponse.setDescriptionMarkdown(company.getDescriptionMarkdown() != null ? company.getDescriptionMarkdown() : "Chưa có mô tả Markdown");
    	companyResponse.setWebsite(company.getWebsite() != null ? company.getWebsite() : "Chưa có website");
        companyResponse.setAddress(company.getAddress() != null ? company.getAddress() : "Chưa có địa chỉ");
        companyResponse.setPhonenumber(company.getPhonenumber() != null ? company.getPhonenumber() : "Chưa có số điện thoại");
        companyResponse.setAmountEmployer(company.getAmountEmployer() != null ? company.getAmountEmployer() : 0);
        companyResponse.setTaxnumber(company.getTaxnumber() != null ? company.getTaxnumber() : "Chưa có mã số thuế");     
        companyResponse.setStatusCode(company.getStatusCode() != null ? company.getStatusCode().getCode() : "Chưa có trạng thái");
        if(company.getFile() == null) {
        	companyResponse.setFile("Chưa có file");
        }
        else {
        	byte[] byteArray = company.getFile();
        	//String encodedString = Base64.getEncoder().encodeToString(byteArray);
        	String decodedString = new String(byteArray, StandardCharsets.UTF_8);
        	companyResponse.setFile(decodedString);
        }
        companyResponse.setAllowPost(company.getAllowPost() != null ? company.getAllowPost() : 0);
        companyResponse.setAllowHotPost(company.getAllowHotPost() != null ? company.getAllowHotPost() : 0);
        companyResponse.setAllowCvFree(company.getAllowCvFree() != null ? company.getAllowCvFree() : 0);
        companyResponse.setAllowCV(company.getAllowCV() != null ? company.getAllowCV() : 0);
        companyResponse.setCensorCode(company.getCensorCode() != null ? company.getCensorCode().getCode() : "Chưa có mã kiểm duyệt");
        companyResponse.setUserId(company.getUser() != null ? company.getUser().getId() : null);
        companyResponse.setCreatedAt(company.getCreatedAt() != null ? company.getCreatedAt() : null);
        companyResponse.setUpdatedAt(company.getUpdatedAt() != null ? company.getUpdatedAt() : null);
        
        CodeResponse censorData = new CodeResponse();
        if(company.getCensorCode() != null) {
        	censorData.setCode(company.getCensorCode().getCode());
        	censorData.setValue(company.getCensorCode().getValue());
            companyResponse.setCensorData(censorData);
        }
        List<PostResponse> lstPostResponse = new ArrayList<PostResponse>();
        if(company.getUser() != null) {
        	List<Post> lstPost = postRepository.findByUserId(company.getUser().getId());
        	if(!lstPost.isEmpty()) {
        		for(Post p : lstPost) {
        	        PostResponse postData = new PostResponse();
                	postData.setUserId(p.getUser().getId());
                	postData.setCreatedAt(p.getCreatedAt());
                	postData.setId(p.getId());
                	postData.setIsHot(p.getIsHot());
                	postData.setThumbnail(company.getThumbnail() != null ? company.getThumbnail() : "Chưa có thumbnail");
                	postData.setStatusCode(p.getStatusCode().getCode());
                	postData.setTimeEnd(p.getTimeEnd());
                	postData.setTimePost(p.getTimePost());
                	postData.setUpdatedAt(p.getUpdatedAt());
            		postDetailResponse postDetailResponse = new postDetailResponse();
                	if(p.getDetailPost()!=null) {
                		postDetailResponse.setId(p.getDetailPost().getId());
                		postDetailResponse.setName(p.getDetailPost().getName());
                		postDetailResponse.setDescriptionHTML(p.getDetailPost().getDescriptionHTML());
                		postDetailResponse.setDescriptionMarkdown(p.getDetailPost().getDescriptionMarkdown());
                		postDetailResponse.setAmount(p.getDetailPost().getAmount());
                		CodeResponse jobTypePostData = new CodeResponse();
                        if(p.getDetailPost().getCategoryJobCode() != null) {
                        	jobTypePostData.setCode(p.getDetailPost().getCategoryJobCode().getCode());
                        	jobTypePostData.setValue(p.getDetailPost().getCategoryJobCode().getValue());
                        	postDetailResponse.setJobTypePostData(jobTypePostData);
                        }
                        CodeResponse workTypePostData = new CodeResponse();
                        if(p.getDetailPost().getCategoryWorktypeCode() != null) {
                        	workTypePostData.setCode(p.getDetailPost().getCategoryWorktypeCode().getCode());
                        	workTypePostData.setValue(p.getDetailPost().getCategoryWorktypeCode().getValue());
                        	postDetailResponse.setWorkTypePostData(workTypePostData);
                        }
                        CodeResponse salaryTypePostData = new CodeResponse();
                        if(p.getDetailPost().getSalaryJobCode()  != null) {
                        	salaryTypePostData.setCode(p.getDetailPost().getSalaryJobCode().getCode());
                        	salaryTypePostData.setValue(p.getDetailPost().getSalaryJobCode().getValue());
                        	postDetailResponse.setSalaryTypePostData(salaryTypePostData);
                        }
                        CodeResponse jobLevelPostData = new CodeResponse();
                        if(p.getDetailPost().getCategoryJoblevelCode()  != null) {
                        	jobLevelPostData.setCode(p.getDetailPost().getCategoryJoblevelCode().getCode());
                        	jobLevelPostData.setValue(p.getDetailPost().getCategoryJoblevelCode().getValue());
                        	postDetailResponse.setJobLevelPostData(jobLevelPostData);
                        }
                        CodeResponse genderPostData = new CodeResponse();
                        if(p.getDetailPost().getGenderPostCode() != null) {
                        	genderPostData.setCode(p.getDetailPost().getGenderPostCode().getCode());
                        	genderPostData.setValue(p.getDetailPost().getGenderPostCode().getValue());
                        	postDetailResponse.setGenderPostData(genderPostData);
                        }
                        CodeResponse provincePostData = new CodeResponse();
                        if(p.getDetailPost().getAddressCode() != null) {
                        	provincePostData.setCode(p.getDetailPost().getAddressCode().getCode());
                        	provincePostData.setValue(p.getDetailPost().getAddressCode().getValue());
                        	postDetailResponse.setProvincePostData(provincePostData);
                        }
                        CodeResponse expTypePostData = new CodeResponse();
                        if(p.getDetailPost().getExperienceJobCode() != null) {
                        	expTypePostData.setCode(p.getDetailPost().getExperienceJobCode().getCode());
                        	expTypePostData.setValue(p.getDetailPost().getExperienceJobCode().getValue());
                        	postDetailResponse.setExpTypePostData(expTypePostData);
                        }
                        postData.setPostDetailData(postDetailResponse);
                	}
                	lstPostResponse.add(postData);
        		}
        	}
            companyResponse.setPostData(lstPostResponse);
        }
        return companyResponse;
    }
    public Map<String, Object> getDetailCompanyByUserId(Integer userId, Integer companyId) {
        Map<String, Object> response = new HashMap<>();

        if (userId == null && companyId == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        try {
            Company company = null;

            if (userId != null) {
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    int idCompany = user.getCompanyId().intValue();
                    company = companyRepository.findById(idCompany).orElse(null);
                }
            } else {
                company = companyRepository.findById(companyId).orElse(null);
            }

            if (company == null) {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy công ty người dùng sở hữu");
            } else {
                if (company.getFile() != null) {
                    // byte[] decodedFile = Base64.getDecoder().decode(company.getFile());
                    // String fileAsBinary = new String(decodedFile, StandardCharsets.UTF_8);
                    company.setFile(company.getFile());
                }
                response.put("errCode", 0);
                response.put("data", company);
            }
        } catch (Exception e) {
            response.put("errCode", 3);
            response.put("errMessage", "An error occurred");
        }

        return response;
    }


    public Map<String, Object> createNewCompany(Company company,MultipartFile filethumb,MultipartFile fileCover) {
        Map<String, Object> response = new HashMap<>();

        if (company.getName() == null || company.getPhonenumber() == null || company.getAddress() == null
        || company.getDescriptionHTML() == null || company.getDescriptionMarkdown() == null
        || company.getAmountEmployer() == 0 || company.getUser().getId() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        // Check if company name exists
        if (companyRepository.existsByName(company.getName())) {
            response.put("errCode", 2);
            response.put("errMessage", "Tên công ty đã tồn tại");
            return response;
        }
        company.setName(company.getName());
        try {
            String thumbnailUrl = "";
            String coverImageUrl = "";
            String FileNamethumb = company.getName()+"ThumbnailsImage";
            String fileNameCover = company.getName()+"CoverImage";
            // Upload to Cloudinary
            if (company.getThumbnail() == null && company.getCoverImage() == null && filethumb != null &&fileCover != null) {
                CloudinaryResponse thumbnailResponse = cloudinaryService.uploadFile(filethumb,FileNamethumb);
                thumbnailUrl = thumbnailResponse.getUrl();
                CloudinaryResponse coverImageResponse = cloudinaryService.uploadFile(fileCover,fileNameCover);
                coverImageUrl = coverImageResponse.getUrl();
            }

            // Set URLs for uploaded images
            company.setThumbnail(thumbnailUrl);
            company.setCoverImage(coverImageUrl);

            // Set other default values
            CodeStatus status = codeStatusRepository.findByCode("S1");
            company.setStatusCode(status);

            // Set CensorCode nếu cần
            CodeCensorstatus censorCode = censorstatusRepository.findById(company.getFile() != null ? "CS3" : "CS2")
                    .orElseThrow(() -> new RuntimeException("CodeCensorstatus not found"));
            company.setCensorCode(censorCode);


            // value default khi tạo
            company.setAllowPost(0); 
            company.setAllowHotPost(0); 
            company.setAllowCvFree(0); 
            company.setAllowCV(0); 
            company.setCreatedAt(Date.from(Instant.now())); 
            company.setUpdatedAt(Date.from(Instant.now()));
            

            company.setDescriptionHTML(company.getDescriptionHTML());
            company.setDescriptionMarkdown(company.getDescriptionMarkdown());
            company.setWebsite(company.getWebsite());
            company.setAddress(company.getAddress());
            company.setPhonenumber(company.getPhonenumber());
            company.setAmountEmployer(company.getAmountEmployer());
            company.setTaxnumber(company.getTaxnumber());
            // Save new company
            System.out.println("id user: "+company.getUser().getId());
            Company savedCompany = companyRepository.save(company);

            // Update user and account
            Optional<User> userOpt = userRepository.findById(company.getUser().getId());
            System.out.println(userOpt.isPresent());
            Account accountOpt = accountRepository.findByUserId(company.getUser().getId());
            System.out.println("ID accountOpt : "+ accountOpt.getId());
            if (userOpt.isPresent() && accountOpt != null) {
                User user = userOpt.get();
                Account account = accountOpt;
            
                // Cập nhật thông tin cho User và Account
                user.setCompanyId(company.getId());
                userRepository.save(user);
            
                // tìm thấy `CodeRule`
                CodeRule companyRole = codeRuleRepository.findById("COMPANY")
                    .orElseThrow(() -> new RuntimeException("CodeRule not found"));
                // save
                account.setRoleCode(companyRole);
                accountRepository.save(account);
            } else {
                // Xử lý khi không tìm thấy User hoặc Account
                if (!userOpt.isPresent()) {
                    throw new RuntimeException("User not found");
                }
                if (accountOpt == null) {
                    throw new RuntimeException("Account not found");
                }
            }
            
            if (userOpt.isPresent() && accountOpt != null) {
                User user = userOpt.get();
                // Account account = accountOpt.get();

                user.setCompanyId(savedCompany.getId());
                userRepository.save(user);

                CodeRule companyRole = codeRuleRepository.findById("COMPANY")
                .orElseThrow(() -> new RuntimeException("CodeRule not found"));
                accountOpt.setRoleCode(companyRole);
                accountRepository.save(accountOpt);

                response.put("errCode", 0);
                response.put("errMessage", "Đã tạo công ty thành công");
                response.put("company", savedCompany);  // Trả về đối tượng Company vừa tạo
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy người dùng");
            }
        } catch (Exception e) {
            response.put("errCode", 3);
            response.put("errMessage", "An error occurred");
        }

        return response;
    }



    public Map<String, Object> GetListCompany(String search, Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        try {
			if (search != null && !search.isEmpty()) 
			{
				search = "%" + search + "%";
			}
            Page<CompanyGetListDTO> company = companyRepository.getListCompany(search, pageable);
            response.put("errCode", 0);
            response.put("errMessage","Get list company successfully");
            response.put("data", company);
        } catch (Exception e) {
            response.put("errCode", 3);
            response.put("errMessage", "Error Query");
        }
        return response;
    }

    public Map<String, Object> getAllCompaniesByAdmin(String search, String censorCode,Pageable pageable) {
        Map<String, Object> response = new HashMap<>();
        
        Page<GetAllCompaniesByAdmin> companiesPage = companyRepository.getAllCompaniesByAdmin(
                search,
                censorCode,
                pageable
        );

        response.put("errCode", 0);
        response.put("data", companiesPage.getContent());
        response.put("count", companiesPage.getTotalElements());
        return response;
    }

    public Map<String, Object> acceptCompanyByRuleAdmin(AcceptCompanyByAdminDTO data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getCompanyId() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        Optional<Company> foundCompanyOpt = companyRepository.findById(data.getCompanyId());
        if (foundCompanyOpt.isPresent()) {
            Company foundCompany = foundCompanyOpt.get();
            
            if ("null".equals(data.getNote())) {
                CodeCensorstatus censorCodeCS1 = censorstatusRepository.findByCode("CS1");
                foundCompany.setCensorCode(censorCodeCS1);
                foundCompany.setAllowPost(foundCompany.getAllowPost() + 5);
                foundCompany.setAllowCvFree(foundCompany.getAllowCvFree() + 5);
            } else {
                CodeCensorstatus censorCodeCS2 = censorstatusRepository.findByCode("CS2");
                foundCompany.setCensorCode(censorCodeCS2);
            }

            companyRepository.save(foundCompany);

            String note = !"null".equals(data.getNote()) ? data.getNote() : 
                          "Công ty " + foundCompany.getName() + " của bạn đã kiểm duyệt thành công";
            
            Optional<User> userOpt = userRepository.findById(foundCompany.getUser().getId());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                if (!"null".equals(data.getNote())) {
                    emailService.sendCompanyApprovalEmail(user.getEmail(), foundCompany.getName(), false, note); // cancel
                } else {
                    emailService.sendCompanyApprovalEmail(user.getEmail(), foundCompany.getName(), true, null); //sucess
                }
                response.put("errCode", 0);
                response.put("errMessage", !"null".equals(data.getNote()) ? 
                                          "Đã quay lại trạng thái chờ" : 
                                          "Đã duyệt công ty thành công");
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "User không tồn tại");
            }
        } else {
            response.put("errCode", 2);
            response.put("errMessage", "Không tồn tại công ty");
        }
        return response;
    }
    public Map<String, Object> BanCompanyByRuleAdmin(Integer companyId) {
        Map<String, Object> response = new HashMap<>();

        if (companyId == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        Optional<Company> foundCompanyOpt = companyRepository.findById(companyId);
        if (foundCompanyOpt.isPresent()) {
            Company foundCompany = foundCompanyOpt.get();
            CodeStatus codeStatusS2 = codestatusRepository.findByCode("S2");
            foundCompany.setStatusCode(codeStatusS2);
            companyRepository.save(foundCompany);

            response.put("errCode", 0);
            response.put("errMessage", "Đã dừng hoạt động công ty");
        } else {
            response.put("errCode", 2);
            response.put("errMessage", "Công ty không tồn tại");
        }

        return response;
    }
    public Map<String, Object> UnBanCompanyByRuleAdmin(Integer companyId) {
        Map<String, Object> response = new HashMap<>();

        if (companyId == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        Optional<Company> foundCompanyOpt = companyRepository.findById(companyId);
        if (foundCompanyOpt.isPresent()) {
            Company foundCompany = foundCompanyOpt.get();
            CodeStatus codeStatus = codestatusRepository.findByCode("S1");
            foundCompany.setStatusCode(codeStatus);  
            companyRepository.save(foundCompany);

            response.put("errCode", 0);
            response.put("message", "Đã mở hoạt động cho công ty");
        } else {
            response.put("errCode", 2);
            response.put("errMessage", "Công ty không tồn tại");
        }

        return response;
    }


    public Map<String, Object> handleAddUserCompany(AddUserToCompanyEmployeer data) {
        Map<String, Object> response = new HashMap<>();

        // Check if required parameters are provided
        if (data.getPhonenumber() == null || data.getCompanyId() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        // Check if company exists
        Optional<Company> companyOpt = companyRepository.findById(data.getCompanyId());
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();

            // Check if user with the given phone number exists
            Account accountOpt = accountRepository.findByPhonenumber(data.getPhonenumber());
            if (accountOpt != null) {
                
                
                // Check if account has the EMPLOYER role
                if (!"EMPLOYER".equals(accountOpt.getRoleCode().getCode())) {
                    response.put("errCode", 1);
                    response.put("errMessage", "Tài khoản không phải là nhà tuyển dụng");
                    return response;
                }

                // Retrieve user associated with the account
                Optional<User> userOpt = userRepository.findById(accountOpt.getUser().getId());
                if (userOpt.isPresent()) {
                    User user = userOpt.get();

                    // Check if user is already associated with a company
                    if (user.getCompanyId() != null) {
                        response.put("errCode", 3);
                        response.put("errMessage", "Nhân viên đã có công ty");
                    } else {
                        // Associate user with the company
                        user.setCompanyId(data.getCompanyId());
                        userRepository.save(user);

                        response.put("errCode", 0);
                        response.put("errMessage", "Đã thêm nhà tuyển dụng vào công ty");
                    }
                } else {
                    response.put("errCode", 2);
                    response.put("errMessage", "Số điện thoại không tồn tại !");
                }
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Số điện thoại không tồn tại !");
            }
        } else {
            response.put("errCode", 2);
            response.put("errMessage", "Công ty không tồn tại !");
        }

        return response;
    }



    public Map<String, Object> handleCreateNewUser(CreateUserByEmployeerDTO data) {
        Map<String, Object> response = new HashMap<>();
        if (data.getPhonenumber() == null || data.getLastName() == null || data.getFirstName() == null || data.getEmail() == null||
            data.getGenderCode() == null || data.getDob() == null || data.getCompanyId() == null || data.getRoleCode() == null || data.getAddress() == null ) {
            response.put("errCode", 2);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        // Check if phone number already exists
        Account existingAccount = accountRepository.findByPhonenumber(data.getPhonenumber());
        if (existingAccount != null) {
            response.put("errCode", 1);
            response.put("errMessage", "Số điện thoại đã tồn tại!");
            return response;
        }

        try {
            String imageUrl = "";
            boolean isHavePass = true;
            if (data.getPassword() == null) {
                data.setPassword(String.valueOf(System.currentTimeMillis()));
                isHavePass = false;
            }
            if (data.getImage() != null) {
                imageUrl = "https://i.pinimg.com/1200x/bc/43/98/bc439871417621836a0eeea768d60944.jpg";
            }

            if (data.getEmail() == null) {
                data.setEmail("defaultemail@example.com");
            }

            // Create user parameters
            User user = new User();
            user.setFirstName(data.getFirstName());
            user.setLastName(data.getLastName());
            user.setAddress(data.getAddress());
            CodeGender codeGender = codeGenderRepository.findByCode(data.getGenderCode());
            user.setGenderCode(codeGender);
            user.setImage(imageUrl);
            user.setDob(data.getDob());
            user.setCompanyId(data.getCompanyId());
            user.setEmail(data.getEmail());
            
            // Save user
            user = userRepository.save(user);

            // Create account if user is saved
            if (user != null) {
                Account account = new Account();
                account.setPhonenumber(data.getPhonenumber());
                account.setPassword(passwordEncoder.encode(data.getPassword()));
                CodeRule role = codeRuleRepository.findByCode(data.getRoleCode());
                account.setRoleCode(role);
                CodeStatus codeStatus = codeStatusRepository.findByCode("S1");
                account.setStatusCode(codeStatus);

                account.setUser(user);
                accountRepository.save(account);
            }

            // Send email if password was autogenerated
            if (!isHavePass) {
                String note = "Tài khoản đã tạo thành công \n " +
                              "Tài khoản: " + data.getPhonenumber() + "\n " +
                              "Mật khẩu: " + data.getPassword() +"\n Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi";
                emailService.sendSimpleEmail(data.getEmail(), "Đã tạo tài khoản thành công, Vui lòng kiểm tra hộp thư tài khoản đăng nhập!", note);
            }

            response.put("errCode", 0);
            response.put("message", "Tạo tài khoản thành công");

        } catch (Exception e) {
            response.put("errCode", 3);
            response.put("errMessage", e.getMessage());
        }

        return response;
    }

    public Map<String, Object> getAllUserByCompanyId(Integer companyId, int limit, int offset) {
        Map<String, Object> response = new HashMap<>();

        if (companyId == null || limit <= 0 || offset < 0) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameter!");
            return response;
        }

        try {
            PageRequest pageRequest = PageRequest.of(offset / limit, limit);
            Page<GetAllUserByCompanyIdDTO> userPage = userRepository.getAllUserByCompanyId(companyId, pageRequest);

            response.put("errCode", 0);
            response.put("data", userPage.getContent());
            response.put("count", userPage.getTotalElements());
        } catch (Exception e) {
            response.put("errCode", 3);
            response.put("errMessage", e.getMessage());
        }

        return response;
    }


    @Transactional
    public Map<String, Object> CancelCompanyByEmployer(Integer userId) {
        Map<String, Object> response = new HashMap<>();

        if (userId == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            response.put("errCode", 2);
            response.put("errMessage", "Người dùng không tồn tại");
            return response;
        }

        User user = userOpt.get();
        Account accountOpt = accountRepository.findByUserId(user.getId());

        if (accountOpt!=null) {
            // Account account = accountOpt.get();

            // Update roleCode if necessary
            if ("COMPANY".equals(accountOpt.getRoleCode().getCode())) {
                CodeRule role = codeRuleRepository.findByCode("EMPLOYER");
                accountOpt.setRoleCode(role);
                accountRepository.save(accountOpt);
            }
        }

        // Update posts to assign them to the company owner
        Optional<Company> companyOpt = companyRepository.findById(user.getCompanyId());
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();

            // Find posts by this user
            List<Post> userPosts = postRepository.findByUserId(user.getId());
            for (Post post : userPosts) {
                User us = userRepository.findById(company.getUser().getId()).get();
                post.setUser(us);  // Reassign post to company's owner
                postRepository.save(post);
            }
        }

        // Remove user from company
        user.setCompanyId(null);
        userRepository.save(user);

        response.put("errCode", 0);
        response.put("errMessage", "Đã rời công ty thành công");
        return response;
    }

    public Map<String, Object> checkSeeCandidate(Integer userId, Integer companyId) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (userId == null && companyId == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            Optional<Company> companyOptional;
            
            if (userId != null) {
                Optional<User> userOptional = userRepository.findById(userId);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    companyOptional = companyRepository.findById(user.getCompanyId());
                } else {
                    response.put("errCode", 2);
                    response.put("errMessage", "Không tìm thấy công ty người dùng sở hữu");
                    return response;
                }
            } else {
                companyOptional = companyRepository.findById(companyId);
            }

            if (!companyOptional.isPresent()) {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy công ty người dùng sở hữu");
                return response;
            }

            Company company = companyOptional.get();

            if (company.getAllowCvFree() > 0) {
                company.setAllowCvFree(company.getAllowCvFree() - 1);
                companyRepository.save(company);
                response.put("errCode", 0);
                response.put("errMessage", "Đã cập nhật thành công lượt xem ");
            } else if (company.getAllowCV() > 0) {
                company.setAllowCV(company.getAllowCV() - 1);
                companyRepository.save(company);
                response.put("errCode", 0);
                response.put("errMessage", "Đã cập nhật thành công lượt xem");
            } else {
                response.put("errCode", 1);
                response.put("errMessage", "Công ty bạn đã hết lượt xem");
            }

        } catch (Exception e) {
            response.put("errCode", 3);
            response.put("errMessage", "An error occurred");
        }

        return response;
    }
    public Integer countCompanyActive() {
    	List<Company> lstCompany = companyRepository.findAll();
    	int cnt = 0;
    	for(Company c : lstCompany) {
    		if(c.getStatusCode()!=null && c.getStatusCode().getCode().equals("S1")) 
    			cnt++;
    	}
    	return cnt;
    }


    public ResponseEntity<Map<String, Object>> checkstatusCompany(Integer companyid)
    {
        
        Map<String, Object> result = new HashMap<>();
        Company company = companyRepository.findById(companyid).orElse(null);
        if(company != null) {
            if(company.getCensorCode().getCode().equals("CS1"))
            {
                result.put("errCode", 1);
                result.put("errMessage", "Công ty Đã kiểm duyệt");
                return ResponseEntity.ok(result);
            }
            else if(company.getStatusCode().getCode().equals("CS2"))
            {
                result.put("errCode", 2);
                result.put("errMessage", "Công ty Chưa kiểm duyệt");
                return ResponseEntity.ok(result);
            }
            else{
                result.put("errCode", 3);
                result.put("errMessage", "Công ty đang chờ kiểm duyệt");
                return ResponseEntity.ok(result);
            }
        }
        result.put("errCode", 3);
        result.put("errMessage", "Công ty không tồn tại");
        return ResponseEntity.ok(result);
    }

    public Map<String, Object> updateCompanybyCompany(Company company, MultipartFile filethumb, MultipartFile fileCover) {
        Map<String, Object> response = new HashMap<>();
    
        if (company.getId() == null || company.getName() == null || company.getPhonenumber() == null || company.getAddress() == null
            || company.getDescriptionHTML() == null || company.getDescriptionMarkdown() == null || company.getDescriptionHTML() == null ||
            company.getWebsite() == null || company.getTaxnumber() == null ) {
            
            if(company.getAmountEmployer() <=0){
                response.put("errCode", 1);
                response.put("errMessage", "Số lượng nhân viên phải lớn hơn 0");
                return response;
            }
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }
    
        // Kiểm tra công ty có tồn tại hay không
        Optional<Company> existingCompanyOpt = companyRepository.findById(company.getId());
        if (!existingCompanyOpt.isPresent()) {
            response.put("errCode", 2);
            response.put("errMessage", "Công ty không tồn tại");
            return response;
        }
    
        Company existingCompany = existingCompanyOpt.get();
        
        // Kiểm tra tên công ty đã thay đổi hay chưa, nếu thay đổi thì kiểm tra trùng lặp
        if (!existingCompany.getName().equals(company.getName()) && companyRepository.existsByName(company.getName())) {
            response.put("errCode", 2);
            response.put("errMessage", "Tên công ty đã tồn tại");
            return response;
        }
    
        // Cập nhật thông tin công ty
        existingCompany.setName(company.getName());
        existingCompany.setDescriptionHTML(company.getDescriptionHTML());
        existingCompany.setDescriptionMarkdown(company.getDescriptionMarkdown());
        existingCompany.setWebsite(company.getWebsite());
        existingCompany.setAddress(company.getAddress());
        existingCompany.setPhonenumber(company.getPhonenumber());
        existingCompany.setAmountEmployer(company.getAmountEmployer());
        existingCompany.setTaxnumber(company.getTaxnumber());
        if(company.getFile()!=null) {
            existingCompany.setFile(company.getFile());    
        }

        // Xử lý ảnh thumbnail và cover nếu có
        try {
            String thumbnailUrl = existingCompany.getThumbnail();
            String coverImageUrl = existingCompany.getCoverImage();
    
            if (filethumb != null) {
                CloudinaryResponse thumbnailResponse = cloudinaryService.uploadFile(filethumb, company.getName() + "ThumbnailsImage");
                thumbnailUrl = thumbnailResponse.getUrl();
                existingCompany.setThumbnail(thumbnailUrl);
            }
            if (fileCover != null) {
                CloudinaryResponse coverImageResponse = cloudinaryService.uploadFile(fileCover, company.getName() + "CoverImage");
                coverImageUrl = coverImageResponse.getUrl();
                existingCompany.setCoverImage(coverImageUrl);
            }   
            // Cập nhật CensorCode nếu có thay đổi file
            
            if(company.getFile() == null){
                System.out.println("không có file");
            }
            CodeCensorstatus censorCode = censorstatusRepository.findById(company.getFile() != null ? "CS3" : "CS2")
                    .orElseThrow(() -> new RuntimeException("CodeCensorstatus not found"));
            existingCompany.setCensorCode(censorCode);
    
            // Cập nhật ngày cập nhật công ty
            existingCompany.setUpdatedAt(Date.from(Instant.now()));
    
            // Cập nhật trạng thái công ty (không thay đổi trạng thái từ "S1")
            if (company.getStatusCode() != null) {
                CodeStatus status = codeStatusRepository.findByCode("S1");
                existingCompany.setStatusCode(status);
            }
    
            // Lưu lại thông tin công ty đã cập nhật
            companyRepository.save(existingCompany);
            response.put("errCode", 0);
            response.put("errMessage", "Cập nhật công ty thành công");
            response.put("company", existingCompany);  

        } catch (Exception e) {
            response.put("errCode", 3);
            response.put("errMessage", "Đã xảy ra lỗi trong quá trình cập nhật");
        }
    
        return response;
    }
}
