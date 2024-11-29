package com.doan.AppTuyenDung.ModelFirebase;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notice implements Serializable {
    private String subject;
    private String image;
    private Map<String,String> data;
    private List<String> userId;
}
