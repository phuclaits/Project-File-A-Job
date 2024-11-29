package com.doan.AppTuyenDung.DTO;

import java.sql.Date;

public class AddNewJobDTO {
    public Long job_id;
    public String job_title;
    public String career;
    public String job_description;
    public String location;
    public int quanlity;
    public Long salary_min;
    public Long salary_max;
    public String city;
    public String district;
    public Date posted_date;
    public Date expiry_date;
    public Boolean urgent;
}
