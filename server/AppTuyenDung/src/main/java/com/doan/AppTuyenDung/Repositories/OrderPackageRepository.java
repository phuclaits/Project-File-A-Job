package com.doan.AppTuyenDung.Repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.doan.AppTuyenDung.entity.OrderPackage;

@Repository
public interface OrderPackageRepository extends JpaRepository<OrderPackage, Integer> {
    @Query("SELECT SUM(op.currentPrice * op.amount) FROM OrderPackage op")
     Double selectPrice();
    Page<OrderPackage> findAll(Specification<OrderPackage> spec, Pageable pageable);
}
