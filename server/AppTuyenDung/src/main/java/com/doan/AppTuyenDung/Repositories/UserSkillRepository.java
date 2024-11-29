package com.doan.AppTuyenDung.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doan.AppTuyenDung.entity.UserSkill;
import com.doan.AppTuyenDung.entity.UserSkillId;


public interface UserSkillRepository extends JpaRepository<UserSkill, UserSkillId> {
	public void deleteByUserId(Integer userId);
	public List<UserSkill> findByUserId(Integer userID);
	
}
