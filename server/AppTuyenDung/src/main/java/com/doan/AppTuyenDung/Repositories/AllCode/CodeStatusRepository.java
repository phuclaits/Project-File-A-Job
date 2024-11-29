package com.doan.AppTuyenDung.Repositories.AllCode;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.doan.AppTuyenDung.entity.CodeStatus;
@Repository
public interface CodeStatusRepository extends JpaRepository<CodeStatus,String>{
    CodeStatus findByCode(String code);
}
