package com.doan.AppTuyenDung.DTO;

import lombok.Data;

@Data
public class PaymentOrderRequestCvDTO {
    private String payerID;
    private String paymentId;
    private String token;
    private Integer packageCvId;
    private Integer amount;
    private Integer userId;
}
