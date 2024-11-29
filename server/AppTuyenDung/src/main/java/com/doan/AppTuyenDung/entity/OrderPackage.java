package com.doan.AppTuyenDung.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "orderpackages")
public class OrderPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double currentPrice;
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "packagePostId")
    private PackagePost packagePost;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    private java.util.Date createdAt;
    private java.util.Date updatedAt;

}