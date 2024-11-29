package com.doan.AppTuyenDung.Repositories.Specification;

import java.util.Date;

import org.springframework.data.jpa.domain.Specification;

import com.doan.AppTuyenDung.ModelFirebase.User;
import com.doan.AppTuyenDung.entity.OrderPackage;
import com.doan.AppTuyenDung.entity.OrderPackageCv;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class OrderPackageSpecification {
	public static Specification<OrderPackage> filterOrderPackagePost(Date fromDate, Date toDate, String companyId) {
        return (root, query, criteriaBuilder) -> {
            Specification<OrderPackage> spec = Specification.where(null);
            if (companyId != null && !companyId.isEmpty()) {
                spec = spec.and((orderPackageRoot, orderPackageQuery, cb) -> {
                    Join<OrderPackage, User> userJoin = orderPackageRoot.join("user", JoinType.LEFT);
                    return cb.equal(userJoin.get("companyId"), companyId);
                });
            }
            if (fromDate != null) {
                spec = spec.and((orderPackageCvRoot, orderPackageCvQuery, cb) -> 
                    cb.greaterThanOrEqualTo(orderPackageCvRoot.get("createdAt"), fromDate)
                );
            }
            if (toDate != null) {
                spec = spec.and((orderPackageCvRoot, orderPackageCvQuery, cb) -> 
                    cb.lessThanOrEqualTo(orderPackageCvRoot.get("createdAt"), toDate)
                );
            }
            
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
	public static Specification<OrderPackageCv> filterOrderPackageCv(Date fromDate, Date toDate, String companyId) {
        return (root, query, criteriaBuilder) -> {
            Specification<OrderPackageCv> spec = Specification.where(null);
            if (companyId != null && !companyId.isEmpty()) {
                spec = spec.and((orderPackageCvRoot, orderPackageCvQuery, cb) -> {
                    Join<OrderPackageCv, User> userJoin = orderPackageCvRoot.join("user", JoinType.LEFT);
                    return cb.equal(userJoin.get("companyId"), companyId);
                });
            }
            if (fromDate != null) {
                spec = spec.and((orderPackageCvRoot, orderPackageCvQuery, cb) -> 
                    cb.greaterThanOrEqualTo(orderPackageCvRoot.get("createdAt"), fromDate)
                );
            }
            if (toDate != null) {
                spec = spec.and((orderPackageCvRoot, orderPackageCvQuery, cb) -> 
                    cb.lessThanOrEqualTo(orderPackageCvRoot.get("createdAt"), toDate)
                );
            }
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
