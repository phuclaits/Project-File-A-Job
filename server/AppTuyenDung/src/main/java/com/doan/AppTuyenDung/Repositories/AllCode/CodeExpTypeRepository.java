package com.doan.AppTuyenDung.Repositories.AllCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doan.AppTuyenDung.DTO.GetAllExpTypeAdmin.ExpTypeDTO;
import com.doan.AppTuyenDung.DTO.GetAllSalaryTypeAdmin.SalaryTypeDTO;
import com.doan.AppTuyenDung.entity.CodeExpType;


import org.springframework.stereotype.Repository;
@Repository
public interface CodeExpTypeRepository extends JpaRepository<CodeExpType,String>{
    public CodeExpType findByCode(String code);
	@Query(value = "SELECT " +
        "expt.code AS code, expt.image AS image, expt.type AS type, expt.value AS value " +
        "FROM code_exp_type expt " +
        "WHERE (:search IS NULL OR expt.code LIKE %:search% )", nativeQuery = true)
    Page<ExpTypeDTO> GetAllExpType(@Param("search") String search, Pageable pageable);
}
