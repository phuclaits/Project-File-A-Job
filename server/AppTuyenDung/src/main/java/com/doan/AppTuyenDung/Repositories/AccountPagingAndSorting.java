package com.doan.AppTuyenDung.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doan.AppTuyenDung.DTO.GetAllUserAdmin.AccountDTO;
import com.doan.AppTuyenDung.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface  AccountPagingAndSorting extends JpaRepository<Account, Integer>{
        @Query(value = "SELECT " +
        "a.id AS Id, a.phonenumber AS Phonenumber, a.role_Code AS RoleCode, a.status_Code AS StatusCode, " +
        "a.created_At AS CreatedAt, a.updated_At AS UpdatedAt, " +
        "rl.code AS RoleDataCode, rl.value AS RoleDataValue, " +
        "stt.code AS StatusAccountDataCode, stt.value AS StatusAccountDataValue, " +
        "us.id AS UserId, us.first_name AS FirstName, us.last_name AS LastName, " +
        "us.email AS Email, us.address AS Address, us.code_gender AS GenderCode, " +
        "us.image AS Image, us.dob AS Dob, us.company_id AS CompanyId, " +
        "gd.code AS GenderDataCode, gd.value AS GenderDataValue " +
        "FROM accounts a " +
        "LEFT JOIN code_rule rl ON a.role_Code = rl.code " +
        "LEFT JOIN code_status stt ON a.status_Code = stt.code " +
        "LEFT JOIN users us ON a.user_Id = us.id " +
        "LEFT JOIN code_gender gd ON us.code_gender = gd.code " +
        "WHERE (:search IS NULL OR a.phonenumber LIKE %:search% )", nativeQuery = true)
    Page<AccountDTO> findAllWithDetails(@Param("search") String search, Pageable pageable);
}
