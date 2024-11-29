package com.doan.AppTuyenDung.Repositories.AllCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.JobtypeDTO;
import com.doan.AppTuyenDung.entity.CodeJobType;
import org.springframework.stereotype.Repository;
@Repository
public interface CodeJobTypeRepository extends JpaRepository<CodeJobType,String>{
    public CodeJobType findByCode(String code);

    @Query(value = "SELECT " +
        "jt.code AS code, jt.image AS image, jt.type AS type, jt.value AS value " +
        "FROM code_job_type jt " +
        "WHERE (:search IS NULL OR jt.value LIKE %:search% )", nativeQuery = true)
    Page<JobtypeDTO> GetAllJobType(@Param("search") String search, Pageable pageable);
}
