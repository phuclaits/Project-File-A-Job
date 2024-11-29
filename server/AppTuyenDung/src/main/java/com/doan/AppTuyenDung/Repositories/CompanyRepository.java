package com.doan.AppTuyenDung.Repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doan.AppTuyenDung.DTO.CompanyGetListDTO;
import com.doan.AppTuyenDung.DTO.GetAllCompaniesByAdmin;
import com.doan.AppTuyenDung.entity.Company;

public interface CompanyRepository extends JpaRepository<Company,Integer>{
    boolean existsByName(String name);
    Optional<Company> findById(Integer id);


     @Query(value = "SELECT " + 
     "c.id as id,c.name as name, c.thumbnail as thumbnail, c.cover_image as coverimage, "+
     "c.descriptionHTML as descriptionHTML, c.description_Markdown as descriptionMarkdown, "+
     "c.website as website, c.address as address, c.phonenumber as phonenumber, c.amount_Employer as amountEmployer, "+
     "c.taxnumber as taxnumber, c.code_status as statusCode, c.user_Id as userId, c.code_censor_status as censorCode, c.file as file, "+
     "c.allow_Post as allowPost, c.allow_Hot_Post as allowHotPost, c.allow_Cv_Free as allowCvFree, c.allowCV as allowCV, "+
     "c.created_At as createdAt, c.updated_At as updatedAt "+
     "FROM companies c "+
     "WHERE c.code_status = \"S1\" AND "+
     "(:search IS NULL OR c.name LIKE %:search% )", nativeQuery = true)
    Page<CompanyGetListDTO> getListCompany(@Param("search") String search, Pageable pageable);
     Company findCompanyByUserId(Integer userId);

     @Query(value = "SELECT c.id as idCompany, c.name as nameCompany, c.phonenumber as phoneNumber, c.taxnumber as taxNumber, status.code as status, " +
     "status.value as statusValue, censor.code as censorCode, censor.value as CensorValue, c.created_at as createdAt " +
     "FROM companies c  "+
     "LEFT JOIN code_censor_status censor ON censor.code = c.code_censor_status "+
     "LEFT JOIN code_status status on status.code = c.code_status  " +
     "WHERE (:censorCode IS NULL OR c.code_censor_status LIKE :censorCode) "+
     "AND (:search IS NULL OR c.name LIKE %:search% )", nativeQuery = true)
    Page<GetAllCompaniesByAdmin> getAllCompaniesByAdmin(@Param("search") String search, @Param("censorCode") String censorCode,Pageable pageable);
}
