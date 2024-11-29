package com.doan.AppTuyenDung.Services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.doan.AppTuyenDung.DTO.CompanyGetListDTO;
import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.JobtypeDTO;
import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.ResponseAllJobType;
import com.doan.AppTuyenDung.DTO.GetAllSkillAdmin.SkillDTO;
import com.doan.AppTuyenDung.DTO.GetAllSkillAdmin.SkillGetListDTO;
import com.doan.AppTuyenDung.DTO.Response.SkillResponse;
import com.doan.AppTuyenDung.Repositories.SkillRepository;
import com.doan.AppTuyenDung.Repositories.UserRepository;
import com.doan.AppTuyenDung.Repositories.UserSettingRepository;
import com.doan.AppTuyenDung.entity.CodeJobType;
import com.doan.AppTuyenDung.entity.Skill;

@Service
public class SkillService {
	@Autowired
	private SkillRepository skillRepository;
	@Autowired
	private UserSettingRepository userSettingRepository;
	public List<SkillResponse> GetSkillByCodeJob(String categoryJobCode) {
	        List<Skill> skills = skillRepository.findByCategoryJobCode(categoryJobCode);
	        return skills.stream()
	            .map(skill -> mapToSkillResponse(skill))
	            .collect(Collectors.toList());
	}
	private SkillResponse mapToSkillResponse(Skill skill) {
	    SkillResponse response = new SkillResponse();
	    response.setId(skill.getId());
	    response.setName(skill.getName());
	    response.setCategoryJobCode(skill.getCategoryJobCode());
	    return response;
	}
	public Page<SkillGetListDTO> getListSkill(Pageable pageable,String search,String categoryJobCode) {
        return skillRepository.getListSkills(pageable,search,categoryJobCode);
    }
	public Map<String, Object> createNewSkill(SkillDTO skillDTO) {
        Map<String, Object> response = new HashMap<>();

        if (skillDTO.getName() == null || skillDTO.getCategoryJobCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
			return response;
        }

        // Check if the skill already exists
        if (skillRepository.existsByCategoryJobCodeAndName(skillDTO.getCategoryJobCode(), skillDTO.getName())) {
            response.put("errCode", 3);
            response.put("errMessage", "Name already exists!");
			return response;
        }
		Skill skill = new Skill();
        skill.setName(skillDTO.getName());
        skill.setCategoryJobCode(skillDTO.getCategoryJobCode());
        skillRepository.save(skill);
		response.put("errCode", 0);
        response.put("errMessage", "Đã thêm Kỹ năng thành công!");
		return response;
	}

	public Map<String, Object> updateSkill(Skill skill) {
		Map<String, Object> response = new HashMap<>();
		if (skill.getId()== null || skill.getName() == null || skill.getCategoryJobCode() == null) {
            response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
			return response;
        }

        Skill existingSkill = skillRepository.findById(skill.getId()).orElse(null);
        if (existingSkill == null) {
			response.put("errCode", 2);
            response.put("errMessage", "Not Existing Skill!");
			return response;
        }
		existingSkill.setName(skill.getName());
        existingSkill.setCategoryJobCode(skill.getCategoryJobCode());
		skillRepository.save(existingSkill);
		response.put("errCode", 0);
        response.put("errMessage", "Update Successfully");
		return response;
	}

	public Map<String, Object> getDetailSkill(Integer id)
	{
		Map<String, Object> response = new HashMap<>();
		if (id == null || id <=0) {
			response.put("errCode", 1);
            response.put("errMessage", "Missing required parameters!");
			return response;
        }

		Optional<Skill> foundSkill = skillRepository.findById(id);
        if (foundSkill.isPresent()) {
            response.put("errCode", 0);
            response.put("errMessage", "Get Detail Successfully");
			response.put("data", foundSkill.get());
			return response;
        } else {
            response.put("errCode", 1);
            response.put("errMessage", "Not find with id");
			return response;
        }
	}

	 public Map<String, Object> DeleteSkillById(Integer id) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (id == null) {
                response.put("errCode", 1);
                response.put("errMessage", "Missing required parameters!");
                return response;
            }

            // Tìm Skill theo id
            Optional<Skill> foundSkill = skillRepository.findById(id);
            if (!foundSkill.isPresent()) {
                response.put("errCode", 2);
                response.put("errMessage", "Không tồn tại kỹ năng");
                return response;
            }

            // Kiểm tra xem Skill có đang được sử dụng trong UserSkill không
            boolean isSkillUsed = userSettingRepository.existsById(foundSkill.get().getId());
            if (isSkillUsed) {
                response.put("errCode", 3);
                response.put("errMessage", "Lỗi! Tồn tại người dùng có kỹ năng này.");
                return response;
            }

            // Xóa Skill
            skillRepository.deleteById(id);
            response.put("errCode", 0);
            response.put("errMessage", "Đã xóa kỹ năng thành công");

        } catch (Exception e) {
            throw new RuntimeException("Error occurred: " + e.getMessage());
        }
        return response;
    }

}
