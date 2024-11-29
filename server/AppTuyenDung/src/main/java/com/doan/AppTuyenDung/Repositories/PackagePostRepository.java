package com.doan.AppTuyenDung.Repositories;

import com.doan.AppTuyenDung.entity.PackagePost;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PackagePostRepository extends JpaRepository<PackagePost,Integer> {
    List<PackagePost> findByIsHot(Boolean isHot);
    List<PackagePost> findByIsHotAndIsActive(Boolean isHot, Boolean isActive);
    Page<PackagePost> findByNameContaining(String name, Pageable pageable);
    boolean existsByNameAndIdNot(String name, Integer id);
    boolean existsByName(String name);
}
