package com.doan.AppTuyenDung.Services;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Repositories.CompanyRepository;
import com.doan.AppTuyenDung.Repositories.DetailPostRepository;
import com.doan.AppTuyenDung.Repositories.NoteReponsitory;
import com.doan.AppTuyenDung.Repositories.PostRepositoriesQuery;
import com.doan.AppTuyenDung.Repositories.PostRepository;
import com.doan.AppTuyenDung.Repositories.UserRepository;

import com.doan.AppTuyenDung.Repositories.AllCode.CodeExpTypeRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeGenderPostRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeJobLevelRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeJobTypeRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodePostStatusRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeProvinceRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeSalaryTypeRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeWorkTypeRepository;
import com.doan.AppTuyenDung.Repositories.Specification.PostSpecification;

import java.util.stream.Collectors;

import com.doan.AppTuyenDung.DTO.AcceptPostByAdminDTO;
import com.doan.AppTuyenDung.DTO.ActivePostByAdminDTO;
import com.doan.AppTuyenDung.DTO.BanPostByAdminDTO;
import com.doan.AppTuyenDung.DTO.DetailPostDTO;
import com.doan.AppTuyenDung.DTO.GetAllPostRuleAdminDTO;
import com.doan.AppTuyenDung.DTO.Response.PostJobTypeCountDTO;
import com.doan.AppTuyenDung.DTO.Response.PostResponse;
import com.doan.AppTuyenDung.entity.CodeExpType;
import com.doan.AppTuyenDung.entity.CodeGenderPost;
import com.doan.AppTuyenDung.entity.CodeJobLevel;
import com.doan.AppTuyenDung.entity.CodeJobType;
import com.doan.AppTuyenDung.entity.CodePostStatus;
import com.doan.AppTuyenDung.entity.CodeProvince;
import com.doan.AppTuyenDung.entity.CodeSalaryType;
import com.doan.AppTuyenDung.entity.CodeWorkType;
import com.doan.AppTuyenDung.entity.Company;
import com.doan.AppTuyenDung.entity.DetailPost;
import com.doan.AppTuyenDung.entity.Note;
import com.doan.AppTuyenDung.entity.Post;
import com.doan.AppTuyenDung.entity.User;
import com.doan.AppTuyenDung.Mapper.PostMapper;
import com.doan.AppTuyenDung.DTO.InfoPostDetailDto;
import com.doan.AppTuyenDung.DTO.NoteDTO;
import com.doan.AppTuyenDung.DTO.PostFilterDTO;
import com.doan.AppTuyenDung.DTO.ReupPostDTO;
import com.doan.AppTuyenDung.DTO.UpdatePostDTO;
import com.doan.AppTuyenDung.DTO.CreatePostEmployerCompany.PostDTO;

import java.util.*;

@Service
public class postService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostRepositoriesQuery postRepositoriesQuery;
    @Autowired 
    private CompanyRepository companyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CodeJobTypeRepository codeJobTypeRepository;
    @Autowired
    private CodeProvinceRepository codeProvinceRepository;
    @Autowired
    private CodeSalaryTypeRepository codeSalaryTypeRepository;
    @Autowired
    private CodeJobLevelRepository codeJobLevelRepository;
    @Autowired
    private CodeWorkTypeRepository codeWorkTypeRepository;
    @Autowired
    private CodeExpTypeRepository codeExpTypeRepository;
    @Autowired
    private CodeGenderPostRepository codeGenderPostRepository;
    @Autowired 
    private DetailPostRepository detailPostRepository;
    @Autowired
    private CodePostStatusRepository codePostStatusRepository;
    @Autowired
    private NoteReponsitory noteReponsitory;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PostMapper postMapper;
    @Autowired
	private AccountRepository accountRepository;
    //amount post and get 
    public Page<PostJobTypeCountDTO> getPostJobTypeAndCountPost(Pageable pageable) {
        Page<Object[]> rawResults = postRepositoriesQuery.findPostJobTypeAndCountPost(pageable);

        List<PostJobTypeCountDTO> dtos = rawResults.stream()
            .map(result -> new PostJobTypeCountDTO(
                (String) result[0],          // categoryJobCode
                ((Number) result[1]).intValue(),
                (String) result[2],          // value
                (String) result[3],          // code
                (String) result[4]           // image
            ))
            .collect(Collectors.toList());
            int pageNumber = rawResults.getNumber(); // Current page number
            int pageSize = rawResults.getSize(); // Size of each page
            long totalElements = rawResults.getTotalElements(); // Total number of elements

            return new PageImpl<>(dtos, PageRequest.of(pageNumber, pageSize), totalElements);
    }

    // get Detail Post 
    public Map<String, Object> getPostDetailById(Integer id) {
        Map<String, Object> response = new HashMap<>();
        List<InfoPostDetailDto> postDetails = postRepositoriesQuery.findPostDetailById(id);
        InfoPostDetailDto postDetail = postDetails.stream().findFirst().orElse(null);

        if (postDetail != null) {
            response.put("errCode", 0);
            response.put("data",postDetail);
            response.put("errMessage", "Tạo bài tuyển dụng thành công hãy chờ quản trị viên duyệt");
            return response;
        } else {
            response.put("errCode", 2);
            response.put("errMessage", "Không tìm thấy bài viết");
            return response;
        }
    }

    // Filter post and detail post
    public Page<DetailPostDTO> getFilteredDetailPosts(String categoryJobCode, String addressCode, String search,
                                                      List<String> experienceJobCodes, List<String> categoryWorktypeCodes,
                                                      List<String> salaryJobCodes, List<String> categoryJoblevelCodes,
                                                      Integer isHot, Pageable pageable) {
        if (search != null && !search.isEmpty()) 
        {
            search = "%" + search + "%";
        }
        
        return postRepositoriesQuery.findFilteredPosts(categoryJobCode, 
                                                            addressCode, 
                                                        search, 
                                                       experienceJobCodes,
                                                     categoryWorktypeCodes, 
                                                     salaryJobCodes, 
                                                     categoryJoblevelCodes, 
                                                     isHot, 
                                                     pageable);
    }


    public Map<String, Object> getStatisticalTypePost(int limit) {
        Map<String, Object> response = new HashMap<>();

        // Lấy danh sách các loại bài đăng và số lượng
        List<Map<String, Object>> statisticalTypePost = postRepository.findStatisticalTypePost(limit);

        List<Post> allPosts = postRepository.findAll();
            List<Post> filteredPosts = allPosts.stream()
                    .filter(post -> "PS1".equals(post.getStatusCode().getCode()))
                    .collect(Collectors.toList());
            
        long totalPosts = filteredPosts.size();

        response.put("errCode", 0);
        response.put("data", statisticalTypePost);
        response.put("totalPost", totalPosts);

        return response;
    }
    public Page<PostResponse> statisticalCv(Date fromDate, Date toDate, String companyId, Pageable pageable) {
        Specification<Post> spec = PostSpecification.filterPosts(null, null, null, null, null, null, null, null, 
        														fromDate, toDate, companyId, null);
        Page<PostResponse> pageRs = mapPostPagePostResponsePage(postRepository.findAll(spec, pageable));
    return pageRs;
    }
    public Page<PostResponse> searchPosts(String name, String categoryJobCode, List<String> categoryWorkTypeCode, 
            String addressCode, List<String> experienceJobCode, List<String> categoryJobLevelCode, List<String> salaryJobCode,Integer isHot, Pageable pageable) {
            Specification<Post> spec = PostSpecification.filterPosts(name, categoryJobCode, categoryWorkTypeCode, 
                    addressCode, experienceJobCode, categoryJobLevelCode, salaryJobCode, isHot, null, null, null,"PS1");
            Page<PostResponse> pageRs = mapPostPagePostResponsePage(postRepository.findAll(spec, pageable));
        return pageRs;
        }
    public Page<PostResponse> mapPostPagePostResponsePage(Page<Post> postPage) {
        List<PostResponse> userResponses = postPage.getContent().stream()
            .map(post -> mapToPostResponseByMapper(post.getId()))
            .collect(Collectors.toList());

        return new PageImpl<>(userResponses, postPage.getPageable(), postPage.getTotalElements());
	}
    public PostResponse mapToPostResponseByMapper(Integer id) {
        Optional<Post> postOptional = postRepository.findById(id);
        if (postOptional.isEmpty()) {
            return null;
        }
        Post post = postOptional.get();
        return postMapper.toPostResponse(post, companyRepository);
    }
    public Map<String, Object> CreateNewPost(PostDTO data){
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate input data
            if (data.getName() == null || data.getCategoryJobCode() == null || data.getAddressCode() == null ||
                data.getSalaryJobCode() == null || data.getAmount() == 0 || data.getTimeEnd() == null ||
                data.getCategoryJoblevelCode() == null || data.getUserId() == null ||
                data.getCategoryWorktypeCode() == null || data.getExperienceJobCode() == null ||
                data.getGenderPostCode() == null || data.getDescriptionHTML() == null || 
                data.getDescriptionMarkdown() == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Fetch the user
            Optional<User> user = userRepository.findById(data.getUserId());
            if (user == null) {
                response.put("errCode", 2);
                response.put("errMessage", "User not found");
                return response;
            }

            // Fetch the company
            Company company = companyRepository.findById(user.get().getCompanyId()).orElse(null);
            if (company == null) {
                response.put("errCode", 2);
                response.put("errMessage", "Người dùng không thuộc công ty");
                return response;
            }

            // Check company status
            if (!(company.getStatusCode().getCode().equals("S1"))) {
                response.put("errCode", 2);
                response.put("errMessage", "Công ty bạn đã bị chặn không thể đăng bài");
                return response;
            }

            // Handle hot post logic
            if (data.getIsHot() == 1) {
                if (company.getAllowHotPost() > 0) {
                    company.setAllowHotPost(company.getAllowHotPost() - 1);
                } else {
                    response.put("errCode", 2);
                    response.put("errMessage", "Công ty bạn đã hết số lần đăng bài viết nổi bật");
                    return response;
                }
            } else {
                if (company.getAllowPost() > 0) {
                    company.setAllowPost(company.getAllowPost() - 1);
                } else {
                    response.put("errCode", 2);
                    response.put("errMessage", "Công ty bạn đã hết số lần đăng bài viết bình thường");
                    return response;
                }
            }

            // Save the post and details
            DetailPost detailPost = new DetailPost();
            detailPost.setName(data.getName());
            detailPost.setDescriptionHTML(data.getDescriptionHTML());
            detailPost.setDescriptionMarkdown(data.getDescriptionMarkdown());
            detailPost.setAmount(data.getAmount());
            // Handle code
            CodeJobType categoryJobCode = codeJobTypeRepository.findByCode(data.getCategoryJobCode());
            if (categoryJobCode != null) {
                detailPost.setCategoryJobCode(categoryJobCode);
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy mã loại công việc");
                return response;
            }
            CodeProvince addressCode = codeProvinceRepository.findByCode(data.getAddressCode());
            if (addressCode != null) {
                detailPost.setAddressCode(addressCode);
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy mã địa chỉ");
                return response;
            }

            // Fetch and set the CodeSalaryType entity
            CodeSalaryType salaryJobCode = codeSalaryTypeRepository.findByCode(data.getSalaryJobCode());
            if (salaryJobCode != null) {
                detailPost.setSalaryJobCode(salaryJobCode);
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy mã lương");
                return response;
            }

            // Fetch and set the CodeJobLevel entity
            CodeJobLevel categoryJoblevelCode = codeJobLevelRepository.findByCode(data.getCategoryJoblevelCode());
            if (categoryJoblevelCode != null) {
                detailPost.setCategoryJoblevelCode(categoryJoblevelCode);
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy mã cấp bậc");
                return response;
            }

            // Fetch and set the CodeWorkType entity
            CodeWorkType categoryWorktypeCode = codeWorkTypeRepository.findByCode(data.getCategoryWorktypeCode());
            if (categoryWorktypeCode != null) {
                detailPost.setCategoryWorktypeCode(categoryWorktypeCode);
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy mã loại công việc");
                return response;
            }

            // Fetch and set the CodeExpType entity
            CodeExpType experienceJobCode = codeExpTypeRepository.findByCode(data.getExperienceJobCode());
            if (experienceJobCode != null) {
                detailPost.setExperienceJobCode(experienceJobCode);
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy mã kinh nghiệm");
                return response;
            }

            // Fetch and set the CodeGenderPost entity
            CodeGenderPost genderPostCode = codeGenderPostRepository.findByCode(data.getGenderPostCode());
            if (genderPostCode != null) {
                detailPost.setGenderPostCode(genderPostCode);
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy mã giới tính người tuyển dụng");
                return response;
            }


            detailPostRepository.save(detailPost);


            CodePostStatus statusCode = codePostStatusRepository.findByCode("PS3");
            if (statusCode == null) {
                // Xử lý lỗi nếu không tìm thấy statusCode
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy mã trạng thái bài viết");
                return response;
            }

            LocalDateTime now = LocalDateTime.now();
            Date currentDate = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
            String timePost = String.valueOf(now.toInstant(ZoneOffset.UTC).toEpochMilli());
            Post post = new Post();
            post.setCreatedAt(currentDate);
            post.setUpdatedAt(currentDate);
            post.setTimePost(timePost);
            post.setStatusCode(statusCode);  
            post.setTimeEnd(data.getTimeEnd());
            // Lấy đối tượng User từ userId
            Optional<User> userP = userRepository.findById(data.getUserId());
            if (userP.isPresent()) {
                post.setUser(user.get());  // Gán đối tượng User vào
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy người dùng");
                return response;
            }
            // post.setUserId(data.getUserId());
            post.setIsHot(data.getIsHot());
            post.setDetailPost(detailPost);

            postRepository.save(post);

            // Save company after updating post limits
            companyRepository.save(company);

            response.put("errCode", 0);
            response.put("errMessage", "Tạo bài tuyển dụng thành công hãy chờ quản trị viên duyệt");
            return response;
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "Có lỗi xảy ra sau khi tạo bài tuyển dụng");
            return response;
        }
    }

    public Page<PostFilterDTO> GetlistpostadminService(Pageable pageable,Integer idCompany,String search, String censorCode) {
        
        return postRepositoriesQuery.GetListPostAdminRp(pageable, idCompany,search, censorCode);
    }

    public Page<GetAllPostRuleAdminDTO> GetAllPostRoleAdminService(Pageable pageable, String search,String censorCode) {

        return postRepositoriesQuery.GetAllPostRoleAdminRp(pageable,search, censorCode);
    }

    public Map<String, Object> acceptPostByRuleAdmin(AcceptPostByAdminDTO data) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate required parameters
            if (data.getId() == null || data.getStatusCode() == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Find the post
            Optional<Post> postOpt = postRepository.findById(data.getId());
            if (postOpt.isPresent()) {
                Post foundPost = postOpt.get();
                CodePostStatus status = codePostStatusRepository.findByCode(data.getStatusCode());
                postOpt.get().setStatusCode(status);
                // Set the posting time if approved
                if ("PS1".equals(data.getStatusCode())) {
                    String currentTimeMillis = String.valueOf(System.currentTimeMillis()); // Get current time in milliseconds
                    foundPost.setTimePost(currentTimeMillis);
                }
                
                // Save post changes
                postRepository.save(foundPost);

                // Create a note
                Note note = new Note();
                note.setPost(foundPost);
                note.setNote("PS1".equals(data.getStatusCode()) ? "Đã duyệt bài thành công" : data.getNote());
                Optional <User> user = userRepository.findById(data.getUserId());
                if(user.isPresent()){
                    User us1 = user.get();
                    note.setUser(us1);
                }
                noteReponsitory.save(note);

                // Find the user
                Optional<User> userOpt = userRepository.findById(foundPost.getUser().getId());
                if (userOpt.isPresent()) {
                    User user1 = userOpt.get();

                    // Send email notification
                    String emailSubject = "PS1".equals(data.getStatusCode())
                            ? "Duyệt bài thành công"
                            : "Bài viết #" + foundPost.getId() + " của bạn đã bị từ chối";
                    String emailLink = "PS1".equals(data.getStatusCode())
                            ? "http://localhost:3000/admin/detail-job/" + foundPost.getId()
                            : "http://localhost:3000/admin/list-post/" + foundPost.getId();
                    emailService.sendSimpleEmail(user1.getEmail(), emailSubject, emailLink);
                    
                }

                response.put("errCode", 0);
                response.put("errMessage", "PS1".equals(data.getStatusCode()) ? "Duyệt bài thành công" : "Đã từ chối bài thành công");
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tồn tại bài viết");
            }
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "Error: " + e.getMessage());
        }

        return response;
    }


    public Map<String, Object> banPostByRuleAdmin(BanPostByAdminDTO data) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate required parameters
            if (data.getPostId() == null || data.getNote() == null || data.getUserId() == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Find the post
            Optional<Post> postOpt = postRepository.findById(data.getPostId());
            if (postOpt.isPresent()) {
                Post foundPost = postOpt.get();
                
                CodePostStatus status = codePostStatusRepository.findByCode("PS4");
                foundPost.setStatusCode(status);
                postRepository.save(foundPost);

                // Create a note
                Note note = new Note();

                
                note.setPost(foundPost);
                note.setNote(data.getNote());
                Optional<User> usfind = userRepository.findById(data.getUserId());
                if(usfind.isPresent()){
                    note.setUser(usfind.get());
                }

                noteReponsitory.save(note);

                // Find the user
                Optional<User> userOpt = userRepository.findById(foundPost.getUser().getId());
                if (userOpt.isPresent()) {
                    User user = userOpt.get();

                    // Send email notification
                    String emailSubject = "Thông báo về việc bài viết bị chặn TuyenDungViecLam.com";
                    String emailContent = String.format("Bài viết #%d của bạn đã bị chặn vì %s.\n" +
                                        "Bạn có thể xem chi tiết tại: %s",
                                        foundPost.getId(), data.getNote(), 
                                        String.format("http://localhost:3000/admin/list-post/%d", foundPost.getId()));
                    emailService.sendSimpleEmail(user.getEmail(), emailSubject, emailContent);
                }

                response.put("errCode", 0);
                response.put("errMessage", "Đã chặn bài viết thành công");
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tồn tại bài viết");
            }
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "Error: " + e.getMessage());
        }


        return response;
    }

    public Map<String, Object> activatePostByRuleAdmin(ActivePostByAdminDTO data) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Validate required parameters
            if (data.getId() == null || data.getUserId() == null || data.getNote() == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Find the post
            Optional<Post> postOpt = postRepository.findById(data.getId());
            if (postOpt.isPresent()) {
                Post foundPost = postOpt.get();
                
                // Set the status code to 'PS3' (indicating active)
                CodePostStatus status = codePostStatusRepository.findByCode("PS3");
                foundPost.setStatusCode(status);
                postRepository.save(foundPost);

                // Create a note
                Note note = new Note();
                note.setPost(foundPost);
                note.setNote(data.getNote());
                Optional<User> usfind = userRepository.findById(data.getUserId());
                if(usfind.isPresent()){
                    note.setUser(usfind.get());
                }

                noteReponsitory.save(note);

                // Find the user
                Optional<User> userOpt = userRepository.findById(foundPost.getUser().getId());
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    // Prepare email subject and body
                    String emailSubject = "Thông báo về việc bài viết được mở lại";
                    String emailContent = String.format("Bài viết #%d của bạn đã được mở lại vì: %s.\n" +
                                                        "Bạn có thể xem chi tiết tại: %s",
                                                        foundPost.getId(), data.getNote(), 
                                                        String.format("http://localhost:3000/admin/list-post/%d", foundPost.getId()));

                    // Send email notification
                    emailService.sendSimpleEmail(user.getEmail(), emailSubject, emailContent);
                }

                response.put("errCode", 0);
                response.put("errMessage", "Đã mở lại trạng thái chờ duyệt");
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tồn tại bài viết");
            }
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "Error: " + e.getMessage());
        }
        return response;
    }

    public Map<String, Object> handleUpdatePost(UpdatePostDTO data) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate required parameters
            if (data.getId() == null || data.getName() == null || data.getCategoryJobCode() == null || 
                data.getAddressCode() == null || data.getSalaryJobCode() == null || data.getAmount() == null ||
                data.getTimeEnd() == null || data.getCategoryJoblevelCode() == null || 
                data.getCategoryWorktypeCode() == null || data.getExperienceJobCode() == null || 
                data.getGenderPostCode() == null || data.getDescriptionHTML() == null || 
                data.getDescriptionMarkdown() == null || data.getUserId() == null) {
                
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Find the post
            Optional<Post> postOpt = postRepository.findById(data.getId());
            if (postOpt.isPresent()) {
                Post post = postOpt.get();

                // Check if there is another post with the same detailPostId
                Optional<Post> otherPostOpt = postRepository.findByDetailPostIdAndIdNot(post.getDetailPost().getId(), post.getId());
                
                if (otherPostOpt.isPresent()) {
                    // Create a new DetailPost if another post with the same detailPostId exists
                    DetailPost newDetailPost = new DetailPost();
                    newDetailPost.setName(data.getName());
                    newDetailPost.setDescriptionHTML(data.getDescriptionHTML());
                    newDetailPost.setDescriptionMarkdown(data.getDescriptionMarkdown());
                    // categoryjobcode
                    // Fetch and set the CodeExpType entity
                    CodeJobType jobtype = codeJobTypeRepository.findByCode(data.getCategoryJobCode());
                    if (jobtype!= null) {
                        newDetailPost.setCategoryJobCode(jobtype);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã Loại công việc");
                        return response;
                    }
                    // newDetailPost.setCategoryJobCode(data.getCategoryJobCode());
                    CodeProvince addressCode = codeProvinceRepository.findByCode(data.getAddressCode());
                    if (addressCode !=null) {
                        newDetailPost.setAddressCode(addressCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã địa chỉ");
                        return response;
                        
                    }
                    // newDetailPost.setAddressCode(data.getAddressCode());
                    CodeSalaryType salaryJobCode = codeSalaryTypeRepository.findByCode(data.getSalaryJobCode());
                    if (salaryJobCode!= null) {
                        newDetailPost.setSalaryJobCode(salaryJobCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã lương");
                        return response;
                    }
                    // newDetailPost.setSalaryJobCode(data.getSalaryJobCode());
                    newDetailPost.setAmount(data.getAmount());

                    CodeJobLevel joblevelCode = codeJobLevelRepository.findByCode(data.getCategoryJoblevelCode());
                    if (joblevelCode!= null) {
                        newDetailPost.setCategoryJoblevelCode(joblevelCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã cấp bậc");
                        return response;
                    }
                    // newDetailPost.setCategoryJoblevelCode(data.getCategoryJoblevelCode());

                    CodeWorkType worktypeCode = codeWorkTypeRepository.findByCode(data.getCategoryWorktypeCode());
                    if (worktypeCode!= null) {
                        newDetailPost.setCategoryWorktypeCode(worktypeCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã loại công việc");
                        return response;
                    }
                    // newDetailPost.setCategoryWorktypeCode(data.getCategoryWorktypeCode());
                    CodeExpType experienceJobCode = codeExpTypeRepository.findByCode(data.getExperienceJobCode());
                    if (experienceJobCode!= null) {
                        newDetailPost.setExperienceJobCode(experienceJobCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã kinh nghiệm");
                        return response;
                    }
                    // newDetailPost.setExperienceJobCode(data.getExperienceJobCode());
                    CodeGenderPost genderPostCode = codeGenderPostRepository.findByCode(data.getGenderPostCode());
                    if (genderPostCode!= null) {
                        newDetailPost.setGenderPostCode(genderPostCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã giới tính của công việc");
                        return response;
                    }
                    // newDetailPost.setGenderPostCode(data.getGenderPostCode());
                    detailPostRepository.save(newDetailPost);

                    post.setDetailPost(newDetailPost);
                } else {
                    // Update the existing DetailPost if no other post uses it
                    DetailPost detailPost = detailPostRepository.findById(post.getDetailPost().getId()).orElse(null);
                    if (detailPost != null) {
                        detailPost.setName(data.getName());
                        detailPost.setDescriptionHTML(data.getDescriptionHTML());
                        detailPost.setDescriptionMarkdown(data.getDescriptionMarkdown());
                        CodeJobType jobtype = codeJobTypeRepository.findByCode(data.getCategoryJobCode());
                    if (jobtype!= null) {
                        detailPost.setCategoryJobCode(jobtype);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã Loại công việc");
                        return response;
                    }
                    // newDetailPost.setCategoryJobCode(data.getCategoryJobCode());
                    CodeProvince addressCode = codeProvinceRepository.findByCode(data.getAddressCode());
                    if (addressCode !=null) {
                        detailPost.setAddressCode(addressCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã địa chỉ");
                        return response;
                        
                    }
                    // newDetailPost.setAddressCode(data.getAddressCode());
                    CodeSalaryType salaryJobCode = codeSalaryTypeRepository.findByCode(data.getSalaryJobCode());
                    if (salaryJobCode!= null) {
                        detailPost.setSalaryJobCode(salaryJobCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã lương");
                        return response;
                    }
                    // newDetailPost.setSalaryJobCode(data.getSalaryJobCode());
                    detailPost.setAmount(data.getAmount());

                    CodeJobLevel joblevelCode = codeJobLevelRepository.findByCode(data.getCategoryJoblevelCode());
                    if (joblevelCode!= null) {
                        detailPost.setCategoryJoblevelCode(joblevelCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã cấp bậc");
                        return response;
                    }
                    // newDetailPost.setCategoryJoblevelCode(data.getCategoryJoblevelCode());

                    CodeWorkType worktypeCode = codeWorkTypeRepository.findByCode(data.getCategoryWorktypeCode());
                    if (worktypeCode!= null) {
                        detailPost.setCategoryWorktypeCode(worktypeCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã loại công việc");
                        return response;
                    }
                    // newDetailPost.setCategoryWorktypeCode(data.getCategoryWorktypeCode());
                    CodeExpType experienceJobCode = codeExpTypeRepository.findByCode(data.getExperienceJobCode());
                    if (experienceJobCode!= null) {
                        detailPost.setExperienceJobCode(experienceJobCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã kinh nghiệm");
                        return response;
                    }
                    // newDetailPost.setExperienceJobCode(data.getExperienceJobCode());
                    CodeGenderPost genderPostCode = codeGenderPostRepository.findByCode(data.getGenderPostCode());
                    if (genderPostCode!= null) {
                        detailPost.setGenderPostCode(genderPostCode);
                    } else {
                        response.put("errCode", 2);
                        response.put("errMessage", "Không tìm thấy mã giới tính của công việc");
                        return response;
                    }
                        detailPostRepository.save(detailPost);
                    }
                }

                // Update the Post details
                User user = userRepository.findById(data.getUserId()).orElse(null);
                if (user != null) {
                    post.setUser(user);
                }
                // post.setUserId(data.getUserId());
                CodePostStatus statusCode = codePostStatusRepository.findByCode("PS3"); 
                post.setStatusCode(statusCode); // Assuming "PS3" is the active status code
                postRepository.save(post);

                response.put("errCode", 0);
                response.put("errMessage", "Đã chỉnh sửa bài viết thành công hãy chờ quản trị viên duyệt");
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Bài đăng không tồn tại!");
            }
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "Error: " + e.getMessage());
        }

        return response;
    }
    public Map<String, Object> checkPostExists(Integer detailPostId, Integer id) {
        Map<String, Object> response = new HashMap<>();
    
        try {
            Optional<Post> postOpt = postRepository.findByDetailPostIdAndId(detailPostId, id);
            if (postOpt.isPresent()) {
                response.put("errCode", 0);
                response.put("errMessage", "Post found");
                response.put("post", postOpt.get());
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Post not found");
            }
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", "Error: " + e.getMessage());
        }
    
        return response;
    }


    public Map<String, Object> handleReupPost(ReupPostDTO data) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (data.getUserId() == null || data.getPostId() == null || data.getTimeEnd() == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }
    
            Optional<User> userOptional = userRepository.findById(data.getUserId());
            if (!userOptional.isPresent()) {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy người dùng!");
                return response;
            }
    
            User user = userOptional.get();
            Company company = companyRepository.findById(user.getCompanyId()).orElse(null);
            if (company == null) {
                response.put("errCode", 2);
                response.put("errMessage", "Người dùng không thuộc bất kỳ công ty nào.");
                return response;
            }
    
            Optional<Post> postOptional = postRepository.findById(data.getPostId());
            if (!postOptional.isPresent()) {
                response.put("errCode", 2);
                response.put("errMessage", "Bài Đăng không tồn tại!");
                return response;
            }
    
            Post post = postOptional.get();
    
            // Check if post is hot and update company allowances
            if (post.getIsHot() != null && post.getIsHot() == 1) {
                if (company.getAllowHotPost() > 0) {
                    company.setAllowHotPost(company.getAllowHotPost() - 1);
                    companyRepository.save(company);
                } else {
                    response.put("errCode", 2);
                    response.put("errMessage", "Công ty của bạn đã sử dụng hết hạn mức đăng bài Nổi bật !");
                    return response;
                }
            } else {
                if (company.getAllowPost() > 0) {
                    company.setAllowPost(company.getAllowPost() - 1);
                    companyRepository.save(company);
                } else {
                    response.put("errCode", 2);
                    response.put("errMessage", "Công ty của bạn đã sử dụng hết hạn mức đăng bài thường.");
                    return response;
                }
            }
    
            // Update the existing post details
            CodePostStatus statusCode = codePostStatusRepository.findByCode("PS3");
            if (statusCode != null) {
                post.setStatusCode(statusCode);
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy mã trạng thái bài viết!");
                return response;
            }
    
            post.setTimeEnd(data.getTimeEnd());
            post.setUser(user); // Associate the user if not already set
            post.setIsHot(post.getIsHot());
    
            DetailPost detailPost = post.getDetailPost();
            if (detailPost == null) {
                response.put("errCode", 2);
                response.put("errMessage", "Không tìm thấy Thông tin chi tiết bài Post!");
                return response;
            }
    
            post.setDetailPost(detailPost); // Ensure detailPost is set (if not already)
    
            postRepository.save(post);
    
            response.put("errCode", 0);
            response.put("errMessage", "Bài viết đã được cập nhật thành công. Vui lòng chờ quản trị viên phê duyệt.");
            return response;
    
        } catch (Exception e) {
            response.put("errCode", 3);
            response.put("errMessage", "An error occurred: " + e.getMessage());
            return response;
        }
    }



    public ResponseEntity<Map<String, Object>> getListNoteByPost(Integer postId, int limit, int offset) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (postId == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return ResponseEntity.ok(response);
            } else {
                // Pagination setup
                Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "updatedAt"));

                // Query database
                Page<Note> page = noteReponsitory.findAllByPostId(postId, pageable);

                // Map to DTO
                List<NoteDTO> noteDTOs = page.getContent().stream()
                        .map(note -> new NoteDTO(
                                note.getUser().getFirstName() + " " + note.getUser().getLastName(),
                                note.getUser().getId(),
                                note.getNote(),
                                note.getUpdatedAt()
                        ))
                        .collect(Collectors.toList());

                // Build response
                response.put("errCode", 0);
                response.put("data", noteDTOs);
                response.put("count", page.getTotalElements());

                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            response.put("errCode", -1);
            response.put("errMessage", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    public Map<String, Integer> getJobTypeCount() {
    	List<Object[]> results = detailPostRepository.findCountByJobType();
        
        Map<String, Integer> resultMap = new HashMap<>();
        for (Object[] result : results) {
            String jobTypeCode = (String) result[0];
            Integer count = ((Long) result[1]).intValue(); 
            resultMap.put(jobTypeCode, count);
        }
        return resultMap;
    }
}


    

