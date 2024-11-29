package com.doan.AppTuyenDung.DTO;

import lombok.Data;

@Data
public class GetListCVByPost {
    private Integer postId;
    private Integer limit;
    private Integer offset;

}
