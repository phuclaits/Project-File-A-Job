package com.doan.AppTuyenDung.DTO.Response;

import lombok.Data;

@Data
public class SkillIdRespones {
	private int SkillId;
	private int UserId;
	private SkillResponse Skill;
}