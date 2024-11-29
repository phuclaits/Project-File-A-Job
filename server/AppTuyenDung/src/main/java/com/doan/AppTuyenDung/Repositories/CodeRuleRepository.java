package com.doan.AppTuyenDung.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doan.AppTuyenDung.entity.CodeRule;

public interface CodeRuleRepository extends JpaRepository<CodeRule, String> {
	CodeRule findByCode(String code);
}
