package com.doan.AppTuyenDung.Repositories.AllCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doan.AppTuyenDung.entity.CodePostStatus;

@Repository
public interface CodePostStatusRepository extends JpaRepository<CodePostStatus,String> {
    public CodePostStatus findByCode(String code);
    
}
