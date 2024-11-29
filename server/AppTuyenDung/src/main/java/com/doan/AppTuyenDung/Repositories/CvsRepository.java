
package com.doan.AppTuyenDung.Repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doan.AppTuyenDung.DTO.Response.CvsResponse;
import com.doan.AppTuyenDung.entity.Cv;
import com.doan.AppTuyenDung.entity.Post;

public interface CvsRepository extends JpaRepository<Cv, Integer> {
	public List<Cv> findByStatus(String status);
	public Page<Cv> findByUserId(Integer id, Pageable pageable);
	Page<Cv> findAllByPostId(Integer postId, Pageable pageable);
	public Page<Cv> findByStatus(String status, Pageable pageable);
	Optional<Cv> findById(Integer id);
	Page<Cv> findByPostId(Integer postId, Pageable pageable);
}
