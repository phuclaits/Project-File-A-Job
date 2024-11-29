package com.doan.AppTuyenDung.Repositories.AllCode;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doan.AppTuyenDung.entity.CodeProvince;
import org.springframework.stereotype.Repository;
@Repository
public interface CodeProvinceRepository extends JpaRepository<CodeProvince,String>{
	public CodeProvince findByCode(String code);
}
