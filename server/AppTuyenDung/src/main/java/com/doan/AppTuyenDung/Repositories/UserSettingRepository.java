package com.doan.AppTuyenDung.Repositories;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doan.AppTuyenDung.DTO.ListUserSettingsDTO;
import com.doan.AppTuyenDung.entity.UserSetting;

public interface UserSettingRepository extends JpaRepository<UserSetting, Integer>  {
	public UserSetting findByUserId(Integer userId);


	@Query("SELECT new com.doan.AppTuyenDung.DTO.ListUserSettingsDTO(" +
           "us.id, us.file, us.isFindJob, us.isTakeMail, " +
           "us.addressCode.code, us.categoryJobCode.code, " +
           "us.experienceJobCode.code, us.salaryJobCode.code, 0, us.user.id, us.user.firstName, us.user.lastName, us.categoryJobCode.value) " + // 0 as initial bonus
           "FROM UserSetting us JOIN us.user user WHERE us.isFindJob = true AND us.file IS NOT NULL " +
           "AND (:categoryJobCode IS NULL OR us.categoryJobCode.code = :categoryJobCode)")
           Page<ListUserSettingsDTO> findByFilter(@Param("categoryJobCode") String categoryJobCode,Pageable pageable);
}