package com.doan.AppTuyenDung.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doan.AppTuyenDung.entity.CodeCensorstatus;

public interface CodeCensorstatusRepository extends JpaRepository<CodeCensorstatus, String> {
	public CodeCensorstatus findByCode(String code);
}
