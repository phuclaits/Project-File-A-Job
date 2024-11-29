package com.doan.AppTuyenDung.Repositories.AllCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doan.AppTuyenDung.DTO.GetAllSalaryTypeAdmin.SalaryTypeDTO;

import com.doan.AppTuyenDung.entity.CodeSalaryType;
import org.springframework.stereotype.Repository;
@Repository
public interface CodeSalaryTypeRepository extends JpaRepository<CodeSalaryType,String>{
	public CodeSalaryType findByCode(String code);
	@Query(value = "SELECT " +
        "st.code AS code, st.image AS image, st.type AS type, st.value AS value " +
        "FROM code_salary_type st " +
        "WHERE (:search IS NULL OR st.code LIKE %:search% )", nativeQuery = true)
    Page<SalaryTypeDTO> GetAllSalaryType(@Param("search") String search, Pageable pageable);
}
