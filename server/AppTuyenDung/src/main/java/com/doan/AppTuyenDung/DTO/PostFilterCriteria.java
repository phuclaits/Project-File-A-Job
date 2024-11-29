package com.doan.AppTuyenDung.DTO;

import java.util.ArrayList;
import java.util.List;


import com.doan.AppTuyenDung.entity.Post;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class PostFilterCriteria {
    private List<Predicate> predicates;
    private CriteriaBuilder cb;
    private Root<Post> postRoot;

    public PostFilterCriteria(CriteriaBuilder cb, Root<Post> postRoot) {
        this.cb = cb;
        this.postRoot = postRoot;
        this.predicates = new ArrayList<>();
    }

    public void addCondition(String attribute, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(cb.equal(postRoot.get(attribute), value));
        }
    }

    public void addLikeCondition(String attribute, String value) {
        if (value != null && !value.isEmpty()) {
            predicates.add(cb.like(postRoot.get(attribute), "%" + value + "%"));
        }
    }

    public void addInCondition(String attribute, String[] values) {
        if (values != null && values.length > 0) {
            List<Predicate> orPredicates = new ArrayList<>();
            for (String value : values) {
                orPredicates.add(cb.equal(postRoot.get(attribute), value));
            }
            predicates.add(cb.or(orPredicates.toArray(new Predicate[0])));
        }
    }

    public CriteriaQuery<Post> buildQuery(CriteriaQuery<Post> query) {
        query.where(cb.and(predicates.toArray(new Predicate[0])));
        return query;
    }
}
