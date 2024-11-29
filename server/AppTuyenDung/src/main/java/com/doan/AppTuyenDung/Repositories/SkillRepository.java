package com.doan.AppTuyenDung.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.doan.AppTuyenDung.DTO.GetAllSkillAdmin.SkillGetListDTO;
import com.doan.AppTuyenDung.entity.Skill;
import java.util.Optional;
public interface SkillRepository extends JpaRepository<Skill, Integer> {
	public List<Skill> findByCategoryJobCode(String code);
    boolean existsByCategoryJobCodeAndName(String categoryJobCode, String name);

	@Query(value = "SELECT " + 
     "s.id as id, s.category_job_code as categoryJobCode, s.name as name " +
     "FROM skills s " +
     "WHERE (coalesce(:categoryJobCode, '') = '' OR s.category_job_code = :categoryJobCode) " +
     "AND (coalesce(:search, '') = '' OR s.name like concat('%', :search, '%'))"
    , nativeQuery = true)
    Page<SkillGetListDTO> getListSkills(Pageable pageable,@Param("search") String search,@Param("categoryJobCode") String categoryJobCode);
    // List<Skill> findByCategoryJobCodeCv(String categoryJobCode);
}
