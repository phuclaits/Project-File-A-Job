package com.doan.AppTuyenDung.Services;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.text.Normalizer;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Iterator;

import com.doan.AppTuyenDung.entity.Account;
import com.doan.AppTuyenDung.DTO.CvDTO;
import com.doan.AppTuyenDung.DTO.CvMobiDTO;
import com.doan.AppTuyenDung.DTO.CvsDTO;
import com.doan.AppTuyenDung.DTO.FilterRequest;
import com.doan.AppTuyenDung.DTO.GetListCVByPost;
import com.doan.AppTuyenDung.DTO.ListUserSettingsDTO;
import com.doan.AppTuyenDung.DTO.Request.UserSettingDTO;
import com.doan.AppTuyenDung.DTO.Response.CvByPostResponse;
import com.doan.AppTuyenDung.DTO.Response.CvStatusResponse;
import com.doan.AppTuyenDung.DTO.Response.CvsResponse;
import com.doan.AppTuyenDung.Exception.AppException;
import com.doan.AppTuyenDung.Exception.ErrorCode;
import com.doan.AppTuyenDung.Repositories.AccountRepository;
import com.doan.AppTuyenDung.Repositories.CvsRepository;
import com.doan.AppTuyenDung.Repositories.PostRepository;
import com.doan.AppTuyenDung.Repositories.SkillRepository;
import com.doan.AppTuyenDung.Repositories.UserRepository;
import com.doan.AppTuyenDung.Repositories.UserSettingRepository;
import com.doan.AppTuyenDung.Repositories.UserSkillRepository;
import com.doan.AppTuyenDung.Repositories.AllCode.CodeJobTypeRepository;
import com.doan.AppTuyenDung.entity.CodeJobType;
import com.doan.AppTuyenDung.entity.Cv;
import com.doan.AppTuyenDung.entity.DetailPost;
import com.doan.AppTuyenDung.entity.Post;
import com.doan.AppTuyenDung.entity.Skill;
import com.doan.AppTuyenDung.entity.User;
import com.doan.AppTuyenDung.entity.UserSetting;
import com.doan.AppTuyenDung.entity.UserSkill;

import jakarta.transaction.Transactional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.time.LocalDateTime;
@Service

public class CvService {
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private CvsRepository cvRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private CodeJobTypeRepository codeJobTypeRepository;
	@Autowired
    private UserSettingRepository userSettingRepository;
	@Autowired
	private UserSkillRepository userSkillRepository;
	@Autowired
    private JWTUtils jwtUtils; 

	public Page<CvsResponse> getAllCvByUserId(int idUser, Pageable pageable) {
	    Page<Cv> cvsPage = cvRepository.findByUserId(idUser, pageable);
		 Page<CvsResponse> responsePage = cvsPage.map(this::convertCvsToCvsResponse);
		
	    return responsePage;
	}

	private CvsResponse convertCvsToCvsResponse(Cv cv) {
	    CvsResponse response = new CvsResponse();
	    response.setIdCv(cv.getId());
	    response.setFile(null);
	    //response.setFile(cv.getFile());
	    response.setIsChecked(cv.getIsChecked());
	    response.setStatus(cv.getStatus());
	    response.setInterviewTime(cv.getInterviewTime());
	    response.setDescription(cv.getDescription());
	    response.setCreatedAtCv(cv.getCreatedAt());
	    response.setUpdatedAtCv(cv.getUpdatedAt());
	    if (cv.getPost() != null) {
	        Post post = cv.getPost();
	        response.setIdPost(post.getId());
	        response.setTimeEnd(post.getTimeEnd());
	        response.setTimePost(post.getTimePost());
	        response.setIsHot(post.getIsHot());
	        response.setCreatedAtPost(post.getCreatedAt());
	        response.setUpdatedAtPost(post.getUpdatedAt());
	        if (post.getDetailPost() != null) {
	            DetailPost detailPost = post.getDetailPost();
	            response.setIdDetailPost(detailPost.getId());
	            response.setName(detailPost.getName());
	            response.setDescriptionHTML(detailPost.getDescriptionHTML());
	            response.setDescriptionMarkdown(detailPost.getDescriptionMarkdown());
	            response.setAmount(detailPost.getAmount());
	            response.setCodeJobType(detailPost.getCategoryJobCode().getCode());
	            response.setValueJobType(detailPost.getCategoryJobCode().getValue());
	            response.setCodeWorkType(detailPost.getCategoryWorktypeCode().getCode());
	            response.setValueWorkType(detailPost.getCategoryWorktypeCode().getValue());
	            response.setCodeSalaryType(detailPost.getSalaryJobCode().getCode());
	            response.setValueSalaryType(detailPost.getSalaryJobCode().getValue());
	            response.setCodeJobLevel(detailPost.getCategoryJoblevelCode().getCode());
	            response.setValueJobLevel(detailPost.getCategoryJoblevelCode().getValue());
	            response.setCodeGender(detailPost.getGenderPostCode().getCode());
	            response.setValueGender(detailPost.getGenderPostCode().getValue());
	            response.setCodeProvince(detailPost.getAddressCode().getCode());
	            response.setValueProvince(detailPost.getAddressCode().getValue());
	            response.setCodeExpType(detailPost.getExperienceJobCode().getCode());
	            response.setValueExpType(detailPost.getExperienceJobCode().getValue());
	        }
	    }
	    return response;
	}

	public Map<String, Object> handleCreateCv(CvDTO data) {
        Map<String, Object> response = new HashMap<>();
		
		User user = userRepository.findById(data.getUserId()).orElse(null);
		if(user == null) {
			response.put("errCode", -1);
            response.put("errMessage", "User not found!");
            return response;
		}
		Post post = postRepository.findById(data.getPostId()).orElse(null);
		if(post == null)
		{
			response.put("errCode", -1);
            response.put("errMessage", "Post not found!");
            return response;
		}

        if (data.getUserId() == null || data.getFile() == null || data.getPostId() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
        } else {
            Cv cv = new Cv();

            cv.setUser(user);
            cv.setFile(data.getFile());
            cv.setPost(post);
			cv.setCreatedAt(new Date());
			cv.setStatus("pending");
            cv.setIsChecked(false); // Mặc định là chưa kiểm tra
            cv.setDescription(data.getDescription());

            Cv savedCv = cvRepository.save(cv);
            if (savedCv != null) {
                response.put("errCode", 0);
                response.put("errMessage", "Đã gửi CV thành công");
            } else {
                response.put("errCode", 2);
                response.put("errMessage", "Đã gửi CV thất bại");
            }
        }
        return response;
    }


	//// su ly mobi
	public Map<String, Object> handleCreateCvMobile(CvMobiDTO data) {
		Map<String, Object> response = new HashMap<>();

		User user = userRepository.findById(data.getUserId()).orElse(null);
		if (user == null) {
			response.put("errCode", -1);
			response.put("errMessage", "User not found!");
			return response;
		}

		Post post = postRepository.findById(data.getPostId()).orElse(null);
		if (post == null) {
			response.put("errCode", -1);
			response.put("errMessage", "Post not found!");
			return response;
		}

		if (data.getUserId() == null || data.getPostId() == null) {
			response.put("errCode", 1);
			response.put("errMessage", "Missing required parameters!");
			return response;
		}

		if (data.getFileBase64() == null || data.getFileBase64().isEmpty()) {
			response.put("errCode", 2);
			response.put("errMessage", "Missing fileBase64!");
			return response;
		}

		try {

			Cv cv = new Cv();
			cv.setUser(user);
			cv.setFile(data.getFile());
			cv.setPost(post);
			cv.setCreatedAt(new Date());
			cv.setStatus("pending");
			cv.setIsChecked(false);
			cv.setDescription(data.getDescription());

			Cv savedCv = cvRepository.save(cv);

			if (savedCv != null) {
				response.put("errCode", 0);
				response.put("errMessage", "Đã gửi CV thành công");
			} else {
				response.put("errCode", 2);
				response.put("errMessage", "Đã gửi CV thất bại");
			}
		} catch (IllegalArgumentException e) {
			response.put("errCode", 3);
			response.put("errMessage", "Lỗi khi giải mã fileBase64: " + e.getMessage());
		}

		return response;
	}



	public String flatAllString(String input) {
		if (input == null) return "";
		
		String output = Normalizer.normalize(input.toLowerCase(), Normalizer.Form.NFD)
				.replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
				.replaceAll("[đĐ]", "d")
				.replaceAll("[^a-zA-Z]", "");
		
		return output;
	}
	public void getMapRequiredSkill(Map<Integer, String> mapRequired, Post post) {
		for (Iterator<Integer> iterator = mapRequired.keySet().iterator(); iterator.hasNext();) {
			Integer key = iterator.next();
			String skillName = mapRequired.get(key).toLowerCase();
	
			if (!flatAllString(post.getDetailPost().getDescriptionHTML()).contains(flatAllString(skillName))) {
				iterator.remove(); // Xóa các kỹ năng không tồn tại trong bài đăng
			}
		}
	}

	public int calculateMatchCv(byte[] fileData, Map<Integer, String> mapRequired) {
        // Sao chép mapRequired vào myMapRequired
        Map<Integer, String> myMapRequired = new HashMap<>(mapRequired);

        if (myMapRequired.isEmpty()) {
            return 0;
        }

        int match = 0;

        try (InputStream inputStream = new ByteArrayInputStream(fileData);
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String cvData = pdfStripper.getText(document);

            // Chuẩn hóa nội dung của CV
            String normalizedCvData = flatAllString(cvData);

            // Duyệt qua các kỹ năng yêu cầu bằng Iterator để tránh lỗi ConcurrentModificationException
            Iterator<Map.Entry<Integer, String>> iterator = myMapRequired.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> entry = iterator.next();
                String skill = flatAllString(entry.getValue());

                if (normalizedCvData.contains(skill)) {
                    match++;
                    iterator.remove(); // Xóa kỹ năng đã tìm thấy để tránh so sánh lại
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return match;
    }


public CompletableFuture<Map<String, Object>> getAllListCvByPostAsync(Integer offset, Integer limit, Integer postId) {
    Map<String, Object> response = new HashMap<>();

    try {
        if (offset == null || limit == null || postId == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return CompletableFuture.completedFuture(response);
        }

        Pageable pageable = PageRequest.of(offset, limit);
        Page<Cv> cvPage = cvRepository.findAllByPostId(postId, pageable);

        Post postInfo = postRepository.findById(postId).orElse(null);
        if (postInfo == null) {
            response.put("errCode", 2);
            response.put("errMessage", "Post not found!");
            return CompletableFuture.completedFuture(response);
        }

        CodeJobType jobType = codeJobTypeRepository.findById(postInfo.getDetailPost().getCategoryJobCode().getCode()).orElse(null);
        if (jobType == null) {
            response.put("errCode", 2);
            response.put("errMessage", "Job type not found!");
            return CompletableFuture.completedFuture(response);
        }
        String codejobtype = jobType.getCode();

        List<Skill> listSkills = skillRepository.findByCategoryJobCode(codejobtype);

        Map<Integer, String> mapRequired = new HashMap<>();
        listSkills.forEach(skill -> mapRequired.put(skill.getId(), skill.getName()));

        getMapRequiredSkill(mapRequired, postInfo);

        List<Map<String, Object>> resultData = new ArrayList<>();

        // Kết hợp thông tin vào từng CV
        List<Map<String, Object>> dataWithAdditionalInfo = new ArrayList<>();
        for (Cv cv : cvPage.getContent()) {
            int match = calculateMatchCv(cv.getFile(), mapRequired);
            String matchPercentage = Math.round((match / (double) mapRequired.size()) * 100) + "%";

            Account account = accountRepository.findByUserId(cv.getUser().getId());
            String phoneNumber = account != null ? account.getPhonenumber() : null;

            // Gộp thông tin matchPercentage và phoneNumber vào từng CV
            Map<String, Object> cvData = new HashMap<>();
            cvData.put("id", cv.getId());
            cvData.put("file", cv.getFile());
            cvData.put("isChecked", cv.getIsChecked());
            cvData.put("status", cv.getStatus());
            cvData.put("description", cv.getDescription());
            cvData.put("user", cv.getUser()); // Giữ nguyên thông tin user
            cvData.put("matchPercentage", matchPercentage); // Thêm matchPercentage
            cvData.put("phoneNumber", phoneNumber); // Thêm phoneNumber
            dataWithAdditionalInfo.add(cvData);
        }

        response.put("errCode", 0);
        response.put("data", dataWithAdditionalInfo); // Gửi danh sách đã kết hợp
        response.put("count", cvPage.getTotalElements());
    } catch (Exception e) {
        response.put("errCode", 3);
        response.put("errMessage", "An error occurred: " + e.getMessage());
    }

    return CompletableFuture.completedFuture(response);
}
	
	public Page<CvStatusResponse> getCvByStatus(String status, Integer idCompany, Pageable pageable) {
		try {
			var authen = SecurityContextHolder.getContext().getAuthentication();
			com.doan.AppTuyenDung.entity.Account account = accountRepository.findByPhonenumber(authen.getName());
			Integer checkIdCompany = account.getUser().getCompanyId();
			if(idCompany == checkIdCompany) {
				Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("updatedAt")));
				Page<CvStatusResponse> cvs = mapToPageCvStatusRes(cvRepository.findByStatus(status, sortedPageable), idCompany);
				return cvs;
			}
			else {
				throw new AppException(ErrorCode.CV_NOT_FIT);
			}
		}
		catch (Exception e) {
			System.out.print(e);
			throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
	}
	public CvStatusResponse mapToCvStatusResponse(Cv cv, Integer idCompany) {
		if(cv.getPost().getUser().getCompanyId() == idCompany) {
			CvStatusResponse rp = new CvStatusResponse();
			rp.setFirstName(cv.getUser().getFirstName());
			rp.setLastName(cv.getUser().getLastName());
			rp.setEmail(cv.getUser().getEmail());
			rp.setUserId(cv.getUser().getId());
			rp.setPostId(cv.getPost().getId());
			rp.setDetailPostId(cv.getPost().getDetailPost().getId());
			rp.setId(cv.getId());
			rp.setInterviewTime(cv.getInterviewTime());
			rp.setIsChecked(cv.getIsChecked());
			rp.setDescription(cv.getDescription());
			rp.setStatus(cv.getStatus());
			rp.setFile(cv.getFile());
			return rp;
		}
		else {
			return null;
		}
		
	}
	public Page<CvStatusResponse> mapToPageCvStatusRes(Page<Cv> pageCv,Integer idCompany) {
		if(!pageCv.isEmpty()) {
			List<CvStatusResponse> cvStatusResponseLst = pageCv.getContent().stream()
					.map(cv -> mapToCvStatusResponse(cv,idCompany ))
					.collect(Collectors.toList());
			return new PageImpl<>(cvStatusResponseLst,pageCv.getPageable(),pageCv.getTotalPages());
		}
		else {
			throw new AppException(ErrorCode.LISTISNULL);
		}
	}
	
	public CvStatusResponse mapToCvDetailResponse(Cv cv) {
		CvStatusResponse rp = new CvStatusResponse();
		rp.setFirstName(cv.getUser().getFirstName());
		rp.setLastName(cv.getUser().getLastName());
		rp.setEmail(cv.getUser().getEmail());
		rp.setUserId(cv.getUser().getId());
		rp.setPostId(cv.getPost().getId());
		rp.setDetailPostId(cv.getPost().getDetailPost().getId());
		rp.setId(cv.getId());
		rp.setInterviewTime(cv.getInterviewTime());
		rp.setIsChecked(cv.getIsChecked());
		rp.setDescription(cv.getDescription());
		rp.setStatus(cv.getStatus());
		rp.setFile(cv.getFile());
		byte[] pdfBytes = cv.getFile();
		String dataFile = new String(pdfBytes, StandardCharsets.UTF_8);
        rp.setFilePDF(dataFile);
		
		return rp;
		
	}
    public CvStatusResponse getCvById(Integer id) {
		try {
			var authen = SecurityContextHolder.getContext().getAuthentication();
			System.out.print(authen.getName());
			com.doan.AppTuyenDung.entity.Account account = accountRepository.findByPhonenumber(authen.getName());

			if(account!=null) {

				Integer checkIdCompany = account.getUser().getCompanyId();
				Cv myCv = cvRepository.findById(id).get();
				if(checkIdCompany == myCv.getPost().getUser().getCompanyId()) {
					CvStatusResponse cvs = mapToCvDetailResponse(myCv);
					return cvs;
				}
				else {
					throw new AppException(ErrorCode.CV_NOT_FIT);
				}
			}
			else {
				throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
			}
		}
		catch (Exception e) {
			System.out.print(e);
			throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
	}
    public CvStatusResponse getCvDetail(Integer id) {
		try {
			var authen = SecurityContextHolder.getContext().getAuthentication();
			System.out.println(authen.getName());
			Account account = accountRepository.findByPhonenumber(authen.getName());
			Cv myCv = cvRepository.findById(id).get();
			CvStatusResponse cvs = new CvStatusResponse();
			if(account.getUser().getId()== myCv.getUser().getId()) {
				cvs = mapToCvDetailResponse(myCv);
				return cvs;
			}
			else {
				throw new AppException(ErrorCode.CV_NOT_FIT);
			}
		}
		catch (Exception e) {
			System.out.print(e);
			throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
	}


	public Map<String, Object> filterCVBySelection(FilterRequest data) {
        Map<String, Object> response = new HashMap<>();

        if (data.getLimit() == null || data.getOffset() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
            return response;
        }

        boolean isHiddenPercent = false;
		Pageable pageable = PageRequest.of(data.getOffset() / data.getLimit(), data.getLimit());
    	Page<ListUserSettingsDTO> listUserSettings = userSettingRepository.findByFilter(data.getCategoryJobCode(),pageable);

        for (ListUserSettingsDTO item : listUserSettings) {
			int bonus = 0;
			if (data.getExperienceJobCode() != null && data.getExperienceJobCode().equals(item.getExperienceJobCode().getCode())) bonus++;
			if (data.getSalaryCode() != null && data.getSalaryCode().equals(item.getSalaryJobCode().getCode())) bonus++;
			if (data.getProvinceCode() != null && data.getProvinceCode().equals(item.getAddressCode().getCode())) bonus++;
			item.setBonus(bonus);
		}
		List<Skill> listSkillRequired = new ArrayList<>();
		if (data.getListSkills() != null && !data.getListSkills().isEmpty()) {
			List<Integer> skillIds = data.getListSkills();
			listSkillRequired = skillRepository.findAllById(skillIds);
		}

		boolean hasBonusOrSkills = listUserSettings.stream().anyMatch(item -> item.getBonus() > 0) || !listSkillRequired.isEmpty();

		if (hasBonusOrSkills) {
			calculateMatchAndSetFilePercentage(listUserSettings, listSkillRequired);
		} else {
			isHiddenPercent = true;
			listUserSettings.forEach(userSetting -> userSetting.setFile(null)); 
		}

        response.put("errCode", 0);
		response.put("data", listUserSettings);
        response.put("isHiddenPercent", isHiddenPercent);
        return response;
    }

    private void calculateMatchAndSetFilePercentage(Page<ListUserSettingsDTO> listUserSettings, List<Skill> listSkillRequired) {
		for (ListUserSettingsDTO userSetting : listUserSettings) {
			int match = calculateMatchUserWithFilter(userSetting, listSkillRequired);
			
			// Calculate the total skills required for the percentage
			double totalSkills = listSkillRequired.size() * 2 + userSetting.getBonus();
	
			// Calculate and set the match percentage
			double percentage = (totalSkills > 0) ? (match / totalSkills) * 100 : 0;
			userSetting.setMatchPercentage(Math.round(percentage) + "%");
		}
	}

    private int calculateMatchUserWithFilter(ListUserSettingsDTO userSetting, List<Skill> listSkillRequired) {
		// Convert required skill IDs to a Set for efficient lookup
		Set<Integer> requiredSkillIds = listSkillRequired.stream()
														 .map(Skill::getId)
														 .collect(Collectors.toSet());
	

		List<UserSkill> userSkills = userSkillRepository.findByUserId(userSetting.getUserId()); 
	

		return (int) userSkills.stream()
							   .filter(userSkill -> requiredSkillIds.contains(userSkill.getSkillId()))
							   .count();
	}

	@Async
    public CompletableFuture<Map<String, Object>> getCVsByStatusAsync(String status) {
        Map<String, Object> response = getCVsByStatus(status);
        return CompletableFuture.completedFuture(response);
    }

	public Map<String, Object> getCVsByStatus(String status) {
        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            if (status == null || status.isEmpty()) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Lấy danh sách CV theo trạng thái
            List<Cv> cvs = cvRepository.findByStatus(status);

            for (Cv cv : cvs) {
                Map<String, Object> cvData = new HashMap<>();

                // Lấy thông tin User theo userId từ CV
                Optional<User> userOpt = userRepository.findById(cv.getUser().getId());
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    cvData.put("firstName", user.getFirstName());
                    cvData.put("lastName", user.getLastName());
                    cvData.put("email", user.getEmail());
					cvData.put("idUser", user.getId());
                } else {
                    cvData.put("firstName", null);
                    cvData.put("lastName", null);
                    cvData.put("email", null);
                }

                // Lấy thông tin từ bảng CV
                cvData.put("idCv", cv.getId());
                cvData.put("idPost", cv.getPost().getId());
                cvData.put("status", cv.getStatus());
                cvData.put("interviewTime", cv.getInterviewTime());

                result.add(cvData);
            }

            response.put("errCode", 0);
            response.put("data", result);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("errCode", -1);
            response.put("errMessage", "Error from server");
        }

        return response;
    }

	public Map<String, Object> updateCVStatus(Integer id, String status) {
        Map<String, Object> response = new HashMap<>();
        Optional<Cv> cvOptional = cvRepository.findById(id);

        if (!cvOptional.isPresent()) {
            response.put("errCode", 1);
            response.put("errMessage", "CV not found");
            return response;
        }

        Cv cv = cvOptional.get();
        cv.setStatus(status);
        cvRepository.save(cv);

        response.put("errCode", 0);
        response.put("errMessage", "Update status successfully");
        return response;
    }

	public Map<String, Object> scheduleInterview(Integer cvId, String interviewTime) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Tìm kiếm CV theo ID
            Optional<Cv> optionalCV = cvRepository.findById(cvId);
            if (!optionalCV.isPresent()) {
                response.put("errCode", 2);
                response.put("errMessage", "CV not found!");
                return response;
            }

            Cv cv = optionalCV.get();

            // Cập nhật thời gian phỏng vấn
			LocalDateTime localDateTime = LocalDateTime.parse(interviewTime);
			Date date = java.sql.Timestamp.valueOf(localDateTime);
			cv.setInterviewTime(date);
            cvRepository.save(cv);

            response.put("errCode", 0);
            response.put("errMessage", "Schedule interview successfully!");
            response.put("data", cv);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("errCode", -1);
            response.put("errMessage", "Error from server");
        }

        return response;
    }
	public Page<CvByPostResponse> getCvByPostId(Integer id, Pageable pageable) {
		var authen = SecurityContextHolder.getContext().getAuthentication();
		com.doan.AppTuyenDung.entity.Account account = accountRepository.findByPhonenumber(authen.getName());
		Integer checkIdCompany = account.getUser().getCompanyId();
		Post post = postRepository.findById(id).get();
		
		if(checkIdCompany == post.getUser().getCompanyId()) {
			Page<CvByPostResponse> pageRs = mapPageCvByPost(cvRepository.findByPostId(id, pageable));
			return pageRs;
		}
		else 
			throw new AppException(ErrorCode.CV_NOT_FIT);
		
	}
	private Page<CvByPostResponse> mapPageCvByPost(Page<Cv> pageCv) {
		List<CvByPostResponse> cvRp = pageCv.getContent().stream()
				.map(cv -> mapToCvByPost(cv))
				.collect(Collectors.toList());
		return new PageImpl<>(cvRp, pageCv.getPageable(), pageCv.getTotalElements());
	}
	private CvByPostResponse mapToCvByPost(Cv cv) {
		CvByPostResponse cvByPost = new CvByPostResponse();
		cvByPost.setIdCv(cv.getId());
		cvByPost.setCreateAt(cv.getCreatedAt());
		cvByPost.setFirstName(cv.getUser().getFirstName());
		cvByPost.setLastName(cv.getUser().getLastName());
		Account account = accountRepository.findByUserId(cv.getUser().getId());
		cvByPost.setPhoneNumber(account.getPhonenumber());
		cvByPost.setStatus(cv.getStatus());
		return cvByPost;
	}
	public Map<String, Integer> staticalCvByStatus(){
		try {
			Map<String, Integer> statical = new TreeMap<String, Integer>();
			String accept = "accepted";
			String pending = "pending";
			String reject = "rejected";
			String review = "under-review";
			int cntAccept = 0;
			int cntPending = 0;
			int cntReject = 0;
			int cntReview = 0;
			List<Cv> lstCv = cvRepository.findAll();
			for(Cv c : lstCv) {
				if(c.getStatus() != null) {
					if(c.getStatus().equals(accept)) 
						cntAccept++;
					if(c.getStatus().equals(pending)) 
						cntPending++;
					if(c.getStatus().equals(reject)) 
						cntReject++;
					if(c.getStatus().equals(review)) 
						cntReview++;
				}
			}
			statical.put(accept, cntAccept);
			statical.put(pending, cntPending);
			statical.put(reject, cntReject);
			statical.put(review, cntReview);
			return statical;
		} catch (Exception e) {
			System.out.print(e.getMessage());
			throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
	}
	

	public Map<String, Integer> staticalCvByStatusnCompany(Integer Id ){
		try {
			var authen = SecurityContextHolder.getContext().getAuthentication();
			com.doan.AppTuyenDung.entity.Account account = accountRepository.findByPhonenumber(authen.getName());
			Integer checkIdCompany  = account.getUser().getCompanyId();
			if(Id == checkIdCompany) {
				Map<String, Integer> statical = new TreeMap<String, Integer>();
				String accept = "accepted";
				String pending = "pending";
				String reject = "rejected";
				String review = "under-review";
				int cntAccept = 0;
				int cntPending = 0;
				int cntReject = 0;
				int cntReview = 0;
				List<Cv> lstCv = cvRepository.findAll();
				for(Cv c : lstCv) {
					if(c.getPost().getUser().getCompanyId() == Id) {
						if(c.getStatus() != null) {
							if(c.getStatus().equals(accept)) 
								cntAccept++;
							if(c.getStatus().equals(pending)) 
								cntPending++;
							if(c.getStatus().equals(reject)) 
								cntReject++;
							if(c.getStatus().equals(review)) 
								cntReview++;
						}
					}
				}
				statical.put(accept, cntAccept);
				statical.put(pending, cntPending);
				statical.put(reject, cntReject);
				statical.put(review, cntReview);
				return statical;
			}
			else {
				throw new AppException(ErrorCode.CV_NOT_FIT);
			}
		} catch (Exception e) {
			System.out.print(e.getMessage());
			throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
		}
		
	} 
}

