package com.doan.AppTuyenDung.DTO.Response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PackageCvResponse {
    private Integer id;
    private String name;
    private String value;
    private double price;
    private int isHot;
    private int isActive;
    private int count;
    private double total;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public PackageCvResponse(Integer id, String name, String value, double price, int isHot, int isActive, int count, double total) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.price = price;
        this.isHot = isHot;
        this.isActive = isActive;
        this.count = count;
        this.total = total;
    }

    public PackageCvResponse() {
    }
}
