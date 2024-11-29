package com.doan.AppTuyenDung.Repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doan.AppTuyenDung.entity.DetailPost;

@Repository
public interface DetailPostRepository  extends JpaRepository<DetailPost,Integer>, JpaSpecificationExecutor<DetailPost>{
	@Query("SELECT dp.categoryJobCode.code, COUNT(dp) FROM DetailPost dp GROUP BY dp.categoryJobCode")
    	List<Object[]> findCountByJobType();
}
