package com.doan.AppTuyenDung.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.doan.AppTuyenDung.DTO.DetailPostDTO;
import com.doan.AppTuyenDung.DTO.GetAllPostRuleAdminDTO;
import com.doan.AppTuyenDung.DTO.InfoPostDetailDto;
import com.doan.AppTuyenDung.DTO.PostFilterDTO;
import com.doan.AppTuyenDung.entity.Post;

@Repository
public interface PostRepositoriesQuery extends JpaRepository<Post,Integer>{
    @Query(value =" SELECT pd.code_job_type as code_job_type,COUNT(pd.code_job_type) as amount, "+
    "a.value as value, "+
    "a.code as code, " +
    "a.image as image "+
    "FROM posts P " +
    "JOIN detailposts pd ON p.detail_post_id = pd.id " +
    "JOIN code_job_type a ON pd.code_job_type = a.code " +
    "WHERE P.status_Code = 'PS1' "+
    "GROUP BY pd.code_job_type, a.value, a.code, a.image "+
    "ORDER BY amount DESC ",nativeQuery = true)
    Page<Object[]> findPostJobTypeAndCountPost(Pageable pageable);

    @Query(value = "SELECT dp.id as id , dp.name as NameValue, ac1.value as CategoryJobCodeValue , ac2.value as CategoryJobLevelCodeValue ,\n" + //
                "ac3.value as CategoryWorktypeCodeValue , \n" + //
                "ac4.value as ExperienceJobCodeValue,\n" + //
                "ac5.value as GenderPostCodeValue , \n" + //
                "ac6.value as AddressCodeValue,\n" + //
                "ac7.value as SalaryCodeValue,\n" + //
                "c.thumbnail as CompanyThumbnailValue,\n" + //
                "p.time_Post as TimePostValue \n"+
                "FROM Posts p \n" + //
                "JOIN detailposts dp ON dp.id = p.detail_post_id \n" + //
                "JOIN companies c ON c.user_id = p.user_id \n " + //
                "LEFT JOIN code_job_type ac1 ON dp.code_job_type = ac1.code \n " + //
                "LEFT JOIN code_job_level ac2 ON dp.code_job_level = ac2.code \n " + //
                "LEFT JOIN code_work_type ac3 ON dp.code_work_type = ac3.code \n " + //
                "LEFT JOIN code_exp_type ac4 ON dp.code_exp_type = ac4.code \n " + //
                "LEFT JOIN code_gender_post ac5 ON dp.code_gender_post = ac5.code \n " + //
                "LEFT JOIN code_province ac6 ON dp.code_adress_code = ac6.code \n " + //
                "LEFT JOIN code_salary_type ac7 ON dp.code_salary_type = ac7.code \n " + //
                "WHERE p.status_Code = 'PS1' \n" + //
                "AND ( :categoryJobCode is null or ac1.code = :categoryJobCode ) \n " + //
                "AND ( :addressCode is null or ac6.code = :addressCode )\n" + //
                "AND ( :search is null or dp.name like '%:search%' )\n" + //
                "AND ( :experienceJobCodes is null or ac4.code in (:experienceJobCodes) )\n" + //
                "AND ( :categoryWorktypeCodes is null or ac3.code in (:categoryWorktypeCodes) )\n" + //
                "AND ( :salaryJobCodes is null or ac7.code in (:salaryJobCodes) )\n" + //
                "AND ( :categoryJoblevelCodes is null or ac2.code in (:categoryJoblevelCodes) )\n" + //
                "AND ( :isHot is null or p.is_hot = :isHot )\n" + //
                "ORDER BY p.time_Post DESC",
        nativeQuery = true)
    Page<DetailPostDTO> findFilteredPosts(@Param("categoryJobCode") String categoryJobCode,
                                           @Param("addressCode") String addressCode,
                                           @Param("search") String search,
                                           @Param("experienceJobCodes") List<String> experienceJobCodes,
                                           @Param("categoryWorktypeCodes") List<String> categoryWorktypeCodes,
                                           @Param("salaryJobCodes") List<String> salaryJobCodes,
                                           @Param("categoryJoblevelCodes") List<String> categoryJoblevelCodes,
                                           @Param("isHot") Integer isHot,
                                          Pageable pageable);


    @Query(value = "SELECT p.id as Id, p.time_Post as TimePostValue, p.time_End as TimeEndValue, dp.name, dp.descriptionhtml as DescriptionHTMLValue, " +
    "dp.description_markdown as DescriptionMarkdownValue, ac1.value as CategoryJobCodeValue, ac2.value as CategoryJobLevelCodeValue, " +
    "ac3.value as CategoryWorktypeCodeValue , ac4.value as ExperienceJobCodeValue, ac5.value as GenderPostCodeValue, " +
    "ac6.value as AdressCodeValue, ac7.value as SalaryCodeValue, c.name as NameCompanyValue, c.address as AddressCompanyValue, " +
    "c.cover_image as CoverImageCompanyValue, c.phonenumber as PhoneCompanyValue, c.taxnumber as TaxNumberValue, c.thumbnail as ThumbnailCompanyValue, " +
    "c.website as WebsiteCompanyValue, c.amount_employer as EmployerCompanyValue, " +
    "ac1.code as CategoryJobCode, ac4.code as ExperienceJobCode, ac2.code as CategoryJobLevelCode,  ac3.code as CategoryWorktypeCode, " +
    "ac7.code as SalaryCode, ac6.code as AdressCode, ac5.code as GenderPostCode, dp.amount as Amount, ac8.email as EmailCompany " +
    "FROM Posts p " +
    "LEFT JOIN detailposts dp ON dp.id = p.detail_post_id " +
    "LEFT JOIN accounts aco ON aco.user_id = p.user_id " +
    "LEFT JOIN users u ON u.id = aco.user_id   " +
    "LEFT JOIN companies c ON c.id = u.company_Id " +
    "LEFT JOIN code_job_type ac1 ON dp.code_job_type = ac1.code " +
    "LEFT JOIN code_job_level ac2 ON dp.code_job_level = ac2.code " +
    "LEFT JOIN code_work_type ac3 ON dp.code_work_type = ac3.code " +
    "LEFT JOIN code_exp_type ac4 ON dp.code_exp_type = ac4.code " +
    "LEFT JOIN code_gender_post ac5 ON dp.code_gender_post = ac5.code " +
    "LEFT JOIN code_province ac6 ON dp.code_adress_code = ac6.code   "+
    "LEFT JOIN code_salary_type ac7 ON dp.code_salary_type = ac7.code "+
    "LEFT JOIN users ac8 ON c.user_id = ac8.id "+
    "WHERE p.id = :id", nativeQuery = true)
    List<InfoPostDetailDto> findPostDetailById(@Param("id") Integer id);
    
    

    @Query(value = "SELECT " +
       "p.id AS Id,  " +
       "dp.name AS PostName,  " +
       "CONCAT(u.first_name, ' ', u.last_name) AS FullName, " +
       "p.status_code AS StatusCode, " +
       "cps.value AS StatusValue, " +
       "p.time_end AS TimeEnd,  " +
       "p.updated_at AS updateTime " +
       "FROM posts p  " +
       "LEFT JOIN detailposts dp ON p.detail_post_id = dp.id  " +
       "LEFT JOIN users u ON p.user_id = u.id  " +
       "LEFT JOIN companies c ON u.company_id = c.id  " +
       "LEFT JOIN code_post_status cps ON p.status_code = cps.code " +
       "WHERE (:idCompany IS NULL OR u.company_id = :idCompany) " +
       "AND (:censorCode IS NULL OR LOWER(p.status_code) = LOWER(:censorCode) OR p.status_code IN (:censorCode)) " +
       "AND (:search IS NULL OR dp.name LIKE %:search%) " +
       "ORDER BY p.updated_at DESC"
       , nativeQuery = true)
    Page<PostFilterDTO> GetListPostAdminRp(Pageable pageable,
                                           @Param("idCompany") Integer idCompany, 
                                           @Param("search") String search,
                                           @Param("censorCode") String censorCode);




    
    
    @Query(value = "SELECT p.id AS Id, dp.name AS PostName, c.name AS CompanyName, " +
        "CONCAT(u.first_name, ' ', u.last_name) AS FullName, p.time_end AS timeEnd, " +
        "p.status_code AS StatusCode, cps.value AS StatusValue, p.updated_at AS UpdateTime "+
        "FROM posts p "+
        "LEFT JOIN detailposts dp ON p.detail_post_id = dp.id "+
        "LEFT JOIN users u ON p.user_id = u.id " +
        "LEFT JOIN companies c ON u.company_id = c.id "+
        "LEFT JOIN code_post_status cps ON p.status_code = cps.code " +
        "WHERE (:censorCode IS NULL OR LOWER(p.status_code) = LOWER(:censorCode) OR p.status_code IN (:censorCode)) "+
        "AND (:search IS NULL OR dp.name LIKE %:search%) "+
        "ORDER BY p.updated_at DESC"
    
    ,nativeQuery = true)
    Page<GetAllPostRuleAdminDTO> GetAllPostRoleAdminRp(Pageable pageable,@Param("search") String search,@Param("censorCode") String censorCode);
    
    

    
} 
