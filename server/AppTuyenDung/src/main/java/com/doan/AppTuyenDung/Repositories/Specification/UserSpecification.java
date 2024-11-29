package com.doan.AppTuyenDung.Repositories.Specification;

import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import com.doan.AppTuyenDung.entity.User;
import com.doan.AppTuyenDung.entity.UserSetting;
import com.doan.AppTuyenDung.entity.UserSkill;
import com.doan.AppTuyenDung.entity.CodeExpType;
import com.doan.AppTuyenDung.entity.CodeJobType;
import com.doan.AppTuyenDung.entity.CodeSalaryType;
import com.doan.AppTuyenDung.entity.Skill;

public class UserSpecification {

	public static Specification<User> filterUsers(String firstName, String lastName, String categoryJobCode, 
            String salaryJobCode, String experienceJobCode, String skillName) {
		return (root, query, criteriaBuilder) -> {
		Specification<User> spec = Specification.where(null);
		
		if (firstName != null && !firstName.isEmpty()) {
			spec = spec.and((userRoot, userQuery, cb) -> 	
			cb.like(userRoot.get("firstName"), "%" + firstName + "%"));
		}
		
		if (lastName != null && !lastName.isEmpty()) {
			spec = spec.and((userRoot, userQuery, cb) -> 
			cb.like(userRoot.get("lastName"), "%" + lastName + "%"));
		}
		
		if (categoryJobCode != null && !categoryJobCode.isEmpty()) {
			spec = spec.and((userRoot, userQuery, cb) -> {
			Join<User, UserSetting> userSettingJoin = userRoot.join("userSetting",JoinType.LEFT);
			Join<UserSetting, CodeJobType> categoryJobJoin = userSettingJoin.join("categoryJobCode",JoinType.LEFT);
			return cb.equal(categoryJobJoin.get("code"), categoryJobCode);
			});
		}
		
		if (salaryJobCode != null && !salaryJobCode.isEmpty()) {
			spec = spec.and((userRoot, userQuery, cb) -> {
			Join<User, UserSetting> userSettingJoin = userRoot.join("userSetting",JoinType.LEFT);
			Join<UserSetting, CodeSalaryType> salaryJobJoin = userSettingJoin.join("salaryJobCode",JoinType.LEFT);
			return cb.equal(salaryJobJoin.get("code"), salaryJobCode);
			});
		}
		
		if (experienceJobCode != null && !experienceJobCode.isEmpty()) {
			spec = spec.and((userRoot, userQuery, cb) -> {
			Join<User, UserSetting> userSettingJoin = userRoot.join("userSetting",JoinType.LEFT);
			Join<UserSetting, CodeExpType> experienceJobJoin = userSettingJoin.join("experienceJobCode",JoinType.LEFT);
			return cb.equal(experienceJobJoin.get("code"), experienceJobCode);
			});
		}
		
		if (skillName != null && !skillName.isEmpty()) {
			spec = spec.and((userRoot, userQuery, cb) -> {
			Join<User, UserSkill> userSkillJoin = userRoot.join("userSkills", JoinType.LEFT);
			Join<UserSkill, Skill> skillJoin = userSkillJoin.join("skill", JoinType.LEFT);
			return cb.like(skillJoin.get("name"), "%" + skillName + "%");
			});
		}
		
		return spec.toPredicate(root, query, criteriaBuilder);
		};
	}

}

