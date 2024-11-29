package com.doan.AppTuyenDung.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doan.AppTuyenDung.entity.CodeGender;

public interface CodeGenderRepository extends JpaRepository<CodeGender, String> {
	CodeGender findByCode(String Code);
}
