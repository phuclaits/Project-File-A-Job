package com.doan.AppTuyenDung.Repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.doan.AppTuyenDung.entity.Post;
import com.doan.AppTuyenDung.entity.User;

import java.util.Map;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {
    // Truy vấn để lấy thống kê loại bài đăng
    @Query(value = "SELECT d.code_job_type AS categoryJobCode, c.value AS categoryJobValue, COUNT(d.code_job_type) AS amount " +
                   "FROM posts p " +
                   "JOIN detailposts d ON p.id = d.id " +
                   "JOIN code_job_type c ON d.code_job_type = c.code " +
                   "WHERE p.status_code = 'PS1' " +
                   "GROUP BY d.code_job_type, c.value " +
                   "ORDER BY amount DESC  "+
                   "LIMIT :limit", nativeQuery = true)
    List<Map<String, Object>> findStatisticalTypePost(@Param("limit") int limit);   

   // List<Post> findTop5ByStatusCodeAndUserIdIn(String statusCode, List<Integer> userIds);
    
    public List<Post> findByUserId(int userId);
    Page<Post> findAll(Specification<Post> spec, Pageable pageable);

    Optional<Post> findByDetailPostIdAndIdNot(Integer detailPostId, Integer id);
    Optional<Post> findByDetailPostIdAndId(Integer detailPostId, Integer id);
}