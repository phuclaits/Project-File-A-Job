package com.doan.AppTuyenDung.Repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doan.AppTuyenDung.entity.OrderPackageCv;

@Repository
public interface OrderPackageCvRepository extends JpaRepository<OrderPackageCv, Integer> {
    @Query (value = "SELECT package_cv_id as id, SUM(amount) as count, SUM(current_price * amount) as total " +
                   "FROM orderpackagecvs " +
                   "WHERE created_at BETWEEN :fromDate AND :toDate " +
                   "GROUP BY package_cv_id " +
                   "ORDER BY total DESC",
           nativeQuery = true)
    List<Object[]> findOrderStatistics(@Param("fromDate") String fromDate, @Param("toDate") String toDate);
    
    @Query("SELECT SUM(opc.currentPrice * opc.amount) FROM OrderPackageCv opc")
    Double selectPrice();
    
    Page<OrderPackageCv> findAll(Specification<OrderPackageCv> spec, Pageable pageable);
}
