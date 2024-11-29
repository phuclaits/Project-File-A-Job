package com.doan.AppTuyenDung.Repositories.AllCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.doan.AppTuyenDung.DTO.GetAllJobLevelAdmin.JobLevelDTO;
import com.doan.AppTuyenDung.DTO.GetAllWorkTypeAdmin.WorkTypeDTO;
import com.doan.AppTuyenDung.entity.CodeWorkType;
import org.springframework.stereotype.Repository;
@Repository
public interface CodeWorkTypeRepository extends JpaRepository<CodeWorkType,String> {
    
    public CodeWorkType findByCode(String code);

    @Query(value = "SELECT " +
        "wt.code AS code, wt.image AS image, wt.type AS type, wt.value AS value " +
        "FROM code_work_type wt " +
        "WHERE (:search IS NULL OR wt.value LIKE %:search% )", nativeQuery = true)
    Page<WorkTypeDTO> GetAllWorkType(@Param("search") String search, Pageable pageable);
}
