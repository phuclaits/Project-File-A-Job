package com.doan.AppTuyenDung.Repositories.AllCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doan.AppTuyenDung.DTO.GetAllGenderPostCode.GenderPostDTO;
// import com.doan.AppTuyenDung.DTO.GetAllExpTypeAdmin.ExpTypeDTO;
import com.doan.AppTuyenDung.entity.CodeGenderPost;

@Repository
public interface CodeGenderPostRepository extends JpaRepository<CodeGenderPost,String> {
    public CodeGenderPost findByCode(String code);

    @Query(value = "SELECT " +
        "expt.code AS code, expt.image AS image, expt.type AS type, expt.value AS value " +
        "FROM code_exp_type expt " +
        "WHERE (:search IS NULL OR expt.code LIKE %:search% )", nativeQuery = true)
    Page<GenderPostDTO> GetAllGenderPosts(@Param("search") String search, Pageable pageable);
}
