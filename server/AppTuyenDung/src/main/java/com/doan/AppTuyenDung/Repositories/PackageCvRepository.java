package com.doan.AppTuyenDung.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doan.AppTuyenDung.entity.PackageCv;

import java.util.List;
import java.util.Optional;



@Repository
public interface PackageCvRepository extends JpaRepository<PackageCv, Integer> {
    Page<PackageCv> findByNameContaining(String search, Pageable pageable);
    Optional<PackageCv> findById(Integer id);
    boolean existsByNameAndIdNot(String name, Integer id);
    boolean existsByName(String name);
    List<PackageCv> findByIsActiveTrue();
}