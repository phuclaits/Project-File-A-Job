package com.doan.AppTuyenDung.Repositories;

import java.util.Optional;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestHeader;

import com.doan.AppTuyenDung.DTO.GetAllUserByCompanyIdDTO;
import com.doan.AppTuyenDung.DTO.UserAccountDTO;
import com.doan.AppTuyenDung.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
    // Optional<User> findByUsername(String username); 
    Optional<User> findById(int id); 
    @Query(value = "select us.id as Id, us.first_name as FirstName, us.last_name as LastName, us.email as Email, us.address as AddressUser, \n" + //
                "us.image as Image, ac1.value as GenderCodeValue, us.company_id as IdCompany, \n " + //
                "acc.phonenumber as PhoneNumber, ac3.value as CodeStatusValue, ac4.value as CodeRoleValue, acc.created_At as CreatedAtUser, us.dob as DobUser, \n " + //
                "acc.role_code as CodeRoleAccount \n" +
                "from accounts acc \n" + //
                "JOIN users us ON us.id = acc.user_id \n" + //
                "LEFT JOIN code_gender ac1 ON us.code_gender = ac1.code \n" + //
                "LEFT JOIN companies ac2 ON us.company_id = ac2.id \n" + //
                "LEFT JOIN code_status ac3 ON acc.status_Code = ac3.code \n" + //
                "LEFT JOIN code_rule ac4 ON acc.role_code = ac4.code \n" + //
                "where acc.phonenumber = :phoneNumber ",nativeQuery= true)
    List<UserAccountDTO> findInfoUser(String phoneNumber);

    List<User> findByCompanyId(Integer companyId);

    Optional<User> findById(Integer id);
    Page<User> findAll(Specification<User> spec, Pageable pageable);
    List<User> findAll(Specification<User> spec);
    @Query(value = "select us.id as Id \n" + //
                "from users us \n" + //
                "where us.company_id = :companyId ",nativeQuery= true)
    List<Integer> findUserIdsByCompanyId(@Param("companyId") Integer companyId);


    @Query(value="SELECT us.id as userId, us.first_name as FirstName, us.last_name as LastName, ac.phonenumber as PhoneNumber, "+
    "cg.code as GenderCode , cg.value as GenderValue, us.dob as Birthday, "+
    "ac.role_code as RoleCode, cr.Value as RoleValue, ac.status_Code as StatusCode, st.Value as StatusValue "+
    "FROM users us "+
    "LEFT JOIN accounts ac ON ac.user_id = us.id "+
    "LEFT JOIN code_gender cg ON cg.code = us.code_gender "+
    "LEFT JOIN code_rule cr ON cr.code = ac.role_code "+
    "LEFT JOIN code_status st ON st.code = ac.status_Code "+
    "where us.company_id IN (:companyId) ",nativeQuery= true)
    Page<GetAllUserByCompanyIdDTO> getAllUserByCompanyId(@Param("companyId") Integer companyId, Pageable pageable);


}
