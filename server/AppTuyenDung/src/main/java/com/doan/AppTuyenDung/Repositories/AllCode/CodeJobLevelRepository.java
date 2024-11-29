package com.doan.AppTuyenDung.Repositories.AllCode;

import com.doan.AppTuyenDung.DTO.GetAllJobLevelAdmin.JobLevelDTO;
import com.doan.AppTuyenDung.DTO.GetAllJobTypeAdmin.JobtypeDTO;
import com.doan.AppTuyenDung.entity.CodeJobLevel;
import com.doan.AppTuyenDung.entity.CodeJobType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface CodeJobLevelRepository extends JpaRepository<CodeJobLevel,String> {
    public CodeJobLevel findByCode(String code);

    @Query(value = "SELECT " +
        "jl.code AS code, jl.image AS image, jl.type AS type, jl.value AS value " +
        "FROM code_job_level jl " +
        "WHERE (:search IS NULL OR jl.value LIKE %:search% )", nativeQuery = true)
    Page<JobLevelDTO> GetAllJobLevel(@Param("search") String search, Pageable pageable);
}
